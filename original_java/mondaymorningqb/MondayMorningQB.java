package mondaymorningqb;

import java.util.ArrayList;
import java.util.Random;

import processing.core.PApplet;

/** Choose a difficulty and destroy the fat MMQBs! */
public class MondayMorningQB extends PApplet implements Constants {
	private Football theFootball_;
	private Arrow theArrow_;
	private Ring theRing_;
	private Field theField_;
	private Quarterback theQB_;
	private QBLifeBar qbBar_;
	private ArrayList<Enemy> enemies_;
	private Boss theBoss_;
	private BossLifeBar bossBar_;
	/** Accuracy is the accuracy with which the beer cans are thrown (lower is less accurate) */
	private TitleScreen theTitleScreen_;
	private GameOverScreen theGameOverScreen_;
	private KillCounter theKillCounter_;
	public Sprites theSprites_;

	/** The difficulty and level of the game */
	Difficulty theDif_; Level theLev_;

	/** Football throw related variables */
	private float throwLRAngle_, throwTOAngle_, throwSpeed_, ballX_, ballY_; 

	/** Time elapsed */
	private float millis_;

	/** Random Generator */
	private Random rand;

	/** Last time enemies were updated, so new ones don't come out on top of eachother */
	private float enemiesLastUpdated_ = 0;

	private static final long serialVersionUID = 1L;
//	public static void main(String args[])
//    {
//      PApplet.main(new String[] { mondaymorningqb.MondayMorningQB.class.getName() });
//    }

	public void setup() {
		//size(Math.round(WINDOW_WIDTH), Math.round(WINDOW_HEIGHT));
		//TODO: Changed for export
		size(600,600);
		
		frameRate(FRAME_RATE);

		resetThrow(); //Sets the throw to default variables
		ballX_ = WORLD_WIDTH/2;
		ballY_ = WORLD_HEIGHT/7;

		/** Set the default level */
		theLev_ = Level.ONE; //Default

		/**Start the game */
		theDif_ = Difficulty.MEDIUM; //Just to be safe here
		startNewGame();

		/** Set up the random generator */
		rand = new Random(millis());

		millis_ = millis();

		/** Set up the Sprites */
		theSprites_ = new Sprites(this);

	}

	/** Takes a player to the title screen to select a difficulty level */
	public void startNewGame() {
		/** Null out everything that might be alive from an old game */
		theFootball_ = null;
		theArrow_ = null;
		theRing_ = null;
		theField_ = null;
		theQB_ = null;
		qbBar_ = null;
		enemies_ = null;
		theBoss_ = null;
		bossBar_ = null;
		theTitleScreen_ = null;
		theGameOverScreen_ = null;
		theKillCounter_ = null;

		/** Build the Field as a temporary background (If you build it, they will come) */
		theField_ = new Field(this, LEFT_FIELD_LIMIT, 0, 0, 0, Level.ONE, Difficulty.EASY);
		/** Now build the title screen to get the selection from it */
		theTitleScreen_ = new TitleScreen(this, LEFT_FIELD_LIMIT+(RIGHT_FIELD_LIMIT-LEFT_FIELD_LIMIT)/8,
				WORLD_HEIGHT/8, RIGHT_FIELD_LIMIT-(RIGHT_FIELD_LIMIT-LEFT_FIELD_LIMIT)/8, 7*WORLD_HEIGHT/8);

		/** Now just wait for the enter key to be pressed! */
	}

	/** Starts a game with a selected difficulty level */
	private void playGame() {
		/** Start on level 1 */
		theLev_ = Level.ONE;

		/** Update the scroll speed of the field */
		theField_ = new Field(this, LEFT_FIELD_LIMIT, 0, 0, 0, theLev_, theDif_);

		/** Set up the QB and his life meter*/
		theQB_ = new Quarterback(this, ballX_, 4*ballY_/5, 0, 0);
		qbBar_ = new QBLifeBar(this, LEFT_FIELD_LIMIT/2, WORLD_HEIGHT/4,0,theQB_.getLifePoints());
		theKillCounter_ = new KillCounter(this, 3*(LEFT_FIELD_LIMIT)/8, .95f*WORLD_HEIGHT,0,0);

		/** Set up the Arrow */
		theArrow_ = new Arrow(this, ballX_, ballY_, 0, 0, throwLRAngle_, throwTOAngle_, throwSpeed_);

		/** Set up the Ring */
		theRing_ = new Ring(this, ballX_, ballY_, 0, 0, throwLRAngle_, throwTOAngle_, throwSpeed_);

		/** Set up the arraylist of enemies, if it doesn't exist already */
		enemies_ = new ArrayList<Enemy>(); 

	}

