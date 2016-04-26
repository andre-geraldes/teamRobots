package sample;
import robocode.*;
import java.awt.Color;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * Charizard - a robot by (your name here)
 */
public class Charizard extends TeamRobot
{
	/**
	 * run: Charizard's default behavior
	 */
	public void run() {
		setColors(Color.black,Color.black,Color.black); // body,gun,radar		

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
		if(isTeammate(e.getName())){
			
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
}
