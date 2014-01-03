package com.mygdxgame.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdxgame.model.Block;
import com.mygdxgame.model.World;

public class WorldRenderer {
	public static final float CAMERA_WIDTH = 15.0F;
	public static final float CAMERA_HEIGHT = 25.0F;
	
	World world;
	OrthographicCamera cam;
	SpriteBatch batch;
	BitmapFont HUDText;
	TextureRegion redBlock, greenBlock, blueBlock, yellowBlock;
	float width, height, ppuX, ppuY;
	
	public WorldRenderer(World world) {
		this.world = world;
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		ppuX = (this.width / CAMERA_WIDTH);
		ppuY = (this.height / CAMERA_HEIGHT);
		cam = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
		cam.position.set(7.5F, 12.5F, 0.0F);
		batch = new SpriteBatch();
		cam.update();
		HUDText = new BitmapFont(Gdx.files.internal("data/menu.fnt"),
				false);
		HUDText.setScale(1.0F);
		loadTextures();
	}

	public void render() {
		batch.begin();
		drawBlocks();
		drawHUD();
		batch.end();
	}

	public void dispose() {
		batch.dispose();
	}

	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
		ppuX = (width / 15.0F);
		ppuY = (height / 25.0F);
	}

	void loadTextures() {
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("textures/textures.pack"));
		redBlock = atlas.findRegion("redblock");
		blueBlock = atlas.findRegion("blueblock");
		greenBlock = atlas.findRegion("greenblock");
		yellowBlock = atlas.findRegion("yellowblock");
	}
	
	void drawHUD(){
		HUDText.draw(batch, "Score: " + world.getScore(), 50.0F,
				height - 20.0F);

		if (world.getMode() == World.Mode.ENDUR) {
			HUDText.draw(batch,
					"Level: " + world.getCurrentLevel(),
					width - 150.0F, height - 20.0F);
			HUDText.draw(batch, "------Keep Blocks Below------",
					width / 7.0F, 21.0F * ppuY);
		} else if (world.getMode() == World.Mode.MINUTE) {
			HUDText.draw(batch,
					"Time: " + world.getTimeRemaining(),
					width - 150.0F, height - 20.0F);
		}
	}

	void drawBlocks() {
		Block[][] blocks = world.getBlocks();
		for (int i = 0; i < World.BLOCKS_WIDTH; i++){
			for (int j = 0; j < World.BLOCKS_HEIGHT; j++){
				if (blocks[i][j] != null) {
					Block currentBlock = blocks[i][j];
					Block.Color c = currentBlock.getColor();
					Vector2 v = currentBlock.getPosition();
					TextureRegion t;
					switch (c) {
					case GREEN:
						t = blueBlock;
						break;
					case BLUE:
						t = redBlock;
						break;
					case RED:
						t = greenBlock;
						break;
					case YELLOW:
						t = yellowBlock;
						break;
					default:
						t = blueBlock;
					}

					this.batch.draw(t, v.x * ppuX * Block.WIDTH, v.y * ppuY * Block.HEIGHT,
							currentBlock.getWidth() * ppuX, currentBlock.getHeight() * ppuY);
				}
			}
		}
	}
}