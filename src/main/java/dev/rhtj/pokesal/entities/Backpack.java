package dev.rhtj.pokesal.entities;

import java.util.HashMap;
import java.util.Map;

import javax.management.RuntimeErrorException;

import dev.rhtj.pokesal.ItemRegistry;

public class Backpack {
    private Map<String, Integer> contents = null;
    private Trainer carrier = null;

    public Backpack(Trainer carrier) {
        this.carrier = carrier;
        contents = new HashMap<>();
    }

    public void sell(String id) {
        validate(id);
        carrier.addCash(ItemRegistry.getSellingPrice(id));

    }

    public void add(String id, int ammount) {
        if(!ItemRegistry.contains(id)) {
            System.err.println(id + " não existe no jogo");
            System.exit(1);
        }

        contents.merge(id, ammount, Integer::sum);
    }

    public void remove(String id, int ammount) {
        validate(id);
        if (contents.get(id) == 1) {
            contents.remove(id);
            return;
        } 
        
        contents.computeIfPresent(id, (key, count) -> count > ammount ? count - ammount : null);

    }

    public void removeAll(String id) {
        validate(id);
        contents.remove(id);
    }

    private void validate(String id) {
        if (!ItemRegistry.contains(id)) {
            System.err.println(id + " não existe no jogo");
            System.exit(1);
        }
        if (!contents.containsKey(id)) {
            System.err.println(id + " não consta na mochila de " + carrier.getName());
            System.exit(1);
        }
    }

}
