package dev.sstol;

import dev.sstol.entities.Creature;
import dev.sstol.entities.Grass;
import dev.sstol.entities.Herbivore;
import dev.sstol.entities.Predator;
import dev.sstol.map.Cell;
import dev.sstol.map.GameMap;
import dev.sstol.map.GameMapRandomCreaturesWrapper;

/**
 * @author Sergey Stol
 * 2024-07-31
 */
class Game {
   private final GameMap gameMap;
   private final MapRender mapRender;

   public Game(GameMap gameMap, MapRender mapRender) {
      this.gameMap = gameMap;
      this.mapRender = mapRender;
   }

   public void start() {
      mapRender.printStatistics();
      GameMapRandomCreaturesWrapper gameMapRndCreatures = new GameMapRandomCreaturesWrapper(gameMap);
      while (true) {
         Creature creature = gameMapRndCreatures.getRandomCreature();
         System.out.println();
         mapRender.renderMap(creature);
         Cell fromCell = gameMap.getCell(creature);
         Cell toCell = creature.move();
         mapRender.printStep(creature, fromCell, toCell);
         mapRender.renderMap(creature);

         if (gameMap.getNumberOf(Herbivore.class) == 0) {
            System.out.println("Predators win!");
            break;
         }

         if (gameMap.getNumberOf(Predator.class) == 0 || gameMap.getNumberOf(Grass.class) == 0) {
            System.out.println("Herbivores win!");
            break;
         }
      }
   }
}
