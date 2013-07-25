package com.mygdxgame.model;

import com.badlogic.gdx.math.Vector2;

public class Block extends Entity {
	
	public enum Color {
		RED, GREEN, YELLOW, BLUE
	}

	Color color;

	public Block(Vector2 position, Color color) {
		super(position, 1.0F, 1.0F);
		this.color = color;
		this.velocity = Vector2.Zero;
	}

	public Color getColor() {
		return this.color;
	}

}