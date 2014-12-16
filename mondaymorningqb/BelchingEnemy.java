package mondaymorningqb;

import java.util.ArrayList;
import java.util.Random;

import processing.core.PApplet;
import processing.core.PImage;

/** This is the guy who drinks soda and belches toxic gas */
public class BelchingEnemy extends Enemy {
	static PApplet theApp_;
	private PImage currentSprite_;
	
	/** Guy needs to keep track of his own cheetos */
	private ArrayList<BelchBubble> bubbles_;
	/** Guy needs to know if he's been defeated */
	private int lifePoints_;
	
	/** Bad Guy moves down with the field */
	private float scrollRate_;
	private float updateTime_;

	/** When BadGuy was last hit */
	private float timeSinceLastHit_ = -TIME_BETWEEN_HITS;
	private float changeSpriteTime_;
	private final boolean LEFT;
	
	/** Cheetos launched depends on difficulty */
	private final int bubblesPerBelch_;
	
	/** Need a random number generator */
	 Random rand;
	private ArrayList<PImage> burningSequence_;
	private float deadTime_;

	BelchingEnemy(PApplet app, float x, float y, float z, float t, float scrollRate, Difficulty dif) {
		super(app, x, y, z, t);
		theApp_ = app;
		
		rand = new Random(theApp_.millis());
		
		scrollRate_ = scrollRate;

		
		/** Set Life Points and cheetos per chew */
		lifePoints_ = setLifePoints(dif);
		bubblesPerBelch_ = setBubblesPerBelch(dif);
		
		/** Set up cheetos list */
		bubbles_ = new ArrayList<BelchBubble>();
		
		
		/** Load the Burning Sequence */
		burningSequence_ = new ArrayList<PImage>();
		burningSequence_.add(theApp_.loadImage("fire_1.png"));
		burningSequence_.add(theApp_.loadImage("fire_2.png"));
		burningSequence_.add(theApp_.loadImage("fire_3.png"));
		
		/** Set the current sprite */
		currentSprite_ = Sprites.belchingSequence_.get(0);
		
		/**Sets up which side of the field this guy's on */
		if (x<(RIGHT_FIELD_LIMIT-LEFT_FIELD_LIMIT)/2) {
			LEFT = true;
		} else {
			LEFT = false;
		}
		
		updateTime_ = theApp_.millis();
	}

	@Override
	public void draw() {
		theApp_.pushMatrix();
		
		
		if (LEFT) {
			//Translate
			theApp_.translate(getX(), getY());
			
			//Scale
			theApp_.scale(-BELCH_GUY_SCALE_FACTOR,-BELCH_GUY_SCALE_FACTOR);
					
			//Draw 
			theApp_.image(currentSprite_, 0, 0, 
				-currentSprite_.width, currentSprite_.height);
			
			if (lifePoints_ < 1) {
				//Draw flame
				theApp_.translate(0, -currentSprite_.width/3);
				theApp_.image(burningSequence_.get((int) (theApp_.millis()/(FRAME_RATE/3) % 3)), 0, 0, 
					-currentSprite_.width, currentSprite_.height);
			}
			
		} else {
			
			//Translate
			theApp_.translate(getX(), getY());
			
			//Scale
			theApp_.scale(BELCH_GUY_SCALE_FACTOR,-BELCH_GUY_SCALE_FACTOR);
					
			//Draw 
			theApp_.image(currentSprite_, 0, 0, 
				currentSprite_.width, currentSprite_.height);
			
			if (lifePoints_ < 1) {
				//Draw flame
				theApp_.translate(0, -currentSprite_.width/3);
				theApp_.image(burningSequence_.get((int) (theApp_.millis()/(FRAME_RATE/3) % 3)), 0, 0, 
					currentSprite_.width, currentSprite_.height);
			}
		} 
		
		theApp_.popMatrix();
		
		
		/** Now draw any and all cheetos */
		for (BelchBubble bubble : bubbles_) {
			bubble.draw();
		}
		
	}
	
	/** Uses the same constants as cheetos */
	private int setBubblesPerBelch(Difficulty dif) {
		switch(dif) {
		case EASY: return BUBBLES_EASY; 
		case MEDIUM: return BUBBLES_MEDIUM;
		case HARD: return BUBBLES_HARD;
		case INSANE: return BUBBLES_INSANE; 
		default: return BUBBLES_MEDIUM;
		}
	}
	
	/** Reduce the bad guy's life points */
	public int hit(float timeNow) {
		//PApplet.println("belch guy hit at "+ timeNow);
		if (timeNow - timeSinceLastHit_ > TIME_BETWEEN_HITS && lifePoints_ >= 1) {
			PApplet.println("cheeto guy points reducted at "+ timeNow);
			--lifePoints_;
			timeSinceLastHit_ = timeNow;
				if (lifePoints_<1) {
					deadTime_ = updateTime_ = timeNow;
				}
		}
		return lifePoints_;
	}

