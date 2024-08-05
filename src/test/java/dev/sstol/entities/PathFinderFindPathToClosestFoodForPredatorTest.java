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
public class PathFinderFindPathToClosestFoodForPredatorTest {

   final int PREDATOR_ATTACK = 2;
   final int PREDATOR_HEALTH = 2;
   final int PREDATOR_SPEED = 2;
   final int MAP_COLUMNS = 6;
   final int MAP_ROWS = 6;

   GameMap gameMap;
   PathFinder pathFinder;
   CreatureStats predatorStats = new CreatureStats(PREDATOR_ATTACK, PREDATOR_HEALTH, PREDATOR_SPEED);
   HerbivoreMock herbivore;
   Predator predator;
   int stepCounter;
   MapRender mapRender;

   @BeforeEach
   void beforeEach() {
      gameMap = new GameMap(MAP_COLUMNS, MAP_ROWS);
      pathFinder = new PathFinder(gameMap);
      mapRender = new MapRender(gameMap);
      herbivore = new HerbivoreMock();
      predator = new Predator(gameMap, pathFinder, predatorStats);
      stepCounter = 0;
   }

   // 0 0 0 0 0 H
   // 0 0 0 0 0 0
   // 0 0 0 0 0 0
   // 0 0 0 0 0 0
   // 0 0 0 0 0 0
   // P 0 0 0 0 0
   @Test
   void predator_shouldMoveToHerbivoreStrongly_whenHerbivoreLocateOutOfOneMotion1() {
      gameMap.putEntityOnMap(new Cell(0, 5), predator);
      gameMap.putEntityOnMap(new Cell(5, 0), herbivore);

      startGame();

      assertEquals(5, stepCounter);
   }

   // 0 0 0 0 0 P
   // 0 0 0 0 0 0
   // 0 0 0 0 0 0
   // 0 0 0 0 0 0
   // 0 0 0 0 0 0
   // H 0 0 0 0 0
   @Test
   void predator_shouldMoveToHerbivoreStrongly_whenHerbivoreLocateOutOfOneMotion2() {
      gameMap.putEntityOnMap(new Cell(5, 0), predator);
      gameMap.putEntityOnMap(new Cell(0, 5), herbivore);

      startGame();

      assertEquals(5, stepCounter);
   }

   // 0 0 0 0 R P
   // 0 T R 0 R 0
   // 0 T R 0 T 0
   // 0 T R 0 R 0
   // 0 T R 0 T 0
   // 0 H R 0 0 0
   @Test
   void predator_shouldMoveToHerbivoreStrongly_whenBlockersArePresent() {
      gameMap.putEntityOnMap(new Cell(1, 1), new Tree());
      gameMap.putEntityOnMap(new Cell(1, 2), new Tree());
      gameMap.putEntityOnMap(new Cell(1, 3), new Tree());
      gameMap.putEntityOnMap(new Cell(1, 4), new Tree());
      gameMap.putEntityOnMap(new Cell(1, 5), herbivore);

      gameMap.putEntityOnMap(new Cell(2, 1), new Rock());
      gameMap.putEntityOnMap(new Cell(2, 2), new Rock());
      gameMap.putEntityOnMap(new Cell(2, 3), new Rock());
      gameMap.putEntityOnMap(new Cell(2, 4), new Rock());
      gameMap.putEntityOnMap(new Cell(2, 5), new Rock());

      gameMap.putEntityOnMap(new Cell(4, 0), new Rock());
      gameMap.putEntityOnMap(new Cell(4, 1), new Rock());
      gameMap.putEntityOnMap(new Cell(4, 2), new Tree());
      gameMap.putEntityOnMap(new Cell(4, 3), new Rock());
      gameMap.putEntityOnMap(new Cell(4, 4), new Tree());

      gameMap.putEntityOnMap(new Cell(5, 0), predator);

      startGame();

      assertEquals(11, stepCounter);
   }

