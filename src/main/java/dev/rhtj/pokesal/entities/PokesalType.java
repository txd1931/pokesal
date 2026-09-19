package dev.rhtj.pokesal.entities;

public enum PokesalType {
    FIRE("Fogo"),
    WATER("Água"),
    PLANT("Planta");

    private final String name;

    PokesalType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static PokesalType fromName(String name) {
        for (PokesalType type : PokesalType.values()) {
            if (type.getName().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }
}
