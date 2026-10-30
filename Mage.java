import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Mage implements CharacterClass {
        public class Colors {
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
}
    private int totalDamage = 0;
    private Random rand = new Random();
    public int getTotalDamage(){
        return totalDamage;
    }
    public int getRandomDamage(int min, int max, int critChance) {
    int dmg = rand.nextInt(max - min + 1) + min; // random mellan min–max

    boolean crit = rand.nextInt(100) < critChance; // critChance i %

    if (crit) {
        System.out.println("CRIT!");
        dmg *= 2;
    }
    return dmg;
}

    public static class SpecialAttacks {
        String name;
        int mana = 100;
        int castTime;
        int minDamage;
        int maxDamage;
        int critChance;
        int cooldown;
        long lastUsed;

        SpecialAttacks(String name, int mana, int castTime, int minDamage, int maxDamage, int critChance, int cooldown) {
            this.name = name;
            this.mana = mana;
            this.castTime = castTime;
            this.minDamage = minDamage;
            this.maxDamage = maxDamage;
            this.critChance = critChance;
            this.cooldown = cooldown;
            this.lastUsed = 0;
        }

        boolean canUse() {
            return (System.currentTimeMillis() - lastUsed) / 1000 >= cooldown;
        }

        void use() {
            lastUsed = System.currentTimeMillis();
        }
    }
        private Map<String, SpecialAttacks> specials = new HashMap<>();

        public Mage() {
        specials.put("1", new SpecialAttacks("Fire Ball",-100, 3, 399, 654, 20, 0));
        specials.put("2", new SpecialAttacks("",+40, 3, 40, 80, 40, 6));
        specials.put("3", new SpecialAttacks("",-60, 3, 150, 250, 20,10));
        specials.put("4", new SpecialAttacks("",-20, 3, 0, 0, 0, 20));
        specials.put("5", new SpecialAttacks("",+100, 3, 0, 0, 100, 60));
        specials.put("6", new SpecialAttacks("",0, 3, 0, 0, 0 ,120));
    }
    public void specialAttack(String key, Monster enemy) {

        SpecialAttacks atk = specials.get(key);
        if (atk == null) return;
        //NAME HERE
        if(key.equals("1")){
            if (atk.canUse()){
                if (!showCastBar(atk)) return;
                atk.use();
                int dmg = getRandomDamage(atk.minDamage, atk.maxDamage, atk.critChance);
                enemy.takeDamage(dmg);
                totalDamage += dmg;
                System.out.println(atk.name + " does " + dmg + " damage");
            } else {
                long cdLeft = atk.cooldown - 
                        ((System.currentTimeMillis() - atk.lastUsed) / 1000);
                               System.out.println(Colors.RED + "[CD] " + atk.name + " is on cooldown " + cdLeft + "s" + Colors.RESET);
            }   
            return;
        }
}

    private boolean showCastBar(SpecialAttacks atk) {
        long castDuration = atk.castTime * 1_000_000_000L;
        long startTime = System.nanoTime();
        int barWidth = 20;

        while (true) {
            long elapsed = System.nanoTime() - startTime;
            double progress = castDuration == 0
                    ? 1.0
                    : Math.min(1.0, (double) elapsed / castDuration);
            int filled = (int) (progress * barWidth);

            System.out.printf("\rCasting %s [%s%s] %3d%%",
                    atk.name,
                    "=".repeat(filled),
                    " ".repeat(barWidth - filled),
                    (int) (progress * 100));
            System.out.flush();

            if (progress >= 1.0) {
                System.out.println();
                return true;
            }

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                System.out.println();
                Thread.currentThread().interrupt();
                return false;
            }
        }
    }
}
