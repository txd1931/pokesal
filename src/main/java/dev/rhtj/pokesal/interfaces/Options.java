package dev.rhtj.pokesal.interfaces;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class Options {

    private static class Option {
        public final String text;
        public final String key;
        public Optional<Consumer<String>> action;

        Option(String text, String key, Optional<Consumer<String>> action) {
            this.text = text;
            this.key = key;
            this.action = action;
        }
    }

    private final List<Option> optionList = new ArrayList<>();
    private int chosenOption = -1;
    private int indentation = 0;

    public boolean choose(int choice) {
        int realIndex = choice - 1;
        if (realIndex >= 0 && realIndex < optionList.size()) {
            chosenOption = choice;
            Option selected = optionList.get(realIndex);
            selected.action.ifPresent(act -> act.accept(selected.text));
            return true;
        }
        return false;
    }

    public boolean choose(String key) {
        for (int i = 0; i < optionList.size(); i++) {
            if (optionList.get(i).key.equals(key)) {
                return choose(i + 1);
            }
        }
        try {
            return choose(Integer.parseInt(key));
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public String result() {
        if(chosenOption == -1) return null;
        return get(chosenOption);
    }

    public void setActionToAll(Consumer<String> action) {
        if (action == null) {
            for (Option option : optionList) {
                option.action = Optional.empty();
            }
        }
        for (Option option : optionList) {
            option.action = Optional.of(action);
        }
    }

    public void add(String option) {
        optionList.add(new Option(option, null, Optional.empty()));
    }

    public void add(String option, Consumer<String> action) {
        optionList.add(new Option(option, null, Optional.of(action)));
    }

    public void add(String option, String key) {
        validateKey(key);
        optionList.add(new Option(option, key, Optional.empty()));
    }

    public void add(String option, String key, Consumer<String> action) {
        validateKey(key);
        optionList.add(new Option(option, key, Optional.of(action)));
    }    

    public void add(String[] options) {
        for (String option : options) {
            add(option);
        }
    }

    private void validateKey(String key) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("A chave customizada não pode ser nula");
        }
        try {
            Integer.parseInt(key);
            throw new IllegalArgumentException("A chave customizada não pode ser um número para evitar colisões: " + key);
        } catch (NumberFormatException e) {

        }
    }

    public boolean remove(int index) {
        int realIndex = index - 1;
        if (realIndex >= 0 && realIndex < optionList.size()) {
            optionList.remove(realIndex);
            return true;
        }
        return false;    
    }

    public boolean remove(String option) {
        if (option == null) return false;
        
        boolean hasRemoved = false;
        Iterator<Option> iterator = optionList.iterator();
        
        while (iterator.hasNext()) {
            if (option.equals(iterator.next().text)) {
                iterator.remove();
                hasRemoved = true;
            }
        }
        return hasRemoved;
    }

    public void clear() {
        optionList.clear();
        chosenOption = -1;
    }

    public String get(int index) {
        int realIndex = index - 1;
        if (realIndex >= 0 && realIndex < optionList.size())
            return optionList.get(realIndex).text;
        throw new IndexOutOfBoundsException("Option " + index + " does not exist");
    }

    public int get(String text) {
        for (int i = 0; i < optionList.size(); i++) {
            if (optionList.get(i).text.equals(text)) {
                return i + 1;
            }
        }
        return 0;
    }

    public String[] getAll() {
        String[] allOptions = new String[optionList.size()];
        for (int i = 0; i < optionList.size(); ++i) {
            allOptions[i] = optionList.get(i).text;
        }
        return allOptions;
    }

    public int getTotal() {
        return optionList.size();
    }

    public void display(int index, PrintStream out) {
        out.println("[" + index + "] " + get(index));
    }

    public void displayAll(PrintStream out) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < optionList.size(); i++) {
            stringBuilder.append("[")
                .append(
                    (optionList.get(i).key == null) ? (i + 1) : (optionList.get(i).key)
                )
                .append("] ");
                stringBuilder.append(optionList.get(i).text)
                .append("\n");
        }
        out.print(stringBuilder.toString());
    }

}
