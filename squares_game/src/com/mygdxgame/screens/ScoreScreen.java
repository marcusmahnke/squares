package com.mygdxgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.mygdxgame.MyGdxGame;
import com.mygdxgame.model.World;
import com.mygdxgame.model.World.Mode;

public class ScoreScreen implements Screen {
	private static final int buttonWidth = 140;
	private static final int buttonHeight = 40;
	private static final int boxWidth = 300;
	private static final int boxHeight = 240;
	
	MyGdxGame game;
	SpriteBatch batch;
	BitmapFont menuFont, HUDText;
	TextureAtlas atlas;
	Stage stage;
	Skin skin;
	
	int score, level, lastScoreIndex;
	Mode mode;
	int[] highScores, levels;

	public ScoreScreen(MyGdxGame game) {
		this.game = game;
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
		Gdx.input.setInputProcessor(stage);

		recordScores();

		Image box = new Image(skin.getRegion("boxgraphic"));
		box.setHeight(boxHeight);
		box.setWidth(boxWidth);
		float boxX = width / 2 - boxWidth / 2;
		float boxY = height * .6f - boxHeight / 2;
		box.setPosition(boxX, boxY);
		stage.addActor(box);

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
				label.setPosition(box.getX() + 15,
						(boxY + (.75f * boxHeight) - i * 40));
				stage.addActor(label);
				label = new Label("" + highScores[i], new Label.LabelStyle(HUDText,
						color));
				label.setPosition(box.getX() + box.getWidth() / 2 - label.getWidth()
						+ 30, (boxY + (.75f * boxHeight) - i * 40));
				stage.addActor(label);
				if (mode == Mode.ENDUR) {
					label = new Label("" + levels[i], new Label.LabelStyle(HUDText,
							color));
					label.setPosition(box.getX() + box.getWidth() - label.getWidth()
							- 15, (boxY + (.75f * boxHeight) - i * 40));
					stage.addActor(label);
				}
			}
		}
	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		atlas = new TextureAtlas("textures/menu.pack");
		skin = new Skin();
		skin.addRegions(atlas);
		menuFont = new BitmapFont(Gdx.files.internal("data/menu_font.fnt"),
				false);
		menuFont.setScale(1f);
		HUDText = new BitmapFont(Gdx.files.internal("data/menu_font2.fnt"), false);
		HUDText.setScale(1f);

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

}
