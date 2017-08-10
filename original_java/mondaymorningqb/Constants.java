package mondaymorningqb;

public interface Constants {
	
	int WINDOW_WIDTH = 600;
	int WINDOW_HEIGHT = 600;

	/**World width is a specification outlined by the assignment
	 */
	float WORLD_WIDTH = 160f * 1.4f;
	float LEFT_FIELD_LIMIT = 160f * .2f;
	float RIGHT_FIELD_LIMIT = 160f * 1.2f;
	int FRAME_RATE = 1000;
	
	/** World-to-pixels-scale maintains aspect ratio as well
	 * as changing the units to pixels
	 * The reverse is a multiplicative inverse operation
	 */
	float WORLD_TO_PIXELS_SCALE = WINDOW_WIDTH/WORLD_WIDTH;
	float PIXELS_TO_WORLD_SCALE = 1.0f/WORLD_TO_PIXELS_SCALE;
	
	float WORLD_HEIGHT = WINDOW_HEIGHT*PIXELS_TO_WORLD_SCALE;
	
	/** Force of gravity in feet/sec^2
	 * used to create the right physics for the footbal trajectory
	 */
	float GRAVITY = 32.174f;

	/** Quarterback height in feet (because the trajectory of the ball begins
	 * roughly at how tall the qb is)
	 */
	float QB_HEIGHT = 6f;
	
	/**Adjusts rotation speed in radians/second as a function of the initial velocity
	 * and time elapsed*/
	float ROT_FACTOR = 0.5f;
	
	/** Determines how the ball is scaled when it has an altitude greater than zero*/
	float VIEWING_ALTITUDE = 150f;
	
	/** Determines how the ball is scaled when it has an altitude greater than zero*/
	float FOOTBALL_PRESCALE = 0.1f;
	float POWERUP = 3f; 
	float ANGLECHANGE = (float) (Math.PI/40);
	float LR_MOVE_CHANGE = 3f;
	
	/** Boundaries on where the ball can be thrown */
	float MAX_LEFT_THROW_ANGLE = (float) Math.PI;
	float MAX_RIGHT_THROW_ANGLE = 0;
	float MAX_UP_THROW_ANGLE = (float) Math.PI/2;
	float MAX_DOWN_THROW_ANGLE = 0;
	
	/** Default throw variables */
	float DEFAULT_LRANGLE = (float) (Math.PI/2);
	float DEFAULT_TOANGLE = (float) (Math.PI/4);
	float DEFAULT_THROWSPEED = 30f;
	/** Maximum Velocity of the Throw*/
	float FOOTBALL_MAX_SPEED = 120f;
	/** Altitude at which to start explosion sequence */
	float EXPLOSION_ALTITUDE = 2f;
	/** Minimum altitudes for the football to be at */
	float MINIMUM_FOOTBALL_ALTITUDE = 0f;
	float EXPLOSION_SCALE_FACTOR = 25;
	float MINIMUM_FOOTBALL_Y = 0;
	float EXPLOSION_DURATION = .5f; //seconds
	
	
	/** Arrow Parameters */
	float ARROW_WEIGHT = 1f;
	
	/** Ring Parameters */
	float RING_SIZE = 10f;
	float MAX_RING_WEIGHT = 2f;
	float BLUE_VALUE = 255f;
	
	/** Enemy Generation Values */
	int EASY_MIN_ENEMIES = 1, EASY_MAX_ENEMIES = 3;
	int MED_MIN_ENEMIES = 2, MED_MAX_ENEMIES = 4;
	int HARD_MIN_ENEMIES = 3, HARD_MAX_ENEMIES = 6;
	int INSANE_MIN_ENEMIES = 5, INSANE_MAX_ENEMIES = 10;
	float ENEMY_GEN_PROB = .01f;//500/FRAME_RATE;
	int ENEMY_UPDATE_RATE = FRAME_RATE/8;
	float FIRE_TIME = 3*1000;//ms for enemy to be on fire till he dies out
	float MIN_TIME_BETWEEN_NEW_ENEMY = 5f*1000;//ms
	
	/** Football Field Parameters */
	float TURF_R = 34, TURF_G = 143, TURF_B = 29;
	float YARD_LINE_THICKNESS = 2f;
	// 3 feet per yard, 1 line marker every 5 yards, 1.5 to spread them out visually
	float FEET_BETWEEN_MARKERS = 3*5*1.8f;
	int YARD_LINES_ON_FIELD = (int) (WORLD_HEIGHT/FEET_BETWEEN_MARKERS) + 1; 
	float DEFAULT_SCROLL_RATE = 3f; //feet per second
	float E_SR = 1f, M_SR = 2f, H_SR = 3f, I_SR = 4f;
	
	/**  Messages */
	String START_MESSAGE = "Destroy the MMQBs!";
	String HUNDRED_YARD_MESSAGE = "Keep Going!";
	String TERRY_DEFAULT_MESSAGE = "You Suck";
	String TERRY_DYING_MESSAGE = "I Quit...";
	
