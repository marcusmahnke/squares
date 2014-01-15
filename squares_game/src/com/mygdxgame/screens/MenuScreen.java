package com.mygdxgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.mygdxgame.MyGdxGame;
import com.mygdxgame.model.World.Mode;

public class MenuScreen implements Screen {
	private static final float LOGO_POS_RATIO = .375f;
	
	MyGdxGame game;
	SpriteBatch batch;
	BitmapFont menuFont;
	Texture logo;
	Image logoimage;
	TextureAtlas atlas;
	TextButton button;
	Stage stage;
	Skin skin;
	int buttonWidth, buttonHeight;
	float fontScale;
	
	public MenuScreen(MyGdxGame game){
		this.game = game;
		if(Gdx.graphics.getHeight() >= 1080){
			fontScale = 2f;
		} else{
			fontScale = 1f;
		}
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
		if(stage == null){
			stage = new Stage(width, height, true);
		}
		stage.clear();
		Gdx.input.setInputProcessor(stage);
		Gdx.input.setCatchBackKey(true);
		
		buttonWidth = (int) (width * .5f);	
		buttonHeight = height / 20;

		
		logoimage = new Image(skin.getRegion("startlogo"));
		logoimage.setPosition(width / 2 - logoimage.getWidth() / 2, height - (height*LOGO_POS_RATIO));
		stage.addActor(logoimage);
		
		TextButtonStyle style = initButtonStyle("greenblock", "darkgreen");
		button = initButton(buttonWidth, buttonHeight, width / 2 - buttonWidth / 2, height / 4 - buttonHeight / 4 + buttonHeight, 
				"Play", style, game.gameScreen, Mode.ENDUR);
		stage.addActor(button);
		
		//style = initButtonStyle("blueblock", "darkblue");
		//button = initButton(buttonWidth, buttonHeight, width / 2 - buttonWidth / 2, height / 4 - buttonHeight / 4, 
		//		"1-Minute", style, game.gameScreen, Mode.MINUTE);
		//stage.addActor(button);
		
		style = initButtonStyle("blueblock", "darkblue");
		button = initButton(buttonWidth, buttonHeight, width / 2 - buttonWidth / 2, height / 4 - buttonHeight / 4, 
				"Statistics", style, game.statsScreen, Mode.ENDUR);
		stage.addActor(button);
	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		atlas = new TextureAtlas("textures/menu.pack");
		skin = new Skin();
		skin.addRegions(atlas);
		menuFont = new BitmapFont(Gdx.files.internal("data/menu_font.fnt"), false);
		menuFont.setScale(fontScale);
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
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		batch.dispose();
		skin.dispose();
		atlas.dispose();
		stage.dispose();
		menuFont.dispose();
	}
	
	TextButton initButton(int x, int y, float xPos, float yPos, String text, TextButtonStyle style, 
			final Screen screen, final Mode mode){
		TextButton b = new TextButton(text, style);
		b.setWidth(x);
		b.setHeight(y);
		b.setX(xPos);
		b.setY(yPos);
		b.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
			}
 
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                switch(mode){
                case MINUTE:
                	game.gameScreen.setMode(Mode.MINUTE);
                	break;
                case ENDUR:
                	game.gameScreen.setMode(Mode.ENDUR);
                	break;
                }
                game.setScreen(screen);
			}
		});
		
		return b;
	}
	
	TextButtonStyle initButtonStyle(String up, String down){
		TextButtonStyle style = new TextButtonStyle();
		style.up = skin.getDrawable(up);
		style.down = skin.getDrawable(down);
		style.font = menuFont;
		
		return style;
	}

}
