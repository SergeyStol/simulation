package dev.sstol.entities;

import dev.sstol.map.Cell;
import dev.sstol.map.GameMap;
import dev.sstol.map.PathFinder;

/**
 * @author Sergey Stol
 * 2024-07-29
 */
public class Predator extends Creature {
   public Predator(GameMap gameMap, PathFinder pathFinder, CreatureStats stats) {
      super(gameMap, pathFinder, stats);
   }

   @Override
   public boolean isTransitive(Cell cell) {
      Entity entity = gameMap.getEntity(cell);
      return entity instanceof Predator || entity instanceof Grass;
   }

   @Override
   public boolean isFood(Cell cell) {
      return gameMap.getEntity(cell) instanceof Herbivore;
   }

   @Override
   public boolean isBlocker(Cell cell) {
      Entity entity = gameMap.getEntity(cell);
      return entity instanceof Tree || entity instanceof Rock;
   }

   @Override
   protected void attackFood(Cell foodCell) {
      Entity entityFood = gameMap.getEntity(foodCell);
      if (!(entityFood instanceof Creature creatureFood)) {
         throw new IllegalArgumentException("The predator" + this + "trying to attack non-creature unit: " + entityFood);
      }
      creatureFood.getDamage(this);
      if (creatureFood.isDead()) {
         gameMap.moveEntityForce(this, foodCell);
         restoreHealth();
         return;
      }

      getDamage(creatureFood);

      if (isDead()) {
         gameMap.removeFromMap(this);
      }
   }
}
