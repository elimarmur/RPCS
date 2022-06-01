package engine.physics.hitbox;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import engine.maths.Vector3f;

public class SphereHitBox extends HitBox{

	private float radius;

	public SphereHitBox(Vector3f origin, Vector3f rotation, float radius) {
		super(origin, rotation);
		this.radius = radius;
	}
	public SphereHitBox(Vector3f origin, Vector3f rotation, String filepath) {
		super(origin, rotation);
		try {
			loadFromFile(filepath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	@Override
	public void loadFromFile(String filepath) throws FileNotFoundException{
		
		File file = new File(filepath);
	    Scanner sc = new Scanner(file);
	 
	    while (sc.hasNextLine()) {
	    	String nextLine = sc.nextLine();
	    	if (nextLine.startsWith("r")) {
	    		radius = Float.parseFloat(nextLine.substring(1).strip());
	    	}
	      
	      }
	    sc.close();
		
	}
	public float findLowestSpot() {
		return origin.getY() - radius; 
	}
	public void setLowestSpot(float f) {
		this.origin.setY(f + radius);
	}
	@Override
	public float offset() {
		return radius;
	}
	public boolean areColliding(HitBox hitbox) {
		return (Vector3f.distance(hitbox.origin, this.origin) <= (hitbox.offset() + this.offset()));
	}



	
}
