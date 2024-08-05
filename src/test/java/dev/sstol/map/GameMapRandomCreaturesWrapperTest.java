package dev.sstol.map;

import dev.sstol.entities.CreatureStats;
import dev.sstol.entities.Entity;
import dev.sstol.entities.Grass;
import dev.sstol.entities.Herbivore;
import dev.sstol.entities.Predator;
import dev.sstol.entities.Rock;
import dev.sstol.entities.Tree;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;


public class GameMapRandomCreaturesWrapperTest {

   GameMap gameMap = new GameMap(10, 10) {
      @Override
      public Collection<Entity> getEntities() {
         return List.of(
           predator1,
           predator2,
           herbivore1,
           herbivore2,
           grass,
           tree,
           rock
         );
      }
   };
   PathFinder pathFinder = new PathFinder(gameMap);
   Predator predator1 = new Predator(gameMap, pathFinder, new CreatureStats(2, 2, 2));
   Predator predator2 = new Predator(gameMap, pathFinder, new CreatureStats(2, 2, 2));
   Herbivore herbivore1 = new Herbivore(gameMap, pathFinder, new CreatureStats(2, 2, 2));
   Herbivore herbivore2 = new Herbivore(gameMap, pathFinder, new CreatureStats(2, 2, 2));
   Tree tree = new Tree();
   Rock rock = new Rock();
   Grass grass = new Grass();

   @Test
   void getRandomCreature_shouldRotateCreatureType() throws Exception {
      GameMapRandomCreaturesWrapper gameMapWrapper = new GameMapRandomCreaturesWrapper(gameMap);

      for (int i = 0; i < 10; i++) {
         assertInstanceOf(Predator.class, gameMapWrapper.getRandomCreature());
         assertInstanceOf(Herbivore.class, gameMapWrapper.getRandomCreature());
         assertInstanceOf(Predator.class, gameMapWrapper.getRandomCreature());
         assertInstanceOf(Herbivore.class, gameMapWrapper.getRandomCreature());
      }
   }

   @Test
   void getRandomCreature_shouldReturnNull_whenNoCreatures() throws Exception {
      GameMap gameMap = new GameMap(10, 10) {
         @Override
         public Collection<Entity> getEntities() {
            return List.of(
              grass,
              tree,
              rock
            );
         }
      };
      GameMapRandomCreaturesWrapper gameMapWrapper = new GameMapRandomCreaturesWrapper(gameMap);

      assertNull(gameMapWrapper.getRandomCreature());
   }
}
