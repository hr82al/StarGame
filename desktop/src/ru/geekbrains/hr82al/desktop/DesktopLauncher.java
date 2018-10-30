package ru.geekbrains.hr82al.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import ru.geekbrains.hr82al.Star2DGame;
import ru.geekbrains.hr82al.StarGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		float aspect = 3f/4f;
		config.height = 640;
		config.width = (int)(config.width * aspect);
		config.resizable = false;
		new LwjglApplication(new Star2DGame(), config);
	}
}
