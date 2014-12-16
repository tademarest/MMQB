package mondaymorningqb;

import processing.core.PApplet;

public class Arrow extends Drawable {
	private PApplet theApp_;
	private float LRangle_, TOangle_, strength_, pointX_, pointY_, rightX_, rightY_, leftX_, leftY_;
	private final float MAX_LINE_LENGTH = 20;
	private float r_,g_,b_;
	
	/** Build the Arrow using the initial coordinates as the origin */
	Arrow(PApplet app, float x, float y, float z, float t, float LRangle, float TOangle, float strength) {
		super(app, x, y, z, t);
		theApp_ = app;
		LRangle_ = LRangle;
		TOangle_ = TOangle;
		strength_ = strength;
		updatePoints();
		updateColors();
	}
	
	/** Updates the color intensities of the arrow */
	private void updateColors() {
		r_ = 255 * (strength_-DEFAULT_THROWSPEED)/FOOTBALL_MAX_SPEED;
		g_ = 0;
		b_ = Math.max(255 - r_,0);
	}
	
	/** Updates the endpoints of the arrow */
	private void updatePoints() {
		float cosLR = (float) Math.cos(LRangle_), 
				sinLR = (float) Math.sin(LRangle_), 
				cosLR90 = (float) Math.cos(LRangle_-Math.PI/2), 
				sinLR90 = (float) Math.sin(LRangle_-Math.PI/2),
				cosTO = (float) Math.cos(TOangle_);
		
		pointX_ = MAX_LINE_LENGTH*cosLR*cosTO;
		pointY_ = MAX_LINE_LENGTH*sinLR*cosTO;
		rightX_ = (float) (cosTO*(0.75*MAX_LINE_LENGTH*cosLR) + 0.2*MAX_LINE_LENGTH*cosLR90);
		rightY_ = (float) (cosTO*(0.75*MAX_LINE_LENGTH*sinLR) + 0.2*MAX_LINE_LENGTH*sinLR90);
		leftX_ = (float) (cosTO*(0.75*MAX_LINE_LENGTH*cosLR) - 0.2*MAX_LINE_LENGTH*cosLR90);
		leftY_ = (float) (cosTO*(0.75*MAX_LINE_LENGTH*sinLR) - 0.2*MAX_LINE_LENGTH*sinLR90);
		
	}

	public void draw() {
		theApp_.pushMatrix();
		theApp_.translate(getX(),getY());
		
		
		theApp_.stroke(r_, g_, b_);
		theApp_.strokeWeight(ARROW_WEIGHT);
		theApp_.fill(r_, g_, b_);
		//theApp_.strokeWeight(strength_/WEIGHT_FACTOR);
		//Main Line
		theApp_.line(0, 0, pointX_, pointY_);
		//Arrow Part
		theApp_.triangle(pointX_, pointY_, leftX_, leftY_, rightX_, rightY_);
//		theApp_.line(pointX_, pointY_, leftX_, leftY_);
//		theApp_.line(pointX_, pointY_, rightX_, rightY_);
		
		theApp_.popMatrix();
		
	}
	
	/** Updates the variables. Called to move the arrow */
	public boolean move(float LRangle, float TOangle, float strength) {
		LRangle_ = LRangle;
		TOangle_ = TOangle;
		strength_ = strength;
		updatePoints();
		updateColors();
		
		return true;
	}

	@Override
	public boolean update(float timeSinceLast) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isInside(float x, float y, float z) {
		// TODO Auto-generated method stub
		return false;
	}

}
