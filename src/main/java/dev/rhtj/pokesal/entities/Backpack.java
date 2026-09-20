package dev.rhtj.pokesal.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dev.rhtj.pokesal.ItemRecord;
import dev.rhtj.pokesal.ItemRegistry;

public class Backpack {
    
    public static class Slot {
        private final ItemRecord item;
        private int ammount;

        public Slot(ItemRecord item, int ammount) {
            this.item = item;
            this.ammount = ammount;
        }

        public ItemRecord getItem() {
            return item;
        }

        public int getAmmount() {
            return ammount;
        }
    }

    private List<Slot> contents = null;
    private Trainer carrier = null;
    
    public Backpack(Trainer carrier) {
        contents = new ArrayList<>();
        this.carrier = carrier;
        getStarterItems(new Random().nextLong());
    }
    
    private void getStarterItems(long seed) {
        Random random = new Random(seed);
        while (random.nextInt(100) > 10) {
            add(ItemRegistry.getRandom(), 1);
        }
    }

    public void sellItem(ItemRecord id) {
        carrier.addCash(id.getSellingPrice());
        remove(id, 1);
    }
    
    public void sellItem(int index) {
        Slot slot = getSlot(index);
        sellItem(slot.item);
    }

    public boolean useItem(int index, Battle battle) {
        if (battle != null) {
            System.err.println("Batalha não encontrada para utilizar o item");
            System.exit(1);
        }
        validateIndex(index);
        battle = carrier.getBattle();
        Pokesal pokesal = carrier.getPokedeck().getNext();
        Pokesal opponent = null;
        opponent = battle.getOpponentPokesal();
        ItemRecord item = getSlot(index).item;
        
        switch (item.id()) {
            case "fireball" -> {
                opponent.setEffect(Pokesal.Effect.BURN, 1.0);
            }
            case "potion_poison_weak" -> {
                pokesal.setEffect(Pokesal.Effect.POISON, 0.5);
            }
            case "potion_poison_strong" -> {
                pokesal.setEffect(Pokesal.Effect.POISON, 1.0);
            }
            case "antidote" -> {
                pokesal.setEffect(Pokesal.Effect.POISON, 0.0);
            }
            case "snowball" -> {
                opponent.setEffect(Pokesal.Effect.FREEZE, 1.0);
            }
            case "mudball" -> {
                opponent.setSpeed((int) (pokesal.getSpeed() * 0.85f));
            }
            case "potion_health_weak" -> {
                pokesal.setEffect(Pokesal.Effect.HEAL, 0.5d);
            }
            case "potion_health_strong" -> {
                pokesal.setEffect(Pokesal.Effect.HEAL, 1.0d);
            }
            case "potion_health_instant" -> {
                pokesal.setHealthPoints(pokesal.getPokesalRecord().healthPoints());
            }
            case "adrenaline" -> {
                pokesal.setSpeed((int) (pokesal.getSpeed() * 1.75d));
            }
            case "shield" -> {
                pokesal.setDefence((int) (pokesal.getDefence() * 1.5d));
            }
            case "sword" -> {
                pokesal.setAtack((int) (pokesal.getAtack() * 1.3d));
            }
            case "nuke" -> {
                opponent.setHealthPoints(0);
                pokesal.setHealthPoints(pokesal.getHealthPoints() - 50);
            }
        }
        remove(index);
        return true;
    }

    public void add(ItemRecord id, int ammount) {
        if(!ItemRegistry.contains(id)) {
            System.err.println(id + " não existe no jogo");
            System.exit(1);
        }
        
        int existingIndex = indexOf(id);
        if (existingIndex >= 0) {
            contents.get(existingIndex).ammount += ammount;
        } else {
            contents.add(new Slot(id, ammount));
        }
    }

    public void remove(ItemRecord id, int ammount) {
        validate(id);
        int index = indexOf(id);
        Slot slot = contents.get(index);
        
        if (slot.ammount <= ammount) {
            contents.remove(index);
        } else {
            slot.ammount -= ammount;
        }
    }

    public void remove(int index) {
        validateIndex(index);
        int realIndex = index - 1;
        if (contents.get(realIndex).ammount == 1) {
            removeAll(index);
            return;
        }
        contents.get(realIndex).ammount--;
    }

    public void removeAll(int index) {
        validateIndex(index);
        contents.remove(index - 1); 
    }

    public void removeAll(ItemRecord id) {
        validate(id);
        contents.remove(indexOf(id));
    }

    public Slot getSlot(int index) {
        validateIndex(index);
        return contents.get(index - 1);
    }

    public Slot getSlot(ItemRecord id) {
        return getSlot(indexOf(id));
    }

    public int indexOf(ItemRecord id) {
        for (int i = 0; i < contents.size(); i++) {
            if (contents.get(i).item.equals(id)) {
                return i;
            }
        }
        return -1;
    }

    public List<Slot> getContents() {
        return new ArrayList<>(contents);
    }

    public int getTotalSlots() {
        return contents.size();
    }

    public int getTotalItems() {
        int count = 0;
        for (Slot slot : contents) {
            count += slot.ammount;
        }
        return count;
    }

    private void validateIndex(int index) {
        int realIndex = index - 1;
        if (realIndex < 0 || realIndex >= contents.size()) {
            throw new IndexOutOfBoundsException("Índice de mochila inválido: " + index);
        } 
    }

    public boolean contains(ItemRecord id) {
        for (Slot slot : contents) {
            if (slot.item.id().equalsIgnoreCase(id.id())) 
                return true;
        }
        return false;
    }

    public Trainer getCarrier() {
        return carrier;
    }

    private void validate(ItemRecord id) {
        if (!ItemRegistry.contains(id)) {
            System.err.println(id + " não existe no jogo");
            System.exit(1);
        }
        if (!contains(id)) {
            System.err.println(id + " não consta na mochila de " + carrier.getName());
            System.exit(1);
        }
    }
}