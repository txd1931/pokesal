package dev.rhtj.pokesal.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import dev.rhtj.pokesal.ItemRegistry;
import dev.rhtj.pokesal.PokesalRegistry;
import dev.rhtj.pokesal.interfaces.ItemStoreMenu;

public class TrainerTest {
    // Verificação de modificações nos atributos de trainer
    @Test
    public void testTrainerAtributes(){
        Trainer trainerTest = new Trainer(true);
        trainerTest.setName("Teste");
        trainerTest.setAppearence(Trainer.Appearance.YELLOW);
        
        // Mudança de nome
        trainerTest.setName("Jurubeba");
        assertEquals("Jurubeba", trainerTest.getName(), "O nome do jogador deve ser'Jurubeba' ");

        // Mudança de aparência
        trainerTest.setAppearence(Trainer.Appearance.RED);
        assertEquals(Trainer.Appearance.RED, trainerTest.getAppearence(), "A aparência do jogador deve ser: RED");
    };

    // O treinador não pode ter cash negativo
    @Test
    public void testTrainerCash(){
        ItemRegistry.loadItems();
        Trainer trainerTest = new Trainer(true);

        trainerTest.setCash(-110);
        assertEquals(0, trainerTest.getCash(),
        "O treinador nao pode ter cash negativo");
    };
}
