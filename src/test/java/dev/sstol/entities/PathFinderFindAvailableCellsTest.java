package dev.sstol.entities;

import dev.sstol.MapRender;
import dev.sstol.map.Cell;
import dev.sstol.map.GameMap;
import dev.sstol.map.PathFinder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PathFinderFindAvailableCellsTest {

   GameMap gameMap;
   MapRender mapRender;
   PathFinder pathFinder;
   CreatureStats stats = new CreatureStats(2, 2, 2);
   Herbivore herbivore1;
   Herbivore herbivore2;
   Predator predator1;
   Predator predator2;

   //    0   1   2   3
   //  0 0   0   0   G
   //  1 0   H   0   P
   //  2 0   T   R   0
   //  3 G   P   H   G

   // P - Predator
   // H - Herbivore
   // T - Tree
   // R - Rock
   // G - Grass
   @BeforeEach
   void setUp() {
      gameMap = new GameMap(4, 4);
      pathFinder = new PathFinder(gameMap);
      mapRender = new MapRender(gameMap);
      herbivore1 = new Herbivore(gameMap, pathFinder,stats);
      herbivore2 = new Herbivore(gameMap, pathFinder,stats);
      predator1 = new Predator(gameMap, pathFinder,stats);
      predator2 = new Predator(gameMap, pathFinder,stats);

      // column 0
      gameMap.putEntityOnMap(new Cell(0, 3), new Grass());
      // column 1
      gameMap.putEntityOnMap(new Cell(1, 1), herbivore1);
      gameMap.putEntityOnMap(new Cell(1, 2), new Tree());
      gameMap.putEntityOnMap(new Cell(1, 3), predator1);
      // column 2
      gameMap.putEntityOnMap(new Cell(2, 2), new Rock());
      gameMap.putEntityOnMap(new Cell(2, 3), herbivore2);
      // column 3
      gameMap.putEntityOnMap(new Cell(3, 0), new Grass());
      gameMap.putEntityOnMap(new Cell(3, 1), predator2);
      gameMap.putEntityOnMap(new Cell(3, 3), new Grass());
   }

   //    0   1   2   3
   //  0 0   0   0   G
   //  1 0   H   0   P
   //  2 0   T   R   0
   //  3 G   P   H   G

   // P - Predator
   // H - Herbivore
   // T - Tree
   // R - Rock
   // G - Grass

   // Available cells for Herbivore(1, 1) with speed 2 are:
   // (0, 1) (0, 2) (0, 0) (1, 0) (2, 0) (2, 1) (1, 1)
   @Test
   void findAvailableCells() {
      Set<Cell> expectedAvailableCells = new HashSet<>(){{
            add(new Cell(1, 1));
            add(new Cell(0, 1));
            add(new Cell(0, 2));
            add(new Cell(0, 0));
            add(new Cell(1, 0));
            add(new Cell(2, 0));
            add(new Cell(2, 1));
         }};
      check(herbivore1, expectedAvailableCells);
   }

   // Available cells for Predator(3, 1) with speed 2 are:
   // (2, 0) (2, 1) (1, 1) (3, 2) (3, 1)
   @Test
   void findAvailableCellsPredator3_1() {
      Set<Cell> expectedAvailableCells = new HashSet<>(){{
         add(new Cell(3, 1));
         add(new Cell(2, 0));
         add(new Cell(2, 1));
         add(new Cell(1, 1));
         add(new Cell(3, 2));
      }};

      check(predator2, expectedAvailableCells);
   }

   void check(Creature creature, Set<Cell> expectedCells) {
      mapRender.renderMap(creature);

      Set<Cell> actualCells = pathFinder.findAvailableCells(creature);

      actualCells.forEach(toCell -> {
         gameMap.moveEntityForce(creature, toCell);
         mapRender.renderMap(creature);
      });
      assertEquals(expectedCells, actualCells);
   }

   //    0   1   2   3
   //  0 0   0   0   G
   //  1 0   H   0   P
   //  2 0   T   R   0
   //  3 G   P   H   G

   // Available cells for Predator(1, 3) with speed 2 are:
   // (0, 2) (2, 3) (1, 3)
   @Test
   void findAvailableCellsPredator1_3() {
      Set<Cell> expectedAvailableCells = new HashSet<>(){{
         add(new Cell(1, 3));
         add(new Cell(0, 2));
         add(new Cell(2, 3));
      }};

      check(predator1, expectedAvailableCells);
   }
}