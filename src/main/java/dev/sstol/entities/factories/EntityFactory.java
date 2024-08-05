package dev.sstol.entities.factories;

import dev.sstol.entities.Entity;

/**
 * @author Sergey Stol
 * 2024-07-30
 */
public abstract class EntityFactory<T extends Entity> {
   public abstract T createEntity();
}
