package mondaymorningqb;

import java.util.ArrayList;
import java.util.Random;

import processing.core.PApplet;
import processing.core.PImage;

public class Boss extends Enemy {
	PApplet theApp_;
	/** Time last throw started to keep up with the animation */
	private float timeThrowStarted_ = -TERRY_THROW_DURATION;
	/** The throw code is which sprite to use during the throw sequence */
	private final ArrayList<PImage> duckingTerry_;
	private final ArrayList<PImage> emergingTerry_;
	private final ArrayList<PImage> laughingTerry_;
	private final ArrayList<PImage> sadTerry_;
	private final ArrayList<PImage> throwingTerry_;
	private final PImage normalTerry_;
	private PImage currentSprite_;
	
	/** Message to display on Terry's Desk and color*/
	private String message_;
	private float mes_r_,mes_g_,mes_b_;
	
	/** Terry needs to keep track of his own beer cans */
	private ArrayList<BeerCan> beerCans_;
	/** Terry needs to know if he's been defeated */
	private int lifePoints_;
	
	/** If Terry dies, he moves down with the field */
	private float scrollRate_, deadTime_;
	private float vX_;
	private float updateTime_;
	/** When the victory laugh started */
	private float timeLaughStarted_ = -TERRY_LAUGH_DURATION;
	private float angleToQB_;
	private boolean buildCan;
	/** When Terry was last hit */
	private float timeSinceLastHit_ = -TIME_BETWEEN_HITS;
	private int cansPerThrow_;
	
	/** Randomness for the throw */
	private Random rand;
	public float lastThrowTime_ = 0;

	Boss(PApplet app, float x, float y, float z, float t, float scrollRate, Difficulty dif) {
		super(app, x, y, z, t);
		theApp_ = app;
		scrollRate_ = scrollRate;
		rand = new Random(theApp_.millis());
		
		/** Set Life Points */
		lifePoints_ = setLifePoints(dif);
		cansPerThrow_ = setCansPerThrow(dif);
		
		/** Set up beer cans list */
		beerCans_ = new ArrayList<BeerCan>();
		
		/** Set X Speed */
		vX_ = setVX(dif);
		
		/** Load Ducking Images */
		duckingTerry_ = new ArrayList<PImage>();
		duckingTerry_.add(theApp_.loadImage("ducking_terry_1.png"));
		duckingTerry_.add(theApp_.loadImage("ducking_terry_2.png"));
		duckingTerry_.add(theApp_.loadImage("ducking_terry_3.png"));
		
		/** Load Emerging Images */
		emergingTerry_ = new ArrayList<PImage>();
		emergingTerry_.add(theApp_.loadImage("emerging_terry_1.png"));
		emergingTerry_.add(theApp_.loadImage("emerging_terry_2.png"));
		emergingTerry_.add(theApp_.loadImage("emerging_terry_3.png"));
		
		/** Load Laughing Images */
		laughingTerry_ = new ArrayList<PImage>();
		laughingTerry_.add(theApp_.loadImage("laughing_terry_1.png"));
		laughingTerry_.add(theApp_.loadImage("laughing_terry_2.png"));
		laughingTerry_.add(theApp_.loadImage("laughing_terry_3.png"));
		laughingTerry_.add(theApp_.loadImage("laughing_terry_4.png"));
		
		/** Load Sad Terry Images */
		sadTerry_ = new ArrayList<PImage>();
		sadTerry_.add(theApp_.loadImage("sad_terry_1.png"));
		sadTerry_.add(theApp_.loadImage("sad_terry_2.png"));
		sadTerry_.add(theApp_.loadImage("sad_terry_3.png"));
		sadTerry_.add(theApp_.loadImage("sad_terry_4.png"));
		
		/** Load Throwing Terry Images */
		throwingTerry_ = new ArrayList<PImage>();
		throwingTerry_.addAll(duckingTerry_);
		throwingTerry_.addAll(emergingTerry_);
		throwingTerry_.add(theApp_.loadImage("throwing_terry_1.png"));
		throwingTerry_.add(theApp_.loadImage("throwing_terry_1.png"));
		throwingTerry_.add(theApp_.loadImage("throwing_terry_1.png"));
		throwingTerry_.add(theApp_.loadImage("throwing_terry_2.png"));
		throwingTerry_.add(theApp_.loadImage("throwing_terry_2.png"));
		throwingTerry_.add(theApp_.loadImage("throwing_terry_2.png"));
		throwingTerry_.add(theApp_.loadImage("emerging_terry_3.png"));
		throwingTerry_.add(theApp_.loadImage("emerging_terry_2.png"));
		throwingTerry_.add(theApp_.loadImage("emerging_terry_1.png"));
		throwingTerry_.add(theApp_.loadImage("ducking_terry_3.png"));
		throwingTerry_.add(theApp_.loadImage("ducking_terry_2.png"));
		throwingTerry_.add(theApp_.loadImage("ducking_terry_1.png"));
		
		/** Load the default, normal sprite */
		normalTerry_ = theApp_.loadImage("normal_terry.png");
		currentSprite_ = normalTerry_;
		
		/** Load the Default Message */
		message_ = TERRY_DEFAULT_MESSAGE;
		mes_r_ = 255; mes_g_ = 255; mes_b_ = 0;
		
	}
	
