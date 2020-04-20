package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.MyGdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = MyGdxGame.Title + " v" + MyGdxGame.VERSION;
		config.width = MyGdxGame.V_WIDTH;
		config.height = MyGdxGame.V_HEIGHT;
		config.backgroundFPS = 60;
		config.foregroundFPS = 60;
		config.resizable = false;//데스크톱으로 실행시, 화면 조절 못하게 설정.


		new LwjglApplication(new MyGdxGame(), config);
	}
}
