package com.panikaxa.batman.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.panikaxa.batman.Batman;
import com.panikaxa.batman.sprites.BackgroundMenu;

public class MenuState implements Screen {

    final Batman batman;
    private BackgroundMenu backgroundMenu;
    OrthographicCamera camera;

    public MenuState(final Batman bat) {
       this.batman = bat;
       camera = new OrthographicCamera();
       camera.setToOrtho(false, Batman.WIDHT/2,
                Batman.HEIGHT/2);
       backgroundMenu = new BackgroundMenu();

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        batman.batch.setProjectionMatrix(camera.combined);
        batman.batch.begin();
        batman.batch.draw(backgroundMenu.getBackground(), 0, 0);
        batman.batch.draw(backgroundMenu.getPlayBtn(), camera.position.x -
                backgroundMenu.getPlayBtn().getWidth()/2, camera.position.y-30 );
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
        backgroundMenu.dispose();
    }
}
