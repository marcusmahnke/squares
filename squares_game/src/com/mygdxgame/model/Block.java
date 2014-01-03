package com.mygdxgame.model;

import com.badlogic.gdx.math.Vector2;

public class Block extends Entity {
	
	public static final float WIDTH = 1.25f;
	public static final float HEIGHT = 1.25f;
	
	public enum Color {
		RED, GREEN, YELLOW, BLUE
	}

	Color color;

	public Block(Vector2 position, Color color) {
		super(position, WIDTH, HEIGHT);
		this.color = color;
		this.velocity = Vector2.Zero;
	}

	public Color getColor() {
		return this.color;
	}

}