package mondaymorningqb;

import processing.core.PApplet;
import processing.core.PImage;

public class BeerCan extends Projectile {
	PApplet theApp_;
	
	/** Explosion sprite image and its scale factor*/
	private final PImage explosion_;
	private float scale_;
	
	/** Time at which this object was constructed */
	private float startTime_, explosionStartTime_ = -EXPLOSION_DURATION, timeElapsed_;
	
	/** The beer can sprite */
	private final PImage beerCan_;
	
	/** Points to the current football image */
	private PImage currentSprite_;

	private boolean inPlay_ = true;

	BeerCan(PApplet app, float x, float y, float z, float t,
			TrajectoryFunction XFcn, TrajectoryFunction YFcn,
			TrajectoryFunction ZFcn, TrajectoryFunction TFcn) {
		super(app, x, y, z, t, XFcn, YFcn, ZFcn, TFcn);
		theApp_ = app;
		explosion_ = theApp_.loadImage("explosion.png");
		beerCan_ = theApp_.loadImage("beer_can.png");
		currentSprite_ = beerCan_;
		scale_ = 1;
		inPlay_ = true;
		
		startTime_ = theApp_.millis();
	}

	public void draw() {

		theApp_.pushMatrix();
		
		//Translate
		theApp_.translate(getX(),getY());
		
		//Scale 
		theApp_.scale(scale_*BEERCAN_PRESCALE,scale_*BEERCAN_PRESCALE);
		
		//Rotate
		theApp_.rotate(getTheta());
		
		//Draw 
		theApp_.image(currentSprite_, -currentSprite_.width/2, -currentSprite_.height/2, 
				currentSprite_.width, currentSprite_.height);;
		  
		theApp_.popMatrix();
		
		
	}

	/** Change the origin and select which sprite to display based
	 * on the trajectory functions. Also check if can is still on the field */
	public boolean update(float timeElapsed) {
		/** Sets the "real" time elapsed to that time since the football was constructed
		 * and reduces it into seconds from milliseconds */
		timeElapsed_ = (timeElapsed-startTime_)/1000;
		
		/** Explode if necessary */
		float expTimeElapsed_ = (timeElapsed-explosionStartTime_)/1000;
		if (currentSprite_ == explosion_ && expTimeElapsed_ < EXPLOSION_DURATION) {
			scale_ = EXPLOSION_SCALE_FACTOR*expTimeElapsed_*(expTimeElapsed_-EXPLOSION_DURATION);
		} else {
			setTheta(getTFcn_().TrajFcn(timeElapsed_));
			setY(getYFcn_().TrajFcn(timeElapsed_));
			setX(getXFcn_().TrajFcn(timeElapsed_));
		}
		
		return isInPlay();
	}
	
	/** Turn the beer can into a ball of hell */
	public void explode(float timeNow) {
		if (currentSprite_ != explosion_) {
			currentSprite_ = explosion_;
			explosionStartTime_ = timeNow;
		}
	}
	
	public boolean isInPlay() {
		/** Make sure the can is still in play */
		if (getX() < LEFT_FIELD_LIMIT || getX() > RIGHT_FIELD_LIMIT  || 
				 getY() <= MINIMUM_FOOTBALL_Y) {
			inPlay_ = false;
			return inPlay_;
		} 
		return inPlay_;
	}

	@Override
	public boolean isInside(float x, float y, float z) {
		// TODO Auto-generated method stub
		return false;
	}

}
