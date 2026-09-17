package dev.rhtj.pokesal;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ItemRegistry {

    private record Item(
        String id,
        String name,
        String description,
        int price
    ) {}


    private static final Map<String, Item> REGISTRY = new HashMap<>();

    private static final String ITEMS_SOURCE = "items.json";

    public static void loadItems() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            InputStream inputStream = ItemRegistry.class.getClassLoader().getResourceAsStream(ITEMS_SOURCE);
            if (inputStream == null)
                throw new RuntimeException("items.json não foi encontrado");

            List<Item> items = mapper.readValue(inputStream, new TypeReference<>() {});

            for (Item item : items) {
                REGISTRY.put(item.id(), item);
            }

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

    public static String getName(String id) {
        return REGISTRY.get(id).name;
    }

    public static String getDescription(String id) {
        return REGISTRY.get(id).description;
    }

    public static int getPrice(String id) {
        return REGISTRY.get(id).price;
    }

    public static int getSellingPrice(String id) {
        return (int) (getPrice(id) * 0.70f);
    }

    public static boolean contains(String id) {
        return REGISTRY.containsKey(id);
    }    

    public static int total() {
        return REGISTRY.size();
    }
}