	/** Update Game Status */
	private void updateStatus() {
		/** Check Collisions */
		checkCollisions();

		/** Check Life Points and Die if Less than 1 */
		if (theQB_ != null && theQB_.getLifePoints() < 1) die();


		/** Check Level by asking the field for current yard */
		if (theField_ != null) {
			int topYard = theField_.getTopYard();

			/** Do messages */
			switch(topYard) {
			case LEVEL_ONE_YARDS: theField_.setMessage(LEVEL_ONE_COMPLETE_MESSAGE);
			break;
			case LEVEL_TWO_YARDS: theField_.setMessage(LEVEL_TWO_COMPLETE_MESSAGE);
			break;
			case LEVEL_THREE_YARDS: theField_.setMessage(LEVEL_THREE_COMPLETE_MESSAGE);
			break;
			case (LEVEL_THREE_YARDS + 5): theField_.setMessage(BOSS_LEVEL_START_MESSAGE);
			break;
			case 666: theField_.setMessage("NUMBER OF THE BEAST!!");
			break;
			}

			/** Promote the game level based on how many yards have passed */
			if (topYard > LEVEL_ONE_YARDS) theLev_ = Level.TWO;
			if (topYard > LEVEL_TWO_YARDS) theLev_ = Level.THREE;
			if (topYard > LEVEL_THREE_YARDS) theLev_ = Level.BOSS;
			if (theKillCounter_ != null) theKillCounter_.setLevel(theLev_);
		}
	}
	/** Updates the enemies based on difficulty and level */
	private void manageEnemies() {
		if (millis_ - enemiesLastUpdated_ > MIN_TIME_BETWEEN_NEW_ENEMY) {
			/** Refresh the timer */
			enemiesLastUpdated_ = millis_;

			/** Create new Enemies based on level */
			int Es= enemies_.size();

			switch(theLev_) {
			/** Don't add enemies for the start screen */
			case ZERO:
				break;
				/** Level 1 is just cheetos guy */
			case ONE:
				switch (theDif_) {
				case EASY: 
					if (Es < EASY_MIN_ENEMIES) addMunchGuy();
					else if (Es < EASY_MAX_ENEMIES) addMunchGuy();
					break;
				case MEDIUM:
					if (Es < MED_MIN_ENEMIES) addMunchGuy();
					else if (Es < MED_MAX_ENEMIES) addMunchGuy();
					break;
				case HARD:
					if (Es < HARD_MIN_ENEMIES) addMunchGuy();
					else if (Es < HARD_MAX_ENEMIES) addMunchGuy();
					break;
				case INSANE:
					if (Es < INSANE_MIN_ENEMIES) addMunchGuy();
					else if (Es < INSANE_MAX_ENEMIES) addMunchGuy();
					break;
				}
				break;

				/** Level 2 is belch guy */
			case TWO:
				switch (theDif_) {
				case EASY: 
					if (Es < EASY_MIN_ENEMIES) addBelchGuy();
					else if (Es < EASY_MAX_ENEMIES) addBelchGuy();
					break;
				case MEDIUM:
					if (Es < MED_MIN_ENEMIES) addBelchGuy();
					else if (Es < MED_MAX_ENEMIES) addBelchGuy();
					break;
				case HARD:
					if (Es < HARD_MIN_ENEMIES) addBelchGuy();
					else if (Es < HARD_MAX_ENEMIES) addBelchGuy();
					break;
				case INSANE:
					if (Es < INSANE_MIN_ENEMIES) addBelchGuy();
					else if (Es < INSANE_MAX_ENEMIES) addBelchGuy();
					break;
				}
				break;
				/** Level 3 is cheetos guy and belch guy */
			case THREE:
				switch (theDif_) {
				case EASY: 
					if (Es < EASY_MIN_ENEMIES) addRandomEnemy();
					else if (Es < EASY_MAX_ENEMIES) addRandomEnemy();
					break;
				case MEDIUM:
					if (Es < MED_MIN_ENEMIES) addRandomEnemy();
					else if (Es < MED_MAX_ENEMIES) addRandomEnemy();
					break;
				case HARD:
					if (Es < HARD_MIN_ENEMIES) addRandomEnemy();
					else if (Es < HARD_MAX_ENEMIES) addRandomEnemy();
					break;
				case INSANE:
					if (Es < INSANE_MIN_ENEMIES) addRandomEnemy();
					else if (Es < INSANE_MAX_ENEMIES) addRandomEnemy();
					break;
				}
				break;
				/** Boss level is the boss plus the other guys here and there */
			case BOSS:
				/** Build the boss if he's not already there */
				if (theBoss_ == null) createBoss();
				if (millis_ - enemiesLastUpdated_ > MIN_TIME_BETWEEN_NEW_ENEMY*2) {
					switch (theDif_) {
					case EASY: 
						if (Es < EASY_MIN_ENEMIES) addRandomEnemy();
						else if (Es < EASY_MAX_ENEMIES) addRandomEnemy();
						break;
					case MEDIUM:
						if (Es < MED_MIN_ENEMIES) addRandomEnemy();
						else if (Es < MED_MAX_ENEMIES) addRandomEnemy();
						break;
					case HARD:
						if (Es < HARD_MIN_ENEMIES) addRandomEnemy();
						else if (Es < HARD_MAX_ENEMIES) addRandomEnemy();
						break;
					case INSANE:
						if (Es < HARD_MIN_ENEMIES) addRandomEnemy();
						else if (Es < INSANE_MAX_ENEMIES) addRandomEnemy();
						break;
					}
				}
				break;
			}
		}

		/** Pop off enemies that are below the playing field */
		for (int i = 0; i < enemies_.size(); i++) {
			if (enemies_.get(i).getY() < -15) {
				enemies_.remove(i);
				i = i-1;
			}
		}
		/** Have Terry throw beer cans if it's time */
		if (theBoss_ != null 
				&& millis_ - theBoss_.lastThrowTime_ > theBoss_.getThrowInterval(theDif_)
				&& rand.nextFloat() < ENEMY_GEN_PROB
				&& theGameOverScreen_ == null) {
			theBoss_.lastThrowTime_ = millis_;
			theBoss_.throwCan(millis_, getAngleToQB());
		}

	}

