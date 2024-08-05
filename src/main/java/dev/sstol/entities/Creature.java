package dev.sstol.entities;

import dev.sstol.map.Cell;
import dev.sstol.map.GameMap;
import dev.sstol.map.PathFinder;

import java.util.Queue;
import java.util.Set;

/**
 * @author Sergey Stol
 * 2024-07-29
 */
public abstract class Creature extends Entity {
   protected final CreatureStats stats;
   protected final GameMap gameMap;
   protected final PathFinder pathFinder;

   public Creature(GameMap gameMap, PathFinder pathFinder, CreatureStats stats) {
      this.gameMap = gameMap;
      this.pathFinder = pathFinder;
      this.stats = stats;
   }

   public Cell move() {
      Queue<Cell> pathToClosestFood = pathFinder.findPathToClosestFoodCell(this);

      if (!pathToClosestFood.isEmpty()) {
         Cell toCell = pathToClosestFood.poll();
         if (isFood(toCell)) {
            attackFood(toCell);
         } else {
            gameMap.moveEntity(this, toCell);
         }
         return toCell;
      }

      Set<Cell> availableCells = pathFinder.findAvailableCells(this);

      if (availableCells.isEmpty()) {
         System.out.println("There are no available moves. The move has been missed.");
         return null;
      }

      Cell availableEmptyCell = availableCells.stream()
        .findAny()
        .orElse(null);

      gameMap.moveEntityForce(this, availableEmptyCell);
      return availableEmptyCell;
   }

   protected abstract void attackFood(Cell foodCell);

   public boolean isEnoughEnergyGoThrowTransitiveCell(int energy, int transitiveQueue) {
      return energy - transitiveQueue >= 1;
   }

   public abstract boolean isTransitive(Cell cell);

   public abstract boolean isFood(Cell cell);

   public abstract boolean isBlocker(Cell cell);

   public int getSpeed() {
      return stats.getSpeed();
   }

   public int getCurrentHealth() {
      return stats.getCurrentHealth();
   }

   public int getTotalHealth() {
      return stats.getTotalHealth();
   }

   public void getDamage(Creature otherCreature) {
      this.stats.reduceCurrentHealth(otherCreature.stats.getAttack());
   }

   public boolean isDead() {
      return stats.getCurrentHealth() <= 0;
   }

   @Override
   public String toString() {
      return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());
   }

   protected void restoreHealth() {
      stats.setCurrentHealth(stats.getTotalHealth());
   }
}
