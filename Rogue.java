import java.util.HashMap;
import java.util.Map;
import java.util.Random;
public class Rogue {
    public class Colors {
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
}
    private long lastAutoAttack = System.currentTimeMillis();
    private int autoInterval = 1000; // 1 sekund
    private Random rand = new Random();
    private boolean hasteOneActive = false;
    private long hasteOneEndTime = 0;
    private boolean hasteTwoActive = false;
    private long hasteTwoEndTime = 0;
    private boolean trinketActive = false;
    private long trinketEndTime;
    private int attackpower = 250; //trinket attack power
    private int totalDamage = 0;
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
        int energy = 100;
        int minDamage;
        int maxDamage;
        int critChance;
        int cooldown;
        long lastUsed;

        SpecialAttacks(String name, int energy, int minDamage, int maxDamage, int critChance, int cooldown) {
            this.name = name;
            this.energy = energy;
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

    public Rogue() {
        specials.put("1", new SpecialAttacks("Saber Slash",-40, 80, 120, 20, 1));
        specials.put("2", new SpecialAttacks("Throw Knife",+40, 40, 80, 40, 6));
        specials.put("3", new SpecialAttacks("Evicerate",-60, 150, 250, 20,10));
        specials.put("4", new SpecialAttacks("Slice and Dice",-20, 0, 0, 0, 20));
        specials.put("5", new SpecialAttacks("Adrenaline Rush",+100, 0, 0, 100, 60));
        specials.put("6", new SpecialAttacks("Berzerk",0, 0, 0, 0 ,120));
    }
    //Autoattack
    public void autoAttack(Monster enemy) {
        long now = System.currentTimeMillis();
        int currentInterval = autoInterval;
        if(hasteOneActive) currentInterval = autoInterval / 2; //SLICE AND DICE *2 speed
        if(hasteTwoActive) currentInterval = autoInterval / 3; //ADRENALINE RUSH *3 speed
        if (hasteOneActive && hasteTwoActive) currentInterval = autoInterval / 2 / 3; //Both // *5 speed
        if (now - lastAutoAttack >= currentInterval) {
            int dmg = getRandomDamage(48, 103, 15); //min, max , crit chance
            int merDamage = dmg + attackpower;
            if(trinketActive) dmg = merDamage;
            enemy.takeDamage(dmg);
            totalDamage += dmg;
            lastAutoAttack = now;
            System.out.println(Colors.GREEN + "                               Auto does " + dmg + " damage" + Colors.RESET);
        if (hasteOneActive && now >= hasteOneEndTime){
            hasteOneActive = false;
            System.out.println(Colors.YELLOW + "Slice and Dice have worn off!" + Colors.RESET);
        }
        if (hasteTwoActive && now >= hasteTwoEndTime){
            hasteTwoActive = false;
            System.out.println(Colors.YELLOW + "Adrenaline rush has worn off!" + Colors.RESET);
        }
      
        if(trinketActive && now >= trinketEndTime){
            trinketActive = false;
            System.out.println(Colors.YELLOW + "Berzerk has worn off!" + Colors.RESET);
        }
        }
    }
    //Special attacks
    public void specialAttack(String key, Monster enemy) {

        SpecialAttacks atk = specials.get(key);
        //SLICE AND DICE
        if(key.equals("4")){
            if (atk.canUse()){
                atk.use();
                hasteOneActive = true;
                hasteOneEndTime = System.currentTimeMillis() + 10000; //10 seconds
                System.out.println("Slice and Dice activated!");
            } else {
                long cdLeft = atk.cooldown - 
                        ((System.currentTimeMillis() - atk.lastUsed) / 1000);
                               System.out.println(Colors.RED + "[CD] " + atk.name + " is on cooldown " + cdLeft + "s" + Colors.RESET);
            }   
            return;
        }
        //ADRENALINE RUSH
        if(key.equals("5")){
            if(atk.canUse()){
                atk.use();
                hasteTwoActive = true;
                hasteTwoEndTime = System.currentTimeMillis() + 15000; //15 seconds
                System.out.println("Adrenaline Rush activated!");
            } else{
                long cdLeft = atk.cooldown -
                        ((System.currentTimeMillis() - atk.lastUsed) / 1000);
                       System.out.println(Colors.RED + "[CD]" + atk.name + " is on cooldown" + cdLeft + "s" + Colors.RESET);
            }
            return;
        }
        //trinket
        if(key.equals("6")){
            if(atk.canUse()){
                atk.use();
                trinketActive = true;
                trinketEndTime = System.currentTimeMillis() + 15000; //15 seconds
                System.out.println("Berzerk activated!");
            } else{
                long cdLeft = atk.cooldown -
                        ((System.currentTimeMillis() - atk.lastUsed) / 1000);
                        System.out.println(Colors.RED + "[CD]" + atk.name + " is on cooldown" + cdLeft + "s" + Colors.RESET);
            }
            return;
        }
        if (atk == null) return;
        
        
        
        if (atk.canUse()) {
            atk.use();
            
            int dmg = getRandomDamage(atk.minDamage, atk.maxDamage, atk.critChance);
            int merDamage = dmg + attackpower;
            if(trinketActive) dmg = merDamage;
            enemy.takeDamage(dmg);
            totalDamage += dmg;
            System.out.println(/*"[SPECIAL " + key + "] "*/  atk.name + " does " + dmg + " damage!");
        } else {
            long cdLeft = atk.cooldown -
                    ((System.currentTimeMillis() - atk.lastUsed) / 1000);
            //System.out.println(Colors.RED + "[CD] " + atk.name + " is on cooldown " + cdLeft + "s" + Colors.RESET);
        }

        
    }
}