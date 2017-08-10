package mondaymorningqb;

import processing.core.PApplet;

public class YardLine extends Drawable {
	private PApplet theApp_;
	/** The rate at which the lines move down in feet per second */
	private static float theScrollRate_;
	/** Time at last update, used to find the difference to move the line */
	private float updateTime_;
	/** Which yard number it is */
	private final String yardNumber_;
	/** Message String */
	private String message_;

	YardLine(PApplet app, float x, float y, float z, float scrollRate, int yardNumber, String message) {
		super(app, x, y, z, scrollRate);
		theScrollRate_ = scrollRate;
		theApp_ = app;
		yardNumber_ = "" + yardNumber;
		message_ = message;
		updateTime_ = theApp_.millis();
	}

	@Override
	public void draw() {
		theApp_.pushMatrix();
		
		/** Draw the main line */
		theApp_.translate(getX(), getY());
		theApp_.noStroke();
		theApp_.fill(255);//Paint it white
		theApp_.rect(0,0,RIGHT_FIELD_LIMIT-LEFT_FIELD_LIMIT,YARD_LINE_THICKNESS);
		
		/** Draw the hash marks */
		theApp_.pushMatrix();
		
		theApp_.popMatrix();
		
		/** Write the yard number above the line on the left side */
		theApp_.scale(1,-1);
		theApp_.translate((float) (0.04*(RIGHT_FIELD_LIMIT-LEFT_FIELD_LIMIT)), -4f);
		//theApp_.rotate(PApplet.PI/2);
		theApp_.textSize(12);
		theApp_.text(yardNumber_,0,0);
		
		/** Write any additional messages above the line number on the right side */
		if (message_ != null) {
			theApp_.translate((float) (0.5*(RIGHT_FIELD_LIMIT-LEFT_FIELD_LIMIT)), 0);
			theApp_.textSize(7);
			theApp_.fill(255,255,0);
			theApp_.text(message_,0,0);
		}
		
		theApp_.popMatrix();
		
	}

	/** Move down the line */
	public boolean update(float timeNow) {
		// y = y - scrollRate*timeElapsed
		setY(getY()-theScrollRate_*(timeNow-updateTime_)/1000);
		updateTime_ = timeNow;
		return getY() >= -15;
	}

	@Override
	public boolean isInside(float x, float y, float z) {
		// TODO Auto-generated method stub
		return false;
	}

}
