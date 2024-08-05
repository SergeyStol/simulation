package dev.sstol.entities.factories;

import dev.sstol.entities.Tree;

/**
 * @author Sergey Stol
 * 2024-07-30
 */
public class TreeFactory extends EntityFactory<Tree> {

   @Override
   public Tree createEntity() {
      return new Tree();
   }
}
