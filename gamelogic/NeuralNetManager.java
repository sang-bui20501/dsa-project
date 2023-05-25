package me.Goldensang.gamelogic;

import javafx.util.Pair;
import me.Goldensang.NeuralNetwork;
import me.Goldensang.gameobject.Bird;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public class NeuralNetManager {
    private final int maxPopulation = 200;
    private static NeuralNetManager neuralNetManager = null;

    private NeuralNetManager() { }

    public static NeuralNetManager getInstance() {
        if (neuralNetManager == null) {
            neuralNetManager = new NeuralNetManager();
        }
        return neuralNetManager;
    }

    private ArrayList<NeuralNetwork> finestGeneration;

    public void initialize() {
        this.finestGeneration = new ArrayList<>();
    }
    public void generateNewBirdAndNeuralNet() {
        for (int i = 0; i < maxPopulation; i++) {
            Bird bird = new Bird();
            bird.setNeuralNetwork(new NeuralNetwork(bird, true));
            bird.setAlive(true);
            GameState.getBirds().add(bird);
        }
    }
    public NeuralNetwork getBestBird() {
        Bird finestBird = GameState.getBirds()
                .stream()
                .sorted(Comparator.comparing(Bird::getTimeAlive))
                .findFirst()
                .get();
        return finestBird.getNeuralNetwork();
    }
    public void storeBestIterationNeuralNet() {
        Bird finestBird = GameState.getBirds()
                .stream()
                .sorted(Comparator.comparing(Bird::getTimeAlive))
                .findFirst()
                .get();

        finestGeneration.add(finestBird.getNeuralNetwork());
    }

    public void learnAndIterateToNextStage() {
        GameState.getBirds().clear();

        generateNewBirdAndNeuralNet();
        this.finestGeneration.sort(new Comparator<NeuralNetwork>() {
            @Override
            public int compare(NeuralNetwork o1, NeuralNetwork o2) {
                return o1.getBird().getTimeAlive() - o2.getBird().getTimeAlive();
            }
        });
        // k = Min 3 - > Finest Gen
        int k = Math.min(3, this.finestGeneration.size());

        int cnt = 0;

        for (int l = k; l < 2 * k; ++l) {
            GameState.getBirds().get(cnt).getBirdNeuralNetwork().mutateweight();
            cnt++;
        }
        for (int l = 2 * k; l < 4 * k; ++l) {
            for (int m = 0; m < Math.random() * l; ++m) {
                GameState.getBirds().get(l).getBirdNeuralNetwork().crossOver(this.finestGeneration.get((int) Math.random() * this.finestGeneration.size()));
            }
        }
        for (int l = 4 * k; l < maxPopulation; ++l) {
            GameState.getBirds().get(l).getNeuralNetwork().crossOver(
                    GameState.getBirds().get((int) Math.random() * 4 * k).getBirdNeuralNetwork()
            );
            GameState.getBirds().get(l).getNeuralNetwork().mutateweight();
        }
    }
}