	/** Adds one of two kinds of enemies each with the same chance of being added */
	private void addRandomEnemy() {
		if (rand.nextFloat()<.5f) addMunchGuy();
		else addBelchGuy();
	}
	/** Adds a belch guy to the enemies array list */
	private void addBelchGuy() {

		if (enemies_ != null && theField_ != null) {
			float x;
			if (theBoss_ != null) { 
				x = (theBoss_.getX()>(RIGHT_FIELD_LIMIT-LEFT_FIELD_LIMIT)/2)?LEFT_FIELD_LIMIT:RIGHT_FIELD_LIMIT;
			} else {
				/** Put the bad guy in the field */
				x = (RIGHT_FIELD_LIMIT-LEFT_FIELD_LIMIT)*(rand.nextFloat());
			}
			/** Find the first empty spot in enemies_ and plug in the new guy */
			enemies_.add( new BelchingEnemy(this, x,WORLD_HEIGHT*1.2f, 0, 0, theField_.getScrollRate(), theDif_));
		}
	}
	/** Adds a munching guy to the enemies array list */
	private void addMunchGuy() {

		if (enemies_ != null && theField_ != null) {
			float x;
			if (theBoss_ != null) { 
				/** If the boss is on the right side of the field, put the new guy on the left, otherwise, right */
				x = (theBoss_.getX()>(RIGHT_FIELD_LIMIT-LEFT_FIELD_LIMIT)/2)?LEFT_FIELD_LIMIT:RIGHT_FIELD_LIMIT;
			} else {
				/** Put the bad guy in the middle 90% of the field */
				x = (RIGHT_FIELD_LIMIT-LEFT_FIELD_LIMIT)*((rand.nextFloat()-.5f)*.9f+.5f);
			}
			/** Add that bad boy! */
			enemies_.add( new MunchingEnemy(this, x, WORLD_HEIGHT*1.2f, 0, 0, theField_.getScrollRate(), theDif_));

		}
	}
	/** Creates the boss and adds him to the enemies array list */
	private void createBoss() {
		/** Creates Boss and his Life Bar*/
		theBoss_ = new Boss(this, LEFT_FIELD_LIMIT+(RIGHT_FIELD_LIMIT-LEFT_FIELD_LIMIT)/2, .87f*WORLD_HEIGHT, 0, 0, theField_.getScrollRate(),theDif_);
		if (enemies_ != null) enemies_.add(theBoss_);
		bossBar_ = new BossLifeBar(this, RIGHT_FIELD_LIMIT + (WORLD_WIDTH-RIGHT_FIELD_LIMIT)/2, WORLD_HEIGHT/4,0,theBoss_.getLifePoints());
	}

