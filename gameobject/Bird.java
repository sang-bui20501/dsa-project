package me.Goldensang.gameobject;

import me.Goldensang.NeuralNetwork;

import java.awt.*;

public class Bird {
    private int gravityScale;

    private Rectangle bird;

    private NeuralNetwork neuralNetwork;

    private int id;

    private int score;

    private int timeAlive;

    private boolean isAlive;
    public Bird() {
        this.bird = new Rectangle(300, 300, 50, 40);;

        this.gravityScale = 0;
        this.score = 0;
        this.timeAlive = 0;
        this.isAlive = true;

        this.neuralNetwork = new NeuralNetwork(this, true);
    }

    public NeuralNetwork getNeuralNetwork() {
        return neuralNetwork;
    }

    public void setNeuralNetwork(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
    }
    public void increaseYPosition(int value) {
        this.bird.y += value;
    }

    public void decreaseYPosition(int value) {
        this.bird.y -= value;
    }

    // Auto generated function
    public NeuralNetwork getBirdNeuralNetwork() {
        return neuralNetwork;
    }

    public void setBirdNeuralNetwork(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTimeAlive() {
        return timeAlive;
    }

    public void setTimeAlive(int timeAlive) {
        this.timeAlive = timeAlive;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public Rectangle getBird() {
        return bird;
    }

    public void setBird(Rectangle bird) {
        this.bird = bird;
    }

    public int getScore() {
        return score;
    }

    public void updateScore(int score) {
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getGravityScale() {
        return gravityScale;
    }

    public void setGravityScale(int gravityScale) {
        this.gravityScale = gravityScale;
    }


}
