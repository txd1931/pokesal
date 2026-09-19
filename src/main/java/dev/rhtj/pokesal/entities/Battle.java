package dev.rhtj.pokesal.entities;

public class Battle {

    private Trainer[] trainers = new Trainer[2];
    private Pokesal[] pokesals = new Pokesal[2];
    private boolean isActive = true;
    private int winner = -2;
    private boolean trainerATurn = true;
    private int round = 0;

    public Battle(Trainer trainerA, Trainer trainerB) {
        trainerA.setBattle(this);
        trainerB.setBattle(this);
        trainers[0] = trainerA;
        pokesals[0] = trainerA.getPokedeck().getNext();
        trainers[1] = trainerB;
        pokesals[1] = trainerB.getPokedeck().getNext();
    }


    public Trainer getTrainer(int index) {
        if (index < 0 || index > 1) {
            throw new IllegalArgumentException("Index tem que ser 0 ou 1");
        }
        return trainers[index];
    }

    public boolean isActive() {
        return isActive;
    }

    public void endBattle() {
        isActive = false;
    }

    public int getRound() {
        return round;
    }

    public void nextRound() {
        round++;
    }

    public boolean isTrainerATurn() {
        return trainerATurn;
    }

    public void switchTurn() {
        trainerATurn = !trainerATurn;
    }

    public Pokesal getCurrentPokesal() {
        return trainerATurn ? pokesals[0] : pokesals[1];
    }

    public Pokesal getOpponentPokesal() {
        return trainerATurn ? pokesals[1] : pokesals[0];
    }

    public Trainer getWinner() {
        return winner == -2 ? null : trainers[winner];
    }

    public Trainer getLoser() {
        return winner == -2 ? null : trainers[winner == 0 ? 1 : 0];
    }

    public Trainer getCurrentTrainer() {
        return trainerATurn ? trainers[0] : trainers[1];
    }

    public Trainer getOpponentTrainer() {
        return trainerATurn ? trainers[1] : trainers[0];
    }

}
