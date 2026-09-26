package dev.rhtj.pokesal.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import dev.rhtj.pokesal.ItemRegistry;
import dev.rhtj.pokesal.PokesalRegistry;

public class BattleTest {

    @Test
    //Validação da iniciativa por SPD.
    public void testOrdemDeAtaquePorVelocidade(){
        PokesalRegistry.loadPokesals();
        ItemRegistry.loadItems();
        Trainer trainerTest1 = new Trainer(true);
        Trainer trainerTest2 = new Trainer(true);

        // O treinador 1 deve ser o primeiro
        trainerTest1.getPokedeck().getNext().setSpeed(20);
        trainerTest2.getPokedeck().getNext().setSpeed(10);
        Battle battleTest = new Battle(trainerTest1, trainerTest2);
        assertEquals(Battle.State.TRAINER_A_TURN, battleTest.getState(),
        "O treinador 1 deve ser o primeiro");
        
        // O treinador 2 deve ser o primeiro
        trainerTest1.getPokedeck().getNext().setSpeed(10);
        trainerTest2.getPokedeck().getNext().setSpeed(20);
        Battle battleTest2 = new Battle(trainerTest1, trainerTest2);
        assertEquals(Battle.State.TRAINER_B_TURN, battleTest2.getState(),
        "O treinador 2 deve ser o primeiro");
    };

    @Test
    // Não permitir que o usuário exceda o limite de uso dos itens.
    public void testUsoLimiteDeItensExcedido(){
        ItemRegistry.loadItems();
        PokesalRegistry.loadPokesals();
        Battle battleTest = new Battle(new Trainer(true), new Trainer(true));
        
        // O jogador não pode utilizar mais que dois itens em batalha
        battleTest.setTrainersRemainingItemUsages(0, 0);
        battleTest.setTrainersRemainingItemUsages(1, 0);
        assertFalse(battleTest.useItem(ItemRegistry.getById("fireball")), 
        "O jogador não pode utilizar mais que dois itens em batalha");
    };
    
    
}
