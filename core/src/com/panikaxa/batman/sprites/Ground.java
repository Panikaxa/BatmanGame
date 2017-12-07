package com.panikaxa.batman.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by PEV on 07.12.2017.
 */

public class Ground {

    private static final int GROUND_Y_OFFSET = -30;

    private Texture ground;
    private Vector2 groundPos1, groundPos2;
    private Rectangle boundsGround1, boundsGround2;

    public  Ground (float x) {

        ground = new Texture("ground.png");
        groundPos1 = new Vector2(x, GROUND_Y_OFFSET);
        groundPos2 = new Vector2(x + ground.getWidth() , GROUND_Y_OFFSET);

        boundsGround1 = new Rectangle(groundPos1.x, groundPos1.y, ground.getWidth(),
                ground.getHeight());
        boundsGround2 = new Rectangle(groundPos2.x, groundPos2.y, ground.getWidth(),
                ground.getHeight());
    }

    public Texture getGround() {
        return ground;
    }

    public Vector2 getGroundPos1() {
        return groundPos1;
    }

    public Vector2 getGroundPos2() {
        return groundPos2;
    }

    /*public Rectangle getBoundsGround1 () {
        return boundsGround1;
    }
    public Rectangle getBoundsGround2 () {
        return boundsGround2;
    }*/

    public void update(float x){
        if (x > groundPos1.x + ground.getWidth())
            groundPos1.add(ground.getWidth()*2, 0);
        boundsGround1.setPosition(groundPos1.x, groundPos1.y);
        if (x > groundPos2.x + ground.getWidth())
            groundPos2.add(ground.getWidth()*2, 0);
        boundsGround2.setPosition(groundPos2.x, groundPos2.y);
    }

    public boolean collides (Rectangle player, Rectangle player1) {
        return player.overlaps(boundsGround1) || player.overlaps(boundsGround2) ||
                player1.overlaps(boundsGround1) || player1.overlaps(boundsGround2);
    }

    public void dispose() {
        ground.dispose();
    }

}
