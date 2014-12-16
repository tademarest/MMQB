package mondaymorningqb;

import java.util.ArrayList;

import processing.core.PApplet;

public class Field extends Drawable {
	private PApplet theApp_;
	
	/** Rate at which to scroll down the field */
	private float scrollRate_;

	/** Array list of enemies on the field */
	private ArrayList<YardLine> yardLines_;
	
	/** When the field was set up and a spot for time elapsed */
	private final float startTime_;
	private float millis_;
	
	/** The yard line at the top of the screen */
	private int topYard_;
	
	/** The level determines the type and quantity of enemies */
	private Level lev_;
	/** The difficulty determines the quantity of the enemies */
	private Difficulty dif_;
	
	/** Current Message to Put on the Yard Markers */
	private String message_;
	
	/** Top yard line so far */
	float topSoFar = Float.MIN_VALUE;
	
	Field(PApplet app, float x, float y, float z, float t, Level lev, Difficulty dif) {
		super(app, x, y, z, t);
		theApp_ = app;
		lev_ = lev; dif_ = dif;
		/**The scroll rate depends also on the level and difficulty */
		updateScrollRate(lev_, dif_);
		message_ = START_MESSAGE;
		
		yardLines_ = new ArrayList<YardLine>();
		startTime_ = theApp_.millis();
		topYard_ = 0;
		initYardLines();
		
		
	}

	/** Changes the scroll rate based on level and difficulty */
	public void updateScrollRate(Level lev, Difficulty dif) {
		scrollRate_ = DEFAULT_SCROLL_RATE;
		
		switch(dif) {
		case EASY:
			scrollRate_ *= E_SR;
			break;
		case MEDIUM:
			scrollRate_ *= M_SR;
			break;
		case HARD:
			scrollRate_ *= H_SR;
			break;
		case INSANE:
			scrollRate_ *= I_SR;
			break;
		default: 
			break;
		}
		switch(lev) {
		case ZERO: scrollRate_ *= 0;
			break;
		case ONE: scrollRate_ *= 1;
			break;
		case TWO: scrollRate_ *= 1.2;
			break;
		case THREE: scrollRate_ *= 1.4;
			break;
		default: 
			break;

		}
		//theApp_.println("updated scroll rate to " + scrollRate_);
		
	}

	/** Populates the new field with some yard lines */
	private void initYardLines() {
		for (int i = 0; i < YARD_LINES_ON_FIELD; i++) {
			YardLine line = new YardLine(theApp_, 0, i*FEET_BETWEEN_MARKERS, startTime_, scrollRate_, topYard_,message_);
			yardLines_.add(line);
			message_ = null;
			topYard_ += 5;
		}
		
	}


	public void draw() {
		millis_ = theApp_.millis();
		
		theApp_.pushMatrix();
		
		//draw the field
		theApp_.translate(getX(),getY());
		theApp_.noStroke();
		theApp_.fill(TURF_R,TURF_G,TURF_B);
		
		theApp_.rect(0, 0, RIGHT_FIELD_LIMIT-LEFT_FIELD_LIMIT, WORLD_HEIGHT);
		
		//draw the lines
		for (YardLine line : yardLines_) {
			line.draw();
		}
		
		theApp_.popMatrix();
	}

	public boolean update(float timeElapsed) {
		topSoFar = Float.MIN_VALUE;
		for (int i = 0; i < yardLines_.size(); i++) {
			if (!yardLines_.get(i).update(millis_)) {
				yardLines_.remove(i);
				i = 0;
			} else {
				topSoFar = Math.max(yardLines_.get(i).getY(),topSoFar);
			}
		}
		
		while (yardLines_.size() < YARD_LINES_ON_FIELD) {
			yardLines_.add(new YardLine(theApp_, 0, topSoFar+FEET_BETWEEN_MARKERS, timeElapsed, scrollRate_, topYard_, message_));
			topYard_ += 5;
			message_ = null;
		}
		
		/** Add a Hundred Yard message */
		if (topYard_ % 100 == 0) {
			message_ = HUNDRED_YARD_MESSAGE;
		}
		
		return true;
	}
	

	@Override
	public boolean isInside(float x, float y, float z) {
		// TODO Auto-generated method stub
		return false;
	}

	public void setScrollRate(float scrollRate) {
		scrollRate_ = scrollRate;
	}
	public float getScrollRate() {
		return scrollRate_;
	}
	public void setMessage(String message) {
		message_ = message;
	}
	public int getTopYard() {
		return topYard_;
	}
	public float getTopOfScreen() {
		return topSoFar;
	}
}