   // 0 0 0 0 0 P
   // 0 T 0 R P P
   // 0 T 0 R P P
   // 0 T 0 R P H
   // 0 T 0 0 0 0
   // H T 0 0 0 0
   @Test
   void predator_shouldGoToClosestHerbivore_whenMoreThanOneHerbivoreOnMap() {
      HerbivoreMock nearestHerbivore = new HerbivoreMock();
      HerbivoreMock farthestHerbivore = new HerbivoreMock();

      gameMap.putEntityOnMap(new Cell(0, 5), nearestHerbivore);

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

      gameMap.putEntityOnMap(new Cell(5, 0), predator);
      gameMap.putEntityOnMap(new Cell(5, 1), new PredatorMock());
      gameMap.putEntityOnMap(new Cell(5, 2), new PredatorMock());
      gameMap.putEntityOnMap(new Cell(5, 3), farthestHerbivore);

      int numberOfHerbivore = 0;
      do {
         mapRender.renderMap(predator);
         Cell toCell = predator.move();
         mapRender.printStep(++stepCounter, herbivore, gameMap.getCell(herbivore), toCell);

         numberOfHerbivore = (int)gameMap.getEntities().stream()
           .filter(e -> e instanceof Herbivore)
           .count();

         if (numberOfHerbivore == 1) {
            Herbivore actualHerbivore = (Herbivore) gameMap.getEntities().stream()
              .filter(e -> e instanceof Herbivore)
              .findAny()
              .orElse(null);
            assertEquals(farthestHerbivore, actualHerbivore);
         }
      } while (numberOfHerbivore > 0);

      mapRender.renderMap(predator);

      assertEquals(13, stepCounter);
   }

   // P 0 G 0 R P
   // 0 T T P P P
   // G T P 0 T 0
   // 0 T 0 G P 0
   // P T 0 G P P
   // H T 0 G 0 0
   @Test
   void predator_shouldGoToHerbivoreThrowAnotherPredatorsAndGrasses() {
      gameMap.putEntityOnMap(new Cell(0, 0), new PredatorMock());
      gameMap.putEntityOnMap(new Cell(0, 2), new Grass());
      gameMap.putEntityOnMap(new Cell(0, 4), new PredatorMock());
      gameMap.putEntityOnMap(new Cell(0, 5), herbivore);

      gameMap.putEntityOnMap(new Cell(1, 1), new Tree());
      gameMap.putEntityOnMap(new Cell(1, 2), new Tree());
      gameMap.putEntityOnMap(new Cell(1, 3), new Tree());
      gameMap.putEntityOnMap(new Cell(1, 4), new Tree());
      gameMap.putEntityOnMap(new Cell(1, 5), new Tree());

      gameMap.putEntityOnMap(new Cell(2, 0), new Grass());
      gameMap.putEntityOnMap(new Cell(2, 1), new Tree());
      gameMap.putEntityOnMap(new Cell(2, 2), new PredatorMock());

      gameMap.putEntityOnMap(new Cell(3, 1), new PredatorMock());
      gameMap.putEntityOnMap(new Cell(3, 3), new Grass());
      gameMap.putEntityOnMap(new Cell(3, 4), new Grass());
      gameMap.putEntityOnMap(new Cell(3, 5), new Grass());

      gameMap.putEntityOnMap(new Cell(4, 0), new Rock());
      gameMap.putEntityOnMap(new Cell(4, 1), new PredatorMock());
      gameMap.putEntityOnMap(new Cell(4, 2), new Tree());
      gameMap.putEntityOnMap(new Cell(4, 3), new PredatorMock());
      gameMap.putEntityOnMap(new Cell(4, 4), new PredatorMock());

      gameMap.putEntityOnMap(new Cell(5, 0), predator);
      gameMap.putEntityOnMap(new Cell(5, 1), new PredatorMock());
      gameMap.putEntityOnMap(new Cell(5, 4), new PredatorMock());

      startGame();

      assertEquals(12, stepCounter);
   }

   void startGame() {
      int numberOfHerbivores = 0;
      do {
         mapRender.renderMap(predator);
         Cell fromCell = gameMap.getCell(predator);
         Cell toCell = predator.move();
         mapRender.printStep(++stepCounter, predator, fromCell, toCell);

         numberOfHerbivores = (int) gameMap.getEntities().stream()
           .filter(e -> e instanceof Herbivore)
           .count();

      } while (numberOfHerbivores > 0);

      mapRender.renderMap(predator);
   }
}

