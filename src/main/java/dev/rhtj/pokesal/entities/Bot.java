package dev.rhtj.pokesal.entities;

import java.util.Random;

public class Bot {
    
    private Trainer npc;

    public Bot(Trainer npc) {
        this.npc = npc;
    }

    public void tick() {
        Random random = new Random(); 
        try {
            Thread.sleep(random.nextLong(1000, 5000));
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        
        Backpack backpack = npc.getBackpack();
        int totalSlots = backpack.getTotalSlots();
        backpack.useItem(random.nextInt(1, totalSlots + 1));
    }
}
