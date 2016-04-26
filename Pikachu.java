package sample;
import robocode.*;
import java.awt.Color;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * Pikachu - a robot by geralds and cardoso
 */
public class Pikachu extends TeamRobot
{

	String[] teammates;
	/**
	 * run: Pikachu's default behavior
	 */
	public void run() {

		setColors(Color.black,Color.black,Color.black); // body,gun,radar

		teammates = getTeammates();

		// Robot main loop
		while(true) {
			// Replace the next 4 lines with any behavior you would like
			ahead(100);
			turnGunRight(360);
			back(100);
			turnGunRight(360);
		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		// Replace the next line with any behavior you would like
		Boolean team = false;
		for(String s : teammates){
			if(getName() == s){
				team = true;
			}
		}
		
		if(team){

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
		back(10);
	}

	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		// Replace the next line with any behavior you would like
		back(20);
	}
}
