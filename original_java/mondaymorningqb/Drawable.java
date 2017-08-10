package mondaymorningqb;

import processing.core.PApplet;

public abstract class Drawable implements Constants {
	private float x_orig, y_orig, z_orig, rot_theta;
	private PApplet theApp_;
	
	
	public PApplet getTheApp_() {
		return theApp_;
	}

	Drawable(PApplet app, float x, float y, float z, float t) {
		theApp_ = app;
		x_orig = x; y_orig = y; z_orig = z; rot_theta = t;
	}
	
	/** The primary purpose is to ensure all Drawable 
	 * objects have a draw method.
	 */
	public abstract void draw();
	
	/** update() will take in the amount of time since the last
	 * draw, and change coordinates and other states to reflect
	 * the amount of time passing. Then it will return true if
	 * the object is still in "play" ie above ground and on the field
	 * Otherwise, false will indicate that it must be removed from 
	 * the context.
	 * @param timeSinceLast
	 * @return
	 */
	public abstract boolean update(float timeElapsed);
	
	/** All drawables must detect collisions
	 * @param x test coordinates
	 * @param y
	 * @param z
	 * @return true or false depending on if the coord is inside
	 */
	public abstract boolean isInside(float x,float y,float z);

	public float getX() {
		return x_orig;
	}
	public float getY() {
		return y_orig;
	}
	public float getZ() {
		return z_orig;
	}
	public float getTheta() {
		return rot_theta;
	}
	
	public void setX(float x) {
		x_orig = x;
	}
	public void setY(float y) {
		y_orig = y;
	}
	public void setZ(float z) {
		z_orig = z;
	}
	public void setTheta(float t) {
		rot_theta = t;
	}

}
