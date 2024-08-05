package dev.sstol.map;

import dev.sstol.entities.Creature;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * @author Sergey Stol
 * 2024-08-04
 */
// Breadth-first search
public class PathFinder {

   private final GameMap gameMap;

   public PathFinder(GameMap gameMap) {
      this.gameMap = gameMap;
   }

   // Breadth-first search
   public Queue<Cell> findPathToClosestFoodCell(Creature creature) {
      int[][] legalMotions = getLegalMotions();
      Cell startedCell = gameMap.getCell(creature);

      AvailablePath startedPath = new AvailablePath(startedCell,
        creature.getSpeed(),
        List.of(startedCell));
      Queue<AvailablePath> availablePaths = new LinkedList<>(List.of(startedPath));

      Set<Cell> visitedCells = new HashSet<>();

      while (!availablePaths.isEmpty()) {
         AvailablePath currentPath = availablePaths.poll();
         visitedCells.add(currentPath.cell);
         for (int[] legalMotion : legalMotions) {
            Cell nextCell = currentPath.cell.withOffset(legalMotion[0], legalMotion[1]);

            if (isAlreadyChecked(visitedCells, nextCell) || gameMap.isOutOfMap(nextCell)) {
               continue;
            }

            if (creature.isBlocker(nextCell)) {
               visitedCells.add(nextCell);
               continue;
            }

            if (creature.isTransitive(nextCell)) {
               int transitiveQueue = currentPath.transitiveQueue + 1;
               if (creature.isEnoughEnergyGoThrowTransitiveCell(currentPath.energy, transitiveQueue)) {
                  AvailablePath nextPath = new AvailablePath(
                    nextCell,
                    currentPath.energy - transitiveQueue,
                    currentPath.path,
                    transitiveQueue
                  );
                  availablePaths.add(nextPath);
               }

               if (transitiveQueue == 1 && currentPath.energy < creature.getSpeed()) {
                  AvailablePath nextPath = new AvailablePath(
                    nextCell,
                    creature.getSpeed() - transitiveQueue,
                    currentPath.path,
                    transitiveQueue
                  );
                  nextPath.path.add(currentPath.cell);
                  availablePaths.add(nextPath);
               }
               continue;
            }

            if (creature.isFood(nextCell)) {
               currentPath.path.add(nextCell);
               currentPath.path.poll();
               return currentPath.path;
            }

            int energy = currentPath.energy - 1;
            Queue<Cell> nextPath = new LinkedList<>(currentPath.path);
            if (energy == 0) {
               nextPath.add(nextCell);
               energy = creature.getSpeed();
            }

            AvailablePath nextAvailablePath = new AvailablePath(
              nextCell,
              energy,
              nextPath
            );
            availablePaths.add(nextAvailablePath);
         }
      }
      return new LinkedList<>();
   }

   public Set<Cell> findAvailableCells(Creature creature) {
      int[][] legalMotions = getLegalMotions();
      Cell startedCell = gameMap.getCell(creature);

      AvailableMove availableMove = new AvailableMove(startedCell, creature.getSpeed());
      Queue<AvailableMove> availableMoves = new LinkedList<>(List.of(availableMove));

      Set<Cell> visitedCells = new HashSet<>();
      Set<Cell> availableCells = new HashSet<>(List.of(startedCell));

      while (!availableMoves.isEmpty()) {
         AvailableMove currentMove = availableMoves.poll();
         visitedCells.add(currentMove.cell);
         for (int[] legalMotion : legalMotions) {
            Cell nextCell = currentMove.cell.withOffset(legalMotion[0], legalMotion[1]);
            AvailableMove nextMove = new AvailableMove(
              nextCell,
              currentMove.energy - 1
            );

            if (isAlreadyChecked(visitedCells, nextCell) || gameMap.isOutOfMap(nextCell)) {
               continue;
            }

            if (creature.isBlocker(nextCell)) {
               visitedCells.add(nextCell);
               continue;
            }

            if (creature.isTransitive(nextCell)) {
               if (nextMove.energy > 0) {
                  availableMoves.add(nextMove);
               }
               continue;
            }

            if (nextMove.energy > 0) {
               availableMoves.add(nextMove);
            }
            availableCells.add(nextCell);
         }
      }
      return availableCells;
   }

   private static int[][] getLegalMotions() {
      int[] up = {0, -1};
      int[] right = {1, 0};
      int[] down = {0, 1};
      int[] left = {-1, 0};
      return new int[][]{up, right, down, left};
   }

   private static boolean isAlreadyChecked(Set<Cell> checkedCells, Cell nextCell) {
      return checkedCells.contains(nextCell);
   }

   record AvailableMove(Cell cell, int energy) {
   }

   static class AvailablePath {
      Cell cell;
      int energy;
      Queue<Cell> path;
      int transitiveQueue;

      public AvailablePath(Cell cell, int energy, Collection<Cell> path, int transitiveQueue) {
         this.cell = cell;
         this.energy = energy;
         this.path = new LinkedList<>(path);
         this.transitiveQueue = transitiveQueue;
      }

      public AvailablePath(Cell cell, int energy, Collection<Cell> path) {
         this(cell, energy, path, 0);
      }
   }
}
