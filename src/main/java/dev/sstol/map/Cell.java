package dev.sstol.map;

/**
 * @author Sergey Stol
 * 2024-07-29
 */
public record Cell(int column, int row) {
   public Cell withOffset(int columnOffset, int rowOffset) {
      return new Cell(column + columnOffset, row + rowOffset);
   }
}