	/** Ends the game by killing the qb and adding a game over screen */
	private void die() {
		/** Quarterback disappears and his stuff are destroyed */
		theQB_ = null;
		theArrow_ = null;
		theRing_ = null;


		/** If the boss is out, he laughs */
		if (theBoss_ != null) theBoss_.victoryLaugh(millis_);

		/** Start the game over screen */
		theGameOverScreen_ = new GameOverScreen(this, LEFT_FIELD_LIMIT+(RIGHT_FIELD_LIMIT-LEFT_FIELD_LIMIT)/8,
				4*WORLD_HEIGHT/8, RIGHT_FIELD_LIMIT-(RIGHT_FIELD_LIMIT-LEFT_FIELD_LIMIT)/8, 6*WORLD_HEIGHT/8, false);
	}

	public void draw() {
		millis_ = millis();


		pushMatrix();
		translate(0,WINDOW_HEIGHT);
		scale(WORLD_TO_PIXELS_SCALE,-WORLD_TO_PIXELS_SCALE);
		//scale(1,-1);

		/** I want it painted black */
		background(0);

		/** Update game status */
		updateStatus();

		/** Draw the Field */
		if (theField_ != null){
			theField_.update(millis_);
			theField_.draw();
		}


		/** Draw the Arrow */
		if (theArrow_ != null) {
			theArrow_.update(millis_);
			theArrow_.draw();
		}

		/** Draw the Enemies */
		if (enemies_ != null) {
			manageEnemies();
			for (int i = 0; i < enemies_.size(); i++) {
				if(enemies_.get(i).update(millis_)){
					enemies_.get(i).draw();
				} else {
					enemies_.remove(i);
					/** Tick the kill counter */
					if (theKillCounter_ != null) theKillCounter_.addKill();
					i--;
				}				
			}
		}

		/** Draw the Ring */
		if (theRing_ != null) {
			theRing_.update(millis_);
			theRing_.draw();
		}

		/** Draw the football */
		if (theFootball_ != null && theFootball_.isInPlay()) {
			theFootball_.update(millis_);
			theFootball_.draw();
		} else {
			theFootball_ = null;
		}
		/** Draw the Quarterback */
		if (theQB_ != null) {
			theQB_.update(millis_);
			theQB_.draw();
		}

		/**Draw the Boss Bar */
		if (bossBar_ != null) {
			bossBar_.draw();
		}

		/**Draw the QB Bar */
		if (qbBar_ != null) {
			qbBar_.draw();
		}

		/** Draw the kill counter */
		if (theKillCounter_ != null) {
			theKillCounter_.draw();
		}

		/**Draw the title screen */
		if (theTitleScreen_ != null) {
			theTitleScreen_.draw();
		}

		/** Draw the Game Over Screen */
		if (theGameOverScreen_ != null) {
			theGameOverScreen_.draw();
		}

		popMatrix();
	}


