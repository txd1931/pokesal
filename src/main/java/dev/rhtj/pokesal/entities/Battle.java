package dev.rhtj.pokesal.entities;

import java.util.Random;

import dev.rhtj.pokesal.ItemRecord;

public class Battle {

    public enum State {
        TRAINER_A_TURN,
        TRAINER_B_TURN,
        TRAINER_A_WON,
        TRAINER_B_WON,
        NULL;

        public int toId() {
            if (this == NULL)
                return -1;
            return this == TRAINER_A_TURN || this == TRAINER_A_WON ? 0 : 1;
        }

        public boolean hasEnded() {
            return this == TRAINER_A_WON || this == TRAINER_B_WON;
        }

        public boolean isActive(){
            return this == TRAINER_A_TURN || this == TRAINER_B_TURN;
        }
    }

    private Trainer[] trainers = new Trainer[2];
    private int[] trainersRemainingItemUsages = new int[2];
    private Pokesal[] pokesals = new Pokesal[2];
    private State state = State.NULL;
    private int turns = 0;

    public Battle(Trainer trainerA, Trainer trainerB) {
        trainerA.setBattle(this);
        trainerB.setBattle(this);
        trainers[0] = trainerA;
        pokesals[0] = trainerA.getPokedeck().getNext();
        trainers[1] = trainerB;
        pokesals[1] = trainerB.getPokedeck().getNext();
        trainersRemainingItemUsages[0] = 2;
        trainersRemainingItemUsages[1] = 2;
        decideNextTrainer();
    }


    public Trainer getTrainer(int index) {
        if (index < 0 || index > 1) {
            throw new IllegalArgumentException("Index tem que ser 0 ou 1");
        }
        return trainers[index];
    }

    public boolean isActive() {
        return state == State.TRAINER_A_TURN || state == State.TRAINER_B_TURN;
    }

    public void endBattle() {
        state = (state == State.TRAINER_A_TURN) ? State.TRAINER_A_WON : State.TRAINER_B_WON;

        int cashDiff = (int) (getLoser().getCash() * 0.1f);
        getWinner().setCash(getWinner().getCash() + cashDiff);
        getLoser().setCash(getLoser().getCash() - cashDiff);
    }

    public int getRound() {
        return turns / 2 + 1;
    }

    public int getTurn() {
        return turns % 2;
    }

    private void nextTurn() {
        turns++;
        if (!state.isActive()) {
            System.err.println("O turno não pode ser avançado em uma batalha inativa");
            System.exit(1);
        }
        decideNextTrainer();
    }

    private void decideNextTrainer() {
        if (getTurn() == 0) {
            int speedA = getTrainer(0).getPokedeck().getNext().getSpeed();
            int speedB = getTrainer(1).getPokedeck().getNext().getSpeed();
            if (speedA == speedB) {
                if (new Random().nextBoolean())
                    state = State.TRAINER_A_TURN;
                else
                    state = State.TRAINER_B_TURN;
            } 
            else if (speedA > speedB) {
                state = State.TRAINER_A_TURN;
            } 
            else {
                state = State.TRAINER_B_TURN;
            }
        } else {
            if (state == State.TRAINER_A_TURN)
                state = State.TRAINER_B_TURN;
            else 
                state = State.TRAINER_A_TURN;
        }
    }

    public State getState() {
        return state;
    }

    public void atack() {
        if (!state.isActive()) {
            System.err.println(getCurrentTrainer().getName() + " tentou atacar em uma batalha inativa");
            System.exit(1);
        }
        getCurrentPokesal().atack(getOpponentPokesal());
        if (getOpponentPokesal().getHealthPoints() <= 0) 
            endBattle();
        else 
            nextTurn();    
    }

    public boolean useItem(ItemRecord item) {
        if (!state.isActive()) {
            System.err.println("Tentou usar item " + item.name() + " em uma batalha inativa");
            System.exit(1);
        }
        if (state == State.TRAINER_A_TURN && trainersRemainingItemUsages[0] == 0 || 
            state == State.TRAINER_B_TURN && trainersRemainingItemUsages[1] == 0)
            return false;
        Pokesal current = getCurrentPokesal();
        Pokesal opponent = getOpponentPokesal();
        switch (item.id()) {
            case "fireball" -> {
                opponent.setEffect(Pokesal.Effect.BURN, 1.0);
            }
            case "potion_poison_weak" -> {
                current.setEffect(Pokesal.Effect.POISON, 0.5);
            }
            case "potion_poison_strong" -> {
                current.setEffect(Pokesal.Effect.POISON, 1.0);
            }
            case "antidote" -> {
                current.setEffect(Pokesal.Effect.POISON, 0.0);
            }
            case "snowball" -> {
                opponent.setEffect(Pokesal.Effect.FREEZE, 1.0);
            }
            case "mudball" -> {
                opponent.setSpeed((int) (opponent.getSpeed() * 0.85f));
            }
            case "potion_health_weak" -> {
                current.setEffect(Pokesal.Effect.HEAL, 0.5d);
            }
            case "potion_health_strong" -> {
                current.setEffect(Pokesal.Effect.HEAL, 1.0d);
            }
            case "potion_health_instant" -> {
                current.setHealthPoints(current.getPokesalRecord().healthPoints());
            }
            case "adrenaline" -> {
                current.setSpeed((int) (current.getSpeed() * 1.75d));
            }
            case "shield" -> {
                current.setDefence((int) (current.getDefence() * 1.5d));
            }
            case "sword" -> {
                current.setAtack((int) (current.getAtack() * 1.3d));
            }
            case "nuke" -> {
                opponent.setHealthPoints(0);
                current.setHealthPoints(current.getHealthPoints() - 50);
            }
        }
        trainersRemainingItemUsages[state.toId()]--;
        return true;
    }

    public Pokesal getCurrentPokesal() {
        if (!state.isActive())
            return null;
        return state == State.TRAINER_A_TURN ? pokesals[0] : pokesals[1];
    }

    public Pokesal getOpponentPokesal() {
        if (!state.isActive())
            return null;
        return state == State.TRAINER_A_TURN ? pokesals[1] : pokesals[0];
    }

    public Trainer getWinner() {
        if (!state.hasEnded())
            return null;
        return trainers[state == State.TRAINER_A_WON ? 0 : 1];
    }

    public Trainer getLoser() {
        if (!state.hasEnded())
            return null;
        return trainers[state == State.TRAINER_A_WON ? 1 : 0];
    }

    public Trainer getCurrentTrainer() {
        if (!state.isActive())
            return null;
        return state == State.TRAINER_A_TURN ? trainers[0] : trainers[1];
    }

    public Trainer getOpponentTrainer() {
        if (!state.isActive())
            return null;
        return state == State.TRAINER_A_TURN ? trainers[1] : trainers[0];
    }

}
