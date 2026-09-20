package dev.rhtj.pokesal.entities;

import java.util.Random;

import dev.rhtj.pokesal.Game;

public class Bot {
    
    private Trainer npc;

    private static float DELAY_MULTIPLYER = 1.0f;

    public Bot(Trainer npc) {
        this.npc = npc;
    }

    public void run() {
        Random random = new Random(); 
        
        Game.sleep(random.nextLong((long) (DELAY_MULTIPLYER * 1000), (long) (DELAY_MULTIPLYER *  5000)));

        Backpack backpack = npc.getBackpack();
        int totalSlots = backpack.getTotalSlots();
        backpack.useItem(random.nextInt(1, totalSlots + 1), npc.getBattle());
        
        Game.sleep(random.nextLong((long) (DELAY_MULTIPLYER * 500), (long) (DELAY_MULTIPLYER *  1500)));    
    }
}
