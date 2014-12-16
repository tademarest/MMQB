package mondaymorningqb;

import java.util.ArrayList;

import processing.core.PApplet;

/** All of the "Monday-morning quarterback" characters */
public abstract class Enemy extends Drawable {

	Enemy(PApplet app, float x, float y, float z, float t) {
		super(app, x, y, z, t);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean update(float timeElapsed) {
		// TODO Auto-generated method stub
		return false;
	}
	public abstract int hit(float timeNow);
	@Override
	public boolean isInside(float x, float y, float z) {
		// TODO Auto-generated method stub
		return false;
	}

	public abstract ArrayList<? extends Projectile> getProjectiles();

}
