package com.panikaxa.batman.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.panikaxa.batman.Batman;


public class GameOver implements Screen {
    final Batman batman;
    OrthographicCamera camera;
    private Texture background;
    private Texture gameOver;

    public GameOver(Batman bat) {
        this.batman = bat;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Batman.WIDHT/2,
                Batman.HEIGHT/2);
        background = new Texture("bg.png");
        gameOver = new Texture("gameover.png");
    }


    public void handleInput() {
        if (Gdx.input.justTouched()) {
            batman.setScreen (new PlayState(batman));
            dispose();
        }
    }


    @Override
    public void render(float dt) {
        handleInput();
        batman.batch.setProjectionMatrix(camera.combined);
        batman.batch.begin();
        batman.batch.draw(background, 0, 0);
        batman.batch.draw(gameOver, camera.position.x - gameOver.getWidth()/2, camera.position.y );
        batman.batch.end();
    }

    @Override
    public void show() {

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
        background.dispose();
        gameOver.dispose();
    }
}
