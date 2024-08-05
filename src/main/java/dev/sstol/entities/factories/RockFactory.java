package dev.sstol.entities.factories;

import dev.sstol.entities.Rock;

/**
 * @author Sergey Stol
 * 2024-07-30
 */
public class RockFactory extends EntityFactory<Rock> {

   @Override
   public Rock createEntity() {
      return new Rock();
   }
}
