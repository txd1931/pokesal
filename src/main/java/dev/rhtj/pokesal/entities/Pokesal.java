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
    private Double[] effects = new Double[4];

    public Pokesal(PokesalRecord pokesalRecord) {
        this.pokesalRecord = pokesalRecord;
        setAttributesToDefault();
    }

    public void setAttributesToDefault() {
        this.healthPoints = pokesalRecord.healthPoints();
        this.atack = pokesalRecord.attack();
        this.defence = pokesalRecord.defense();
        this.speed = pokesalRecord.speed();
        for (int i = 0; i < effects.length; i++) {
            effects[i] = Double.valueOf(0);
        }
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

    public int getAtackDamage(Pokesal opponent) {
        float typeMultiplier = 1;

        switch (this.getPokesalRecord().type()) {
            case FIRE:
                if(opponent.getPokesalRecord().type() == PokesalType.PLANT){typeMultiplier = 2;}
                if(opponent.getPokesalRecord().type() == PokesalType.WATER){typeMultiplier = 0.5f;}
            break;
            case WATER:
                if(opponent.getPokesalRecord().type() == PokesalType.FIRE){typeMultiplier = 2;}
                if(opponent.getPokesalRecord().type() == PokesalType.PLANT){typeMultiplier = 0.5f;}
            break;
            case PLANT:
                if(opponent.getPokesalRecord().type() == PokesalType.WATER){typeMultiplier = 2;}
                if(opponent.getPokesalRecord().type() == PokesalType.FIRE){typeMultiplier = 0.5f;}
            break;
        };
        
        int damage = (int) (this.atack*typeMultiplier - opponent.getDefence());
        
        if (damage < 0) {
            damage = 0;
        }
        return damage;
    }

    private void applyEffects() {
        
        healthPoints -= getEffect(Effect.BURN) * 10;
        healthPoints -= getEffect(Effect.POISON) * 10;
        healthPoints += getEffect(Effect.HEAL) * 10;
        speed -= getEffect(Effect.FREEZE) * 2;

        for (int i = 0; i < effects.length; ++i) {
            if (effects[i] > 0.01d) {
                effects[i] *= 0.85d;
                effects[i] -= 0.05d;
            } else {
                effects[i] = 0d;
            }
        }
    }

    public Double getEffect(Effect effect) {
        return effects[effect.getIndex()];
    }

    public Double[] getAllEffects() {
        return effects.clone();
    } 
    
    public void attacking(Pokesal opponent) {
        int damage = getAtackDamage(opponent);
        opponent.setHealthPoints(opponent.getHealthPoints() - damage);
        applyEffects();
        if(opponent.getHealthPoints()<0){opponent.setHealthPoints(0);}
    }
}
