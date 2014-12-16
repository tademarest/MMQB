package mondaymorningqb;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;

/** This is the main sprite in the game
 * If it hits an enemy, the enemy falls back 
 * in his couch. It's a projectile, so it gets 
 * trajectory functions.
 * @author ty
 */
public class Football extends Projectile {
	/** Lets you know if the ball should be displayed */
	private boolean inPlay_;
	
	/** Contains pointers to the PImages of the football sprites */
	private ArrayList<PImage> sprites_;
	
	/** Explosion sprite image */
	private PImage explosion_;
	
	/** Points to the current football image */
	private PImage currentSprite_;
	
	/** Number of sprites */
	private int numSprites_;
	
	/** Time at which this object was constructed */
	private float startTime_, explosionStartTime_ = -EXPLOSION_DURATION, timeElapsed_;
	
	/** Scale factor changes size of football based on its altitude
	 * viewingAngle is how much of the viewing angle is occupied by the football*/
	private float scale_;//, viewingAngle_;
	
	/** Image rotation angle */
	private float LRangle_;

	private PApplet theApp_; 

	/** Standard constructor for projectiles plus verifies that the ball is now inplay_
	 * as well as loading the images for the sprites
	 */
	Football(PApplet app, float x, float y, float z, float t, TrajectoryFunction XFcn, 
			TrajectoryFunction YFcn, TrajectoryFunction ZFcn, TrajectoryFunction TFcn) {
		super(app, x, y, z, t, XFcn, YFcn, ZFcn, TFcn);
		inPlay_ = true;
		theApp_ = app;
		LRangle_ = (float) (t - Math.PI/2);
		
		//Build the sprites arraylist
		sprites_ = new ArrayList<PImage>();
		sprites_.add(theApp_.loadImage("gamebal1.png"));
		sprites_.add(theApp_.loadImage("gamebal2.png"));
		sprites_.add(theApp_.loadImage("gamebal3.png"));
		//Add the third one twice because it has no laces
		sprites_.add(theApp_.loadImage("gamebal3.png"));
		numSprites_ = sprites_.size();
		//Set the current sprite to the first one
		currentSprite_ = sprites_.get(0);
		
		explosion_ = theApp_.loadImage("explosion.png");
	
		startTime_ = theApp_.millis();
	}

	/** Just draw the current sprite at the origin with the 
	 * right rotation angle.
	 */
	public void draw() {
		
		theApp_.pushMatrix();
		
		//Translate
		theApp_.translate(getX(),getY());
		
		//Scale 
		theApp_.scale(scale_*FOOTBALL_PRESCALE,scale_*FOOTBALL_PRESCALE);
		
		//Rotate
		theApp_.rotate(LRangle_);
		
		//Draw 
		theApp_.image(currentSprite_, -currentSprite_.width/2, -currentSprite_.height/2, 
				currentSprite_.width, currentSprite_.height);;
		  
		theApp_.popMatrix();
		
	}

	/** Change the origin and select which sprite to display based
	 * on the trajectory functions. Also check if ball is still in play
	 */
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
			changeSprite(getTheta());
		} else {
			explode(timeNow);
		}
		
		return isInPlay();
	}

	/** Sets the scale variable based on the distance from the viewer and the width of the football */
	private void setScale() {
		//viewingAngle_ = (float) Math.asin(currentSprite_.height/(VIEWING_ALTITUDE-getZ()));
		float projectedWidth =  (float) (VIEWING_ALTITUDE*((currentSprite_.width)/Math.max(1,VIEWING_ALTITUDE-getZ())));// (float) (VIEWING_ALTITUDE*Math.sin(viewingAngle_));
		scale_ = projectedWidth/currentSprite_.width;
	}

	/** Changes the picture of the football based on the rotation angle. 
	 * @param theta the rotation angle of the football
	 */
	private void changeSprite(float theta) {
		currentSprite_ = sprites_.get((int) (numSprites_*((theta % (Math.PI*2))/(Math.PI*2))));		
	}
	
	/** Turn the football into a ball of hell if it's low enough to impact the ground */
	public void explode(float timeNow) {
		if (currentSprite_ != explosion_) {
			currentSprite_ = explosion_;
			explosionStartTime_ = timeNow;
		}
		float expTimeElapsed_ = (timeNow - explosionStartTime_)/1000;
		scale_ = Math.max(-EXPLOSION_SCALE_FACTOR*expTimeElapsed_*(expTimeElapsed_-EXPLOSION_DURATION),0);
	}

	/** Not Used */
	public boolean isInside(float x, float y, float z) {
		return false;
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

}
