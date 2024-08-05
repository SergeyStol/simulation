package dev.sstol.entities;

import dev.sstol.MapRender;
import dev.sstol.map.Cell;
import dev.sstol.map.GameMap;
import dev.sstol.map.PathFinder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Sergey Stol
 * 2024-08-02
 */
public class PathFinderFindPathToClosestFoodForHerbivoreTest {

   final int HERBIVORE_ATTACK = 2;
   final int HERBIVORE_HEALTH = 2;
   final int HERBIVORE_SPEED = 2;
   final int MAP_COLUMNS = 6;
   final int MAP_ROWS = 6;

   GameMap gameMap;
   PathFinder pathFinder;
   CreatureStats stats = new CreatureStats(HERBIVORE_ATTACK, HERBIVORE_HEALTH, HERBIVORE_SPEED);
   Herbivore herbivore;
   int stepCounter;
   MapRender mapRender;

   @BeforeEach
   void beforeEach() {
      gameMap = new GameMap(MAP_COLUMNS, MAP_ROWS);
      pathFinder = new PathFinder(gameMap);
      mapRender = new MapRender(gameMap);
      herbivore = new Herbivore(gameMap, pathFinder, stats);
      stepCounter = 0;
   }

   // 0 0 0 0 0 G
   // 0 0 0 0 0 0
   // 0 0 0 0 0 0
   // 0 0 0 0 0 0
   // 0 0 0 0 0 0
   // H 0 0 0 0 0
   @Test
   void herbivore_shouldMoveToGrassStrongly_whenGrassLocateOutOfOneMotion() {
      gameMap.putEntityOnMap(new Cell(0, 5), herbivore);
      gameMap.putEntityOnMap(new Cell(5, 0), new Grass());

      startGame();

      assertEquals(5, stepCounter);
   }

   // 0 0 0 0 0 H
   // 0 0 0 0 0 0
   // 0 0 0 0 0 0
   // 0 0 0 0 0 0
   // 0 0 0 0 0 0
   // G 0 0 0 0 0
   @Test
   void herbivore_shouldMoveToGrassStrongly_whenGrassLocateOutOfOneMotion1() {
      gameMap.putEntityOnMap(new Cell(5, 0), herbivore);
      gameMap.putEntityOnMap(new Cell(0, 5), new Grass());

      startGame();

      assertEquals(5, stepCounter);
   }

   // 0 0 0 0 P H
   // 0 T R 0 P 0
   // 0 T R 0 P 0
   // 0 T R 0 P 0
   // 0 T R 0 P 0
   // 0 G R 0 0 0
   @Test
   void herbivore_shouldMoveToGrassStrongly_whenBlockersArePresent() {
      gameMap.putEntityOnMap(new Cell(1, 1), new Tree());
      gameMap.putEntityOnMap(new Cell(1, 2), new Tree());
      gameMap.putEntityOnMap(new Cell(1, 3), new Tree());
      gameMap.putEntityOnMap(new Cell(1, 4), new Tree());
      gameMap.putEntityOnMap(new Cell(1, 5), new Grass());

      gameMap.putEntityOnMap(new Cell(2, 1), new Rock());
      gameMap.putEntityOnMap(new Cell(2, 2), new Rock());
      gameMap.putEntityOnMap(new Cell(2, 3), new Rock());
      gameMap.putEntityOnMap(new Cell(2, 4), new Rock());
      gameMap.putEntityOnMap(new Cell(2, 5), new Rock());

      gameMap.putEntityOnMap(new Cell(4, 0), new PredatorMock());
      gameMap.putEntityOnMap(new Cell(4, 1), new PredatorMock());
      gameMap.putEntityOnMap(new Cell(4, 2), new PredatorMock());
      gameMap.putEntityOnMap(new Cell(4, 3), new PredatorMock());
      gameMap.putEntityOnMap(new Cell(4, 4), new PredatorMock());

      gameMap.putEntityOnMap(new Cell(5, 0), herbivore);

      startGame();

      assertEquals(11, stepCounter);
   }

