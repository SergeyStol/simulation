package dev.sstol.map;

import dev.sstol.entities.Entity;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * @author Sergey Stol
 * 2024-07-31
 */
public class GameMapRandomFreeCellsGeneratorWrapper {
   private final GameMap gameMap;
   private final Set<Cell> freeCells = new HashSet<>();
   private final Random random = new Random();

   public GameMapRandomFreeCellsGeneratorWrapper(GameMap gameMap) {
      this.gameMap = gameMap;

      if (gameMap.getMapSize() - gameMap.getNumberOfEntities() > 0) {
         fillUpFreeCells();
      }
   }

   public Cell getRandomFreeCell() {
      int rnd = random.nextInt(freeCells.size());
      return freeCells.stream().skip(rnd).findFirst().orElse(null);
   }

   public void putEntityOnMap(Cell cell, Entity entity) {
      freeCells.remove(cell);
      gameMap.putEntityOnMap(cell, entity);
   }

   private void fillUpFreeCells() {
      for (int column = 0; column < gameMap.getColumns(); column++) {
         for (int row = 0; row < gameMap.getRows(); row++) {
            Cell cell = new Cell(column, row);
            if (!gameMap.isCellBusy(cell)) {
               freeCells.add(cell);
            }
         }
      }
   }
}
