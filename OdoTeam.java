package sample;
import robocode.*;
import static robocode.util.Utils.normalRelativeAngleDegrees;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * OdoRobot - a robot by andre geraldes
 */
public class OdoTeam extends TeamRobot
{
	private ArrayList<Point> sittingDucks;
	
	public void run() {
		setColors(Color.white,Color.white,Color.white); // body,gun,radar
		setBulletColor(Color.white);
		sittingDucks = new ArrayList<Point>();

		// Robot main loop
		while(true) {
			//ahead(100);
        	//turnRight(90);
			turnGunLeft(360);
			
			System.out.println("-----------------");
			if(sittingDucks.size() > 0){
				for(Point p : sittingDucks)
					System.out.println(p);
			}
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
		if(e.getName().contains("SittingDuck") || e.getName().contains("Rock")){
			// Calculate enemy bearing
			double enemyBearing = this.getHeading() + e.getBearing();
			// Calculate enemy's position
			double enemyX = getX() + e.getDistance() * Math.sin(Math.toRadians(enemyBearing));
			double enemyY = getY() + e.getDistance() * Math.cos(Math.toRadians(enemyBearing));
	
			try {
				broadcastMessage(new Point((int)enemyX, (int)enemyY));
				System.out.println("Message sent");
			} catch (Exception ex) {
				ex.printStackTrace(out);
			}
			
			sittingDucks.add(new Point((int)enemyX, (int)enemyY));
		}
	
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
	
	public void onMessageReceived(MessageEvent e) {
		System.out.println("Message received from " + e.getSender());
		if (e.getMessage() instanceof Point) {
			Point p = (Point) e.getMessage();
			
			double dx = p.getX();
			double dy = p.getY();
			
			sittingDucks.add(p);
		}
	}
}
