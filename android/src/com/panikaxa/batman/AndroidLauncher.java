package com.panikaxa.batman;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.panikaxa.batman.Batman;
import com.panikaxa.batman.states.MenuState;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useCompass = false;
		config.useAccelerometer = false;
		initialize(new Batman(), config);
	}

}

