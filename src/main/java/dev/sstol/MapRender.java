package dev.sstol;

import dev.sstol.entities.Creature;
import dev.sstol.entities.Entity;
import dev.sstol.entities.Grass;
import dev.sstol.entities.Herbivore;
import dev.sstol.entities.Predator;
import dev.sstol.entities.Rock;
import dev.sstol.entities.Tree;
import dev.sstol.map.Cell;
import dev.sstol.map.GameMap;

/**
 * @author Sergey Stol
 * 2024-07-31
 */
public class MapRender {

   private final GameMap gameMap;
   private final int columns;
   private final int rows;

   public MapRender(GameMap gameMap) {
      this.gameMap = gameMap;
      this.columns = gameMap.getColumns();
      this.rows = gameMap.getRows();
   }

   public void printStep(Creature creature, Cell fromCell, Cell toCell) {
      System.out.println(creature + ": " + fromCell + " -> " + toCell);
   }

   public void printStep(int stepNumber, Creature creature, Cell fromCell, Cell toCell) {
      System.out.print("Step №" + stepNumber + " ");
      printStep(creature, fromCell, toCell);
   }

   public void renderMap(Entity activeEntity) {
      System.out.print("\t");
      for (int column = 0; column < columns; column++) {
         System.out.printf("\t%02d \t", column);
      }
      System.out.println();
      for (int row = 0; row < rows; row++) {
         for (int column = 0; column < columns; column++) {

            if (column == 0) {
               System.out.printf("%02d\t", row);
            }

            Entity entity = gameMap.getEntity(new Cell(column, row));
            String img = getEntityImage(activeEntity, entity);
            System.out.printf("\t%s\t", img);
         }
         System.out.println();
      }
   }

   private static String getEntityImage(Entity activeEntity, Entity entity) {
      String img = switch (entity) {
         case Predator p -> "\uD83D\uDC3A" + p.getCurrentHealth() + "/" + p.getTotalHealth();
         case Tree ignored -> "\uD83C\uDF33 ";
         case Herbivore h -> "\uD83D\uDC30" + h.getCurrentHealth() + "/" + h.getTotalHealth();
         case Rock ignored -> "\uD83D\uDDFB ";
         case Grass ignored -> "\uD83C\uDF3F ";
         case null, default -> "   ";
      };

      if (entity != null && entity.equals(activeEntity)) {
         if (activeEntity instanceof Herbivore h) {
            img = "\uD83D\uDC2E" + h.getCurrentHealth() + "/" + h.getTotalHealth();
         }
         if (activeEntity instanceof Predator p) {
            img = "\uD83E\uDD81" + p.getCurrentHealth() + "/" + p.getTotalHealth();
         }
      }
      return img;
   }

   public void printStatistics() {
      System.out.println("getNumberOf(Herbivore) = " + gameMap.getNumberOf(Herbivore.class));
      System.out.println("getNumberOf(Predator) = " + gameMap.getNumberOf(Predator.class));
      System.out.println("getNumberOf(Creature) = " + gameMap.getNumberOf(Creature.class));
      System.out.println("getNumberOf(Grass) = " + gameMap.getNumberOf(Grass.class));
      System.out.println("getNumberOf(Rock) = " + gameMap.getNumberOf(Rock.class));
      System.out.println("getNumberOf(Tree) = " + gameMap.getNumberOf(Tree.class));
   }
}
