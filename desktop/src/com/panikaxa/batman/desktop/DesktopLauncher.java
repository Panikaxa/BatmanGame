package com.panikaxa.batman.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.panikaxa.batman.Batman;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Batman.WIDHT;
		config.height = Batman.HEIGHT;
		config.title = Batman.TITLE;
		new LwjglApplication(new Batman(), config);
	}
}