	/** QB Values */
	float QB_SCALE_FACTOR = .65f;
	float QB_THROW_DURATION = .5f*1000; //Milliseconds
	float DOWN_MARKER_SCALE_FACTOR = .5f;
	float TIME_BETWEEN_HITS = (.25f + EXPLOSION_DURATION)*1000; //ms
	
	/** Boss values */
	float TERRY_DEFAULT_XSPEED = .06f;
	float TERRY_THROW_DURATION = 1.5f*1000; //Milliseconds
	float TERRY_SCALE_FACTOR = .25f;
	float TERRY_DYING_DURATION = 3f*1000; //ms
	float TERRY_LAUGH_DURATION = .5f*1000; //ms
	float TERRY_THROW_SPEED = DEFAULT_THROWSPEED*5;
	
	/** Bad Guy Values */
	float MUNCH_GUY_SCALE_FACTOR = .12f;
	float BELCH_GUY_SCALE_FACTOR = .09f;
	/** How fast to change sprites */
	float CHANGE_SPRITE_INTERVAL = .15f*1000; //ms //Used for Munch GUy
	float CHANGE_BELCH_INTERVAL = .18f*1000;
	
	/** Cheeto Values */
	float DEFAULT_CHEETO_SPEED = DEFAULT_THROWSPEED*1.3f;
	int CHEETOS_PER_CHEW = 6;
	float CHEETO_EXPLOSION_SCALE_FACTOR = EXPLOSION_SCALE_FACTOR*.6f;
	float CHEETO_PRESCALE = .05f;
	int CHEETOS_EASY = 1, CHEETOS_MEDIUM = 2, CHEETOS_HARD = 4, CHEETOS_INSANE = 5;
	
	/** Belch Bubble Values */
	float BUBBLE_SCALE = .007f;
	float BUBBLE_EXPLOSION_SCALE_FACTOR = CHEETO_EXPLOSION_SCALE_FACTOR*.22f;
	float DEFAULT_BUBBLE_SPEED = DEFAULT_CHEETO_SPEED*.2f;
	float DEFAULT_BUBBLE_SIZE = 7f;
	float BUBBLE_LIFETIME = 14f*1000; //ms
	int[] BELCH_REDS = {255, 249, 231, 149, 46, 1, 0};
	int[] BELCH_GREENS = {255, 255, 244, 255, 215, 110, 35};
	int[] BELCH_BLUES = {140, 45, 17, 88, 0, 1, 0};
	int BUBBLES_EASY = 2;
	int BUBBLES_MEDIUM = 4;
	int BUBBLES_HARD = 6;
	int BUBBLES_INSANE = 8;
	
	/** Beercan values */
	float BEERCAN_PRESCALE = .05f;
	int CANS_EASY = 1;
	int CANS_MEDIUM = 3;
	int CANS_HARD = 5;
	int CANS_INSANE = 10;
	
	/** Life Points for each Enemy and Difficulty */
	int BOSS_LP_EASY = 2;
	int BOSS_LP_MEDIUM = 4;
	int BOSS_LP_HARD = 8;
	int BOSS_LP_INSANE = 15;
	int ENEMY_LP_EASY = 1;
	int ENEMY_LP_MEDIUM = 1;
	int ENEMY_LP_HARD = 2;
	int ENEMY_LP_INSANE = 3;
	/** Boss Throw Intervals */
	float BOSS_TI_EASY = 3f*1000;
	float BOSS_TI_MEDIUM = 2f*1000;
	float BOSS_TI_HARD = 1.5f*1000;
	float BOSS_TI_INSANE = 1.5f*1000;
	
	/**Title Image Variables */
	float TITLE_IMAGE_SCALE_FACTOR = 0.13f;
	String EASY_STRING = "EASY";
	String MEDIUM_STRING = "MEDIUM";
	String HARD_STRING = "HARD";
	String INSANE_STRING = "INSANE";
	String DIFFICULTY_STRINGS[] = {"Easy","Medium","Hard","Insane"};
	int TITLE_FONT_SIZE = 13;
	int TITLE_FONT_RGB[] = {6, 154, 6};
	
	/** Game Over Variables */
	String GAME_OVER_STRINGS[] = {"Play Again", "Quit"};
	float GAME_OVER_IMAGE_SCALE_FACTOR = .13f;
	
	/** Game Status Variable */
	int LEVEL_ONE_YARDS = 100;
	int LEVEL_TWO_YARDS = 200;
	int LEVEL_THREE_YARDS = 300;
	
	String LEVEL_ONE_COMPLETE_MESSAGE = "Level 1 Complete!";
	String LEVEL_TWO_COMPLETE_MESSAGE = "Level 2 Complete!";
	String LEVEL_THREE_COMPLETE_MESSAGE = "Level 3 Complete!";
	String BOSS_LEVEL_START_MESSAGE = "Destroy Terry Bradshaw!";
	String TERRY_VICTORY_MESSAGE = "You Lose!";
	
	/**Kill Counter*/
	int[] ORANGE_RGB = {243, 132, 0};
	
	
	/** This interface is passed 
	 * to the Projectile classes as a way to send a function as a parameter */
	public interface TrajectoryFunction {
		float TrajFcn(float x);
	}

}