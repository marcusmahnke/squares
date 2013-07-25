package com.mygdxgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.mygdxgame.MyGdxGame;
import com.mygdxgame.controller.WorldController;
import com.mygdxgame.model.World;
import com.mygdxgame.model.World.Mode;
import com.mygdxgame.view.WorldRenderer;

public class GameScreen implements Screen, InputProcessor{
	MyGdxGame game;
	Mode mode;
	World world;
	WorldRenderer renderer;
	WorldController controller;
	
	public GameScreen(MyGdxGame game){
		this.game = game;
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(.93f, .91f, .85f, 1f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		controller.update(delta);
		if(world.isGameDone()){
			game.scoreScreen.setWorldData(world.getScore(), world.getMode(), world.getCurrentLevel());
			game.setScreen(game.scoreScreen);
		}
		
		renderer.render();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		world = new World(mode);
		renderer = new WorldRenderer(world);
		controller = new WorldController(world);
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		Gdx.input.setInputProcessor(null);
		
	}
	
	public void setMode(Mode mode){
		this.mode = mode;
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		controller.touchUp(screenX, screenY);
		//Gdx.app.log("Example", "touch done at (" + screenX + ", " + screenY + ")");
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
