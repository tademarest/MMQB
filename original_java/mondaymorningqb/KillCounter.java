package mondaymorningqb;

import processing.core.PApplet;

public class KillCounter extends Drawable {
	PApplet theApp_;
	private int level_;
	private int kills_;
	final static float SPACE_BETWEEN = 1;
	
	KillCounter(PApplet app, float x, float y, float z, float t) {
		super(app, x, y, z, t);
		theApp_ = app;
		level_ = (int) z;
		kills_ = (int) t;
	}

	@Override
	public void draw() {
		theApp_.pushMatrix();
		theApp_.translate(getX(),getY());
		
		theApp_.noStroke();
		theApp_.fill(ORANGE_RGB[0],ORANGE_RGB[1],ORANGE_RGB[2]);
		
		/** Write the label */
		theApp_.scale(1,-1);
		theApp_.textSize(6);
		theApp_.text("Level:" + level_ + "/4",-10,5);
		theApp_.text("Kills: "+ kills_,-10,10);
		theApp_.scale(1,-1);
		
		theApp_.popMatrix();
		
	}

	public void setLevel(Level level) {
		switch(level) {
		case ZERO: level_ = 0;
			break;
		case ONE: level_ = 1;
			break;
		case TWO: level_ = 2;
			break;
		case THREE: level_ = 3;
			break;
		case BOSS: level_ = 4;
			break;
		}
	}
	public void addKill() {
		kills_++;
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
