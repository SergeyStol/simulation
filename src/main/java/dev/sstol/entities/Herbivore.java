package dev.sstol.entities;

import dev.sstol.map.Cell;
import dev.sstol.map.GameMap;
import dev.sstol.map.PathFinder;

/**
 * @author Sergey Stol
 * 2024-07-29
 */
public class Herbivore extends Creature {
   public Herbivore(GameMap gameMap, PathFinder pathFinder, CreatureStats stats) {
      super(gameMap, pathFinder, stats);
   }

   @Override
   public boolean isTransitive(Cell cell) {
      return gameMap.getEntity(cell) instanceof Herbivore;
   }

   @Override
   public boolean isFood(Cell cell) {
      return gameMap.getEntity(cell) instanceof Grass;
   }

   @Override
   public boolean isBlocker(Cell cell) {
      Entity entity = gameMap.getEntity(cell);
      return entity instanceof Tree || entity instanceof Rock || entity instanceof Predator;
   }

   @Override
   protected void attackFood(Cell foodCell) {
      restoreHealth();
      gameMap.moveEntityForce(this, foodCell);
   }
}

