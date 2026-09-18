package dev.rhtj.pokesal.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dev.rhtj.pokesal.ItemRegistry;

public class Backpack {
    
    public static class Slot {
        private final String itemId;
        private int ammount;

        public Slot(String itemId, int ammount) {
            this.itemId = itemId;
            this.ammount = ammount;
        }

        public String getItemId() {
            return itemId;
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
            add(ItemRegistry.getRandomId(), 1);
        }
        System.out.println(getTotalItems());
    }

    public void sell(String id) {
        validate(id);
        carrier.addCash(ItemRegistry.getSellingPrice(id));
        remove(id, 1);
    }
    
    public void sell(int index) {
        Slot slot = getSlot(index);
        carrier.addCash(ItemRegistry.getSellingPrice(slot.getItemId()));
        remove(slot.getItemId(), 1);
    }

    public void useItem(int index) {
        remove(index);
    }

    public void add(String id, int ammount) {
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

    public void remove(String id, int ammount) {
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

    public void removeAll(String id) {
        validate(id);
        contents.remove(indexOf(id));
    }

    public Slot getSlot(String id) {
        validate(id);
        return contents.get(indexOf(id));
    }

    public Slot getSlot(int index) {
        validateIndex(index);
        return contents.get(index - 1);
    }

    public int indexOf(String id) {
        for (int i = 0; i < contents.size(); i++) {
            if (contents.get(i).itemId.equals(id)) {
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

    private void validate(String id) {
        if (!ItemRegistry.contains(id)) {
            System.err.println(id + " não existe no jogo");
            System.exit(1);
        }
        if (indexOf(id) == -1) {
            System.err.println(id + " não consta na mochila de " + carrier.getName());
            System.exit(1);
        }
    }
}