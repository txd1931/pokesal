package dev.rhtj.pokesal;

import dev.rhtj.pokesal.entities.PokesalType;

public record PokesalRecord(
    Integer id,
    String name,
    String description,
    PokesalType type,
    Integer healthPoints,
    Integer attack,
    Integer defense,
    Integer speed,
    Integer price
) {}
