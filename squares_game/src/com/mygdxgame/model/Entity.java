package com.mygdxgame.model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity {

	protected Vector2 position;
	protected float width;
	protected float height;
	protected Rectangle bounds;
	protected Vector2 velocity;
	protected Vector2 acceleration;
	
	public Entity(Vector2 position, float width, float height){
		this.position = position;
		this.width = width;
		this.height = height;
		bounds = new Rectangle(position.x, position.y, width, height);
		velocity = new Vector2();
		acceleration = new Vector2();
	}
	
	public Vector2 getPosition() {
		return position;
	}
	public void setPosition(Vector2 position) {
		this.position = position;
	}
	public float getWidth() {
		return width;
	}
	public void setWidth(float width) {
		this.width = width;
	}
	public float getHeight() {
		return height;
	}
	public void setHeight(float height) {
		this.height = height;
	}
	public Rectangle getBounds() {
		return bounds;
	}
	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}
	public Vector2 getVelocity(){
		return velocity;
	}
	public void setVelocity(Vector2 velocity){
		this.velocity = velocity;
	}
	public Vector2 getAcceleration(){
		return acceleration;
	}
	public void setAcceleration(Vector2 acceleration){
		this.acceleration = acceleration;
	}

}
