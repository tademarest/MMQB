package mondaymorningqb;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;

public class Quarterback extends Drawable {
	private PApplet theApp_;
	
	/** Contains pointers to the PImages of the qb sprites */
	private final ArrayList<PImage> leftThrow_;
	private final ArrayList<PImage> centerThrow_;
	private final ArrayList<PImage> rightThrow_;
	private final PImage centerSprite_;
	private PImage currentSprite_;
	
	/** The throw angle */
	private float throwLRAngle_ = PApplet.PI/2;
	
	/** Determines via subtraction from now how much more time QB needs to throw */
	private float timeThrowStarted_ = -QB_THROW_DURATION;
	
	/** Says which of Left,Center,or Right to throw (ie code 2,1, or 0) */
	private int throwCode_ = 1;
	
	/** How many "downs" the QB has, aka life points */
	private int lifePoints_ = 4;

	private float timeSinceLastHit_ = -TIME_BETWEEN_HITS;

	Quarterback(PApplet app, float x, float y, float z, float t) {
		super(app, x, y, z, t);
		theApp_ = app;
		
		/** Load Left Images */
		leftThrow_ = new ArrayList<PImage>();
		leftThrow_.add(theApp_.loadImage("qb_l_1.png"));
		leftThrow_.add(theApp_.loadImage("qb_l_2.png"));
		leftThrow_.add(theApp_.loadImage("qb_l_3.png"));
		leftThrow_.add(theApp_.loadImage("qb_l_4.png"));
		
		/** Load Center Images */
		centerThrow_ = new ArrayList<PImage>();
		centerThrow_.add(theApp_.loadImage("qb_c_1.png"));
		centerThrow_.add(theApp_.loadImage("qb_c_2.png"));
		centerThrow_.add(theApp_.loadImage("qb_c_3.png"));
		centerThrow_.add(theApp_.loadImage("qb_c_4.png"));
		
		/** Load Right Images */
		rightThrow_ = new ArrayList<PImage>();
		rightThrow_.add(theApp_.loadImage("qb_r_1.png"));
		rightThrow_.add(theApp_.loadImage("qb_r_2.png"));
		rightThrow_.add(theApp_.loadImage("qb_r_3.png"));
		rightThrow_.add(theApp_.loadImage("qb_r_4.png"));
		
		/** Load the default, center sprite */
		centerSprite_ = theApp_.loadImage("qb_c_0.png");
		currentSprite_ = centerSprite_;
	}

	@Override
	public void draw() {
		theApp_.pushMatrix();
		theApp_.translate(getX(),getY());
		//Rotate
		//theApp_.rotate(throwLRAngle_+PApplet.PI/2);
		//Scale
		theApp_.scale(QB_SCALE_FACTOR,-QB_SCALE_FACTOR);
		
		//Draw 
		theApp_.image(currentSprite_, -currentSprite_.width/2, -currentSprite_.height/2, 
				currentSprite_.width, currentSprite_.height);
		
		theApp_.popMatrix();
	}

	/** Changes the sprite based on the time of the last throw, and the throw code */
	public boolean update(float timeNow) {
		float timeSinceThrow_ = timeNow-timeThrowStarted_;
		if (timeSinceThrow_ < QB_THROW_DURATION) {
			int spriteCode_ = (int)(4*((timeSinceThrow_)/QB_THROW_DURATION));
			switch (throwCode_) {
			case 0:
				currentSprite_ = rightThrow_.get(spriteCode_);
				break;
			case 1:
				currentSprite_ = centerThrow_.get(spriteCode_);
				break;
			case 2:
				currentSprite_ = leftThrow_.get(spriteCode_);
				break;
			}
		} else {
			currentSprite_ = centerSprite_;
		}
		return true;
	}

	/** Returns true if the x,y coordinates are where the sprite is drawn */
	public boolean isInside(float x, float y, float z) {
		if (x <= getX() + currentSprite_.width/3 && x >= getX() -currentSprite_.width/3
				&& y <= getY() + currentSprite_.height/3 && y >= getY() - currentSprite_.height/3) {
			return true;
		} else return false;
	}
	/** Changes the rotation angle of the QB and also determines
	 * which throw sequence to use 
	 * @param throwLRAngle */
	public void setLRAngle(float throwLRAngle) {
		throwLRAngle_ = throwLRAngle;
		
	}
	/** Starts the throw sequence by increasing the throw timer to max */
	public void throwBall(float timeThrowStarted) {
		/** Reset the throw timer */
		timeThrowStarted_ = timeThrowStarted;
		
		/** Splits the range of throws into three sectors, one for each sequence */
		throwCode_ = Math.min((int) (3*(throwLRAngle_/PApplet.PI)),2);		
	}

	public void hit(float now) {
		if (now - timeSinceLastHit_ > TIME_BETWEEN_HITS) {
			lifePoints_ -= 1;
			timeSinceLastHit_ = now;
		}
	}

	public int getLifePoints() {
		return lifePoints_;
	}


}
