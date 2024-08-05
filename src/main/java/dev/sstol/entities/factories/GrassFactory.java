package dev.sstol.entities.factories;

import dev.sstol.entities.Grass;

/**
 * @author Sergey Stol
 * 2024-07-30
 */
public class GrassFactory extends EntityFactory<Grass> {

   @Override
   public Grass createEntity() {
      return new Grass();
   }
}
