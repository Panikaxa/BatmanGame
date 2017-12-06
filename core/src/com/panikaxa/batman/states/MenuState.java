package com.panikaxa.batman.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.panikaxa.batman.Batman;

public class MenuState implements Screen {

    final Batman batman;
    private Texture background;
    private Texture playBtn;
    OrthographicCamera camera;

    public MenuState(final Batman bat) {
       this.batman = bat;
       camera = new OrthographicCamera();
       camera.setToOrtho(false, Batman.WIDHT/2,
                Batman.HEIGHT/2);
       background = new Texture("bg.png");
       playBtn = new Texture("playbtn.png");
    }

    @Override
    public void show() {

    }

   /* @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()) {
            gsm.set (new PlayState(gsm));
        }
    }*/

   /* @Override
    public void update(float dt) {
        handleInput();

    }*/

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        batman.batch.setProjectionMatrix(camera.combined);
        batman.batch.begin();
        batman.batch.draw(background, 0, 0);
        batman.batch.draw(playBtn, camera.position.x - playBtn.getWidth()/2, camera.position.y-30 );
        batman.batch.end();

        if (Gdx.input.justTouched()) {
            batman.setScreen (new PlayState(batman));
            dispose();
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
        background.dispose();
        playBtn.dispose();
    }
}