   // 0 0 0 0 0 H
   // 0 T 0 R P P
   // 0 T 0 R P G
   // 0 T 0 R P 0
   // 0 T 0 0 0 0
   // G T 0 0 0 0
   @Test
   void herbivore_shouldGoToClosestGrass_whenMoreThanOneGrassOnMap() {
      Grass nearestGrass = new Grass();
      Grass farthestGrass = new Grass();

      gameMap.putEntityOnMap(new Cell(0, 5), nearestGrass);

      gameMap.putEntityOnMap(new Cell(1, 1), new Tree());
      gameMap.putEntityOnMap(new Cell(1, 2), new Tree());
      gameMap.putEntityOnMap(new Cell(1, 3), new Tree());
      gameMap.putEntityOnMap(new Cell(1, 4), new Tree());
      gameMap.putEntityOnMap(new Cell(1, 5), new Tree());

      gameMap.putEntityOnMap(new Cell(3, 1), new Rock());
      gameMap.putEntityOnMap(new Cell(3, 2), new Rock());
      gameMap.putEntityOnMap(new Cell(3, 3), new Rock());

      gameMap.putEntityOnMap(new Cell(4, 1), new PredatorMock());
      gameMap.putEntityOnMap(new Cell(4, 2), new PredatorMock());
      gameMap.putEntityOnMap(new Cell(4, 3), new PredatorMock());

      gameMap.putEntityOnMap(new Cell(5, 0), herbivore);
      gameMap.putEntityOnMap(new Cell(5, 1), new PredatorMock());
      gameMap.putEntityOnMap(new Cell(5, 2), farthestGrass);

      int numberOfGrasses = 0;
      do {
         mapRender.renderMap(herbivore);
         Cell toCell = herbivore.move();
         mapRender.printStep(++stepCounter, herbivore, gameMap.getCell(herbivore), toCell);

         numberOfGrasses = (int)gameMap.getEntities().stream()
           .filter(e -> e instanceof Grass)
           .count();

         if (numberOfGrasses == 1) {
            Grass actualGrass = (Grass) gameMap.getEntities().stream()
              .filter(e -> e instanceof Grass)
              .findAny()
              .orElse(null);
            assertEquals(farthestGrass, actualGrass);
         }
      } while (numberOfGrasses > 0);

      mapRender.renderMap(herbivore);

      assertEquals(13, stepCounter);
   }

   // H 0 0 0 P H
   // 0 T T H P H
   // H T H 0 P 0
   // 0 T 0 H P 0
   // H T 0 H H H
   // G T 0 H 0 0
   @Test
   void herbivore_shouldGoToGrassThrowAnotherHerbivores() {
      gameMap.putEntityOnMap(new Cell(0, 0), new HerbivoreMock());
      gameMap.putEntityOnMap(new Cell(0, 2), new HerbivoreMock());
      gameMap.putEntityOnMap(new Cell(0, 4), new HerbivoreMock());
      gameMap.putEntityOnMap(new Cell(0, 5), new Grass());

      gameMap.putEntityOnMap(new Cell(1, 1), new Tree());
      gameMap.putEntityOnMap(new Cell(1, 2), new Tree());
      gameMap.putEntityOnMap(new Cell(1, 3), new Tree());
      gameMap.putEntityOnMap(new Cell(1, 4), new Tree());
      gameMap.putEntityOnMap(new Cell(1, 5), new Tree());

      gameMap.putEntityOnMap(new Cell(2, 1), new Tree());
      gameMap.putEntityOnMap(new Cell(2, 2), new HerbivoreMock());

      gameMap.putEntityOnMap(new Cell(3, 1), new HerbivoreMock());
      gameMap.putEntityOnMap(new Cell(3, 3), new HerbivoreMock());
      gameMap.putEntityOnMap(new Cell(3, 4), new HerbivoreMock());
      gameMap.putEntityOnMap(new Cell(3, 5), new HerbivoreMock());

      gameMap.putEntityOnMap(new Cell(4, 0), new PredatorMock());
      gameMap.putEntityOnMap(new Cell(4, 1), new PredatorMock());
      gameMap.putEntityOnMap(new Cell(4, 2), new PredatorMock());
      gameMap.putEntityOnMap(new Cell(4, 3), new PredatorMock());
      gameMap.putEntityOnMap(new Cell(4, 4), new HerbivoreMock());

      gameMap.putEntityOnMap(new Cell(5, 0), herbivore);
      gameMap.putEntityOnMap(new Cell(5, 1), new HerbivoreMock());
      gameMap.putEntityOnMap(new Cell(5, 4), new HerbivoreMock());

      startGame();

      assertEquals(12, stepCounter);
   }

   void startGame() {
      int numberOfGrasses = 0;
      do {
         mapRender.renderMap(herbivore);
         Cell fromCell = gameMap.getCell(herbivore);
         Cell toCell = herbivore.move();
         mapRender.printStep(++stepCounter, herbivore, fromCell, toCell);

         numberOfGrasses = (int) gameMap.getEntities().stream()
           .filter(e -> e instanceof Grass)
           .count();

      } while (numberOfGrasses > 0);

      mapRender.renderMap(herbivore);
   }
}

