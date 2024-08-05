package dev.sstol.map;

import dev.sstol.entities.Creature;
import dev.sstol.entities.Entity;
import dev.sstol.entities.Herbivore;
import dev.sstol.entities.Predator;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author Sergey Stol
 * 2024-07-31
 */
public class GameMapRandomCreaturesWrapper implements Consumer<Entity> {
   private final GameMap gameMap;
   private final Set<Predator> predators = new HashSet<>();
   private final Set<Herbivore> herbivores = new HashSet<>();
   private final Random random = new Random();
   private boolean switcher = false;

   public GameMapRandomCreaturesWrapper(GameMap gameMap) {
      this.gameMap = gameMap;
      fillUpCreaturesCache();
      gameMap.registerEntityRemoveSubscriber(this);
   }

   private void fillUpCreaturesCache() {
      gameMap.getEntities().forEach(e -> {
         switch (e) {
            case Predator p -> predators.add(p);
            case Herbivore h -> herbivores.add(h);
            default -> {}
         }
      });
   }

   @Override
   public void accept(Entity entity) {
      predators.remove(entity);
      herbivores.remove(entity);
   }

   public Creature getRandomCreature() {
      if (switcher) {
         Herbivore herbivore = herbivores.stream().findAny().orElse(null);
         if (herbivore == null) {
            fillUpCreaturesCache();
            herbivore = herbivores.stream().findAny().orElse(null);
         }
         herbivores.remove(herbivore);
         switcher = !switcher;
         return herbivore;
      }

      Predator predator = predators.stream().findAny().orElse(null);
      if (predator == null) {
         fillUpCreaturesCache();
         predator = predators.stream().findAny().orElse(null);
      }
      predators.remove(predator);
      switcher = !switcher;
      return predator;
   }
}