	private int setCansPerThrow(Difficulty dif) {
		switch(dif) {
		case EASY: return CANS_EASY;
		case MEDIUM: return CANS_MEDIUM;
		case HARD: return CANS_HARD;
		case INSANE: return CANS_INSANE;
		default: return CANS_MEDIUM;
		}
	}

	/** Sets how fast Terry moves back and forth based on the difficulty */
	private float setVX(Difficulty dif) {
		switch(dif) {
		case EASY: return TERRY_DEFAULT_XSPEED*.5f;
		case MEDIUM: return TERRY_DEFAULT_XSPEED;
		case HARD: return TERRY_DEFAULT_XSPEED*2;
		case INSANE: return TERRY_DEFAULT_XSPEED*8;
		default: return TERRY_DEFAULT_XSPEED;
		}
	}

	/** Initializes Terry's life points */
	private int setLifePoints(Difficulty dif) {
		switch(dif) {
		case EASY: return BOSS_LP_EASY; //2;
		case MEDIUM: return BOSS_LP_MEDIUM;//4;
		case HARD: return BOSS_LP_HARD; //8;
		case INSANE: return BOSS_LP_INSANE; //15;
		default: return 4;
		}
	}

	/**Draws the boss, Terry Bradshaw, at the top of the screen, moving back and forth */
	public void draw() {
		theApp_.pushMatrix();
		theApp_.translate(getX(), getY());
		
		//Scale
		theApp_.scale(TERRY_SCALE_FACTOR,-TERRY_SCALE_FACTOR);
				
		//Draw 
		theApp_.image(currentSprite_, -currentSprite_.width/2, -currentSprite_.height/2, 
			currentSprite_.width, currentSprite_.height);
		
		/** Write the message on Terry's desk screen */
		theApp_.scale(1/TERRY_SCALE_FACTOR,1/TERRY_SCALE_FACTOR);
		theApp_.translate(-normalTerry_.height/25, normalTerry_.height/15);
		theApp_.noStroke();
		theApp_.fill(mes_r_,mes_g_,mes_b_);//Paint it steelers yellow
		theApp_.textSize(3);
		theApp_.text(message_,0,0);
		
		theApp_.popMatrix();
		
		/** Now draw any and all beer cans */
		for (BeerCan can : beerCans_) {
			can.draw();
		}
		
	}

	/** If Terry is dead, this moves him down the screen and starts
	 * the pouting sequence.
	 * If Terry is alive, he checks to see if he needs to throw the ball again
	 * Also, if Terry has won, he starts the
	 */
	public boolean update(float timeNow) {
		/** First update any and all beer cans */
		for (int i = 0; i < beerCans_.size(); i++) {
			if (!beerCans_.get(i).update(timeNow)) beerCans_.remove(i);
		}
		
		/**IF DEAD: move down with scroll rate, proceed with sadness sequence*/
		if (lifePoints_ < 1) {
			float timeSinceDead_ = timeNow-deadTime_;
			setY(getY()-scrollRate_*(timeNow-updateTime_)/1000);
			updateTime_ = timeNow;
			//Sadness
			if (timeSinceDead_ < TERRY_DYING_DURATION) {
				int spriteCode_ = (int)(sadTerry_.size()*((timeSinceDead_)/TERRY_DYING_DURATION));
				currentSprite_ = sadTerry_.get(spriteCode_);
			} else {
				//Pass
				//Maybe set the desk on fire later TODO:
			}
		} else {
		
		
		/**IF ALIVE: Move left and right (current pos + sin(time*something)*(RIGHT-LEfT)/2)
			//Every so many seconds, throw another can (also update cans then, take out the far ones)*/
			/** Move Terry back and forth */
			float TERRY_LEFT_LIMIT = LEFT_FIELD_LIMIT+TERRY_SCALE_FACTOR*currentSprite_.width/2;
			float TERRY_RIGHT_LIMIT = RIGHT_FIELD_LIMIT-TERRY_SCALE_FACTOR*currentSprite_.width/2;
			if (getX()+vX_ < TERRY_LEFT_LIMIT || getX()+vX_ > TERRY_RIGHT_LIMIT) {
				vX_ = -vX_;
			}
			setX(getX() + vX_);
			
			/** Check for a throw, and throw if there is one started
			 * Or check for a victory laugh, and laugh if started */
			float timeSinceThrow_ = timeNow-timeThrowStarted_;
			float timeSinceLaugh_ = timeNow-timeLaughStarted_;
			if (timeSinceThrow_ < TERRY_THROW_DURATION) {
				int spriteCode_ = (int)(throwingTerry_.size()*((timeSinceThrow_)/TERRY_THROW_DURATION));
				currentSprite_ = throwingTerry_.get(spriteCode_);
				/**Wait a while to build and throw the can */
				if (spriteCode_ > throwingTerry_.size()/2 && buildCan ) {
					for (int i = 0; i < cansPerThrow_; i++) buildCan();
				}
			/** Check if Terry is supposed to be laughing */
			} else if (timeLaughStarted_ > 0) {
				/** Victory Laugh */
				int spriteCode_ = (int)(laughingTerry_.size()*((timeSinceLaugh_ % TERRY_LAUGH_DURATION)/TERRY_LAUGH_DURATION));
				currentSprite_ = laughingTerry_.get(spriteCode_);
			
			/** Otherwise go back to regular old Terry */
			}else {
				currentSprite_ = normalTerry_;
			}
			
		}
		updateMessageFlash(timeNow);
		return true;
	}
	
