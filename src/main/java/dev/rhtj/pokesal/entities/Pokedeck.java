package dev.rhtj.pokesal.entities;

import java.util.ArrayList;
import java.util.List;

public class Pokedeck {
    
    private List<Pokesal> pokesals = null;

    public Pokedeck() {
        pokesals = new ArrayList<>();
    }

    public void add(Pokesal pokesal) {
        pokesals.add(pokesal);
    }

    public void remove(int index) {
        pokesals.remove(index);
    }

    public void remove(Pokesal pokesal) {
        pokesals.remove(pokesal);
    }

    public void empty() {
        pokesals.clear();
    }
    
    public boolean moveUp(int index) {
        if (index <= 0) 
            return false;
        Pokesal temp = get(index - 1);
        pokesals.set(index - 1, get(index));
        pokesals.set(index, temp);
        return true;
    }
    
    public boolean moveDown(int index) {
        if (index >= pokesals.size() - 1) 
            return false;
        Pokesal temp = get(index + 1);
        pokesals.set(index + 1, get(index));
        pokesals.set(index, temp);
        return true;
    }

    public Pokesal get(int index) {
        if (index < 0 || index >= pokesals.size()) {
            throw new IllegalArgumentException("Index tem que ser entre 0 e " + (pokesals.size() - 1));
        }
        return pokesals.get(index);
    }

    public Pokesal[] getAll() {
        return pokesals.toArray(new Pokesal[0]);
    }

    public int getSize() {
        return pokesals.size();
    }

    public Pokesal getNext() {
        return get(0);
    }
}
