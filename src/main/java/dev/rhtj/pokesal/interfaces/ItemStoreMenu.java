package dev.rhtj.pokesal.interfaces;

import java.io.PrintStream;
import java.util.Scanner;

import dev.rhtj.pokesal.AnsiCode;
import dev.rhtj.pokesal.Game;
import dev.rhtj.pokesal.ItemRecord;
import dev.rhtj.pokesal.ItemRegistry;
import dev.rhtj.pokesal.entities.Trainer;

public class ItemStoreMenu implements Menu {
    
    private int input = 0;
    private Options options = new Options();
    private String[] itemOptions = null;
    private Trainer player = null;
    private Menu nextMenu = null;
    private Menu caller = null;
    
    public ItemStoreMenu(Menu caller) {
        this.caller = caller;
    }

    private void setupOptions() {
        options.clear();
        player = Game.getInstance().getWorld().getPlayer();
        int itemAmmount = ItemRegistry.getRegistrySize();
        int avaliableCash = player.getCash();
        itemOptions = new String[itemAmmount];
        for (int i = 0; i < itemAmmount; ++i) {
            int price = ItemRegistry.getByIndex(i).price();
            itemOptions[i] = 
            AnsiCode.apply(
                "$" + String.valueOf(price),
                (price > avaliableCash ? AnsiCode.RED : AnsiCode.GREEN),
                AnsiCode.BOLD
            ) + 
            AnsiCode.apply(
                " - ",
                AnsiCode.WHITE, AnsiCode.BOLD 
            ) + 
            AnsiCode.apply(
                ItemRegistry.getByIndex(i).name(), 
                AnsiCode.WHITE, AnsiCode.BOLD
            ) + "\n" +
            AnsiCode.apply(
                ItemRegistry.getByIndex(i).description(),
                AnsiCode.DARK_GRAY
            ) + "\n";
        }
        options.add(itemOptions);
        options.setActionToAll(this::purchasedItem);
        options.add("Voltar", (text) -> {
            nextMenu = caller;
        });
    }

    private void purchasedItem(String text) {
        nextMenu = this;
        ItemRecord chosenItem = ItemRegistry.getByIndex(input - 1);
        if (player.getCash() < chosenItem.price())
            return;
        player.setCash(player.getCash() - chosenItem.price());
        player.getBackpack().add(chosenItem, 1);
    }

    @Override
    public void display(PrintStream out) {
        setupOptions();
        out.println(
            AnsiCode.apply(
                "LOJA DE ITEMS", 
                AnsiCode.CYAN, AnsiCode.BOLD
            ) + "\n"
        );
        out.println(
            AnsiCode.apply(
                "$" + String.valueOf(player.getCash()), 
                AnsiCode.GREEN, AnsiCode.BOLD
            ) + "\n"
        );
        for (int i = 0; i < itemOptions.length; i++) {
            options.display(i + 1, out);
        }
        out.println(AnsiCode.CYAN);
        options.display(itemOptions.length + 1, out);
        out.print(AnsiCode.RESET);
    }

    @Override
    public String getInput(Scanner scanner) {
        try{
            input = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            nextMenu = this;
            return null;
        }
        if (input < 1 || input > options.getTotal()) {
            nextMenu = this;
            return null;
        }
        options.choose(input);
        
        return String.valueOf(input);    }

    @Override
    public Menu next() {
        return nextMenu;
    }
}
