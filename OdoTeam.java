package sample;
import robocode.*;
import static robocode.util.Utils.normalRelativeAngleDegrees;
import java.awt.Color;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * OdoRobot - a robot by andre geraldes
 */
public class OdoTeam extends TeamRobot
{
	public void run() {
		setColors(Color.white,Color.white,Color.white); // body,gun,radar
		setBulletColor(Color.white);

		// Robot main loop
		while(true) {
			//ahead(100);
        	//turnRight(90);
			turnGunLeft(360);
		}
	}

	public void smartFire(double robotDistance) {
		if (robotDistance > 200 || getEnergy() < 15) {
			fire(2);
		} else if (robotDistance > 50) {
			fire(3);
		} else {
			fire(4);
		}
	}

	public void onScannedRobot(ScannedRobotEvent e) {
		if (isTeammate(e.getName()) || e.getName().contains("SittingDuck") || e.getName().contains("Rock")) {
			return;
		}	

		double absoluteBearing = getHeading() + e.getBearing();
		double bearingFromGun = normalRelativeAngleDegrees(absoluteBearing - getGunHeading());

		if (Math.abs(bearingFromGun) <= 3) {
			turnGunRight(bearingFromGun);

			if (getGunHeat() == 0) {
				smartFire(Math.min(3 - Math.abs(bearingFromGun), getEnergy() - .1));
				ahead(40);
			}
		}
		else {
			turnGunRight(bearingFromGun);
		}
		if (bearingFromGun == 0) {
			scan();
		}
	}

	public void onHitByBullet(HitByBulletEvent e) {
		turnRight(30);
		ahead(90);
	}

	public void onHitWall(HitWallEvent e) {
		turnRight(180);
		ahead(70);
	}

	public void OnBulletMissed(BulletMissedEvent e) {
		turnLeft(30);
		ahead(90);
	}
}