	/** Returns the throw interval based on the diffuclty level of the game */
	public float getThrowInterval(Difficulty dif) {
		switch(dif) {
		case EASY: return BOSS_TI_EASY; 
		case MEDIUM: return BOSS_TI_MEDIUM; 
		case HARD: return BOSS_TI_HARD; 
		case INSANE: return BOSS_TI_INSANE;
		default: return BOSS_TI_MEDIUM;
		}
	}
	
	/** Update the message flashing */
	private void updateMessageFlash(float timeNow) {
		float sinTime = Math.abs(PApplet.sin(timeNow/250));
		mes_r_ = 255*sinTime; mes_g_ = 255*sinTime; mes_b_ = 0;
	}

	/** Checks to see if the football is inside terry */
	public boolean isInside(float x, float y, float z) {
		if (x <= getX() + currentSprite_.width/20 && x >= getX() -currentSprite_.width/20
				&& y <= getY() + currentSprite_.height/3 && y >= getY() && 
				z >= getZ() -0.01 && z <= getZ() + 0.01) {
			return true;
		} else return false;
	}
	
	/** Decrement Terry's life points and return the value */
	public int hit(float timeNow) {
		if (timeNow - timeSinceLastHit_ > TIME_BETWEEN_HITS && lifePoints_ >= 1) {
			--lifePoints_;
			timeSinceLastHit_ = timeNow;
				if (lifePoints_<1) {
					deadTime_ = timeNow; 
					updateTime_ = timeNow;
					message_ = TERRY_DYING_MESSAGE;
				}
		}
		return lifePoints_;
	}
	
	/** Starts the throw sequence by increasing the throw timer to max */
	public void throwCan(float timeThrowStarted, float angleToQB) {
		/** Reset the throw timer */
		timeThrowStarted_ = timeThrowStarted;
		angleToQB_ = angleToQB;
		buildCan = true;
	}
	
	/** Builds a can and adds it to the list of cans */
	private void buildCan(){
		float myAngleToQB = angleToQB_ += (PApplet.PI/12)*(rand.nextFloat()-.5);
		final float xVel = TERRY_THROW_SPEED*PApplet.cos(myAngleToQB) ;
		final float yVel = TERRY_THROW_SPEED*PApplet.sin(myAngleToQB);
		final float xOrig = getX();
		final float yOrig = getY();
		
		/** Build the X Function */
		TrajectoryFunction xFcn = new TrajectoryFunction() {
			public float TrajFcn(float timeElapsed) {
				return (float) (xVel*timeElapsed+xOrig); }};
		
		/** Build the Y Function */
		TrajectoryFunction yFcn = new TrajectoryFunction() {
			public float TrajFcn(float timeElapsed) {
				return (float) (yVel*timeElapsed+yOrig); }};

		/** Build the Z Function, don't let the football be lower than the field*/
		TrajectoryFunction zFcn = new TrajectoryFunction() {
			public float TrajFcn(float timeElapsed) {
				/** Terry Throws line drives */
				return 0; }};
		
		/** Build the Theta Function, constant rotation based on initial velocity of the pass */
		TrajectoryFunction thetaFcn = new TrajectoryFunction() {
			public float TrajFcn(float timeElapsed) {
				return (float) (ROT_FACTOR*20*timeElapsed); }};
		
		BeerCan can = new BeerCan(theApp_, 0, 0, 0, 0, xFcn, yFcn, zFcn, thetaFcn);
		beerCans_.add(can);
		buildCan = false;
	}
	/** Starts the laugh sequence by increasing the throw timer to max */
	public void victoryLaugh(float timeLaughStarted) {
		/** Reset the throw timer */
		timeLaughStarted_ = timeLaughStarted;
		message_ = TERRY_VICTORY_MESSAGE;
	}
	
	public int getLifePoints() {
		return lifePoints_;
	}

	/** Returns all the beer cans in sequence */
	public ArrayList<BeerCan> getProjectiles() {
		return  beerCans_;
	}


}
