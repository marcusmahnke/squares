package com.mygdxgame;

import com.badlogic.gdx.Game;
import com.mygdxgame.screens.GameScreen;
import com.mygdxgame.screens.MenuScreen;
import com.mygdxgame.screens.ScoreScreen;

public class MyGdxGame extends Game{
	public MenuScreen menuScreen;
	public GameScreen gameScreen;
	public ScoreScreen scoreScreen;
	
	@Override
	public void create() {
		menuScreen = new MenuScreen(this);
		gameScreen = new GameScreen(this);
		scoreScreen = new ScoreScreen(this);
		setScreen(menuScreen);
	}

}