	/** Returns the bubbles */
	public ArrayList<? extends Projectile> getProjectiles() {
		return bubbles_;
	}
	
	/** Initializes Bad Guy's life points */
	private int setLifePoints(Difficulty dif) {
		switch(dif) {
		case EASY: return ENEMY_LP_EASY; 
		case MEDIUM: return ENEMY_LP_MEDIUM;
		case HARD: return ENEMY_LP_HARD;
		case INSANE: return ENEMY_LP_INSANE; 
		default: return ENEMY_LP_MEDIUM;
		}
	}
	/** Updates bad guy's belch bubbles and his position based on time */
	public boolean update(float timeNow) {
		/** First update any and all bubbles */
		for (int i = 0; i < bubbles_.size(); i++) {
			if (!bubbles_.get(i).update(timeNow)) {
				bubbles_.remove(i);
				i = 0;
			}
		}
		
		/** Move with the field */
		setY(getY()-scrollRate_*(timeNow-updateTime_)/1000);
		
		/**IF DEAD: set on fire else proceed with sprite sequence*/
		if (lifePoints_ < 1) {
			//TODO change to fire sequence
		
			 /** IF still alive, set new sprite */
			} else if (timeNow-changeSpriteTime_ > CHANGE_BELCH_INTERVAL) {
				int current = Sprites.belchingSequence_.indexOf(currentSprite_);
				int size = Math.max(Sprites.belchingSequence_.size(),1);
				currentSprite_ = Sprites.belchingSequence_.get((current+1)%size);
				//currentSprite_ = munchingSequenceIter_.next();
				changeSpriteTime_ = timeNow;
				
				/** Also build some new cheetos if he's quarter way through the cycle */
				if (current == Math.min(7, Sprites.belchingSequence_.size()-1)) {
					for (int i = 0; i < bubblesPerBelch_; i++) bubbles_.add(addBelchBubble(theApp_.millis()));
				}
			}
			
		
		updateTime_ = timeNow;
		/** Returns false only if the guy's been dead for FIRE_TIME milliseconds */
		return !(lifePoints_ < 1 && (timeNow-deadTime_ > FIRE_TIME));
	}
private BelchBubble addBelchBubble(float timeNow) {
		
		final float initVelocity = DEFAULT_BUBBLE_SPEED * (rand.nextFloat()+3);
		final float A = DEFAULT_BUBBLE_SIZE*.7f;
		final float phaseShift = 2* PApplet.PI * rand.nextFloat();
		final float LRangle;
		final float xOrig = getX() + currentSprite_.width/30, yOrig = getY() - currentSprite_.width/30;
		float angle = PApplet.PI/2*(rand.nextFloat()-1);//Starts the angle somewhere in the 4th quadrant
		
		/** Calculate the take off and left/right angles */
		if (LEFT) {
			LRangle = angle; //Puts the angle somewhere in the 4th quadrant
		} else LRangle = angle - PApplet.PI/2; //3rd quadrant
		// PApplet.println("angle " + angle*180/PApplet.PI + " TOangle " +TOangle*180/PApplet.PI + " LRangle " + LRangle*180/PApplet.PI + " hi ty " + 3);
		
		/** Build the X Function */
		TrajectoryFunction xFcn = new TrajectoryFunction() {
			public float TrajFcn(float timeElapsed) {
				return (float) (initVelocity*Math.cos(LRangle)*timeElapsed+A*Math.cos(timeElapsed*10-phaseShift)+xOrig); }};//TODO:  add circle component Math.cos(timeElapsed)
		
		/** Build the Y Function */
		TrajectoryFunction yFcn = new TrajectoryFunction() {
			public float TrajFcn(float timeElapsed) {
				return (float) (initVelocity*Math.sin(LRangle)*timeElapsed+A*Math.sin(timeElapsed*10-phaseShift)+yOrig); }};//TODO: ADD CIRCLE (SIN) COMPONENT +++

		/** Build the Z Function, bubbles will just stay on the field*/
		TrajectoryFunction zFcn = new TrajectoryFunction() {
			public float TrajFcn(float timeElapsed) {
				/** Classic free fall trajectory without winds */
				return 0; }};
		
		/** Build the Theta Function, not used for belch bubbles */
		TrajectoryFunction thetaFcn = new TrajectoryFunction() {
			public float TrajFcn(float timeElapsed) {
				return 0; }};
		
		BelchBubble newBubble = new BelchBubble(theApp_, getX(), getY(), getZ(), 0, xFcn, yFcn, zFcn, thetaFcn);
		return newBubble;
	}

	/** Checks to see if the football is inside Bad Guy */
	public boolean isInside(float x, float y, float z) {
		if (x <= getX() + WORLD_WIDTH/7 && x >= getX() -WORLD_WIDTH/7
				&& y <= getY() + WORLD_WIDTH/7 && y >= getY() - WORLD_WIDTH/7 && 
				z >= getZ() -0.01 && z <= getZ() + 0.01) {
			return true;
		} else return false;
	}

}
