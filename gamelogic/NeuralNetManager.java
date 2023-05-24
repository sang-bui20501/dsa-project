package me.Goldensang.gamelogic;

import me.Goldensang.NeuralNetwork;

public class NeuralNetManager {
    private static NeuralNetManager neuralNetManager = null;

    private NeuralNetManager() { }


    public static NeuralNetManager getInstance() {
        if (neuralNetManager == null) {
            neuralNetManager = new NeuralNetManager();
        }
        return neuralNetManager;
    }

}
