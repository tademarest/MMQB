package mondaymorningqb;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;

public class QBLifeBar extends Drawable {
	PApplet theApp_;
	int lifeRemaining_;
	static float imgLength_;
	static float imgWidth_;
	final static float SPACE_BETWEEN = 1;
	final static String QB_LIFE_1 = "Downs", QB_LIFE_2 = "Remaining";
	final static ArrayList<PImage> downMarkers_ = new ArrayList<PImage>();
	
	QBLifeBar(PApplet app, float x, float y, float z, float t) {
		super(app, x, y, z, t);
		theApp_ = app;
		lifeRemaining_ = Math.max(4,(int) t);
		downMarkers_.add(theApp_.loadImage("1_down.png"));
		downMarkers_.add(theApp_.loadImage("2_down.png"));
		downMarkers_.add(theApp_.loadImage("3_down.png"));
		downMarkers_.add(theApp_.loadImage("4_down.png"));
		imgLength_ = downMarkers_.get(0).height;
		imgWidth_ = downMarkers_.get(0).width;
	}

	@Override
	public void draw() {
		theApp_.pushMatrix();
		theApp_.translate(getX(),getY());
		
		/** Draw the down markers */
		theApp_.scale(DOWN_MARKER_SCALE_FACTOR,-DOWN_MARKER_SCALE_FACTOR);
		for (int i = 0; i < lifeRemaining_; i++) {
			theApp_.image(downMarkers_.get(i), -imgWidth_/3,-(imgLength_+SPACE_BETWEEN)*(i+1),2*imgWidth_/3,imgLength_);
		}
		
		
		/** Write the label */
		theApp_.noStroke();
		theApp_.fill(0,0,225);
		theApp_.scale(1/DOWN_MARKER_SCALE_FACTOR,1/DOWN_MARKER_SCALE_FACTOR);
		theApp_.textSize(5);
		theApp_.text(QB_LIFE_1,-10,5);
		theApp_.text(QB_LIFE_2,-14,10);
		
		
		
		
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
