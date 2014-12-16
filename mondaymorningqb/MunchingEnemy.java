package mondaymorningqb;

import java.util.ArrayList;

import java.util.Random;

import processing.core.PApplet;
import processing.core.PImage;

/** This is the guy who eats Cheetos and sprays dangerous crumbs */
public class MunchingEnemy extends Enemy{
	static PApplet theApp_; 
	private PImage currentSprite_;
	
	/** Guy needs to keep track of his own cheetos */
	private ArrayList<Cheeto> cheetos_;
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
	private final int cheetosPerChew_;
	
	/** Need a random number generator */
	Random rand;
	private float deadTime_;

	MunchingEnemy(PApplet app, float x, float y, float z, float t, float scrollRate, Difficulty dif) {
		super(app, x, y, z, t);
		theApp_ = app;
		
		rand = new Random(theApp_.millis());
		
		cheetos_ = new ArrayList<Cheeto>();
		
		scrollRate_ = scrollRate;
		updateTime_ = theApp_.millis();
		
		/** Set Life Points and cheetos per chew */
		lifePoints_ = setLifePoints(dif);
		cheetosPerChew_ = setCheetosPerChew(dif);
				
		
		/** Set the current sprite */
		currentSprite_ = Sprites.munchingSequence_.get(0);
		
		/**Sets up which side of the field this guy's on */
		if (x<(RIGHT_FIELD_LIMIT-LEFT_FIELD_LIMIT)/2) {
			LEFT = true;
		} else {
			LEFT = false;
		}
		
	}

	private int setCheetosPerChew(Difficulty dif) {
		switch(dif) {
		case EASY: return CHEETOS_EASY; 
		case MEDIUM: return CHEETOS_MEDIUM;
		case HARD: return CHEETOS_HARD;
		case INSANE: return CHEETOS_INSANE; 
		default: return CHEETOS_MEDIUM;
		}
	}

	/** Reduce the bad guy's life points */
	public int hit(float timeNow) {
		if (timeNow - timeSinceLastHit_ > TIME_BETWEEN_HITS && lifePoints_ >= 1) {
			--lifePoints_;
			timeSinceLastHit_ = timeNow;
				if (lifePoints_<1) {
					deadTime_ = updateTime_ = timeNow;
				}
		}
		return lifePoints_;
	}

