package dev.rhtj.pokesal.interfaces;

import java.io.PrintStream;
import java.util.Scanner;

import dev.rhtj.pokesal.AnsiCode;
import dev.rhtj.pokesal.Game;
import dev.rhtj.pokesal.PokesalRecord;
import dev.rhtj.pokesal.PokesalRegistry;
import dev.rhtj.pokesal.entities.Pokesal;
import dev.rhtj.pokesal.entities.Trainer;

public class PokesalStoreMenu implements Menu{

    private int input = 0;
    private Options options = new Options();
    private String[] pokesalOptions = null;
    private Trainer player = null;
    private Menu nextMenu = null;
    private Menu caller = null;

    public PokesalStoreMenu(Menu caller) {
        this.caller = caller;
    }

    private void setupOptions() {
        options.clear();
        player = Game.getInstance().getWorld().getPlayer();
        int pokesalAmmount = PokesalRegistry.getRegistrySize();
        int avaliableCash = player.getCash();
        pokesalOptions = new String[pokesalAmmount];
        for (int i = 0; i < pokesalAmmount; ++i) {
            int price = PokesalRegistry.getById(i).price();
            pokesalOptions[i] = 
            AnsiCode.apply(
                String.valueOf(price), 
                (price > avaliableCash ? AnsiCode.RED : AnsiCode.GREEN), AnsiCode.BOLD
            ) +
            AnsiCode.apply(
                " - ", 
                AnsiCode.WHITE, AnsiCode.BOLD
            ) + 
            AnsiCode.apply(
                PokesalRegistry.getById(i).name(), 
                AnsiCode.WHITE, AnsiCode.BOLD
            );
        }
        options.add(pokesalOptions);
        options.setActionToAll(this::purchasedPokesal);
        options.add("Voltar", (text) -> { 
            nextMenu = caller;
        });
    }

    private void purchasedPokesal(String text) {
        nextMenu = this;
        PokesalRecord chosenPokesal = PokesalRegistry.getById(input - 1);
        if (player.getCash() < chosenPokesal.price())
            return;
        player.setCash(player.getCash() - chosenPokesal.price());
        player.getPokedeck().add(new Pokesal(chosenPokesal));
    }

    @Override
    public void display(PrintStream out) {
        setupOptions();
        out.println(
            AnsiCode.apply(
                "LOJA DE POKESAL", 
                AnsiCode.CYAN, AnsiCode.BOLD
            )
        );

        out.println(
            AnsiCode.apply(
                "$" + String.valueOf(player.getCash()), 
                AnsiCode.GREEN, AnsiCode.BOLD
            )
        );
        
        for (int i = 0; i < pokesalOptions.length; i++) {
            options.display(i + 1, out);
        }
        out.print(AnsiCode.CYAN);
        options.display(pokesalOptions.length + 1, out);
        out.print(AnsiCode.RESET);
    }

    @Override
    public String getInput(Scanner scanner) {
        input = Integer.parseInt(scanner.nextLine());

        options.choose(input);
        
        return null;
    }
    
    @Override
    public Menu next() {
        return nextMenu;
    }
}
