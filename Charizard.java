package sample;
import robocode.*;
import java.awt.Color;
import java.awt.Point;
import static robocode.util.Utils.normalRelativeAngleDegrees;


// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * Charizard - a robot by (your name here)
 */
public class Charizard extends TeamRobot
{
	/**
	 * run: Charizard's default behavior
	 */

	private boolean leader = false;

	public void run() {
		setColors(Color.black,Color.black,Color.black); // body,gun,radar		
		setBulletColor(Color.red);		

		// Robot main loop
		while(true) {
			turnGunRight(360);
			scan();
		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		// Replace the next line with any behavior you would like
		if(leader){
			if(isTeammate(e.getName()) || e.getName().contains("SittingDuck") || e.getName().contains("Rock")){
			}
			else {
				// Calculate enemy bearing
				double enemyBearing = this.getHeading() + e.getBearing();
				// Calculate enemy's position
				double enemyX = getX() + e.getDistance() * Math.sin(Math.toRadians(enemyBearing));
				double enemyY = getY() + e.getDistance() * Math.cos(Math.toRadians(enemyBearing));

				try {
					// Send enemy position to teammates
					broadcastMessage(new Point((int)enemyX, (int)enemyY));
				} catch (Exception ex) {
					out.println("Unable to send order: ");
					ex.printStackTrace(out);
				}
				}
			}
		else{
			fire(1);
		}
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		// Replace the next line with any behavior you would like
		if(isTeammate(e.getName())){
			
		}
		else{
			fire(1);
		}
	}
	
	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		// Replace the next line with any behavior you would like
		back(20);
	}	

	public void onRobotDeath(RobotDeathEvent evnt){
		if(evnt.getName().contains("Pikachu")){
			leader = true;
		}
	}

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
}
