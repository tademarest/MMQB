package mondaymorningqb;

import processing.core.PApplet;

/** Indicator ring for the first level shows where the 
 * ball will land with the given inputs. Pulsates between
 * two colors for heightened visibiility.
 * @author ty
 *
 */
public class Ring extends Drawable {
	private PApplet theApp_;
	/** The color of the ring, changes with time */
	private float r_, g_, w_;
	
	/** Where the ball would be thrown from, different from drawing origin */
	private float launchX_, launchY_;
	
	/** The ring is mostly blue, so this doesn't change */
	private final float b_ = BLUE_VALUE;
	
	Ring(PApplet app, float x, float y, float z, float t, 
			float LRangle, float TOangle, float throwSpeed) {
		super(app, x, y, z, t);
		theApp_ = app;
		launchX_ = x;
		launchY_ = y;
		
		move(launchX_,launchY_,LRangle, TOangle, throwSpeed);
	}

	
	public void draw() {
		theApp_.pushMatrix();
		theApp_.translate(getX(),getY());
		
		theApp_.stroke(r_,g_,b_);
		theApp_.strokeWeight(w_);
		theApp_.noFill();
		theApp_.ellipse(0, 0, RING_SIZE, RING_SIZE);
		
		
		theApp_.popMatrix();
		
	}

	/** Update changes the color and weight of the circle as 
	 * well as changes the coordinates of the ring based
	 * @param timeElapsed
	 * @return
	 */
	public boolean update(float timeElapsed) {
		float rgValue = (float) Math.sin(timeElapsed/500);
		r_ = rgValue * 255;
		g_ = rgValue * 255;
		w_ = (float) Math.abs(MAX_RING_WEIGHT*(rgValue));
		
		return true;
	}

	@Override
	public boolean isInside(float x, float y, float z) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/** Moves the origin based on the maximum solution to the quadratic
	 * equation given by the free fall formula
	 * @param LRangle
	 * @param TOangle
	 * @param strength
	 * @return a boolean in case we want to return an "inPlay" type variable
	 */
	public boolean move(float launchX, float launchY, float LRangle, float TOangle, float initVel) {
		launchX_ = launchX;
		launchY_ = launchY;
		
		float a = -GRAVITY;
		float b = (float) (Math.sin(TOangle)*initVel);
		float c = QB_HEIGHT;
		/** Solution to when the football will land on the ground, predicates x,y of ring */
		float timeAtImpact = (float) ((-b-Math.sqrt(b*b-4*a*c))/(2*a));
		float cosTO = (float) Math.cos(TOangle);
		float newX = (float) (initVel*cosTO*Math.cos(LRangle)*timeAtImpact) + launchX_;
		float newY = (float) (initVel*cosTO*Math.sin(LRangle)*timeAtImpact) + launchY_;
		
		setX(newX);
		setY(newY);

		return true;
	}

}
