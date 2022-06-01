package engine.physics;

import engine.maths.Vector2f;
import engine.maths.Vector3f;
import engine.physics.hitbox.HitBox;
import engine.physics.hitbox.SphereHitBox;
import engine.terrains.Terrain;

public class RigidBody {

	public static final float START_POS_X = 135;
	public static final float START_POS_Z = 10;
	public static final float MAX_HORIZONTAL_VELOCITY = 50;
	public static final float MAX_ROTATIONAL_VELOCITY = 50;
	public static final float GRAVITY = -80;
	private static final float TICK_TIME = 1 / (float) Calculator.TICK_RATE;
	private static final float SLOW_FACTOR = 1;
	
	private static final float TRACK_LIMITS_OUT = 12, TRACK_LIMITS_IN = 20;

	public boolean win_status = false;
	
	private HitBox hitbox;

	private boolean isStatic;
	private Vector3f position;
	private Vector3f rotation;
	private Vector3f lateralVelocity;
	private Vector3f rotationalVelocity;
	private Vector3f accelaration;
	private float inverseMass;

	private long start, end;

	private Terrain terrain;

	private boolean isOnGround = false;

	private Vector3f constantAccelaration;

	public RigidBody(Vector3f position, Vector3f rotation, float mass, boolean isStatic) {
		this.position = position;
		this.rotation = rotation;
		this.inverseMass = 1f / mass;
		this.isStatic = isStatic;
		this.lateralVelocity = new Vector3f(0, 0, 0);
		this.rotationalVelocity = new Vector3f(0, 0, 0);
		this.constantAccelaration = new Vector3f(0, GRAVITY, 0);
		this.accelaration = new Vector3f(constantAccelaration);
	}

	public void update(Terrain terrain) {
		start = System.currentTimeMillis();

		this.terrain = terrain;

		calculateVelocity();
		calculatePosition();
		terrainCollisionDetection(terrain);
		checkOutsideLimits();
		checkInsideLimits();
		calculateFriction();
		win_status = didCrossFinishLine();

		end = System.currentTimeMillis();
		float delta = deltaSecs();
		while (delta < TICK_TIME) {
			end = System.currentTimeMillis();
			delta = deltaSecs();
		}
	}

	public static void collide(RigidBody body1, RigidBody body2) {
		if (body1.hitbox.areColliding(body2.hitbox)) {
			calculateCollision(body1, body2);
			System.out.println("COLLISION!");
		}
	}

	private static void calculateCollision(RigidBody body1, RigidBody body2) {
		Vector3f vel = body1.lateralVelocity;
		body1.lateralVelocity = body2.lateralVelocity;
		body2.lateralVelocity = vel;
	}

	private void terrainCollisionDetection(Terrain terrain) {
		float terrainHeight = terrain.getHeightOfTerrain(position.getX(), position.getZ());
		if (hitbox.findLowestSpot() < terrainHeight) {
			hitbox.setLowestSpot(terrainHeight);
			if (lateralVelocity.getY() < 0)
				lateralVelocity.setY(0);
			isOnGround = true;
		} else {
			isOnGround = false;
		}
	}

	private float deltaSecs() {
		return (end - start) / 1000f;
	}

	private void calculateVelocity() {
		lateralVelocity.add(0, TICK_TIME * SLOW_FACTOR * accelaration.getY(), 0);
		if (horizontalVelocity() <= MAX_HORIZONTAL_VELOCITY && nextHorizontalVelocity() <= MAX_HORIZONTAL_VELOCITY) {
			lateralVelocity.add(TICK_TIME * SLOW_FACTOR * accelaration.getX(), 0,
					TICK_TIME * SLOW_FACTOR * accelaration.getZ());
		}
		if (nextHorizontalVelocity() > MAX_HORIZONTAL_VELOCITY) {
			float x = (lateralVelocity.getX() * MAX_HORIZONTAL_VELOCITY) / horizontalVelocity();
			float z = (lateralVelocity.getZ() * MAX_HORIZONTAL_VELOCITY) / horizontalVelocity();
			lateralVelocity.setX(x);
			lateralVelocity.setZ(z);
		}
		
	}

	public void setRotationalVelocity(Vector3f vector) {
		rotationalVelocity.set(vector.getX(), vector.getY(), vector.getZ());
	}

	private void calculatePosition() {
		position.add(lateralVelocity.getX() * TICK_TIME, lateralVelocity.getY() * TICK_TIME,
				lateralVelocity.getZ() * TICK_TIME);
	}


	private void calculateFriction() {
		
		if (isOnGround) {
			Vector2f velocity = Vector2f.geoToCoords(horizontalVelocity(), rotation.getY());
			lateralVelocity.setX(velocity.getX());
			lateralVelocity.setZ(velocity.getY());
		}
		
	}

