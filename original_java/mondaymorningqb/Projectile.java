package mondaymorningqb;

import processing.core.PApplet;

public abstract class Projectile extends Drawable {
	private TrajectoryFunction XFcn_;
	private TrajectoryFunction YFcn_;
	private TrajectoryFunction ZFcn_;
	private TrajectoryFunction TFcn_;

	Projectile(PApplet app, float x, float y, float z, float t, TrajectoryFunction XFcn, 
			TrajectoryFunction YFcn, TrajectoryFunction ZFcn, TrajectoryFunction TFcn) {
		super(app, x,y,z,t);
		XFcn_ = XFcn;
		YFcn_ = YFcn;
		ZFcn_ = ZFcn;
		TFcn_ = TFcn;
	}
	
	public abstract void explode(float timeNow);

	public TrajectoryFunction getXFcn_() {
		return XFcn_;
	}

	public TrajectoryFunction getYFcn_() {
		return YFcn_;
	}

	public TrajectoryFunction getZFcn_() {
		return ZFcn_;
	}

	public TrajectoryFunction getTFcn_() {
		return TFcn_;
	}
	
}
