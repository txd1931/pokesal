package dev.rhtj.pokesal;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PokesalRegistry {
    
    private static final List<PokesalRecord> REGISTRY = new ArrayList<>();

    private static final String POKESALS_SOURCE = "pokesals.json";

    public static void loadPokesals() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            InputStream inputStream = PokesalRegistry.class.getClassLoader().getResourceAsStream(POKESALS_SOURCE);
            if (inputStream == null)
                throw new RuntimeException("pokesals.json não foi encontrado");
            List<PokesalRecord> pokesals = mapper.readValue(inputStream, new TypeReference<>() {});
            REGISTRY.addAll(pokesals);
        } catch (IOException e) {

            System.err.println(
                AnsiCode.apply(
                    "Erro ao carregar os pokesals: ", 
                    AnsiCode.BRIGHT_RED
                ) +
                e.getMessage()
            );
            System.exit(1);
        }
    }


    public static PokesalRecord getById(int id) {
        if (id < 0 || id >= REGISTRY.size()) {
            throw new IllegalArgumentException("ID de Pokesal inválido: " + id);
        }
        return REGISTRY.get(id);
    }

    public static PokesalRecord getByName(String name) {
        for (PokesalRecord record : REGISTRY) {
            if (record.name().equalsIgnoreCase(name)) {
                return record;
            }
        }
        throw new IllegalArgumentException("Nome de Pokesal inválido: " + name);
    }

    public static int getRegistrySize() {
        return REGISTRY.size();
    }
}
