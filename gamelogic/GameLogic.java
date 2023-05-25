package me.Goldensang.gamelogic;

import me.Goldensang.gameobject.Bird;
import me.Goldensang.gameobject.Pipe;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GameLogic implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        GameState.updateTickCount();

        GameState.updateBirdGravityScale();

        GameState.updateScoreForNewTick();

        Pipe closest = GameState.findTheLeftMostAlivePipe();

        GameState.updateNeuralNetworkWithNewPipeData(closest);

        GameState.updatePipeForNewIteration();

        GameState.removeDeadPipe();

        GameState.updateBirdAndPipeState();

        GameState.updateBirdGravity();

        if (GameState.checkForStopState()) {
            GameState.proceedStopState();
        }
        GameState.iterate();
    }
}
