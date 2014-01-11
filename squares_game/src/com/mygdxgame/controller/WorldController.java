package com.mygdxgame.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.mygdxgame.model.Block;
import com.mygdxgame.model.World;
import java.util.ArrayList;
import java.util.List;

public class WorldController {
	private static final int SCORE_PENALTY = -100;
	private static final float BLOCK_SPEED = 0.1f;
	private static final long ONE_SECOND = 1000;
	private World world;
	Sound s1,s2,s3,s4,s5,s6,s7,s8;
	long startTime;

	public WorldController(World world) {
		this.world = world;
		startTime = System.currentTimeMillis();
		s1 = Gdx.audio.newSound(Gdx.files.internal("data/sound1.wav"));
		s2 = Gdx.audio.newSound(Gdx.files.internal("data/sound2.wav"));
		s3 = Gdx.audio.newSound(Gdx.files.internal("data/sound3.wav"));
		s4 = Gdx.audio.newSound(Gdx.files.internal("data/sound4.wav"));
		s5 = Gdx.audio.newSound(Gdx.files.internal("data/sound5.wav"));
		s6 = Gdx.audio.newSound(Gdx.files.internal("data/sound6.wav"));
		s7 = Gdx.audio.newSound(Gdx.files.internal("data/sound7.wav"));
		s8 = Gdx.audio.newSound(Gdx.files.internal("data/sound8.wav"));
	}

	public void touchDown(int x, int y) {
		Vector2 coords = world.getCoords(x, y);
		Block[][] blocks = world.getBlocks();

		if ((coords.x >= 0.0f) && (coords.x < World.BLOCKS_WIDTH)
				&& (coords.y >= 0.0f) && (coords.y < World.BLOCKS_HEIGHT)
				&& (blocks[((int) coords.x)][((int) coords.y)] != null)) {
			world.resetBlocksToRemove();
			world.setCheckedBlocks(new ArrayList<Vector2>());
			world.checkSurroundingBlocks((int) coords.x, (int) coords.y);
			world.setBlocksPressed();
		}
	}

	public void touchUp(int x, int y) {
		Block[][] blocks = world.getBlocks();
		Vector2 coords = world.getCoords(x, y);

		world.setBlocksUnPressed();
		if ((coords.x >= 0.0f) && (coords.x < World.BLOCKS_WIDTH)
				&& (coords.y >= 0.0f) && (coords.y < World.BLOCKS_HEIGHT)
				&& (blocks[((int) coords.x)][((int) coords.y)] != null)) {
			world.resetBlocksToRemove();
			world.setCheckedBlocks(new ArrayList<Vector2>());
			world.checkSurroundingBlocks((int) coords.x, (int) coords.y);
			List<Vector2> blocksRem = world.getBlocksToRemove();
			int numBlocks = blocksRem.size();
			handleBlockSound(numBlocks);	
			if (numBlocks > 1) {
				int multiplier = blocksRem.size() / 3;
				world.addToScore(blocksRem.size() * 10 * multiplier
						* (world.getCurrentLevel() + 1));
				
				world.removeBlocks();
				world.checkForEmptyCols();
			} else {
				world.setBlocksUnPressed();
				world.addToScore(SCORE_PENALTY);
			}
		}
	}

	public void update(float delta) {
		handleGameTime();
		handleBlockMovement();
	}

	void handleGameTime() {
		long msExpired = System.currentTimeMillis() - startTime;

		if (world.getMode() == World.Mode.ENDUR) {
			if (world.computeCurrentLevel()) {
				world.computeMilliSecondsPerRow();
			}
			long secondsPerRow = world.getMilliSecondsPerRow();
			if (msExpired > secondsPerRow) {
				Gdx.app.log("New Row", secondsPerRow + " milliseconds");
				world.makeNewRow();
				startTime = System.currentTimeMillis();
			}
		} else {
			if (msExpired > ONE_SECOND) {
				startTime = System.currentTimeMillis();
				world.decTimeRemaining();
			}
		}
	}
	
	void handleBlockSound(int numBlocks){
		if(numBlocks <= 2)
			s1.play();
		else if(numBlocks >= 8)
			s5.play();
		else
			s4.play();
	}

	void handleBlockMovement() {
		Block[][] blocks = world.getBlocks();
		for (int i = 0; i < World.BLOCKS_WIDTH; i++) {
			for (int j = 0; j < World.BLOCKS_HEIGHT; j++) {
				if (blocks[i][j] != null) {
					Vector2 currentPos = blocks[i][j].getPosition();
					Vector2 newPos = world.getBlockPosition(i, j, 0);
					if (currentPos != newPos) {

						if (currentPos.y > newPos.y)
							blocks[i][j].setVelocity(new Vector2(0f,
									-BLOCK_SPEED));
						else if (currentPos.y < newPos.y)
							blocks[i][j].setVelocity(new Vector2(0f,
									BLOCK_SPEED));

						if (currentPos.x < newPos.x)
							blocks[i][j].setVelocity(new Vector2(BLOCK_SPEED,
									0f));
						else if (currentPos.x > newPos.x)
							blocks[i][j].setVelocity(new Vector2(-BLOCK_SPEED,
									0f));

						Vector2 currentVelocity = blocks[i][j].getVelocity();
						if (currentVelocity != Vector2.Zero) {
							currentPos.add(currentVelocity);

							if (currentVelocity.y < 0
									&& currentPos.y < newPos.y)
								currentPos.y = newPos.y;
							else if (currentVelocity.y > 0
									&& currentPos.y > newPos.y)
								currentPos.y = newPos.y;

							if (currentVelocity.x < 0
									&& currentPos.x < newPos.x)
								currentPos.x = newPos.x;
							else if (currentVelocity.x > 0
									&& currentPos.x > newPos.x)
								currentPos.x = newPos.x;

						}

						if (currentPos == newPos)
							blocks[i][j].setVelocity(Vector2.Zero);
					}
				}
			}
		}
	}
}
