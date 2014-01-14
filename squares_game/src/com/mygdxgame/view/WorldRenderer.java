package com.mygdxgame.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.mygdxgame.model.Block;
import com.mygdxgame.model.World;

public class WorldRenderer {
	public static final float CAMERA_WIDTH = 15.0F;
	public static final float CAMERA_HEIGHT = 25.0F;

	World world;
	OrthographicCamera cam;
	SpriteBatch batch;
	ShapeRenderer shapeRenderer;
	BitmapFont HUDText;
	TextureRegion redBlock, greenBlock, blueBlock, yellowBlock;
	TextureRegion darkRedBlock, darkGreenBlock, darkBlueBlock, darkYellowBlock;
	float width, height, ppuX, ppuY, ppBlockX, ppBlockY;

	public WorldRenderer(World world) {
		shapeRenderer = new ShapeRenderer();
		this.world = world;
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		float fontScale;
		if(height >= 1080){
			fontScale = 2f;
		} else{
			fontScale = 1f;
		}
		ppuX = (this.width / CAMERA_WIDTH);
		ppuY = (this.height / CAMERA_HEIGHT);
		ppBlockX = ppuX * Block.WIDTH;
		ppBlockY = ppuY * Block.HEIGHT;
		cam = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
		cam.position.set(7.5F, 12.5F, 0.0F);
		batch = new SpriteBatch();
		cam.update();
		HUDText = new BitmapFont(Gdx.files.internal("data/menu.fnt"), false);
		HUDText.setScale(fontScale);
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
		TextureAtlas atlas = new TextureAtlas(
				Gdx.files.internal("textures/textures.pack"));
		redBlock = atlas.findRegion("redblock");
		blueBlock = atlas.findRegion("blueblock");
		greenBlock = atlas.findRegion("greenblock");
		yellowBlock = atlas.findRegion("yellowblock");
		darkRedBlock = atlas.findRegion("darkred");
		darkBlueBlock = atlas.findRegion("darkblue");
		darkGreenBlock = atlas.findRegion("darkgreen");
		darkYellowBlock = atlas.findRegion("darkyellow");
	}

	void drawHUD() {
		HUDText.draw(batch, "Score: " + world.getScore(), 50.0F, height - 20.0F);

		TextBounds tb = HUDText.getBounds("Level: 000 ");
		if (world.getMode() == World.Mode.ENDUR) {
			HUDText.draw(batch, "Level: " + world.getCurrentLevel(),
					width - tb.width, height - 20.0F);
		} else if (world.getMode() == World.Mode.MINUTE) {
			HUDText.draw(batch, "Time: " + world.getTimeRemaining(),
					width - 150.0F, height - 20.0F);
		}
	}

	void drawBlocks() {
		batch.end();
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(.43f, .43f, .40f, 1f);
		shapeRenderer.rect(World.OFFSET_X * ppBlockX - 1, World.OFFSET_Y * ppBlockY - 1, 
				ppBlockX * World.BLOCKS_WIDTH + 2, ppBlockY * World.BLOCKS_HEIGHT + 2);
		shapeRenderer.setColor(.75f, .74f, .69f, 1f);
		shapeRenderer.rect(World.OFFSET_X * ppBlockX, World.OFFSET_Y * ppBlockY, 
				ppBlockX * World.BLOCKS_WIDTH, ppBlockY * World.BLOCKS_HEIGHT);
		shapeRenderer.end();
		batch.begin();
		Block[][] blocks = world.getBlocks();
		for (int i = 0; i < World.BLOCKS_WIDTH; i++) {
			for (int j = 0; j < World.BLOCKS_HEIGHT; j++) {
				if (blocks[i][j] != null) {
					Block currentBlock = blocks[i][j];
					Block.Color c = currentBlock.getColor();
					Vector2 v = currentBlock.getPosition();
					boolean pressed = currentBlock.isPressed();
					TextureRegion t;
					if (pressed) {
						switch (c) {
						case GREEN:
							t = darkGreenBlock;
							break;
						case BLUE:
							t = darkBlueBlock;
							break;
						case RED:
							t = darkRedBlock;
							break;
						case YELLOW:
							t = darkYellowBlock;
							break;
						default:
							t = darkBlueBlock;
						}
					} else {
						switch (c) {
						case GREEN:
							t = greenBlock;
							break;
						case BLUE:
							t = blueBlock;
							break;
						case RED:
							t = redBlock;
							break;
						case YELLOW:
							t = yellowBlock;
							break;
						default:
							t = blueBlock;
						}
					}

					this.batch.draw(t, v.x * ppuX * Block.WIDTH, v.y * ppuY
							* Block.HEIGHT, currentBlock.getWidth() * ppuX,
							currentBlock.getHeight() * ppuY);
				} else{
					
				}
			}
		}
	}
}