package dev.sstol.entities.factories;

import dev.sstol.entities.CreatureStats;
import dev.sstol.map.PathFinder;
import dev.sstol.map.GameMap;
import dev.sstol.entities.Herbivore;

/**
 * @author Sergey Stol
 * 2024-07-31
 */
public class HerbivoreFactory extends CreatureFactory {

   public HerbivoreFactory(GameMap gameMap, PathFinder pathFinder, CreatureStats stats) {
      super(gameMap, pathFinder, stats);
   }

   @Override
   public Herbivore createEntity() {
      return new Herbivore(gameMap, pathFinder,
        new CreatureStats(stats.getAttack(), stats.getTotalHealth(), stats.getSpeed()));
   }

}
