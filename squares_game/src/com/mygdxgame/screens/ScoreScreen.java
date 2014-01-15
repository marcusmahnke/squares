package com.mygdxgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.mygdxgame.MyGdxGame;
import com.mygdxgame.model.World;
import com.mygdxgame.model.World.Mode;

public class ScoreScreen implements Screen {
	
	MyGdxGame game;
	SpriteBatch batch;
	BitmapFont menuFont, HUDText;
	TextureAtlas atlas;
	Stage stage;
	Skin skin;
	ShapeRenderer shapeRenderer;
	
	float fontScale;
	int width, height, buttonWidth, buttonHeight, boxWidth, boxHeight;
	float boxX, boxY;
	int score, level, lastScoreIndex;
	Mode mode;
	int[] highScores, levels;

	public ScoreScreen(MyGdxGame game) {
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
		shapeRenderer.begin(ShapeType.Filled);
		drawScoreBox();
		shapeRenderer.end();
		
		batch.begin();
		stage.draw();
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		if (stage == null) {
			stage = new Stage(width, height, true);
		}
		
		this.width = width;
		this.height = height;
		
		stage.clear();
		Gdx.input.setInputProcessor(stage);	
		setHUDDimensions(width, height);
		recordScores();
		addLabels();
		addButtons();		
	}

	@Override
	public void show() {
		lastScoreIndex = 5;
		
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		atlas = new TextureAtlas("textures/menu.pack");
		skin = new Skin();
		skin.addRegions(atlas);
		menuFont = new BitmapFont(Gdx.files.internal("data/menu_font.fnt"),
				false);
		menuFont.setScale(fontScale);
		HUDText = new BitmapFont(Gdx.files.internal("data/menu_font2.fnt"), false);
		HUDText.setScale(fontScale);
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
		HUDText.dispose();

	}
	
	//initiates data from the last game played
	public void setWorldData(int score, World.Mode mode, int level) {
		this.score = score;
		this.level = level;
		this.mode = mode;
	}
	
	//parse the scores file and write back the new score if necessary
	void recordScores() {
		highScores = new int[5];
		levels = new int[5];
		String scoreStrings[];
		String filestring, initString;
		if (mode == Mode.MINUTE) {
			filestring = "scores_minute.txt";
			initString = "0,0,0,0,0";
		} else {
			filestring = "scores_endur.txt";
			initString = "0,0,0,0,0,0,0,0,0,0";
		}

		FileHandle file = Gdx.files.local(filestring);
		if (!file.exists()) {
			file.writeString(initString, false);
		}

		String scores = file.readString();
		scoreStrings = scores.split(",");
		for (int i = 0; i < highScores.length; i++) {
			highScores[i] = Integer.parseInt(scoreStrings[i]);
		}
		if (mode == Mode.ENDUR) {
			for (int i = 0; i < levels.length; i++) {
				levels[i] = Integer.parseInt(scoreStrings[i + 5]);
			}
		}
		for (int i = 0; i < highScores.length; i++) {
			if (highScores[i] < score) {
				lastScoreIndex = i;
				int temp = highScores[i];
				int templvl = levels[i];
				int temp2;
				levels[i] = level;
				highScores[i] = score;
				for (int j = i + 1; j < highScores.length; j++) {
					temp2 = highScores[j];
					highScores[j] = temp;
					temp = temp2;

					temp2 = levels[j];
					levels[j] = templvl;
					templvl = temp2;
				}

				break;
			}
		}

		file.writeString(genScoreString(), false);

	}
	
	//returns a string of scores followed by their associated levels, separated by commas
	String genScoreString(){
		String s = "";
		for (int i = 0; i < highScores.length; i++) {
			if (i < highScores.length - 1)
				s += highScores[i] + ",";
			else
				s += highScores[i];
		}

		if (mode == Mode.ENDUR) {
			for (int i = 0; i < levels.length; i++) {
				if (i == 0)
					s += "," + levels[i] + ",";
				else if (i < levels.length - 1)
					s += levels[i] + ",";
				else
					s += levels[i];
			}
		}

		return s;
	}

