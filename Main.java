import java.io.IOException;
import java.util.Scanner;
import java.awt.event.KeyEvent;

public class Main {
    

    public static void main(String[] args) throws InterruptedException, IOException {
        Monster boss = new Monster("Onyxia", 500000, 0);
        Monster player = new Monster("Player", 2391, 0);
        SystemMessages startGame = new SystemMessages();
        Rogue attacks = new Rogue();
        KeyInputExample knapp = new KeyInputExample();
        Scanner scanner = new Scanner(System.in);
        System.out.println("=======Welcome to the simulator!=======");
        startGame.rogueStart();
       /*  System.out.println("Welcome to the simulator!" + 
            "\n 1 for Saber slash! (1 second cooldown)" + 
            "\n 2 for Throw knife! (6 second cooldown)" + 
            "\n 3 for Evicerate! (10 second cooldown)" +
            "\n 4 for Slice and dice (20 second cooldown)" + 
            "\n 5 for Adrenaline Rush! (60 second cooldown)" + 
            "\n 6 for Berzerk (120 second cooldown"); */
        
        System.out.println("Boss pull timer! 10 seconds");
        for (int i = 2; i >= 1; i--) { // Ändra till 10 sekunder!
            System.out.println(i);
            Thread.sleep(1000);
        }
        System.out.println("GO!");
        long startTimer = System.currentTimeMillis();
        while (!boss.isDead() && !player.isDead()) {
            // Auto Attack
            attacks.autoAttack(boss);

            int keyCode = knapp.getKeyCode();
            if (keyCode != -1 && keyCode != KeyEvent.VK_ENTER) {
                char key = knapp.getKeyChar();

                if (key == '1') attacks.specialAttack("1", boss);
                if (key == '2') attacks.specialAttack("2", boss);
                if (key == '3') attacks.specialAttack("3", boss);
                if (key == '4') attacks.specialAttack("4", player);
                if (key == '5') attacks.specialAttack("5", player);
                if (key == '6') attacks.specialAttack("6", player);
                if (key == '0') break;
                scanner.close();
                knapp.clear();
            }

            if (!boss.isDead()) {
                // player.takeDamage(boss.getDamage()); SPELARE BEHÖVER INTE TA DAMAGE ÄN
            }
            Thread.sleep(100);
        }

        if (boss.isDead()) {
            long endTime = System.currentTimeMillis();
            long totalTime = (endTime - startTimer) / 1000;
            System.out.print("You defeated the boss!");
            System.out.println("\nYou did: " + attacks.getTotalDamage() + " damage in " + totalTime + " seconds!");
        }
         else {
            long endTime = System.currentTimeMillis();
            long totalTime = (endTime - startTimer) / 1000;
            System.out.print("You died!");
            System.out.println("\nYou did " + attacks.getTotalDamage() + " damage in " + totalTime + " seconds!");
        }
    }

    
}