package dev.rhtj.pokesal.entities;

import dev.rhtj.pokesal.PokesalRecord;

public class Pokesal {
    
    public enum Effect {
        BURN(0, "Queimadura"),
        FREEZE(1, "Congelamento"),
        HEAL(2, "Cura"),
        POISON(3, "Veneno");


        private final int index;
        private final String name;

        Effect(int index, String name) {
            this.index = index;
            this.name = name;
        }

        public int getIndex() {
            return index;
        }

        public String getName() {
            return name;
        }

        public static Effect fromName(String name) {
            for (Effect effect : Effect.values()) {
                if (effect.getName().equalsIgnoreCase(name)) {
                    return effect;
                }
            }
            return null;
        }

        public static Effect fromIndex(int index) {
            for (Effect effect : Effect.values()) {
                if (effect.getIndex() == index) {
                    return effect;
                }
            }
            return null;
        }
    }

    private PokesalRecord pokesalRecord;
    private int healthPoints;
    private int atack;
    private int defence;
    private int speed;
    private double[] effects = new double[4];

    public Pokesal(PokesalRecord pokesalRecord) {
        this.pokesalRecord = pokesalRecord;
        setAttributesToDefault();
    }

    public void setAttributesToDefault() {
        this.healthPoints = pokesalRecord.healthPoints();
        this.atack = pokesalRecord.attack();
        this.defence = pokesalRecord.defense();
        this.speed = pokesalRecord.speed();
    }

    public PokesalRecord getPokesalRecord() {
        return pokesalRecord;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public int getAtack() {
        return atack;
    }

    public int getDefence() {
        return defence;
    }

    public int getSpeed() {
        return speed;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    public void setAtack(int atack) {
        this.atack = atack;
    }

    public void setDefence(int defence) {
        this.defence = defence;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public boolean hasEffect(Effect effect) {
        return effects[effect.getIndex()] != 0;
    }

    public void setEffect(Effect effect, double value) {
        effects[effect.getIndex()] = value;
    }

}
