package com.panikaxa.batman.sprites;

import com.badlogic.gdx.graphics.Texture;

public class BackgroundMenu {
    private Texture background, playBtn;

    public BackgroundMenu() {
        background = new Texture("bg.png");
        playBtn = new Texture("playbtn.png");

    }

    public Texture getBackground() {
        return background;
    }
    public Texture getPlayBtn() {
        return playBtn;
    }

    public void dispose() {
        background.dispose();
        playBtn.dispose();
    }
}