	/** Check collisions */
	private void checkCollisions() {
		if (enemies_ != null && theQB_ != null) {
			/** Enemies Hit by Ball*/
			if (theFootball_ != null) {
				float fx = theFootball_.getX();
				float fy = theFootball_.getY();
				float fz = theFootball_.getZ();

				//Cycle through the enemies and hit them if the ball is in there
				for (Enemy enemy : enemies_) {
					if (enemy != null) {
						if (enemy.isInside(fx, fy, fz)) {
							enemy.hit(millis_);
							if (enemy == theBoss_) {
								int bossLifePoints = theBoss_.getLifePoints();
								bossBar_.setLifePoints(bossLifePoints);
								/** Start the game over screen if the boss is dead*/
								if (bossLifePoints < 1) {
									theGameOverScreen_ = new GameOverScreen(this, LEFT_FIELD_LIMIT+(RIGHT_FIELD_LIMIT-LEFT_FIELD_LIMIT)/8,
											4*WORLD_HEIGHT/8, RIGHT_FIELD_LIMIT-(RIGHT_FIELD_LIMIT-LEFT_FIELD_LIMIT)/8, 6*WORLD_HEIGHT/8, true);
								}
							}
						}
					}
				}

			}

			/** QB Hit By Enemies */
			for (Enemy enemy : enemies_) {
				if (enemy != null) {
					for (Projectile proj : enemy.getProjectiles()) {
						if (theQB_.isInside(proj.getX(), proj.getY(), proj.getZ())) {
							proj.explode(millis_);
							theQB_.hit(millis_);
							qbBar_.setLifePoints(theQB_.getLifePoints());
						}
					}
				}
			}
		}

	}

	/** Build a football and set it into motion along a standard free fall trajectory 
	 * @param LRangle  "Left/Right" angle 
	 * @param TOangle  "Takeoff" angle
	 * @param initVelocity 
	 * @param xPos Where the football starts
	 * @param yPos 
	 */
	public void throwFootball(final float LRangle, final float TOangle,
			final float initVelocity, final float xPos, final float yPos) { 
		/** Tell the qb to throw the ball */
		theQB_.throwBall(millis_);

		/** Build the X Function */
		TrajectoryFunction xFcn = new TrajectoryFunction() {
			public float TrajFcn(float timeElapsed) {
				return (float) (initVelocity*Math.cos(TOangle)*Math.cos(LRangle)*timeElapsed+xPos); }};

				/** Build the Y Function */
				TrajectoryFunction yFcn = new TrajectoryFunction() {
					public float TrajFcn(float timeElapsed) {
						return (float) (initVelocity*Math.cos(TOangle)*Math.sin(LRangle)*timeElapsed+yPos); }};

						/** Build the Z Function, don't let the football be lower than the field*/
						TrajectoryFunction zFcn = new TrajectoryFunction() {
							public float TrajFcn(float timeElapsed) {
								/** Classic free fall trajectory without winds */
								return (float) (Math.max(-GRAVITY*timeElapsed*timeElapsed +
										initVelocity*Math.sin(TOangle)*timeElapsed + QB_HEIGHT,MINIMUM_FOOTBALL_ALTITUDE)); }};

										/** Build the Theta Function, constant rotation based on initial velocity of the pass */
										TrajectoryFunction thetaFcn = new TrajectoryFunction() {
											public float TrajFcn(float timeElapsed) {
												return (float) (ROT_FACTOR*initVelocity*timeElapsed); }};

												/** Build the football with the new functions, at the right position */
												theFootball_ = new Football(this, xPos, yPos, 0, LRangle, xFcn, yFcn, zFcn, thetaFcn);

	}

