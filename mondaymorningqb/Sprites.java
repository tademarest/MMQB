package mondaymorningqb;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;

public class Sprites {
	public static final ArrayList<PImage> belchingSequence_= new ArrayList<PImage>(12);
	public static final ArrayList<PImage> munchingSequence_ = new ArrayList<PImage>(15);
	public static final ArrayList<PImage> burningSequence_ = new ArrayList<PImage>(3);
	
	Sprites(PApplet theApp_) {

		final PImage[] belches_ =  {				
				theApp_.loadImage("en2_1.png"), 
				theApp_.loadImage("en2_2.png"),
				theApp_.loadImage("en2_3.png"),
				theApp_.loadImage("en2_4.png"),
				theApp_.loadImage("en2_4.png"),
				theApp_.loadImage("en2_4.png"),
				theApp_.loadImage("en2_5.png"),
				theApp_.loadImage("en2_6.png"),
				theApp_.loadImage("en2_7.png"),
				theApp_.loadImage("en2_7.png"),
				theApp_.loadImage("en2_5.png"),
				theApp_.loadImage("en2_1.png")

		};
		/** Load the Belching Sequence */
		for (int i = 0; i < belches_.length; i++) {
			belchingSequence_.add(belches_[i]);
		}



		final PImage[] munches_ = { 				theApp_.loadImage("en1_1.png"),
				theApp_.loadImage("en1_2.png"),
				theApp_.loadImage("en1_3.png"),
				theApp_.loadImage("en1_4.png"),
				theApp_.loadImage("en1_5.png"),
				theApp_.loadImage("en1_6.png"),
				theApp_.loadImage("en1_7.png"),
				theApp_.loadImage("en1_8.png"),
				theApp_.loadImage("en1_7.png"),
				theApp_.loadImage("en1_8.png"),
				theApp_.loadImage("en1_7.png"),
				theApp_.loadImage("en1_8.png"),
				theApp_.loadImage("en1_7.png"),
				theApp_.loadImage("en1_2.png"),
				theApp_.loadImage("en1_1.png")
		};
		/** Load the Munching Sequence */
		for (int i = 0; i < munches_.length; i++) {
			munchingSequence_.add(munches_[i]);
		}
		
		/** Load the Burning Sequence */
		burningSequence_.add(theApp_.loadImage("fire_1.png"));
		burningSequence_.add(theApp_.loadImage("fire_2.png"));
		burningSequence_.add(theApp_.loadImage("fire_3.png"));
	}
}
