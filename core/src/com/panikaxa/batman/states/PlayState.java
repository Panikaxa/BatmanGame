package com.panikaxa.batman.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.panikaxa.batman.Batman;
import com.panikaxa.batman.sprites.Animation;
import com.panikaxa.batman.sprites.Bird;
import com.panikaxa.batman.sprites.Ground;
import com.panikaxa.batman.sprites.Tube;



public class PlayState implements Screen {

    private static final int TUBE_SPACING = 125;
    private static final int TUBE_COUNT = 4;
    private BitmapFont font, font1;

    private int score = 0;

    private String s;
    private float startTime = 4;
    private float time = 4;


    public final Batman batman;
    private OrthographicCamera camera;
    private OrthographicCamera camera2;
    private Bird bird;
    private Ground ground;
    private Texture bg;
    private Texture end;
    private Animation endAnim;
    private ShapeRenderer shapeRenderer;
    private Sound endSound;
    private GameState currentState;
    private Array<Tube> tubes;

    private float rateh;

    public PlayState(final Batman bat) {
        this.batman = bat;
        currentState = GameState.READY;
        bird = new Bird(50, 230);
        float aspectRatio = (float) Gdx.graphics.getWidth()/Gdx.graphics.getHeight();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Batman.HEIGHT/2*aspectRatio,Batman.HEIGHT/2);
        camera2 = new OrthographicCamera();
        camera2.setToOrtho(false, Batman.HEIGHT*aspectRatio, Batman.HEIGHT);

        rateh = camera2.viewportHeight/camera.viewportHeight;


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

        font = new BitmapFont(Gdx.files.internal("font.fnt"));
        font1 = new BitmapFont(Gdx.files.internal("font.fnt"));
        font1.getData().setScale(3);


    }

    private TextureRegion getEnd() {
        return endAnim.getFrame();
    }

    public enum GameState {
        READY, RUNNING, GAMEOVER
    }


    private void handleInput() {

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

    private void update(float dt) {

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
        camera2.position.x =  camera.position.x;

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

            if (!tube.getIsScored() && tube.getPosTopTube().x < bird.getPosition().x) {
                addScore();
                tube.setIsScored(true);
            }

            if (score >= 10) bird.setVelosity(1.5f);
            if (score >= 20) bird.setVelosity(2f);
            if (score >= 30) bird.setVelosity(2.5f);
            if (score >= 40) bird.setVelosity(3f);
        }

        camera.update();
        camera2.update();
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
        batman.batch.setProjectionMatrix(camera2.combined);
        float x = camera2.position.x - 3 * getScore(score).length();
        float y = camera2.position.y * rateh;
        font.draw(batman.batch, getScore(score), x, y);


        if(isReady()) {
            startTime -= dt;
            if(startTime < time){
                time--;
                if (startTime <= 4 && startTime > 3) s = "3";
                    else if (startTime <= 3 && startTime > 2) s = "2";
                    else if (startTime <= 2 && startTime > 1) s = "1";
                    else if (startTime <= 1 && startTime > 0) s = "go";
                    else if (startTime <=0) start();
            }
            font1.draw(batman.batch, s, x, y / 1.5f);
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
        font.dispose();
        font1.dispose();
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


    private String getScore(int score) {
        return score + "";
    }

    private void addScore() {
        score += 1;
    }
}
