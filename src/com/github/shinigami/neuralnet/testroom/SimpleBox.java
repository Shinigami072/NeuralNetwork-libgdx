package com.github.shinigami.neuralnet.testroom;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class SimpleBox extends Controllable implements Collideable {

	Vector2 posinput;
	Vector2 velocity;
	float bounciness = 0.2f;
	public SimpleBox(TestRoom t,Vector2 pos, float width,float height){
			posinput=pos;
			position = new Vector2(MathUtils.random(t.width),MathUtils.random(t.height));
			velocity = new Vector2(0f,0f);
			boundingbox = new Rectangle(pos.x + width/2f,pos.y + height/2f,width,height);
			movespace = new Rectangle(0f,0f,t.width,t.height);
	}
	
	@Override
	public void simulate(TestRoom t, float delta) {
		
		velocity.set(posinput);
		position.mulAdd(velocity, 90f*delta);
		updatePosition();

		
		
	}
	
	private void updatePosition(){
		boundingbox.setCenter(position);
		if(!movespace.contains(boundingbox)){
			
			if(movespace.x>boundingbox.x){
				position.set(movespace.x+boundingbox.width/2f, position.y);
				velocity.set(-(bounciness)*velocity.x,velocity.y);

			}else if(movespace.x+movespace.width<boundingbox.x+boundingbox.width)
			{	position.set(movespace.x+movespace.width-boundingbox.width/2f,position.y);	
				velocity.set(-(bounciness)*velocity.x,velocity.y);
			}
			
			if(movespace.y>boundingbox.y){
				position.set(position.x, movespace.y+boundingbox.height/2f);
				velocity.set(velocity.x,-(bounciness)*velocity.y);
			}else if(movespace.y+movespace.height<boundingbox.y+boundingbox.height)				
			{	position.set(position.x,movespace.y+movespace.height-boundingbox.height/2f);
				velocity.set(velocity.x,-(bounciness)*velocity.y);
			}
			boundingbox.setCenter(position);
		}
	}

	@Override
	public void draw(TestRoom t, float x,float y,float delta) {
		Vector2 dir = new Vector2(position);
		dir.mulAdd(posinput,60f);
		
		Vector2 vel = new Vector2(position);
		vel.mulAdd(velocity,10f*delta);
	
		t.shr.begin(ShapeType.Filled);
		t.shr.setColor(1f, 0.1f, 1f, 1f);
		t.shr.rect(x+boundingbox.x,y+boundingbox.y,boundingbox.width,boundingbox.height);
		t.shr.setColor(0.1f, 0.1f, 1f, 1f);
		t.shr.line(position, dir);
		t.shr.setColor(0f, 1f, 0f, 1f);
		t.shr.line(position,vel);

		t.shr.end();
	}

	@Override
	public Rectangle getBoundingBox() {
		return boundingbox;
	}
	@Override
	public Vector2 getVelocity() {
		return velocity;
	}

	@Override
	public void onCollision(Collideable a) {
	}


}