	public void checkInsideLimits() {
		float limit = Terrain.getSize() / 2 - TRACK_LIMITS_IN;
		float offset = hitbox.offset();
		float x = position.getX();
		float z = position.getZ();
		
		float x_distance = Math.abs(x);
		float z_distance = Math.abs(z);
		// inside borders
		
		if (x_distance - offset < limit && z_distance - offset < limit) {
			position.setX(START_POS_X);
			position.setZ(START_POS_Z);
			rotation.setY(0);
		}

		
	}
	
	public void checkOutsideLimits() {
		float limit = Terrain.getSize() / 2 - TRACK_LIMITS_OUT;
		float offset = hitbox.offset();
		float reset = offset + 0.1f;
//		 outside borders
		if (position.getX() + offset > limit) {
			position.setX(limit - reset);
			lateralVelocity.setX(0);
			};
		if (position.getX() -offset < -limit) {
			position.setX(-limit + reset);
			lateralVelocity.setX(0);
		}
		if (position.getZ() + offset > limit) {
			position.setZ(limit - reset);
			lateralVelocity.setZ(0);
		}
		if (position.getZ() - offset < -limit) {
			position.setZ(-limit + reset);
			lateralVelocity.setZ(0);
		}
	}
	
	public boolean didCrossFinishLine() {
		float offset = hitbox.offset();
		if (position.getX() + offset > 0 && position.getZ() + offset >= -4 && position.getZ() + offset <= -2 && (rotation.getY() < 90 && rotation.getY() > -90)) {
			return true;
		}
		return false;
	}
	
	public void applyForce(Vector3f force) {
		accelaration.add(force.getX() * inverseMass, force.getY() * inverseMass, force.getZ() * inverseMass);
		calculateVelocity();
		accelaration.add(-force.getX() * inverseMass, -force.getY() * inverseMass, -force.getZ() * inverseMass);

		isOnGround = false;
	}

	public boolean isStatic() {
		return isStatic;
	}

	public boolean isInAir() {
		if (terrain == null) {
			return false;
		}
		float terrainHeight = terrain.getHeightOfTerrain(position.getX(), position.getZ());
		return (hitbox.findLowestSpot() > terrainHeight);
	}

	public boolean canAirRoll() {
		if (terrain == null) {
			return false;
		}
		float terrainHeight = terrain.getHeightOfTerrain(position.getX(), position.getZ());
		return (hitbox.findLowestSpot() > terrainHeight + hitbox.offset());
	}

	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}

	public void setMass(float mass) {
		this.inverseMass = 1 / mass;
	}

	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public Vector3f getVelocity() {
		return lateralVelocity;
	}

	public Vector3f getAccelaration() {
		return accelaration;
	}

	public float getHorizontalAccelaration() {
		return (float) Math.sqrt(accelaration.getX() * accelaration.getX() + accelaration.getZ() * accelaration.getZ());
	}

	public float getMass() {
		return 1 / inverseMass;
	}

	public boolean isOnGround() {
		return isOnGround;
	}

	public void setSphereHitbox(String filepath) {
		this.hitbox = new SphereHitBox(position, rotation, filepath);
	}

	public void setSphereHitbox(float radius) {
		this.hitbox = new SphereHitBox(position, rotation, radius);
	}

	public static boolean canCollide(RigidBody body1, RigidBody body2) {
		if (body1.hitbox == null || body2.hitbox == null)
			return false;
		return Vector3f.distance(body1.position, body2.position) <= (body1.hitbox.offset() + body2.hitbox.offset());
	}

	public boolean isMoving() {
		return (Math.abs(lateralVelocity.getX()) + Math.abs(lateralVelocity.getZ()) > 0);
	}

	private float horizontalVelocity() {
		return (float) Math.sqrt(
				lateralVelocity.getX() * lateralVelocity.getX() + lateralVelocity.getZ() * lateralVelocity.getZ());
	}
	private float nextHorizontalVelocity() {
		float x = lateralVelocity.getX() + TICK_TIME * SLOW_FACTOR * accelaration.getX();
		float z = lateralVelocity.getZ() + TICK_TIME * SLOW_FACTOR * accelaration.getZ();
		return (float) Math.sqrt(x * x + z * z);
	}
	public float horizontalDirectionAngle() {
		if (lateralVelocity.getX() == 0) return rotation.getY();
		if (lateralVelocity.getZ() == 0) return rotation.getY();
		return (float) Math.toDegrees(Math.atan(lateralVelocity.getX() / lateralVelocity.getZ()));
	}

	public void cleanUp() {
		position.set(0,0,0);
		lateralVelocity.set(0,0,0);
		rotation.set(0,0,0);
		
	}
}
