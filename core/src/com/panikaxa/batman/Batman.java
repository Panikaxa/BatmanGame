package com.panikaxa.batman;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.panikaxa.batman.states.MenuState;

public class Batman extends Game {
	public static final int WIDHT = 480;
	public static final int HEIGHT = 800;
	public static final String TITLE = "Batman";

	public SpriteBatch batch;
	private Music music;


	@Override
	public void create () {
		batch = new SpriteBatch();
		music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
		music.setLooping(true);
		music.setVolume(0.2f);
		music.play();
		this.setScreen(new MenuState(this));
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		super.dispose();
		batch.dispose();
		music.dispose();
	}
}

