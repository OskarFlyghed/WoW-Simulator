    
   public class Monster {

    private String namn;
    private int hp;
    private int damage;

    public Monster(String namn, int hp, int damage) {
        this.namn = namn;
        this.hp = hp;
        this.damage = damage;
    }

    public void takeDamage(int dmg) {
        hp -= dmg;
        // System.out.print("\n" + namn + " tog: " + dmg + " skada");
        if (hp < 0) hp = 0;
    }
    public boolean isDead(){
    return hp <= 0;
    }
        
    public int getHp() {
        return hp;
    }
    public int getDamage(){
        return damage;
    }

    public String getNamn() {
        return namn;
    }
    
}