	TextButton initButton(int x, int y, float xPos, float yPos, String text,
			TextButtonStyle style, final Screen screen) {
		TextButton b = new TextButton(text, style);
		b.setWidth(x);
		b.setHeight(y);
		b.setX(xPos);
		b.setY(yPos);
		b.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				return true;
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				// Gdx.app.log("Example", "touch done at (" + x + ", " + y +
				// ")");
				game.setScreen(screen);
			}
		});

		return b;
	}

	TextButtonStyle initButtonStyle(String up, String down) {
		TextButtonStyle style = new TextButtonStyle();
		style.up = skin.getDrawable(up);
		style.down = skin.getDrawable(down);
		style.font = menuFont;

		return style;
	}
	
	void setHUDDimensions(int width, int height){
		boxWidth = (int) (width * .65f);
		boxHeight = (int) (height * .3f);
		boxX = width / 2 - boxWidth / 2;
		boxY = height * .6f - boxHeight / 2;
		
		buttonWidth = (int) (width * .5f);	
		buttonHeight = height / 20;

	}
	
	void drawScoreBox(){
		shapeRenderer.setColor(.59f, .56f, .48f, 1f);
		shapeRenderer.rect((480 / 2 - boxWidth / 2) + 4, (800 * .6f - boxHeight / 2) - 4, boxWidth, boxHeight);
		shapeRenderer.setColor(.96f, .94f, .88f, 1f);
		shapeRenderer.rect(480 / 2 - boxWidth / 2, 800 * .6f - boxHeight / 2, boxWidth, boxHeight);
	}
	
	void addButtons(){
		TextButtonStyle style = initButtonStyle("blueblock", "darkblue");
		TextButton button = initButton(buttonWidth, buttonHeight, width / 2 - buttonWidth
				/ 2, height / 4 - buttonHeight / 4, "Play Again", style,
				game.gameScreen);
		stage.addActor(button);

		style = initButtonStyle("redblock", "darkred");
		button = initButton(buttonWidth, buttonHeight, width / 2 - buttonWidth
				/ 2, height / 4 - buttonHeight / 4 - buttonHeight,
				"Back to Menu", style, game.menuScreen);
		stage.addActor(button);
	}
	
	void addLabels(){
		Label label = new Label("Last Round: " + score,
				new Label.LabelStyle(HUDText, Color.RED));
		label.setPosition(width / 2 - label.getWidth() / 2, boxY + boxHeight
				+ 50);
		stage.addActor(label);

		label = new Label("Your Top Scores",
				new Label.LabelStyle(HUDText, Color.BLACK));
		label.setPosition(width / 2 - label.getWidth() / 2, boxY + boxHeight
				+ 10);
		stage.addActor(label);
		
		for (int i = 0; i < highScores.length; i++) {
			if (highScores[i] != 0) {
				Gdx.app.log("test", "score index:" + lastScoreIndex);
				Color color;
				if(i == lastScoreIndex)
					color = Color.RED;
				else
					color = Color.BLACK;
				
				label = new Label((i + 1) + ". ", new Label.LabelStyle(
						HUDText, Color.BLACK));
				label.setPosition(boxX + 15,
						(boxY + (.75f * boxHeight) - i * 40));
				stage.addActor(label);
				label = new Label("" + highScores[i], new Label.LabelStyle(HUDText,
						color));
				label.setPosition(boxX + boxWidth / 2 - label.getWidth()
						+ 30, (boxY + (.75f * boxHeight) - i * 40));
				stage.addActor(label);
				if (mode == Mode.ENDUR) {
					label = new Label("" + levels[i], new Label.LabelStyle(HUDText,
							color));
					label.setPosition(boxX + boxWidth - label.getWidth()
							- 15, (boxY + (.75f * boxHeight) - i * 40));
					stage.addActor(label);
				}
			}
		}
	}

}
