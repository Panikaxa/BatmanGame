package com.panikaxa.batman.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class Tube {

    public static final int TUBE_WIDTH = 52;
    private static final int FLUCTUATION = 130;
    private static final int TUBE_GAP = 100;
    private static final int LOWEST_OPENING = 120;

    private Texture topTube, bottomTube;
    private Vector2 posTopTube, posBotTube;
    private Random rand;
    private Rectangle boundsTop, boundsTop2, boundsTop3, boundsBot, boundsBot2, boundsBot3;
    private boolean isScored;

    public Texture getTopTube() {
        return topTube;
    }

    public Texture getBottomTube() {
        return bottomTube;
    }

    public Vector2 getPosTopTube() {
        return posTopTube;
    }

    public Vector2 getPosBotTube() {
        return posBotTube;
    }

    public boolean getIsScored(){
        return isScored;
    }

    public void setIsScored(boolean s){
        isScored = s;
    }


   /* public Rectangle getBoundsTop(){
        return  boundsTop;
    }
    public Rectangle getBoundsTop2(){
        return  boundsTop2;
    }
    public Rectangle getBoundsTop3(){
        return  boundsTop3;
    }
    public Rectangle getBoundsBot(){
        return  boundsBot;
    }
    public Rectangle getBoundsBot2(){
        return  boundsBot2;
    }
    public Rectangle getBoundsBot3(){
        return  boundsBot3;
    }*/


    public Tube(float x) {
        topTube = new Texture("toptube.png");
        bottomTube = new Texture("bottomtube.png");
        rand = new Random();
        isScored = false;

        posTopTube = new Vector2(x, rand.nextInt(FLUCTUATION) + TUBE_GAP + LOWEST_OPENING);
        posBotTube = new Vector2(x, posTopTube.y - TUBE_GAP - bottomTube.getHeight());

        boundsTop = new Rectangle(posTopTube.x, posTopTube.y + 40, topTube.getWidth(),
                topTube.getHeight()-20);
        boundsTop2 = new Rectangle(posTopTube.x, posTopTube.y + 25, (topTube.getWidth()/2)-2,
                15);
        boundsTop3 = new Rectangle(posTopTube.x+2, posTopTube.y, (topTube.getWidth()/3)-4, 25);
        boundsBot = new Rectangle(posBotTube.x+2, posBotTube.y, 52,175);
        boundsBot2= new Rectangle(posBotTube.x+7, posBotTube.y + boundsBot.getHeight(),
                40,82);
        boundsBot3= new Rectangle(posBotTube.x+18, posBotTube.y + boundsBot.getHeight()
                + boundsBot2.getHeight(),22,50);
    }

    public void reposition (float x) {
        setIsScored(false);
        posTopTube.set(x, rand.nextInt(FLUCTUATION) + TUBE_GAP + LOWEST_OPENING);
        posBotTube.set(x, posTopTube.y - TUBE_GAP - bottomTube.getHeight());
        boundsTop.setPosition(posTopTube.x, posTopTube.y+40);
        boundsTop2.setPosition(posTopTube.x, posTopTube.y + 25);
        boundsTop3.setPosition(posTopTube.x+2, posTopTube.y);
        boundsBot.setPosition(posBotTube.x+2, posBotTube.y);
        boundsBot2.setPosition(posBotTube.x+7, posBotTube.y + boundsBot.getHeight());
        boundsBot3.setPosition(posBotTube.x+18, posBotTube.y + boundsBot.getHeight()
                + boundsBot2.getHeight());
    }

    public boolean collides (Rectangle player, Rectangle player1) {
        return player.overlaps(boundsTop) || player.overlaps(boundsBot) ||
                player1.overlaps(boundsTop) || player1.overlaps(boundsBot) ||
                player.overlaps(boundsTop2) || player.overlaps(boundsBot2) ||
                player1.overlaps(boundsTop2) || player1.overlaps(boundsBot2) ||
                player.overlaps(boundsTop3) || player.overlaps(boundsBot3) ||
                player1.overlaps(boundsTop3) || player1.overlaps(boundsBot3);
    }

    public void dispose() {
        topTube.dispose();
        bottomTube.dispose();

    }
}
