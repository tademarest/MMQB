package mondaymorningqb;

import java.util.Random;

import processing.core.PApplet;
import processing.core.PImage;

public class BelchBubble extends Projectile {
	PApplet theApp_;
	
	/** Lets you know if the ball should be displayed */
	private boolean inPlay_;
	
	/** Explosion sprite image */
	private PImage explosion_;
	
	/** Points to the current image */
	private PImage currentSprite_;
	
	/** Time at which this object was constructed */
	private float explosionStartTime_ = -EXPLOSION_DURATION, timeElapsed_;
	private final float startTime_;
	
	/** Scale factor changes size of football based on its altitude
	 * viewingAngle is how much of the viewing angle is occupied by the football*/
	private float scale_;
	
	/** Current gross color */
	private float r_, g_, b_;
	
	/** Need a random number */
	Random rand;

	/** Random between half and default size */
	private float bubbleSize_;

	BelchBubble(PApplet app, float x, float y, float z, float t,
			TrajectoryFunction XFcn, TrajectoryFunction YFcn,
			TrajectoryFunction ZFcn, TrajectoryFunction TFcn) {
		super(app, x, y, z, t, XFcn, YFcn, ZFcn, TFcn);
		inPlay_ = true;
		theApp_ = app;
		
		rand = new Random(theApp_.millis());
		setColor();
		
		/** Randomly set the full size of the bubble to between 50% and 100% of the 
		 * full bubble size*/
		bubbleSize_ = .5f*DEFAULT_BUBBLE_SIZE*(1+rand.nextFloat());
		
		explosion_ = theApp_.loadImage("explosion.png");
	
		startTime_ = theApp_.millis();
		
	}

	/** Explodes the bubble a little smaller than the football */
	public void explode(float timeNow) {
		if (currentSprite_ != explosion_) {
			currentSprite_ = explosion_;
			explosionStartTime_ = timeNow;
		}
		float expTimeElapsed_ = (timeNow - explosionStartTime_)/1000;
		scale_ = Math.max(-CHEETO_EXPLOSION_SCALE_FACTOR*expTimeElapsed_*(expTimeElapsed_-EXPLOSION_DURATION),0);
	}

	/** Draw the Bubble in flight, or explosion if appropriate */
	public void draw() {
		theApp_.pushMatrix();
		
		//Translate
		theApp_.translate(getX(),getY());
		
		//Draw 
		if (currentSprite_ == explosion_) {
			theApp_.scale(scale_,scale_);
			theApp_.image(currentSprite_, -currentSprite_.width/2, -currentSprite_.height/2, 
					currentSprite_.width, currentSprite_.height);
		} else { //Draw the bubble
			//Scale 
			theApp_.scale(scale_,scale_);
			/** Set the color */
			theApp_.noStroke();
			//First a black one for the outline
			theApp_.fill(0);
			theApp_.ellipse(0, 0, bubbleSize_, bubbleSize_);
			
			//Then the actual bubble
			theApp_.fill(r_, g_, b_);//various colors of bile
			theApp_.ellipse(0, 0, .95f*bubbleSize_, .95f*bubbleSize_);
		}
				
				
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
			scale_ = BUBBLE_EXPLOSION_SCALE_FACTOR*expTimeElapsed_*(expTimeElapsed_-EXPLOSION_DURATION);
		} else {
			scale_ = Math.min(timeElapsed_*timeElapsed_*timeElapsed_+.25f, 1);
			setTheta(getTFcn_().TrajFcn(timeElapsed_));
			setY(getYFcn_().TrajFcn(timeElapsed_));
			setX(getXFcn_().TrajFcn(timeElapsed_));
		}
		return isInPlay();
	}

	private void setColor() {
		int colorCode = rand.nextInt(BELCH_REDS.length);
		r_ = BELCH_REDS[colorCode];
		g_ = BELCH_GREENS[colorCode];
		b_ = BELCH_BLUES[colorCode];
	}

	/** Calculates whether the bubble is still alive, asigns it to inPlay_ and then
	 * Returns the field inPlay_ */
	public boolean isInPlay() {
		/** Make sure the ball is still in play */
		if (getX() < LEFT_FIELD_LIMIT || getX() > RIGHT_FIELD_LIMIT  || 
				getZ() < MINIMUM_FOOTBALL_ALTITUDE || getY() <= MINIMUM_FOOTBALL_Y) {
			inPlay_ = false;
			return inPlay_;
		} else if (currentSprite_ == explosion_ && (theApp_.millis()-explosionStartTime_)/1000 > EXPLOSION_DURATION) {
			inPlay_ = false;
			return inPlay_;
		} else if (theApp_.millis()-startTime_ >= BUBBLE_LIFETIME) {
			inPlay_ = false;
			return inPlay_;
		}
		return inPlay_;
	}
	
	/**Not used *///possibly used later, we'll test if the bubble works well as a projectile
	public boolean isInside(float x, float y, float z) {
		return false;
	}

}
