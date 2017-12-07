package com.panikaxa.batman.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.panikaxa.batman.Batman;
import com.panikaxa.batman.sprites.Animation;
import com.panikaxa.batman.sprites.Bird;
import com.panikaxa.batman.sprites.Ground;
import com.panikaxa.batman.sprites.Tube;
import static com.badlogic.gdx.graphics.GL20.GL_BLEND;


public class PlayState implements Screen {

    private static final int TUBE_SPACING = 125;
    private static final int TUBE_COUNT = 4;


    final Batman batman;
    OrthographicCamera camera;
    private Bird bird;
    private Ground ground;
    private Texture bg;
    private Texture end;
    private Animation endAnim;
    private ShapeRenderer shapeRenderer;
    private Sound endSound;
    private GameState currentState;
    private Array<Tube> tubes;

    public PlayState(final Batman bat) {
        this.batman = bat;
        currentState = GameState.READY;
        bird = new Bird(50, 230);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Batman.WIDHT/2,
                Batman.HEIGHT/2);
        ground = new Ground(camera.position.x-(camera.viewportWidth/2));
        bg = new Texture("bg.png");
        end = new Texture("end.png");
        endAnim = new Animation(new TextureRegion(end), 5, 0.5f);
        endSound = Gdx.audio.newSound(Gdx.files.internal("4.wav"));

        tubes = new Array<Tube>();

        for (int i = 1; i <= TUBE_COUNT; i++) {
            tubes.add(new Tube(i * (TUBE_SPACING + Tube.TUBE_WIDTH)));
        }
        shapeRenderer = new ShapeRenderer();

    }

    private TextureRegion getEnd() {
        return endAnim.getFrame();
    }

    public enum GameState {
        READY, RUNNING, GAMEOVER
    }


    public void handleInput() {

        /*if (Gdx.input.isKeyPressed(Input.Keys.BACK)){
            Gdx.input.setCatchBackKey(true);
            pause();
            }*/

        if (Gdx.input.justTouched() && isReady()) {
            start();
        }

        if (Gdx.input.justTouched() && isRunning()) {
            bird.jump();
        }
        if (Gdx.input.justTouched() && isGameOver()) {
            batman.setScreen(new GameOver(batman));
            dispose();
            restart();
        }
    }

    public void update(float dt) {

        switch (currentState) {
            case READY:
                updateReady();
                break;

            case RUNNING:
            default:
                updateRunning(dt);
                break;
        }
    }

    private void updateRunning(float dt) {

        endAnim.update(dt);
        handleInput();
        ground.update(camera.position.x - (camera.viewportWidth/2));
        bird.update(dt);
        camera.position.x = bird.getPosition().x + 80;

        for (int i = 0; i < tubes.size; i++) {
            Tube tube = tubes.get(i);
            if (camera.position.x - (camera.viewportWidth/2) > tube.getPosTopTube().x
                    + tube.getTopTube().getWidth()) {
                tube.reposition(tube.getPosTopTube().x
                        + ((Tube.TUBE_WIDTH + TUBE_SPACING)* TUBE_COUNT));
            }

            if (tube.collides(bird.getBoundsBird(), bird.getBoundsBird1()) ||
                    ground.collides(bird.getBoundsBird(), bird.getBoundsBird1())) {
                if (bird.isAlive()) {
                    bird.stop();
                    endSound.play();
                }
                currentState = GameState.GAMEOVER;
            }
        }

        camera.update();
    }

    private void updateReady() {
        handleInput();
    }

    @Override
    public void render(float dt) {

        batman.batch.setProjectionMatrix(camera.combined);

        shapeRenderer.setProjectionMatrix(camera.combined);

        batman.batch.begin();
        batman.batch.draw(bg, camera.position.x-(camera.viewportWidth/2), 0);

        for (Tube tube : tubes) {
            batman.batch.draw(tube.getTopTube(), tube.getPosTopTube().x, tube.getPosTopTube().y);
            batman.batch.draw(tube.getBottomTube(), tube.getPosBotTube().x, tube.getPosBotTube().y);
        }

        batman.batch.draw(ground.getGround(),ground.getGroundPos1().x, ground.getGroundPos1().y);
        batman.batch.draw(ground.getGround(),ground.getGroundPos2().x, ground.getGroundPos2().y);
        if (!isGameOver()) {
            batman.batch.draw(bird.getBird(), bird.getPosition().x, bird.getPosition().y);
        } else {
            batman.batch.draw(getEnd(), bird.getPosition().x +20, bird.getPosition().y);
        }
        batman.batch.end();
        update(dt);

       /* //Отрисовка прямоугольников
        Gdx.gl.glEnable(GL_BLEND);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(255,255,255, 0.5f);
        shapeRenderer.rect(bird.getBoundsBird().x, bird.getBoundsBird().y,
                bird.getBoundsBird().width, bird.getBoundsBird().height);
        shapeRenderer.rect(bird.getBoundsBird1().x, bird.getBoundsBird1().y,
                bird.getBoundsBird1().width, bird.getBoundsBird1().height);
        for (Tube tube : tubes) {
            shapeRenderer.rect(tube.getBoundsTop().x, tube.getBoundsTop().y,
                    tube.getBoundsTop().width, tube.getBoundsTop().height);
            shapeRenderer.rect(tube.getBoundsTop2().x, tube.getBoundsTop2().y,
                    tube.getBoundsTop2().width, tube.getBoundsTop2().height);
            shapeRenderer.rect(tube.getBoundsTop3().x, tube.getBoundsTop3().y,
                    tube.getBoundsTop3().width, tube.getBoundsTop3().height);
            shapeRenderer.rect(tube.getBoundsBot().x, tube.getBoundsBot().y,
                    tube.getBoundsBot().width, tube.getBoundsBot().height);
            shapeRenderer.rect(tube.getBoundsBot2().x, tube.getBoundsBot2().y,
                    tube.getBoundsBot2().width, tube.getBoundsBot2().height);
            shapeRenderer.rect(tube.getBoundsBot3().x, tube.getBoundsBot3().y,
                    tube.getBoundsBot3().width, tube.getBoundsBot3().height);
        }
        shapeRenderer.rect(ground.getBoundsGround1().x, ground.getBoundsGround1().y,
                ground.getBoundsGround1().width, ground.getBoundsGround1().height);
        shapeRenderer.rect(ground.getBoundsGround2().x, ground.getBoundsGround2().y,
                ground.getBoundsGround2().width, ground.getBoundsGround2().height);
        shapeRenderer.end();*/

    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        bird.stop();
        currentState = GameState.READY;
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        bg.dispose();
        end.dispose();
        ground.dispose();
        bird.dispose();
        endSound.dispose();
        for (Tube tube : tubes)
            tube.dispose();
    }

    private void start() {
        currentState = GameState.RUNNING;
        bird.onRestart();
    }

    private void restart() {
        currentState = GameState.READY;
        bird.onRestart();
    }

    private boolean isReady() {
        return currentState == GameState.READY;
    }

    private boolean isRunning() {
        return currentState == GameState.RUNNING;
    }

    private boolean isGameOver() {
        return currentState == GameState.GAMEOVER;
    }
}
