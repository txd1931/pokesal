package dev.rhtj.pokesal;

public class Main {
    public static void main(String[] args) {
        Game.getInstance().
        setup(System.out, System.in);
        Game.getInstance().start();
    }
}