	/** Returns the cheetos*/
	public ArrayList<? extends Projectile> getProjectiles() {
		return cheetos_;
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
	
	/** Checks to see if the football is inside Bad GUy */
	public boolean isInside(float x, float y, float z) {
		if (LEFT) {
			if (x <= getX() + WORLD_WIDTH/10 && x >= getX() -WORLD_WIDTH/10
					&& y <= getY() + WORLD_WIDTH/10 && y >= getY() - WORLD_WIDTH/10 && 
					z >= getZ() -0.01 && z <= getZ() + 0.01) {
				return true;
			} else return false;
		} else {
			if (x <= getX() + 2*WORLD_WIDTH/10 && x >= getX()
					&& y <= getY() + WORLD_WIDTH/10 && y >= getY() - WORLD_WIDTH/10 && 
					z >= getZ() -0.01 && z <= getZ() + 0.01) {
				return true;
			} else return false;
		}
	}

	@Override
	public void draw() {
		theApp_.pushMatrix();
		
		
		if (LEFT) {
			//Translate
			theApp_.translate(getX(), getY());
			
			//Scale
			theApp_.scale(MUNCH_GUY_SCALE_FACTOR,-MUNCH_GUY_SCALE_FACTOR);
					
			//Draw 
			theApp_.image(currentSprite_, 0, 0, 
				currentSprite_.width, currentSprite_.height);
			
			if (lifePoints_ < 1) {
				//Draw flame
				theApp_.translate(0, -currentSprite_.width/3);
				theApp_.image(Sprites.burningSequence_.get((int) (theApp_.millis()/(FRAME_RATE/3) % 3)), 0, 0, 
					currentSprite_.width, currentSprite_.height);
			}
			
		} else {
			
			//Translate
			theApp_.translate(getX(), getY());
			
			//Scale
			theApp_.scale(-MUNCH_GUY_SCALE_FACTOR,-MUNCH_GUY_SCALE_FACTOR);
					
			//Draw 
			theApp_.image(currentSprite_, 0, 0, 
				-currentSprite_.width, currentSprite_.height);
			
			if (lifePoints_ < 1) {
				//Draw flame
				theApp_.scale(-1,1);
				theApp_.translate(0, -currentSprite_.width/3);
				theApp_.image(Sprites.burningSequence_.get((int) (theApp_.millis()/(FRAME_RATE/3) % 3)), 0, 0, 
					currentSprite_.width, currentSprite_.height);
			}
		} 
		
		theApp_.popMatrix();
		
		
		/** Now draw any and all cheetos */
		for (Cheeto cheeto : cheetos_) {
			cheeto.draw();
		}
		
	}

	@Override
	public boolean update(float timeNow) {
		/** First update any and all cheetos */
		for (int i = 0; i < cheetos_.size(); i++) {
			if (!cheetos_.get(i).update(timeNow)) cheetos_.remove(i);
		}
		
		/** Move with the field */
		setY(getY()-scrollRate_*(timeNow-updateTime_)/1000);
		
		/**IF DEAD: set on fire else proceed with sprite sequence*/
		if (lifePoints_ < 1) {
			//TODO change to fire sequence
		
			 /** IF still alive, set new sprite */
			} else if (timeNow-changeSpriteTime_ > CHANGE_SPRITE_INTERVAL) {
				int current = Sprites.munchingSequence_.indexOf(currentSprite_);
				int size = Math.max(Sprites.munchingSequence_.size(),1);
				currentSprite_ = Sprites.munchingSequence_.get((current+1)%size);
				//currentSprite_ = munchingSequenceIter_.next();
				changeSpriteTime_ = timeNow;
				
				/** Also build some new cheetos if he's quarter way through the cycle */
				if (current == (int) Sprites.munchingSequence_.size()/4) {
					for (int i = 0; i < cheetosPerChew_; i++) cheetos_.add(addCheeto(theApp_.millis()));
				}
			}
			
		
		updateTime_ = timeNow;
		/** Returns false only if the guy's been dead for FIRE_TIME milliseconds */
		return !(lifePoints_ < 1 && (timeNow-deadTime_ > FIRE_TIME));
	}

	private Cheeto addCheeto(float timeNow) {
		
		final float initVelocity = DEFAULT_CHEETO_SPEED * (rand.nextFloat()+3);
		final float rot = .2f*(rand.nextFloat()-1);
		final float LRangle;
		final float xOrig = getX() + currentSprite_.width/30, yOrig = getY() - currentSprite_.width/50, zOrig = QB_HEIGHT;
		final float TOangle;
		float angle = PApplet.PI/2*(rand.nextFloat()-1);//Starts the angle somewhere in the 4th quadrant
		
		/** Calculate the take off and left/right angles */
		TOangle = rand.nextFloat()*PApplet.PI/4 + PApplet.PI/12;
		if (LEFT) {
			LRangle = angle; //Puts the angle somewhere in the 4th quadrant
		} else LRangle = angle - PApplet.PI/2; //3rd quadrant
		// PApplet.println("angle " + angle*180/PApplet.PI + " TOangle " +TOangle*180/PApplet.PI + " LRangle " + LRangle*180/PApplet.PI + " hi ty " + 3);
		
		/** Build the X Function */
		TrajectoryFunction xFcn = new TrajectoryFunction() {
			public float TrajFcn(float timeElapsed) {
				return (float) (initVelocity*Math.cos(TOangle)*Math.cos(LRangle)*timeElapsed+xOrig); }};
		
		/** Build the Y Function */
		TrajectoryFunction yFcn = new TrajectoryFunction() {
			public float TrajFcn(float timeElapsed) {
				return (float) (initVelocity*Math.cos(TOangle)*Math.sin(LRangle)*timeElapsed+yOrig); }};

		/** Build the Z Function, don't let the cheeto be lower than the field*/
		TrajectoryFunction zFcn = new TrajectoryFunction() {
			public float TrajFcn(float timeElapsed) {
				/** Classic free fall trajectory without winds */
				return (float) (Math.max(-GRAVITY*timeElapsed*timeElapsed +
						initVelocity*Math.sin(TOangle)*timeElapsed + zOrig,MINIMUM_FOOTBALL_ALTITUDE)); }};
		
		/** Build the Theta Function, constant rotation based on initial velocity of the pass */
		TrajectoryFunction thetaFcn = new TrajectoryFunction() {
			public float TrajFcn(float timeElapsed) {
				return (float) (rot*initVelocity*timeElapsed); }};
		
		Cheeto newCheeto = new Cheeto(theApp_, getX(), getY(), getZ(), 0, xFcn, yFcn, zFcn, thetaFcn);
		return newCheeto;
	}

}
