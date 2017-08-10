package mondaymorningqb;

import processing.core.PApplet;
import processing.core.PImage;

public class TitleScreen extends Drawable {
	private PApplet theApp_;
	private int boxSelected_;
	/**Where the main title box should stop */
	private final float endX_, endY_; 
	private final PImage titleImage_;
	
	TitleScreen(PApplet app, float x, float y, float endX, float endY) {
		super(app, x, y, endX, endY);
		theApp_ = app;
		boxSelected_ = 1; //Medium is the default box selected
		endX_ = endX;
		endY_ = endY;
		titleImage_ = theApp_.loadImage("title.png");
		
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
		theApp_.scale(TITLE_IMAGE_SCALE_FACTOR,-TITLE_IMAGE_SCALE_FACTOR);
		theApp_.image(titleImage_, -titleImage_.width/2, -titleImage_.height/2, titleImage_.width, titleImage_.height);
		theApp_.popMatrix();
		
		//Draw Text Options: 
		theApp_.stroke(TITLE_FONT_RGB[0],TITLE_FONT_RGB[1],TITLE_FONT_RGB[2]);
		theApp_.fill(TITLE_FONT_RGB[0],TITLE_FONT_RGB[1],TITLE_FONT_RGB[2]);
		theApp_.textSize(TITLE_FONT_SIZE);
		
		for (int i = 0; i < DIFFICULTY_STRINGS.length; i++) {
			theApp_.pushMatrix();
			theApp_.translate((endX_-getX())/4, (5-i)*(endY_-getY())/8);
			theApp_.scale(1,-1);
			theApp_.text(DIFFICULTY_STRINGS[i], 0, 0);
			theApp_.popMatrix();
		}
		
		//Draw the selector Box
		theApp_.stroke(255);
		theApp_.strokeWeight(2);
		theApp_.noFill();
		theApp_.translate((endX_-getX())/4, (5-boxSelected_)*(endY_-getY())/8);
		theApp_.rect(-TITLE_FONT_SIZE/6,-TITLE_FONT_SIZE/6, .7f*TITLE_FONT_SIZE*DIFFICULTY_STRINGS[boxSelected_].length(), TITLE_FONT_SIZE);
		
		theApp_.popMatrix();
	}


	public boolean update(float timeElapsed) {
		// TODO Auto-generated method stub
		return false;
	}

	/** Not Used */
	public boolean isInside(float x, float y, float z) {
		return false;
	}

	/** Moving the box down selects a harder difficulty*/
	public void moveDown() {
		if (boxSelected_ < 3) {
			boxSelected_++;
		}
	}

	/** Moving the box up selects an easier difficulty*/
	public void moveUp() {
		if (boxSelected_ > 0) {
			boxSelected_--;
		}
	}
	
	/** Gets the difficulty based on what box is currently 
	 * selected */
	public Difficulty getDifficulty() {
		switch(boxSelected_) {
		case 0: return Difficulty.EASY;
		case 1: return Difficulty.MEDIUM;
		case 2: return Difficulty.HARD;
		case 3: return Difficulty.INSANE;
		default: return Difficulty.MEDIUM;
		}
	}

}
