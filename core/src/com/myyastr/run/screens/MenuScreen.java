package com.myyastr.run.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class MenuScreen implements Screen {

    private SpriteBatch batch;
    private BitmapFont fnt;
    private BitmapFont defaultFnt;
    private Color colorRed;
    private Color colorWhite;
    private Color colorBlue;
    private Music introMusic;

    final Game gam;

    public MenuScreen(Game gam) {
        this.batch = new SpriteBatch();
        this.fnt = new BitmapFont(Gdx.files.internal("fonts/myFont.fnt"));
        this.defaultFnt = new BitmapFont();
        this.colorRed = new Color(1, 0, 0, 1);
        this.colorWhite = new Color(1, 1,1 ,1);
        this.colorBlue = new Color(0,0,1,1);
        this.introMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/intro.mp3"));

        this.gam = gam;


    }

    @Override
    public void show() {

    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0.0f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        fnt.setColor(colorBlue);
        fnt.draw(batch, "Best Running Game Ever", 100, 350);
        fnt.setColor(colorWhite);
        fnt.draw(batch, "This is not a Joke", 200, 260);
        fnt.setColor(colorRed);
        fnt.draw(batch,"CLICK", 400, 190);
        defaultFnt.draw(batch, "TO", 400, 130);
        defaultFnt.draw(batch, "PLAY", 400,90);
        batch.end();
         if (Gdx.input.isTouched()) {
             introMusic.stop();
             introMusic.dispose();
             dispose();
             gam.setScreen(new GameScreen());
         }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }
    @Override
    public void dispose() {

    }
}
