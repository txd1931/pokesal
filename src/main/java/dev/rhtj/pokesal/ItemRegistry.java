package dev.rhtj.pokesal;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class ItemRegistry {


    private static final List<ItemRecord> REGISTRY = new ArrayList<>();

    private static final String ITEMS_SOURCE = "items.json";

    public static void loadItems() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            InputStream inputStream = ItemRegistry.class.getClassLoader().getResourceAsStream(ITEMS_SOURCE);
            if (inputStream == null)
                throw new RuntimeException("items.json não foi encontrado");

            List<ItemRecord> items = mapper.readValue(inputStream, new TypeReference<>() {});
            REGISTRY.addAll(items);

        } catch (IOException e) {
            System.err.println(
                AnsiCode.apply(
                    "Erro ao carregar os itens: ", 
                    AnsiCode.BRIGHT_RED
                ) +
                e.getMessage()
            );
            System.exit(1);
        }
    }

    public static ItemRecord getById(String id) {
        for (ItemRecord item : REGISTRY) {
            if (item.id().equals(id)) {
                return item;
            }
        }
        throw new IllegalArgumentException("ID de item inválido: " + id);
    }

    public static ItemRecord getByIndex(int index) {
        if (index < 0 || index >= REGISTRY.size()) 
            throw new IndexOutOfBoundsException("Index inválido: " + index);
        return REGISTRY.get(index);
    }

    public static ItemRecord getByName(String name) {
        for (ItemRecord item : REGISTRY) {
            if (item.name().equalsIgnoreCase(name)) {
                return item;
            }
        }
        throw new IllegalArgumentException("Nome de item inválido: " + name);
    }

    public static ItemRecord getRandom() {
        Random random = new Random();
        int randomIndex = random.nextInt(REGISTRY.size());
        return REGISTRY.get(randomIndex);
    }

    public static boolean contains(ItemRecord id) {
        return REGISTRY.contains(id);
    }    

    public static int getRegistrySize() {
        return REGISTRY.size();
    }
}
