package com.panikaxa.batman.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.panikaxa.batman.Batman;
import com.panikaxa.batman.sprites.Animation;
import com.panikaxa.batman.sprites.Bird;
import com.panikaxa.batman.sprites.Tube;


public class PlayState extends State {

    private static final int TUBE_SPACING = 125;
    private static final int TUBE_COUNT = 4;
    private static final int GROUND_Y_OFFSET = -30;

    private Bird bird;
    private Texture bg;
    private Texture end;
    private Animation endAnim;
    private Texture ground;
    private Vector2 groundPos1, groundPos2;
    private Rectangle boundsGround1, boundsGround2;
    private ShapeRenderer shapeRenderer;
    private Sound endSound;
    private GameState currentState;

    private Array<Tube> tubes;

    public PlayState(GameStateManager gsm) {
        super(gsm);

        currentState = GameState.READY;
        bird = new Bird(50, 230);

        camera.setToOrtho(false, Batman.WIDHT/2,
                Batman.HEIGHT/2);
        bg = new Texture("bg.png");
        end = new Texture("end.png");
        endAnim = new Animation(new TextureRegion(end), 5, 0.5f);
        endSound = Gdx.audio.newSound(Gdx.files.internal("4.wav"));
        // Земля
        ground = new Texture("ground.png");
        groundPos1 = new Vector2(camera.position.x-(camera.viewportWidth/2), GROUND_Y_OFFSET);
        groundPos2 = new Vector2((camera.position.x-(camera.viewportWidth/2))
                + ground.getWidth() , GROUND_Y_OFFSET);

        boundsGround1 = new Rectangle(groundPos1.x, groundPos1.y, ground.getWidth(),
                ground.getHeight());
        boundsGround2 = new Rectangle(groundPos2.x, groundPos2.y, ground.getWidth(),
                ground.getHeight());


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

    /*
    public Rectangle getBoundsGround1 () {
        return boundsGround1;
    }
    public Rectangle getBoundsGround2 () {
        return boundsGround2;
    }
    */

    @Override
    protected void handleInput() {

        if (Gdx.input.justTouched() && isReady()) {
            start();
        }

        if (Gdx.input.justTouched() && isRunning()) {
            bird.jump();
        }
        if (Gdx.input.justTouched() && isGameOver()) {
            restart();
            gsm.set(new GameOver(gsm));
        }
    }

    @Override
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
        updateGround();
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
                    collides(bird.getBoundsBird(), bird.getBoundsBird1())) {
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
        // Пока ничего не делаем
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);

        shapeRenderer.setProjectionMatrix(camera.combined);

        sb.begin();
        sb.draw(bg, camera.position.x-(camera.viewportWidth/2), 0);

        for (Tube tube : tubes) {
            sb.draw(tube.getTopTube(), tube.getPosTopTube().x, tube.getPosTopTube().y);
            sb.draw(tube.getBottomTube(), tube.getPosBotTube().x, tube.getPosBotTube().y);
        }

        sb.draw(ground,groundPos1.x, groundPos1.y);
        sb.draw(ground,groundPos2.x, groundPos2.y);

        if (bird.isAlive()) {
            sb.draw(bird.getBird(), bird.getPosition().x, bird.getPosition().y);
        } else {
            sb.draw(getEnd(), bird.getPosition().x +20, bird.getPosition().y);
        }
        sb.end();

        /* Отрисовка прямоугольников
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
        shapeRenderer.rect(getBoundsGround1().x, getBoundsGround1().y,
                getBoundsGround1().width, getBoundsGround1().height);
        shapeRenderer.rect(getBoundsGround2().x, getBoundsGround2().y,
                getBoundsGround2().width, getBoundsGround2().height);
        shapeRenderer.end();
        */
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

    private void updateGround(){
        if (camera.position.x - (camera.viewportWidth/2) > groundPos1.x + ground.getWidth())
            groundPos1.add(ground.getWidth()*2, 0);
            boundsGround1.setPosition(groundPos1.x, groundPos1.y);
        if (camera.position.x - (camera.viewportWidth/2) > groundPos2.x + ground.getWidth())
            groundPos2.add(ground.getWidth()*2, 0);
            boundsGround2.setPosition(groundPos2.x, groundPos2.y);
    }

    private boolean collides (Rectangle player, Rectangle player1) {
        return player.overlaps(boundsGround1) || player.overlaps(boundsGround2) ||
                player1.overlaps(boundsGround1) || player1.overlaps(boundsGround2);
    }

    private boolean isReady() {
        return currentState == GameState.READY;
    }

    private void start() {
        currentState = GameState.RUNNING;
    }

    private void restart() {
        currentState = GameState.READY;
        bird.onRestart();
    }

    private boolean isGameOver() {
        return currentState == GameState.GAMEOVER;
    }

    private boolean isRunning() {
        return currentState == GameState.RUNNING;
    }
}
