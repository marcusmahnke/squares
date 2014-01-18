package com.mygdxgame.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.mygdxgame.model.Block;
import com.mygdxgame.model.World;

public class WorldRenderer {
	public static final float CAMERA_WIDTH = 15.0F;
	public static final float CAMERA_HEIGHT = 25.0F;
	public static final Color GREEN = new Color(.13f, .69f, .29f, 1f);
	public static final Color DGREEN = new Color(.12f, .53f, .24f, 1f);
	public static final Color BLUE = new Color(0f, .63f, .90f, 1f);
	public static final Color DBLUE = new Color(0f, .41f, .59f, 1f);
	public static final Color RED = new Color(.92f, .1f, .14f, 1f);
	public static final Color DRED = new Color(.71f, .08f, .1f, 1f);
	public static final Color YELLOW = new Color(1f, .78f, .05f, 1f);
	public static final Color DYELLOW = new Color(.82f, .65f, .05f, 1f);			
	public static final Color GREY = new Color(.75f, .74f, .69f, 1f);
	public static final Color FAILING = new Color(.84f, .57f, .57f, 1f);
	
	World world;
	OrthographicCamera cam;
	SpriteBatch batch;
	ShapeRenderer shapeRenderer;
	BitmapFont HUDText;
	Color bgColor;
	float width, height, ppuX, ppuY, ppBlockX, ppBlockY;
	long startTime;
	
	public WorldRenderer(World world) {
		startTime = System.currentTimeMillis();
		bgColor = GREY;
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
		long time = System.currentTimeMillis() - startTime;
		if(world.isReachingFailure()){
			if(time < 500){
				bgColor = FAILING;
			}else if(time>=500 && time < 600){
				bgColor = GREY;
				//startTime = System.currentTimeMillis();
			}else if(time>=1000){
				//bgColor = Color.RED;
				startTime = System.currentTimeMillis();
			}
		} else{
			bgColor = GREY;
		}
		shapeRenderer.begin(ShapeType.Filled);
		drawBlocks();
		shapeRenderer.end();
		
		batch.begin();
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
		shapeRenderer.setColor(.43f, .43f, .40f, 1f);
		shapeRenderer.rect(World.OFFSET_X * ppBlockX - 1, World.OFFSET_Y * ppBlockY - 1, 
				ppBlockX * World.BLOCKS_WIDTH + 2, ppBlockY * World.BLOCKS_HEIGHT + 2);
		shapeRenderer.setColor(bgColor);
		shapeRenderer.rect(World.OFFSET_X * ppBlockX, World.OFFSET_Y * ppBlockY, 
				ppBlockX * World.BLOCKS_WIDTH, ppBlockY * World.BLOCKS_HEIGHT);

		Block[][] blocks = world.getBlocks();
		for (int i = 0; i < World.BLOCKS_WIDTH; i++) {
			for (int j = 0; j < World.BLOCKS_HEIGHT; j++) {
				if (blocks[i][j] != null) {
					Block currentBlock = blocks[i][j];
					Block.Color c = currentBlock.getColor();
					Vector2 v = currentBlock.getPosition();
					boolean pressed = currentBlock.isPressed();
					if (pressed) {
						switch (c) {
						case GREEN:
							shapeRenderer.setColor(DGREEN);
							break;
						case BLUE:
							shapeRenderer.setColor(DBLUE);
							break;
						case RED:
							shapeRenderer.setColor(DRED);
							break;
						case YELLOW:
							shapeRenderer.setColor(DYELLOW);
							break;
						default:
							shapeRenderer.setColor(DBLUE);
						}
					} else {
						switch (c) {
						case GREEN:
							shapeRenderer.setColor(GREEN);
							break;
						case BLUE:
							shapeRenderer.setColor(BLUE);
							break;
						case RED:
							shapeRenderer.setColor(RED);
							break;
						case YELLOW:
							shapeRenderer.setColor(YELLOW);
							break;
						default:
							shapeRenderer.setColor(BLUE);
						}
					}

					shapeRenderer.rect(v.x * ppuX * Block.WIDTH, v.y * ppuY
							* Block.HEIGHT, currentBlock.getWidth() * ppuX, currentBlock.getHeight() * ppuY);
				}
			}
		}
	}
}