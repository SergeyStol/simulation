package dev.sstol.entities;

import dev.sstol.MapRender;
import dev.sstol.map.Cell;
import dev.sstol.map.GameMap;
import dev.sstol.map.PathFinder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Sergey Stol
 * 2024-08-05
 */
public class PredatorAttackHerbivoreTest {

   //    0   1   2
   //  0 P   0   H

   // P - Predator
   // H - Herbivore

   @Test
   void predator_shouldEatHerbivoreOnTheSecondAttempt_AndLost4Lives() {
      GameMap gameMap = new GameMap(3, 1);
      MapRender mapRender = new MapRender(gameMap);
      PathFinder pathFinder = new PathFinder(gameMap);
      Predator predator = new Predator(gameMap, pathFinder, new CreatureStats(5, 8, 2));
      Herbivore herbivore = new Herbivore(gameMap, pathFinder, new CreatureStats(2, 7, 2));

      gameMap.putEntityOnMap(new Cell(0, 0), predator);
      gameMap.putEntityOnMap(new Cell(2, 0), herbivore);

      mapRender.renderMap(predator);
      Cell fromCell = gameMap.getCell(predator);
      Cell toCell = predator.move();
      mapRender.printStep(1, predator, fromCell, toCell);
      mapRender.renderMap(predator);
      mapRender.printStep(2, predator, fromCell, toCell);
      predator.move();
      mapRender.renderMap(predator);
   }
}
