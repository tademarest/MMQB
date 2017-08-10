package mondaymorningqb;

import processing.core.PApplet;
import processing.core.PImage;

public class GameOverScreen extends Drawable {
	private PApplet theApp_;
	private int boxSelected_;
	/**Where the main title box should stop */
	private final float endX_, endY_; 
	private final PImage gameOverImage_;

	GameOverScreen(PApplet app, float x, float y, float endX, float endY, boolean won) {
		super(app, x, y, endX, endY);
		theApp_ = app;
		boxSelected_ = 0; //Yes is default
		endX_ = endX;
		endY_ = endY;
		if (won) gameOverImage_ = theApp_.loadImage("victory.png");
		else gameOverImage_ = theApp_.loadImage("game_over.png");
	}

	@Override
	public void draw() {
		theApp_.pushMatrix();
		theApp_.translate(getX(), getY());
		
		// Draw the main box
		theApp_.noStroke();
		theApp_.fill(0);
		theApp_.rect(0,0,endX_-getX(),endY_-getY(), 10);
		
		//Draw the title image
		theApp_.pushMatrix();
		theApp_.translate((endX_-getX())/2,7*(endY_-getY())/8);
		theApp_.scale(GAME_OVER_IMAGE_SCALE_FACTOR,-GAME_OVER_IMAGE_SCALE_FACTOR);
		theApp_.image(gameOverImage_, -gameOverImage_.width/2, -gameOverImage_.height/2, gameOverImage_.width, gameOverImage_.height);
		theApp_.popMatrix();
		
		//Draw Text Options: 
		theApp_.stroke(TITLE_FONT_RGB[0],TITLE_FONT_RGB[1],TITLE_FONT_RGB[2]);
		theApp_.fill(TITLE_FONT_RGB[0],TITLE_FONT_RGB[1],TITLE_FONT_RGB[2]);
		theApp_.textSize(TITLE_FONT_SIZE);
		theApp_.translate((endX_-getX())/4, 9*gameOverImage_.height/32);
		
		for (int i = 0; i < GAME_OVER_STRINGS.length; i++) {
			theApp_.pushMatrix();
			theApp_.translate(0,- (i)*(endY_-getY())/3);
			theApp_.scale(1,-1);
			theApp_.text(GAME_OVER_STRINGS[i], 0, 0);
			theApp_.popMatrix();
		}
		
		//Draw the selector Box
		theApp_.stroke(255);
		theApp_.strokeWeight(2);
		theApp_.noFill();
		theApp_.translate(0, (-boxSelected_)*(endY_-getY())/3);
		theApp_.rect(-1.1f*TITLE_FONT_SIZE/6,-1.1f*TITLE_FONT_SIZE/6, .54f*TITLE_FONT_SIZE*GAME_OVER_STRINGS[boxSelected_].length(), 1.05f*TITLE_FONT_SIZE);
		
		theApp_.popMatrix();
		
	}

	/** Not Used */
	public boolean update(float timeElapsed) {
		return false;
	}

	/** Not used */
	public boolean isInside(float x, float y, float z) {
		return false;
	}

	public boolean playAgainSelected() {
		return boxSelected_ == 0;
	}

	/** Moving the box up selects playagain*/
	public void moveUp() {
		if (boxSelected_ > 0) {
			boxSelected_--;
		}
	}
	/** Moving the box down selects quit*/
	public void moveDown() {
		if (boxSelected_ < 1) {
			boxSelected_++;
		}

	}

}
