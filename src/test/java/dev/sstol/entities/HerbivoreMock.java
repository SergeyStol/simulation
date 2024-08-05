package dev.sstol.entities;

import dev.sstol.map.GameMap;
import dev.sstol.map.PathFinder;

/**
 * @author Sergey Stol
 * 2024-08-04
 */
class HerbivoreMock extends Herbivore {

   public HerbivoreMock() {
      super(
        new GameMap(0, 0),
        new PathFinder(new GameMap(0, 0)),
        new CreatureStats(0, 0, 0)
      );
   }
}
