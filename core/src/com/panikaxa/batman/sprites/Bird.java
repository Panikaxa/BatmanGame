package com.panikaxa.batman.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class Bird {

    private int movement;
    private int gravity;
    private Vector3 position;
    private Vector3 velosity;
    private Texture texture;
    private Rectangle boundsBird, boundsBird1;
    private Animation birdAnimation;
    private Sound flap;
    private boolean isAlive;

    public  Bird (int x, int y) {
        movement = 100;
        gravity = -15;
        position = new Vector3(x, y, 0);
        velosity = new Vector3(0, 0, 0);
        isAlive = true;
        texture = new Texture("birdanimation.png");
        birdAnimation = new Animation(new TextureRegion(texture), 4, 0.5f);
        boundsBird = new Rectangle(x, y, (texture.getWidth()/4)-30, texture.getHeight()-10);
        boundsBird1 = new Rectangle(x, y, texture.getWidth()/18, texture.getHeight()-15);
        flap = Gdx.audio.newSound(Gdx.files.internal("1.wav"));
    }

    public Vector3 getPosition() {
        return position;
    }

    public TextureRegion getBird() {
        return birdAnimation.getFrame();
    }

    public void update(float dt) {
        birdAnimation.update(dt);
        if (position.y > 0)
            velosity.add(0, gravity, 0);
        velosity.scl(dt);
        position.add(movement * dt, velosity.y, 0);
        if (position.y < 75)
            position.y = 75;
        velosity.scl(1/dt);
        boundsBird.setPosition(position.x+5, position.y+5);
        boundsBird1.setPosition(position.x+boundsBird.getWidth()+5, position.y+10);
    }

    public  void jump() {
        if (isAlive) {
            velosity.y = 250;
            flap.play(0.3f);
        }
    }

    public Rectangle getBoundsBird(){
        return  boundsBird;
    }
    public Rectangle getBoundsBird1(){
        return  boundsBird1;
    }

    public void dispose() {
        texture.dispose();
        flap.dispose();
    }

    public void stop() {
        isAlive = false;
        movement = 0;
        velosity.y = 0;
        gravity = 0;
    }

    public  void onRestart() {
        movement = 100;
        velosity.y = 0;
        gravity = -15;
        isAlive = true;
    }

    public boolean isAlive() {
        return isAlive;
    }

}
