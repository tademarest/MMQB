package mondaymorningqb;

import processing.core.PApplet;

public class BossLifeBar extends Drawable {
	PApplet theApp_;
	int lifeRemaining_;
	final static float BAR_LENGTH = 10;
	final static float BAR_WIDTH = 8;
	final static float SPACE_BETWEEN = 1;
	final static String BOSS_LIFE_1 = "Terry's", BOSS_LIFE_2 = "Hubris";
	
	BossLifeBar(PApplet app, float x, float y, float z, float t) {
		super(app, x, y, z, t);
		theApp_ = app;
		lifeRemaining_ = (int) t;
	}

	@Override
	public void draw() {
		theApp_.pushMatrix();
		theApp_.translate(getX(),getY());
		
		theApp_.noStroke();
		theApp_.fill(255,25,0);
		
		/** Write the label */
		theApp_.scale(1,-1);
		theApp_.textSize(5);
		theApp_.text(BOSS_LIFE_1,-10,5);
		theApp_.text(BOSS_LIFE_2,-10,10);
		theApp_.scale(1,-1);
		
		for (int i = 0; i < lifeRemaining_; i++) {
			theApp_.rect(-BAR_WIDTH/2,(BAR_LENGTH+SPACE_BETWEEN)*i,BAR_WIDTH/2,BAR_LENGTH);
		}
		
		
		theApp_.popMatrix();
		
	}

	public void setLifePoints(int points) {
		lifeRemaining_ = points;
	}
	
	@Override
	public boolean update(float timeNow) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isInside(float x, float y, float z) {
		// TODO Auto-generated method stub
		return false;
	}

}
