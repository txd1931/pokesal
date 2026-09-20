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
    private boolean canSellItems = false;
    private boolean canUseItems = false;
    private Options options;
    private int input = 0;
    private boolean hasSellOption = false;
    private boolean hasUseOption = false;
    
    private int selectedItem = 0;

    public BackpackMenu(Backpack backpack, boolean canUseItems, boolean canSellItems, Menu caller) {
        this.backpack = backpack;
        this.canUseItems = canUseItems;
        this.canSellItems = canSellItems;
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
        if (canSellItems && selectedItem > 0 && backpack.getTotalSlots() > 0) {
            options.add("Vender por " + 
            AnsiCode.apply("$" + backpack.getSlot(selectedItem).getItem().getSellingPrice(), AnsiCode.BOLD, AnsiCode.GREEN)
            , this::selectedToSellItem);
            hasSellOption = true;
        } 
        if (canUseItems && backpack.getTotalSlots() > 0) {
            options.add("Usar", this::selectedToUseItem);
            hasUseOption = true;
        }
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
        backpack.useItem(selectedItem, backpack.getCarrier().getBattle());
        nextMenu = caller;

    }

    private void selectedToSellItem(String text) {
        if (backpack.getTotalItems() < 1) {
            nextMenu = this;
            return; 
        }
        if (selectedItem <= 0){
            nextMenu = this;
            return;
        } 
        backpack.sellItem(selectedItem);
        nextMenu = this;
    }

    private void selectItem(String text) {
        if (!canSellItems && !canUseItems) {
            nextMenu = this;
            return;
        }
        int selectedId = options.get(text);
        if (selectedId == selectedItem) 
            selectedItem = 0;
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
            ) + "\n"
        );
        out.println(
            AnsiCode.apply(
                "$" + backpack.getCarrier().getCash(), 
                AnsiCode.GREEN,
                AnsiCode.BOLD
            ) + "\n"
        );

        for (int i = 1; i <= backpack.getTotalSlots(); i++) {
            if (i == selectedItem)
                out.print(AnsiCode.REVERSE);
            options.display(i, out);
            if (i == selectedItem)
                out.print(AnsiCode.RESET);
        }

        out.println(AnsiCode.CYAN);
        for (int i = backpack.getTotalSlots() + 1; i <= options.getTotal(); i++) {
            options.display(i, out);
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

        if (input < 1 || input > options.getTotal()) {
            nextMenu = this;
            return null;
        }

        options.choose(input);


        return String.valueOf(input);
    }
    
    @Override
    public Menu next() {
        return nextMenu;
    }
}
