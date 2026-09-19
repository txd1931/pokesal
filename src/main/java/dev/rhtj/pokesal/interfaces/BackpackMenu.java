package dev.rhtj.pokesal.interfaces;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import dev.rhtj.pokesal.AnsiCode;
import dev.rhtj.pokesal.entities.Backpack;

public class BackpackMenu implements Menu {

    private Menu caller = null;
    private Menu nextMenu = null;
    private Backpack backpack = null;
    private boolean canSell = false;
    private Options options;
    private int input = 0;
    
    private int selectedItem = 0;

    public BackpackMenu(Backpack backpack, boolean canSell, Menu caller) {
        this.backpack = backpack;
        this.canSell = canSell;
        this.caller = caller;
        options = new Options();
    }

    private void defineOptions() {
        options.clear();
        List<String> items = new ArrayList<>();        
        for (Backpack.Slot slot : backpack.getContents()) {
            items.add("(" + slot.getAmmount() + ") " + slot.getItem().name());
        }
        options.add(items.toArray(new String[0]));
        options.setActionToAll(this::selectItem);
        if (canSell) {
            options.add("Vender", this::selectItem);
        } 
        options.add("Usar", this::selectedToUseItem);
        options.add("Voltar", (text) -> { nextMenu = caller; });
    }

    private void selectedToUseItem(String text) {
        if (backpack.getTotalItems() < 1) {
            nextMenu = this;
            return; 
        }
        if (selectedItem <= 0){
            nextMenu = this;
            return;
        } 
        backpack.useItem(selectedItem);
        nextMenu = caller;
    }

    private void selectItem(String text) {
        int selectedId = options.get(text);
        if (selectedId == selectedItem) 
            selectedItem = -1;
        else
            selectedItem = selectedId;
        nextMenu = this;
    }

    @Override
    public void display(PrintStream out) {
        defineOptions();
        out.println(
            AnsiCode.apply(
                "MOCHILA", 
                AnsiCode.BOLD,
                AnsiCode.CYAN
            )
        );
        for (int i = 1; i <= options.getTotal(); i++) {
            if (i <= backpack.getTotalSlots()) {
                if (i == selectedItem)
                    out.print(AnsiCode.REVERSE);
                options.display(i, out);
                if (i == selectedItem)
                    out.print(AnsiCode.RESET);
            } else {
                options.display(i, out);
            }
            if (i == backpack.getTotalSlots()) {
                out.println(AnsiCode.BOLD + "" + AnsiCode.CYAN);
            }
        }
        out.println(AnsiCode.RESET);
    }

    @Override
    public String getInput(Scanner scanner) {
        try {
            input = Integer.valueOf(scanner.nextLine());
        } catch (NumberFormatException e) { 
            nextMenu = this;
            return String.valueOf(input);
        }

        options.choose(input);


        return String.valueOf(input);
    }
    
    @Override
    public Menu next() {
        return nextMenu;
    }
}
