package dev.rhtj.pokesal.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import dev.rhtj.pokesal.PokesalRegistry;
import dev.rhtj.pokesal.PokesalRecord;
import dev.rhtj.pokesal.entities.Pokesal;

public class PokesalTest {

    public int semNegativo(double dano){
            if(dano < 0){
                dano = 0;
            }
            return (int)dano;
    }
    
    @Test
    // Validação dos multiplicadores de dano.
    public void testVantagemElemental(){
        PokesalRegistry.loadPokesals();
        Pokesal pokeTest1 = new Pokesal(PokesalRegistry.getByName("CharSal"));
        Pokesal pokeTest2 = new Pokesal(PokesalRegistry.getByName("BulbaSal"));
        Pokesal pokeTest3 = new Pokesal(PokesalRegistry.getByName("SquirtSal"));

        // Fogo vs Planta
        assertTrue(pokeTest1.getAtackDamage(pokeTest2) == 
        semNegativo((PokesalRegistry.getByName("CharSal").attack()*2)-PokesalRegistry.getByName("BulbaSal").defense()), 
        "O dano de CharSal para BulbaSal deve ser dobrado devido à vantagem elemental.");
        // Fogo vs Água
        assertTrue(pokeTest1.getAtackDamage(pokeTest3) == 
        semNegativo((PokesalRegistry.getByName("CharSal").attack()*0.5)-PokesalRegistry.getByName("SquirtSal").defense()),
        "O dano de CharSal para SquirtSal deve ser reduzido à metade devido à vantagem elemental.");
        // Fogo vs Fogo
        assertTrue(pokeTest1.getAtackDamage(pokeTest1) == 
        semNegativo((PokesalRegistry.getByName("CharSal").attack())-PokesalRegistry.getByName("CharSal").defense()),
        "O dano de CharSal para CharSal deve permanecer sem modificador.");
        
        // Planta vs Planta
        assertTrue(pokeTest2.getAtackDamage(pokeTest2) == 
        semNegativo((PokesalRegistry.getByName("BulbaSal").attack())-PokesalRegistry.getByName("BulbaSal").defense()),
        "O dano de BulbaSal para BulbaSal deve permanecer sem modificador.");
        // Planta vs Água
        assertTrue(pokeTest2.getAtackDamage(pokeTest3) == 
        semNegativo((PokesalRegistry.getByName("BulbaSal").attack()*2)-PokesalRegistry.getByName("SquirtSal").defense()),
        "O dano de BulbaSal para SquirtSal deve ser dobrado devido à vantagem elemental.");
        // Planta vs Fogo
        assertTrue(pokeTest2.getAtackDamage(pokeTest1) == 
        semNegativo((PokesalRegistry.getByName("BulbaSal").attack()*0.5)-PokesalRegistry.getByName("CharSal").defense()),
        "O dano de BulbaSal para CharSal deve ser reduzido à metade devido à vantagem elemental.");

        // Água vs Planta
        assertTrue(pokeTest3.getAtackDamage(pokeTest2) == 
        semNegativo((PokesalRegistry.getByName("SquirtSal").attack()*0.5)-PokesalRegistry.getByName("BulbaSal").defense()),
        "O dano de SquirtSal para BulbaSal deve ser reduzido à metade devido à vantagem elemental.");
        // Água vs Água
        assertTrue(pokeTest3.getAtackDamage(pokeTest3) == 
        semNegativo((PokesalRegistry.getByName("SquirtSal").attack())-PokesalRegistry.getByName("SquirtSal").defense()),
        "O dano de SquirtSal para SquirtSal deve permanecer sem modificador.");
        // Água vs Fogo
        assertTrue(pokeTest3.getAtackDamage(pokeTest1) == 
        semNegativo((PokesalRegistry.getByName("SquirtSal").attack()*2)-PokesalRegistry.getByName("CharSal").defense()),
        "O dano de SquirtSal para CharSal deve ser dobrado devido à vantagem elemental.");
    };

    
    @Test
    // Validação de valores limite de HP, ATK e DEF.
    public void testCalculoDanoBoundaryValues(){
        PokesalRegistry.loadPokesals();
        Pokesal pokeTest1 = new Pokesal(PokesalRegistry.getByName("CharSal"));
        
        // Dano deve ser 0 quando o atacante tiver ATK 0 e o defensor DEF > 0
        pokeTest1.setAtack(0);
        assertEquals(0, pokeTest1.getAtackDamage(pokeTest1));

        // Dano deve ser 0 quando o atacante tiver ATK 0 e o defensor DEF 0
        pokeTest1.setAtack(0);
        pokeTest1.setDefence(0);
        assertEquals(0, pokeTest1.getAtackDamage(pokeTest1));

        // Os cálculos devem ocorrer normalmente idependente de quão grandes sejam os stats
        pokeTest1.setAtack(999999999);
        pokeTest1.setDefence(999999999);
        assertEquals(0, pokeTest1.getAtackDamage(pokeTest1));

        // O HP não pode se tornar negativo
        pokeTest1.setHealthPoints(100);
        pokeTest1.setAtack(200);
        pokeTest1.setDefence(0);
        pokeTest1.attacking(pokeTest1);
        assertEquals(0, pokeTest1.getHealthPoints(), "HP nao pode se tornar negativo");

    };

}
