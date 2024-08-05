package dev.sstol.map;

import dev.sstol.entities.Entity;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author Sergey Stol
 * 2024-07-29
 */
public class GameMap {
   private final Map<Cell, Entity> entities = new HashMap<>();
   private final Map<Entity, Cell> entitiesIndex = new HashMap<>();
   private final Set<Consumer<Entity>> entityRemoveSubscribers = new HashSet<>();

   private final int columns;
   private final int rows;


   public GameMap(int columns, int rows) {
      this.columns = columns;
      this.rows = rows;
   }

   void registerEntityRemoveSubscriber(Consumer<Entity> subscriber) {
      entityRemoveSubscribers.add(subscriber);
   }

   void notifyEntityRemoveSubscriber(Entity entity) {
      entityRemoveSubscribers.forEach(subscriber -> subscriber.accept(entity));
   }

   public void putEntityOnMap(Cell cell, Entity entity) {
      if (entities.containsKey(cell)) {
         throw new IllegalArgumentException("Cell with col=" + cell.column() +
                                            " row=" + cell.row() + " is already busy");
      }
      if (entitiesIndex.containsKey(entity)) {
         throw new IllegalArgumentException("Entity { " + entity +
                                            " }" + " is already on the Map");
      }
      if (cell.column() < 0 && cell.column() >= columns && cell.row() < 0 && cell.row() >= rows) {
         throw new IndexOutOfBoundsException("Cell with col=" + cell.column() +
                                             " row=" + cell.row() + " is out of map");
      }
      entities.put(cell, entity);
      entitiesIndex.put(entity, cell);
   }

   public int getMapSize() {
      return columns * rows;
   }

   public int getNumberOfEntities() {
      return entities.size();
   }

   public boolean isCellBusy(Cell cell) {
      return entities.containsKey(cell);
   }

   public int getColumns() {
      return columns;
   }

   public int getRows() {
      return rows;
   }

   public Cell getCell(Entity entity) {
      return entitiesIndex.get(entity);
   }

   public Entity getEntity(Cell cell) {
      return entities.get(cell);
   }

   public void moveEntity(Entity entity, Cell toCell) {
      removeFromMap(entity);
      putEntityOnMap(toCell, entity);
   }

   public void moveEntityForce(Entity entity, Cell toCell) {
      removeFromMap(toCell);
      moveEntity(entity, toCell);
   }

   public void removeFromMap(Entity entity) {
      removeFromMap(entity, getCell(entity));
   }

   public void removeFromMap(Cell cell) {
      removeFromMap(getEntity(cell), cell);
   }

   public void removeFromMap(Entity entity, Cell cell) {
      entities.remove(cell);
      entitiesIndex.remove(entity);
      notifyEntityRemoveSubscriber(entity);
   }

   public int getNumberOf(Class<?> clazz) {
      return (int) entitiesIndex.keySet().stream()
        .filter(c -> c.getClass().equals(clazz))
        .count();
   }

   public boolean isOutOfMap(Cell cell) {
      return cell.column() < 0
             || cell.row() < 0
             || cell.column() > (getColumns() - 1)
             || cell.row() > (getRows() - 1);
   }

   public Collection<Entity> getEntities() {
      return entities.values();
   }
}
