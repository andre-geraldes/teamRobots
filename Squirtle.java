package sample;
import robocode.*;
import java.awt.Color;
import java.awt.Point;
import static robocode.util.Utils.normalRelativeAngleDegrees;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * Squirtle - a robot by (your name here)
 */
public class Squirtle extends TeamRobot implements Droid
{
	private boolean boss = false;
	private boolean subboss = false;
	
	public void run() {
		setColors(Color.black,Color.black,Color.black); // body,gun,radar
		setBulletColor(Color.blue);

		// Robot main loop
	}

	/**
	 * onMessageReceived:  What to do when our leader sends a message
	 */
	public void onMessageReceived(MessageEvent e) {
		// Fire at a point
		if (e.getMessage() instanceof Point) {
			System.out.println("Coordenadas recebidas");
			Point p = (Point) e.getMessage();
			// Calculate x and y to target
			double dx = p.getX() - this.getX();
			double dy = p.getY() - this.getY();
			// Calculate angle to target
			double theta = Math.toDegrees(Math.atan2(dx, dy));

			// Turn gun to target
			turnGunRight(normalRelativeAngleDegrees(theta - getGunHeading()));
			// Fire hard!
			fire(3);
		}
	}

	public void onBulletMissed(BulletMissedEvent event){
		turnRight(10);
		ahead(10);
	}


	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		// Replace the next line with any behavior you would like
		back(20);
	}

	public void onHitByBullet(HitByBulletEvent event)
	{ 	
		if(boss && subboss){
		turnGunRight(event.getBearing());
		fire(1);
		}
	}
	
	public void onRobotDeath(RobotDeathEvent evnt){
		if(evnt.getName().contains("Pikachu")){
			boss = true;
		}
		else if(evnt.getName().contains("Charizard")){
			subboss = true;
		}
	}

}
