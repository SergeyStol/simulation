package dev.sstol.entities.factories;

import dev.sstol.entities.CreatureStats;
import dev.sstol.map.PathFinder;
import dev.sstol.map.GameMap;
import dev.sstol.entities.Predator;

/**
 * @author Sergey Stol
 * 2024-07-31
 */
public class PredatorFactory extends CreatureFactory {

   public PredatorFactory(GameMap gameMap, PathFinder pathFinder, CreatureStats stats) {
      super(gameMap, pathFinder, stats);
   }

   @Override
   public Predator createEntity() {
      return new Predator(gameMap, pathFinder,
        new CreatureStats(stats.getAttack(), stats.getTotalHealth(), stats.getSpeed()));
   }
}
