package dev.sstol.entities;

/**
 * @author Sergey Stol
 * 2024-08-04
 */
public class CreatureStats {
   private final int attack;
   private final int totalHealth;
   private int currentHealth;

   private final int speed;

   public CreatureStats(int attack, int totalHealth, int speed) {
      this.attack = attack;
      this.totalHealth = totalHealth;
      this.currentHealth = totalHealth;
      this.speed = speed;
   }

   public int getAttack() {
      return attack;
   }

   public int getCurrentHealth() {
      return currentHealth;
   }

   public int getSpeed() {
      return speed;
   }

   public int getTotalHealth() {
      return totalHealth;
   }

   public void setCurrentHealth(int currentHealth) {
      this.currentHealth = currentHealth;
   }

   public void reduceCurrentHealth(int sum) {
      this.currentHealth -= sum;
   }
}
