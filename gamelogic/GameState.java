package me.Goldensang.gamelogic;

import me.Goldensang.FComp;
import me.Goldensang.gameobject.Bird;
import me.Goldensang.gameobject.Pipe;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class GameState {
    private final static int screenWidth = 800;
    private final static int screenHeight = 800;
    private final static int gameTick = 3;
    private final static int maxGravityScaleOnBird = 15;
    private final static int birdFixLocation = 300;
    private final static int locationEpsilonForBirdToEarnScore = 6;
    private final static int maxBirdGravityNegativeScale = 10;
    private final static int pipeSpeed = 10;
    private static final int gravityIncrementPerOddTick = 2;
    private static int tickCount;
    private static ArrayList<Pipe> pipes;

    private static HashMap<Bird, ArrayList<Integer> > birdPipeScoreMapper;
    private static ArrayList<Bird> birds;

    private static JFrame mainFrame;

    private static Rectangle background;
    private static Rectangle dirt;

    private static Timer timer;

    public static void initialize() {
        pipes = new ArrayList<>();
        birds = new ArrayList<>();
        birdPipeScoreMapper = new HashMap<>();

        // Load images & default settings for JFrames
        mainFrame = new JFrame();
        mainFrame.add(new FComp());
        mainFrame.setVisible(true);
        mainFrame.setSize(screenWidth, screenHeight);
        mainFrame.setResizable(false);

        // Initialize backgrounds objects (dirts, backgrounds, ...)
        background = new Rectangle(0, 0, screenWidth, screenHeight);
        dirt = new Rectangle(0, 650, screenWidth, 150);

        timer = new Timer(gameTick, new GameLogic());
        timer.start();
    }

    public static boolean checkForStopState() {
        return birds.stream().anyMatch(bird -> bird.isAlive());
    }

    public static void proceedStopState() {
        timer.stop();
        // Add Neural Net logic here
    }

    public static void updateBirdGravity() {
        birds.forEach(bird -> {
            if (bird.isAlive())
                bird.increaseYPosition(Math.max(maxBirdGravityNegativeScale , bird.getGravityScale()));
        });
    }
    public static boolean isIntersect(Pipe pipe, Bird bird) {
        int birdYCords = bird.getBird().y;

        if (birdYCords > screenHeight - dirt.height || birdYCords < 0) {
            return true;
        }
        if (bird.getBird().intersects(pipe.r1) || bird.getBird().intersects(pipe.r2)) {
            return true;
        }
        return false;
    }
    public static void updateBirdScoreWhenIntersect(Bird bird, Pipe pipe) {
        if (isIntersect(pipe, bird)) {
            return;
        }

        int birdXCords = bird.getBird().x;
        int birdWidth = bird.getBird().width;

        if (birdXCords + birdWidth - locationEpsilonForBirdToEarnScore < pipe.p1upx || birdXCords + birdWidth > pipe.p1upx + pipe.w) {
            return;
        }
        if (birdPipeScoreMapper.get(bird).contains(pipe.id)) {
            return;
        }

        bird.setScore(bird.getScore() + 1);

        if (!birdPipeScoreMapper.containsKey(bird)) {
            birdPipeScoreMapper.put(bird, new ArrayList<>());
        }

        birdPipeScoreMapper.get(bird).add(pipe.id);

    }
    public static void updateBirdAndPipeState() {
        pipes.forEach(pipe -> {
            birds.forEach(bird -> {
                if (bird.isAlive()) {
                    updateBirdScoreWhenIntersect(bird, pipe);
                }
            });
        });
    }

    public static void removeDeadPipe() {
        pipes = pipes.stream().filter(pipe -> pipe.p1upx + pipe.w >= 0).collect(Collectors.toCollection(ArrayList::new));
    }
    public static void updatePipeForNewIteration() {
        pipes.forEach(pipe -> pipe.transLeft(pipeSpeed));
    }
    public static void updateNeuralNetworkWithNewPipeData(Pipe newPipeData) {
        birds.forEach(bird -> {
            bird.getBirdNeuralNetwork().update(bird, newPipeData, bird.getGravityScale());
        });
    }

    public static Pipe findTheLeftMostAlivePipe() {
        return pipes.stream()
                .filter(pipe -> pipe.p1upx - birdFixLocation >= 0 || pipe.p1upx + pipe.w - birdFixLocation >= 0)
                .findFirst()
                .get();
    }

    public static void updateBirdGravityScale() {
        birds.forEach(bird -> {
            if (tickCount % 2 == 0 && bird.getGravityScale() < maxGravityScaleOnBird) {
                bird.setGravityScale(bird.getGravityScale() + gravityIncrementPerOddTick);
            }
        });
    }
    public static void updateScoreForNewTick() {
        birds.forEach(bird -> {
            if (bird.isAlive())
                bird.setTimeAlive(tickCount);
        });
    }

    public static void updateTickCount() {
        tickCount = tickCount + 1;
    }

    // Auto generated function
    public static Rectangle getBackground() {
        return background;
    }

    public static void setBackground(Rectangle background) {
        GameState.background = background;
    }

    public static Rectangle getDirt() {
        return dirt;
    }

    public static void setDirt(Rectangle dirt) {
        GameState.dirt = dirt;
    }

    public static int getTickCount() {
        return tickCount;
    }

    public static void setTickCount(int tickCount) {
        GameState.tickCount = tickCount;
    }

    public static ArrayList<Pipe> getPipes() {
        return pipes;
    }

    public static void setPipes(ArrayList<Pipe> pipes) {
        GameState.pipes = pipes;
    }

    public static ArrayList<Bird> getBirds() {
        return birds;
    }

    public static void setBirds(ArrayList<Bird> birds) {
        GameState.birds = birds;
    }

    public static JFrame getMainFrame() {
        return mainFrame;
    }

    public static void setMainFrame(JFrame mainFrame) {
        GameState.mainFrame = mainFrame;
    }

    public static void iterate() {
        mainFrame.repaint();
    }
}
