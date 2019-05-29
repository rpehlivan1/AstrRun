package com.myyastr.run.utils;

import com.myyastr.run.enums.Difficulty;
import com.myyastr.run.enums.GameState;

public class GameManager {
    private static GameManager myInstance = new GameManager();

    private GameState gameState;
    private Difficulty difficulty;


    private GameManager(){
        gameState = GameState.RUNNING;
        difficulty = Difficulty.DIFFICULTY_1;
    }

   //gražina dabartinę difficulty
    public Difficulty getDifficulty(){
        return this.difficulty;
    }

    //Pakeičia dabartine difficulty į naują.
    public void setDifficulty(Difficulty difficulty){
        this.difficulty = difficulty;
    }

  //true if game is max difficulty
    public boolean isMaxDifficulty() {
        return difficulty == Difficulty.values()[Difficulty.values().length - 1];
    }

    //difficulty užsetinimas
    public void resetDifficulty() {
        setDifficulty(Difficulty.values()[0]);
    }

    //current gamestate
    public GameState getGameState(){
        return this.gameState;
    }

    //Leidžia sukurti instance
    public static GameManager getInstance(){
        return myInstance;
    }

   //Pakeičia dabartine gamestate į kitą
    public void setGameState(GameState gameState){
        this.gameState = gameState;
    }

}
