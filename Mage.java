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
    private boolean pyroHaste = false;
    private long pyroHasteEndTime = 0;
    private boolean combustionActive = false;
    private long combustionEndTime = 0;
    private boolean trinketActive = false;
    private long trinketEndTime = 0;
    private int spellPower = 300;
    private int totalMana = 3000; //TOTAL MANA ON MAGE

    public int getTotalDamage(){
        return totalDamage;
    }
    public int getRandomDamage(int min, int max, int critChance) {
    int dmg = rand.nextInt(max - min + 1) + min; // random mellan min–max

    if (isCombustionActive()) {
        critChance = 100;
    }
    boolean crit = rand.nextInt(100) < critChance; // critChance i %

    if (crit) {
        System.out.println(Colors.RED + "CRIT!" + Colors.RESET);
        dmg *= 2;
    }
    return dmg;
}

    public static class SpecialAttacks {
        String name;
        int manaCost;
        int castTime;
        int minDamage;
        int maxDamage;
        int critChance;
        int cooldown;
        long lastUsed;

        SpecialAttacks(String name, int manaCost, int castTime, int minDamage, int maxDamage, int critChance, int cooldown) {
            this.name = name;
            this.manaCost = manaCost;
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
        specials.put("1", new SpecialAttacks("Fire Ball",139, 2, 399, 654, 20, 0));
        specials.put("2", new SpecialAttacks("Fire Blast!",50, 0, 137, 159, 100, 6));
        specials.put("3", new SpecialAttacks("Pyro Blast!",399, 4, 875, 1139, 20,0));
        specials.put("4", new SpecialAttacks("Evocation",+0, 3, 0, 0, 0, 20));
        specials.put("5", new SpecialAttacks("Combustion",100, 0, 0, 0, 100, 60));
        specials.put("6", new SpecialAttacks("Mind Quickening Gem",0, 0, 0, 0, 0 ,120));
    }
    public void specialAttack(String key, Monster enemy) {

        SpecialAttacks atk = specials.get(key);
        if (atk == null) return;
        
        //Fire Ball
        if(key.equals("1")){
            if (atk.canUse()){
                if (totalMana < atk.manaCost) {
                    System.out.println("Not enough mana!");
                    return;
                }
                if (!showCastBar(atk, atk.castTime)) return;
                totalMana -= atk.manaCost;
                atk.use();
                int dmg = getRandomDamage(atk.minDamage, atk.maxDamage, atk.critChance);
                int merDamage = dmg + spellPower;
                if(trinketActive) dmg = merDamage;
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
        //Fire Blast
        if(key.equals("2")){
            if (atk.canUse()){
                if (totalMana < atk.manaCost) {
                    System.out.println("Not enough mana!");
                    return;
                }
                totalMana -= atk.manaCost;
                atk.use();
                pyroHaste = true;
                pyroHasteEndTime = System.currentTimeMillis() + 10000; //10 seconds
                int dmg = getRandomDamage(atk.minDamage, atk.maxDamage, atk.critChance);
                int merDamage = dmg + spellPower;
                if(trinketActive) dmg = merDamage;
                enemy.takeDamage(dmg);
                totalDamage += dmg;
                System.out.println(atk.name + " does " + dmg + " damage");
                System.out.println(Colors.YELLOW + "Pyro Blast empowered!" + Colors.RESET);
            } else {
                long cdLeft = atk.cooldown - 
                        ((System.currentTimeMillis() - atk.lastUsed) / 1000);
                               System.out.println(Colors.RED + "[CD] " + atk.name + " is on cooldown " + cdLeft + "s" + Colors.RESET);
            }   
            return;
        }
        //Pyro Blast
        if(key.equals("3")){
            if (atk.canUse()){
                if (totalMana < atk.manaCost) {
                    System.out.println("Not enough mana!");
                    return;
                }
                boolean hasPyroHaste = pyroHaste
                        && System.currentTimeMillis() < pyroHasteEndTime;
                int castTime = hasPyroHaste ? 1 : atk.castTime; //Cast time with Pyro haste
                if (!showCastBar(atk, castTime)) return;
                totalMana -= atk.manaCost;
                atk.use();
                if (hasPyroHaste) {
                    pyroHaste = false;
                }
                int dmg = getRandomDamage(atk.minDamage, atk.maxDamage, atk.critChance);
                int merDamage = dmg + spellPower;
                if(trinketActive) dmg = merDamage;
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
        //EVOCATION
        if(key.equals("4")){
            if (atk.canUse()){
                if (!showCastBar(atk, atk.castTime)) return;
                atk.use();
                int manaBefore = totalMana;
                totalMana = Math.min(3000, totalMana + 3000); //3000 is amount restores
                int manaRestored = totalMana - manaBefore;
                System.out.println(atk.name + " have restored: " + manaRestored + " Mana");
            } else {
                long cdLeft = atk.cooldown - 
                        ((System.currentTimeMillis() - atk.lastUsed) / 1000);
                               System.out.println(Colors.RED + "[CD] " + atk.name + " is on cooldown " + cdLeft + "s" + Colors.RESET);
            }   
            return;
        }
        //Combustion
        if(key.equals("5")){
            if (atk.canUse()){
                if (totalMana < atk.manaCost) {
                    System.out.println("Not enough mana!");
                    return;
                }
                totalMana -= atk.manaCost;
                atk.use();
                combustionActive = true;
                combustionEndTime = System.currentTimeMillis() + 10000; //10 seconds
                System.out.println(Colors.YELLOW + "Combustion activated!" + Colors.RESET);
            } else {
                long cdLeft = atk.cooldown - 
                        ((System.currentTimeMillis() - atk.lastUsed) / 1000);
                               System.out.println(Colors.RED + "[CD] " + atk.name + " is on cooldown " + cdLeft + "s" + Colors.RESET);
            }   
            return;
        }
        //TRINKET
        if(key.equals("6")){
            if (atk.canUse()){
                atk.use();
                trinketActive = true;
                trinketEndTime = System.currentTimeMillis() + 15000; // 15 seconds
                System.out.println(Colors.YELLOW + "Mind Quickening Gem activated!" + Colors.RESET);
            } else {
                long cdLeft = atk.cooldown - 
                        ((System.currentTimeMillis() - atk.lastUsed) / 1000);
                               System.out.println(Colors.RED + "[CD] " + atk.name + " is on cooldown " + cdLeft + "s" + Colors.RESET);
            }   
            return;
        }
}

    private boolean showCastBar(SpecialAttacks atk, int castTime) {
        if (isCombustionActive() && castTime > 0) {
            castTime = Math.max(1, castTime / 2);
        }
        long castDuration = castTime * 1_000_000_000L;
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

    
    
    public void updateBuffs() {
        long now = System.currentTimeMillis();

        isCombustionActive();

        if (trinketActive && now >= trinketEndTime) {
            trinketActive = false;
            System.out.println(Colors.YELLOW
                    + "Mind Quickening Gem has worn off!" + Colors.RESET);
        }
        if (pyroHaste && now >= pyroHasteEndTime) {
            pyroHaste = false;
            System.out.println(Colors.YELLOW + "Pyro Haste has worn off!" + Colors.RESET);
        }
    }

    private boolean isCombustionActive() {
        if (combustionActive && System.currentTimeMillis() >= combustionEndTime) {
            combustionActive = false;
            System.out.println(Colors.YELLOW + "Combustion has worn off!" + Colors.RESET);
        }
        return combustionActive;
    }
}
