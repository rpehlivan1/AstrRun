package com.myyastr.run.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.myyastr.run.enums.GameState;
import com.myyastr.run.utils.Constants;
import com.myyastr.run.utils.GameManager;

public class Score extends Actor {

    private static Preferences prefs;
    private float score;
    private Rectangle rectangle;
    private BitmapFont font;
    private Texture slideTexture;
    private Texture jumpTexture;

   //Display score
    public Score(Rectangle rectangle) {
        prefs = Gdx.app.getPreferences("Game");
        this.rectangle = rectangle;
        setWidth(rectangle.width);
        setHeight(rectangle.height);
        font = new BitmapFont(Gdx.files.internal("fonts/myFont.fnt"));

        slideTexture = new Texture(Gdx.files.internal("slide.png"));
        jumpTexture = new Texture(Gdx.files.internal("jump.png"));



    }

    @Override
    public void act(float delta){
        super.act(delta);
        if(GameManager.getInstance().getGameState() == GameState.RUNNING){
            score += delta;
            setHighScore();
        }

    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        font.setColor(1.0f, 1.0f,1.0f, 0.8f);

        font.draw(batch, "Score : "+ getScore()  +" ! ", rectangle.x+70, rectangle.y);
        font.setColor(0.0f, 0.0f, 0.0f, 0.8f);
        font.draw(batch, "Best : "+ prefs.getInteger
                ("highScore") +  " ! ", rectangle.x+70, rectangle.y-100);

        batch.draw(slideTexture, 20,
                Constants.APP_HEIGHT-150, 150, 150);
        batch.draw(jumpTexture, Constants.APP_WIDTH-200,
                Constants.APP_HEIGHT-150, 150, 150);
    }


    public int getScore(){
        return (int)score;
    }


    public void addScore(int add){
        score += add;
    }

    //Saugo localiai score
    public int getHighScore(){
        return Gdx.app.getPreferences
                ("Game").getInteger("highScore");
    }


    public void setHighScore(){
        if(getScore() > getHighScore()){
            prefs.putInteger("highScore", (int)score);
            prefs.flush();
        }

    }


    public void reset() {
        score = 0f;
        //TODO

    }

}
