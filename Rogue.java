import java.util.HashMap;
import java.util.Map;
import java.util.Random;
public class Rogue implements CharacterClass {
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
    private int attackpower = 150; //trinket attack power
    private int totalDamage = 0;
    private double energy = 100;
    public static final int MIN_ENERGY = 0;
    private static final int MAX_ENERGY = 100;
    private long lastEnergyUpdate = System.currentTimeMillis();

    private int comboPoints = 0;
    private static final int MAX_COMBO_POINTS = 5;
    private static final double ENERGY_REGEN_PER_SECOND = 10.0;
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
        int energyCost;
        int comboPointCost;
        int comboPoiintGenerated;
        int minDamage;
        int maxDamage;
        int critChance;
        int cooldown;
        long lastUsed;

        SpecialAttacks(String name, int energyCost, int comboPointCost, int comboPointsGenerated, int minDamage, int maxDamage, int critChance, int cooldown) {
            this.name = name;
            this.energyCost = energyCost;
            this.comboPointCost = comboPointCost;
            this.comboPoiintGenerated = comboPointsGenerated;
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
        specials.put("1", new SpecialAttacks("Saber Slash",40, 0, 1, 80, 120, 15, 1));
        specials.put("2", new SpecialAttacks("Throw Knife",0, 0, 2, 40, 80, 25, 6));
        specials.put("3", new SpecialAttacks("Evicerate",60, 5, 0, 150, 250, 20,0));
        specials.put("4", new SpecialAttacks("Slice and Dice", 20, 5, 0, 0, 0, 0, 20));
        specials.put("5", new SpecialAttacks("Adrenaline Rush",0, 0, 0, 0, 0, 100, 60));
        specials.put("6", new SpecialAttacks("Berzerk",0 ,0,0, 0, 0, 0 ,120));
    }
    //Autoattack
    public void autoAttack(Monster enemy) {
        regenerateEnergy();
        long now = System.currentTimeMillis();
        int currentInterval = autoInterval;
        if(hasteOneActive) currentInterval = autoInterval / 2; //SLICE AND DICE *2 speed
        if(hasteTwoActive) currentInterval = autoInterval / 3; //ADRENALINE RUSH *3 speed
        if (hasteOneActive && hasteTwoActive) currentInterval = autoInterval / 2 / 3; //Both // *5 speed
        if (now - lastAutoAttack >= currentInterval) {
            int dmg = getRandomDamage(49, 77, 15); //min, max , crit chance
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
        if (atk == null) return;

        //THROW KNIFE ENERGY REGEN
        if(key.equals("2")){
            energy = Math.min(MAX_ENERGY, energy + 40);
        }
        //SLICE AND DICE
        if(key.equals("4")){
            if (tryUse(atk)){
                hasteOneActive = true;
                hasteOneEndTime = System.currentTimeMillis() + 10000; //10 seconds
                System.out.println(Colors.YELLOW + "Slice and Dice activated!" + Colors.RESET);
            }   
            return;
        }
        //ADRENALINE RUSH
        if(key.equals("5")){
            if(atk.canUse()){
                atk.use();
                hasteTwoActive = true;
                hasteTwoEndTime = System.currentTimeMillis() + 15000; //15 seconds
                System.out.println(Colors.YELLOW + "Adrenaline Rush activated!" + Colors.RESET);
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
                System.out.println(Colors.YELLOW + "Berzerk activated!" + Colors.RESET);
            } else{
                long cdLeft = atk.cooldown -
                        ((System.currentTimeMillis() - atk.lastUsed) / 1000);
                        System.out.println(Colors.RED + "[CD]" + atk.name + " is on cooldown" + cdLeft + "s" + Colors.RESET);
            }
            return;
        }
        if (tryUse(atk)) {
            int dmg = getRandomDamage(atk.minDamage, atk.maxDamage, atk.critChance);
            int merDamage = dmg + attackpower;
            if(trinketActive) dmg = merDamage;
            enemy.takeDamage(dmg);
            totalDamage += dmg;
            comboPoints = Math.min(MAX_COMBO_POINTS, comboPoints + atk.comboPoiintGenerated);
            System.out.println("Combo points: " + comboPoints);
            System.out.println("Current Energy: " + (int)energy);
            System.out.println(/*"[SPECIAL " + key + "] "*/  atk.name + " does " + dmg + " damage!");
        } else {
            long cdLeft = atk.cooldown -
                    ((System.currentTimeMillis() - atk.lastUsed) / 1000);
            //System.out.println(Colors.RED + "[CD] " + atk.name + " is on cooldown " + cdLeft + "s" + Colors.RESET);
        }

        
    }
    private boolean tryUse(SpecialAttacks atk) {
    long now = System.currentTimeMillis();
    long elapsed = now - lastEnergyUpdate;

    energy = Math.min(MAX_ENERGY,
            energy + elapsed * ENERGY_REGEN_PER_SECOND / 1000.0);
    lastEnergyUpdate = now;

    if (!atk.canUse()) {
        long cdLeft = atk.cooldown - (now - atk.lastUsed) / 1000;
        System.out.println(Colors.RED + atk.name + " is on cooldown "
                + cdLeft + "s" + Colors.RESET);
        return false;
    }
    if (energy < atk.energyCost) {
        System.out.println("Not enough energy!");
        return false;
    }
    if (comboPoints < atk.comboPointCost) {
        System.out.println("Not enough combo points!");
        return false;
    }
    energy -= atk.energyCost;
    comboPoints -= atk.comboPointCost;
    atk.use();
    return true;
}
private void regenerateEnergy(){
    long now = System.currentTimeMillis();
    double elapsedSeconds = (now - lastEnergyUpdate) / 1000.0;

    energy = Math.min(MAX_ENERGY, energy + elapsedSeconds * ENERGY_REGEN_PER_SECOND);
    lastEnergyUpdate = now;
}
}