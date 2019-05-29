package com.myyastr.run;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.myyastr.run.screens.MenuScreen;

public class AstrRun extends Game {

	@Override
	public void create () {
		this.setScreen(new MenuScreen(this));
	}

}