	/** Returns the angle from the boss to the qb */
	public float getAngleToQB() {
		if (theBoss_ != null && theQB_ != null) {
			return PApplet.atan2(theQB_.getY()-theBoss_.getY(), theQB_.getX()-theBoss_.getX());
		} else return 0;
	}
	/** Respond to user input */
	public void keyPressed() {
		switch (key) {
		//Test Keys
		case '1': theLev_ = Level.ONE; break;
		case '2': theLev_ = Level.TWO; break;
		case '3': theLev_ = Level.THREE; break;
		case '4': theLev_ = Level.BOSS; break;
		case 't': theBoss_.throwCan(millis_, getAngleToQB()); break;

		/** Build and Throw the football */
		case ' ': 
			/** Throw the football if there's a qb and the football is either dead or out of play */
			if (theQB_ != null && (theFootball_ == null || !theFootball_.isInPlay())) throwFootball(throwLRAngle_, throwTOAngle_, throwSpeed_, ballX_, ballY_);
			break;
		case 'z': 
			ballX_ = Math.max(ballX_ - LR_MOVE_CHANGE, LEFT_FIELD_LIMIT);
			if (theArrow_ != null) theArrow_.setX(ballX_);
			if (theRing_ != null) theRing_.setX(ballX_);
			if (theQB_ != null) theQB_.setX(ballX_);
			break;
		case 'x': 
			ballX_ = Math.min(ballX_ + LR_MOVE_CHANGE, RIGHT_FIELD_LIMIT);
			if (theArrow_ != null) theArrow_.setX(ballX_);
			if (theRing_ != null) theRing_.setX(ballX_);
			if (theQB_ != null) theQB_.setX(ballX_);
			break;
		case ENTER:
			/** Get the difficulty from the title screen if it exists */
			if (theTitleScreen_ != null ) {
				theDif_ = theTitleScreen_.getDifficulty();
				/** Then kill the title bar */
				theTitleScreen_ = null;
				playGame();
			} else if (theGameOverScreen_ != null && theGameOverScreen_.playAgainSelected()) {
				startNewGame();
				theGameOverScreen_ = null;
			} else if (theGameOverScreen_ != null && !theGameOverScreen_.playAgainSelected()) {
				/** Quit if the user chooses not to play again */
				System.exit(0);
			}
			break;
		case RETURN:
			/** Get the difficulty from the title screen if it exists */
			if (theTitleScreen_ != null ) {
				theDif_ = theTitleScreen_.getDifficulty();
				println("Got the difficulty " + theDif_);
				/** Then kill the old field and the title bar */
				theField_ = null;
				theTitleScreen_ = null;
				playGame();
			}
			break;
		case CODED:
			switch (keyCode) {
			case DOWN:
				/**Increase Angle*/
				/** Move selection on title screen */
				if (theTitleScreen_ != null) {
					theTitleScreen_.moveDown();
				} else if (theGameOverScreen_ != null) {
					/** Move down the game over screen */
					theGameOverScreen_.moveDown();
				} else { /** Or just move the TakeOff angle */
					throwTOAngle_ = Math.min(throwTOAngle_ + ANGLECHANGE, MAX_UP_THROW_ANGLE);
				} 
				break;
			case UP:
				if (theTitleScreen_ != null) {
					/** change the title screen selection*/
					theTitleScreen_.moveUp();
				} else if (theGameOverScreen_ != null) {
					theGameOverScreen_.moveUp();
				} else {
					/**Or change the takeoff angle */
					throwTOAngle_ = Math.max(throwTOAngle_ - ANGLECHANGE, MAX_DOWN_THROW_ANGLE);
				}
				break;
			case LEFT:
				/** Move Angle Left */
				throwLRAngle_ = Math.min(throwLRAngle_ + ANGLECHANGE, MAX_LEFT_THROW_ANGLE);
				if (theQB_ != null) theQB_.setLRAngle(throwLRAngle_);
				break;
			case RIGHT:
				/** Move Angle Right */
				throwLRAngle_ = Math.max(throwLRAngle_ - ANGLECHANGE, MAX_RIGHT_THROW_ANGLE);
				if (theQB_ != null) theQB_.setLRAngle(throwLRAngle_);
				break;
			case SHIFT:
				/** Powerup the Throw Velocity */
				throwSpeed_ = Math.min(throwSpeed_+POWERUP, FOOTBALL_MAX_SPEED);
				break;
			case ALT:
				/** Powerdown the Throw Velocity */
				throwSpeed_ = Math.max(throwSpeed_-POWERUP, DEFAULT_THROWSPEED);
				break;
			}

		}
		/** If any key is pressed, move the arrow and/or ring */
		if (theArrow_ != null) theArrow_.move(throwLRAngle_, throwTOAngle_, throwSpeed_);
		if (theArrow_ != null) theRing_.move(ballX_,ballY_,throwLRAngle_, throwTOAngle_, throwSpeed_);
	}

	/** Default throw variables */
	private void resetThrow() {
		throwLRAngle_ = DEFAULT_LRANGLE;
		throwTOAngle_ = DEFAULT_TOANGLE;
		throwSpeed_ = DEFAULT_THROWSPEED;
	}
}
