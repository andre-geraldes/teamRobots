package sample;
import robocode.*;
import java.awt.Color;
import java.awt.Point;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * Pikachu - a robot by geralds and cardoso
 */
public class Pikachu extends TeamRobot
{
	public void run() {

		setColors(Color.black,Color.black,Color.black); // body,gun,radar
		setBulletColor(Color.yellow);

		// Robot main loop
		while(true) {
			ahead(200);
			scan();
			back(200);
		}
	}

	public void onScannedRobot(ScannedRobotEvent e) {
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

	public void onHitByBullet(HitByBulletEvent e) {
		back(10);
	}

	public void onHitWall(HitWallEvent e) {
		back(20);
	}
}
