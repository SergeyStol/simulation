package dev.sstol.entities.factories;

import dev.sstol.entities.Creature;
import dev.sstol.entities.CreatureStats;
import dev.sstol.map.PathFinder;
import dev.sstol.map.GameMap;

/**
 * @author Sergey Stol
 * 2024-07-31
 */
public abstract class CreatureFactory extends EntityFactory<Creature> {
   protected final PathFinder pathFinder;
   protected final GameMap gameMap;
   protected final CreatureStats stats;

   protected CreatureFactory(GameMap gameMap, PathFinder pathFinder, CreatureStats stats) {
      this.gameMap = gameMap;
      this.pathFinder = pathFinder;
      this.stats = stats;
   }
}
