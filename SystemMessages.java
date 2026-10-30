public class SystemMessages {
    public void rogueStart(){
        System.out.println("\n 1 for Saber slash! (1 second cooldown)" + 
            "\n 2 for Throw knife! (6 second cooldown)" + 
            "\n 3 for Evicerate! (10 second cooldown)" +
            "\n 4 for Slice and dice (20 second cooldown)" + 
            "\n 5 for Adrenaline Rush! (60 second cooldown)" + 
            "\n 6 for Berzerk (120 second cooldown");
    }
    public void mageStart(){
        System.out.println("\n 1 for Fire Ball! (3 Second Cast Time)" + 
            "\n 2 for Fire Blast! (6 second cooldown, gives reduced cast time on Pyro Blast " + 
            "\n 3 for Pyro Blast! (5 second cast time, 1 second after casting Fire Blast" +
            "\n 4 for Combustion! (60 second cooldown, Increase cast time and crit chance to 100%)" + 
            "\n 5 for Evocation! (20 second cooldown, restores 100% of the caster)" + 
            "\n 6 for Mind Quickening Gem! (120 second cooldown, 300 extra spellpower");
    }
}
