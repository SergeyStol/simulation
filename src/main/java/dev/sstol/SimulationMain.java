package dev.sstol;

import dev.sstol.entities.CreatureStats;
import dev.sstol.map.PathFinder;
import dev.sstol.entities.factories.EntityFactory;
import dev.sstol.entities.factories.GrassFactory;
import dev.sstol.entities.factories.HerbivoreFactory;
import dev.sstol.entities.factories.PredatorFactory;
import dev.sstol.entities.factories.RockFactory;
import dev.sstol.entities.factories.TreeFactory;
import dev.sstol.map.Cell;
import dev.sstol.map.GameMap;
import dev.sstol.map.GameMapRandomFreeCellsGeneratorWrapper;

public class SimulationMain {
   static int NUMBER_OF_COLUMNS = 8;
   static int NUMBER_OF_ROWS = 8;

   static int START_NUMBER_OF_HERBIVORES = 5;
   static int START_NUMBER_OF_PREDATORS = 5;
   static int START_NUMBER_OF_GRASSES = 7;
   static int START_NUMBER_OF_TREES = 5;
   static int START_NUMBER_OF_ROCKS = 5;

   static int HERBIVORE_ATTACK = 3;
   static int HERBIVORE_HEALTH = 7;
   static int HERBIVORE_SPEED = 2;

   static int PREDATOR_ATTACK = 5;
   static int PREDATOR_HEALTH = 9;
   static int PREDATOR_SPEED = 2;

   public static void main(String[] args) {
      var gameMap = new GameMap(NUMBER_OF_COLUMNS, NUMBER_OF_ROWS);
      var pathFinder = new PathFinder(gameMap);
      addRandomEntities(gameMap, pathFinder);
      MapRender mapRender = new MapRender(gameMap);
      Game game = new Game(gameMap, mapRender);
      game.start();
   }

   private static void addRandomEntities(GameMap gameMap, PathFinder pathFinder) {
      var gameMapRandom = new GameMapRandomFreeCellsGeneratorWrapper(gameMap);

      var herbivoreFactory = new HerbivoreFactory(gameMap, pathFinder,
        new CreatureStats(HERBIVORE_ATTACK, HERBIVORE_HEALTH, HERBIVORE_SPEED));
      addEntity(herbivoreFactory, START_NUMBER_OF_HERBIVORES, gameMapRandom);

      var predatorFactory = new PredatorFactory(gameMap, pathFinder,
        new CreatureStats(PREDATOR_ATTACK, PREDATOR_HEALTH, PREDATOR_SPEED));
      addEntity(predatorFactory, START_NUMBER_OF_PREDATORS, gameMapRandom);

      addEntity(new GrassFactory(), START_NUMBER_OF_GRASSES, gameMapRandom);
      addEntity(new RockFactory(), START_NUMBER_OF_ROCKS, gameMapRandom);
      addEntity(new TreeFactory(), START_NUMBER_OF_TREES, gameMapRandom);
   }

   private static void addEntity(EntityFactory<?> entityFactory,
                                 int numberOfCreature,
                                 GameMapRandomFreeCellsGeneratorWrapper gameMapRandom) {
      for (int i = 0; i < numberOfCreature; i++) {
         var herbivore = entityFactory.createEntity();
         Cell randomFreeCell = gameMapRandom.getRandomFreeCell();
         gameMapRandom.putEntityOnMap(randomFreeCell, herbivore);
      }
   }
}

