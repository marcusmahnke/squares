package com.mygdxgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdxgame.MyGdxGame;

public class StatsScreen implements Screen, InputProcessor{
	private static final float LOGO_POS_RATIO = .375f;
	private static final Color GREEN = new Color(.13f, .69f, .29f, 1f);
	private static final Color BLUE = new Color(0f, .63f, .90f, 1f);
	private static final Color RED = new Color(.92f, .1f, .14f, 1f);
	
	MyGdxGame game;
	Stage stage;
	Skin skin;
	TextureAtlas atlas;
	SpriteBatch batch;
	BitmapFont menuFont, HUDText;
	Image logoimage;
	int width, height;
	int cleared, points, games;
	
	public StatsScreen(MyGdxGame game){
		this.game = game;
	}
	
	@Override
	public void dispose() {
		batch.dispose();
		skin.dispose();
		atlas.dispose();
		stage.dispose();
		menuFont.dispose();
		HUDText.dispose();
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(.93f, .91f, .85f, 1f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		stage.act(delta);
		batch.begin();
		stage.draw();
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		if (stage == null) {
			stage = new Stage(width, height, true);
		}
		stage.clear();
		this.width = width;
		this.height = height;	
		Gdx.input.setInputProcessor(this);
		
		getStats();
		
		addLabels();
		
		logoimage = new Image(skin.getRegion("startlogo"));
		logoimage.setPosition(width / 2 - logoimage.getWidth() / 2, height - (height*LOGO_POS_RATIO));
		stage.addActor(logoimage);
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		atlas = new TextureAtlas("textures/menu.pack");
		skin = new Skin();
		skin.addRegions(atlas);
		HUDText = new BitmapFont(Gdx.files.internal("data/menu_font2.fnt"), false);
		
	}
	
	void addLabels(){
		float tempY;
		
		Label label = new Label("Games Played",
				new Label.LabelStyle(HUDText, Color.BLACK));
		label.setPosition(width / 2 - label.getWidth() / 2, .5f * height - label.getHeight());
		stage.addActor(label);
		tempY = label.getY();
		
		label = new Label(String.valueOf(games),
				new Label.LabelStyle(HUDText, GREEN));
		label.setPosition(width / 2 - label.getWidth() / 2, tempY - label.getHeight() * 1.5f);
		stage.addActor(label);	
		tempY = label.getY();
		
		label = new Label("Squares Cleared",
				new Label.LabelStyle(HUDText, Color.BLACK));
		label.setPosition(width / 2 - label.getWidth() / 2, tempY - label.getHeight() * 3);
		stage.addActor(label);
		tempY = label.getY();
		
		label = new Label(String.valueOf(cleared),
				new Label.LabelStyle(HUDText, BLUE));
		label.setPosition(width / 2 - label.getWidth() / 2, tempY - label.getHeight() * 1.5f);
		stage.addActor(label);	
		tempY = label.getY();
		
		label = new Label("Points Accumulated",
				new Label.LabelStyle(HUDText, Color.BLACK));
		label.setPosition(width / 2 - label.getWidth() / 2, tempY - label.getHeight() * 3);
		stage.addActor(label);
		tempY = label.getY();
		
		label = new Label(String.valueOf(points),
				new Label.LabelStyle(HUDText, RED));
		label.setPosition(width / 2 - label.getWidth() / 2, tempY - label.getHeight() * 1.5f);
		stage.addActor(label);
		

	}
	
	void getStats(){
		String statStrings[];
		String filestring = "stats.txt";
		String initString = "0,0,0";
		FileHandle file = Gdx.files.local(filestring);
		if (!file.exists()) {
			file.writeString(initString, false);
		}
		String stats = file.readString();
		statStrings = stats.split(",");
		
		games = Integer.parseInt(statStrings[0]);
		cleared = Integer.parseInt(statStrings[1]);
		points = Integer.parseInt(statStrings[2]);
	}

	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.BACK){
			game.setScreen(game.menuScreen);
	    }
		return false;
	}

	@Override
	public boolean keyTyped(char arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}

}
