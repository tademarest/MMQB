package mondaymorningqb;

import java.util.Random;

import processing.core.PApplet;
import processing.core.PImage;

public class Cheeto extends Projectile {
	PApplet theApp_;
	
	/** Lets you know if the ball should be displayed */
	private boolean inPlay_;
	
	/** Explosion sprite image */
	private PImage explosion_;
	
	/** Points to the current image */
	private PImage currentSprite_;
	
	/** Time at which this object was constructed */
	private float startTime_, explosionStartTime_ = -EXPLOSION_DURATION, timeElapsed_;
	
	/** Scale factor changes size of football based on its altitude
	 * viewingAngle is how much of the viewing angle is occupied by the football*/
	private float scale_;
	
	/** Need a random number */
	Random rand;
	
	Cheeto(PApplet app, float x, float y, float z, float t,
			TrajectoryFunction XFcn, TrajectoryFunction YFcn,
			TrajectoryFunction ZFcn, TrajectoryFunction TFcn) {
		super(app, x, y, z, t, XFcn, YFcn, ZFcn, TFcn);
		inPlay_ = true;
		theApp_ = app;
		
		rand = new Random(theApp_.millis());
		
		/** Initialize the sprite*/
		if (rand.nextFloat()< .5) {
			currentSprite_ = theApp_.loadImage("cheeto1.png");
		} else {
			currentSprite_ = theApp_.loadImage("cheeto2.png");
		}
		
		explosion_ = theApp_.loadImage("explosion.png");
	
		startTime_ = theApp_.millis();
		
	}

	/** Explodes the cheeto a little smaller than the football */
	public void explode(float timeNow) {
		if (currentSprite_ != explosion_) {
			currentSprite_ = explosion_;
			explosionStartTime_ = timeNow;
		}
		float expTimeElapsed_ = (timeNow - explosionStartTime_)/1000;
		scale_ = Math.max(-CHEETO_EXPLOSION_SCALE_FACTOR*expTimeElapsed_*(expTimeElapsed_-EXPLOSION_DURATION),0);
	}

	/** Draw the Cheeto Image in flight, or explosion if appropriate */
	public void draw() {
		theApp_.pushMatrix();
		
		//Translate
		theApp_.translate(getX(),getY());
		
		//Scale 
		theApp_.scale(scale_*CHEETO_PRESCALE,scale_*CHEETO_PRESCALE);
		
		//Rotate
		theApp_.rotate(getTheta());
		
		//Draw 
		theApp_.image(currentSprite_, -currentSprite_.width/2, -currentSprite_.height/2, 
				currentSprite_.width, currentSprite_.height);;
		  
		theApp_.popMatrix();
		
	}

	/** Change the origin and select which sprite to display based
	 * on the trajectory functions. Also check if cheeto is still in play */
	public boolean update(float timeNow) {
		/** Sets the "real" time elapsed to that time since the football was constructed
		 * and reduces it into seconds from milliseconds */
		timeElapsed_ = (timeNow-startTime_)/1000;
		
		setZ(getZFcn_().TrajFcn(timeElapsed_));
		/** Only move the other coordinates if the ball is still flying */
		if (getZ() > EXPLOSION_ALTITUDE) {
			setTheta(getTFcn_().TrajFcn(timeElapsed_));
			setX(getXFcn_().TrajFcn(timeElapsed_));
			setY(getYFcn_().TrajFcn(timeElapsed_));
			setScale();
		} else {
			explode(timeNow);
		}
		
		return isInPlay();
	}
	/** Sets the scale variable based on the distance from the viewer and the width of the sprite */
	private void setScale() {
		//viewingAngle_ = (float) Math.asin(currentSprite_.height/(VIEWING_ALTITUDE-getZ()));
		float projectedWidth =  (float) (VIEWING_ALTITUDE*((currentSprite_.width)/Math.max(1,VIEWING_ALTITUDE-getZ())));// (float) (VIEWING_ALTITUDE*Math.sin(viewingAngle_));
		scale_ = projectedWidth/currentSprite_.width;
	}
	
	public boolean isInPlay() {
		/** Make sure the ball is still in play */
		if (getX() < LEFT_FIELD_LIMIT || getX() > RIGHT_FIELD_LIMIT  || 
				getZ() < MINIMUM_FOOTBALL_ALTITUDE || getY() <= MINIMUM_FOOTBALL_Y) {
			inPlay_ = false;
			return inPlay_;
		} else if (currentSprite_ == explosion_ && (theApp_.millis()-explosionStartTime_)/1000 > EXPLOSION_DURATION) {
			inPlay_ = false;
			return inPlay_;
		}
		return inPlay_;
	}
	
	/**Not used */
	public boolean isInside(float x, float y, float z) {
		return false;
	}

}
