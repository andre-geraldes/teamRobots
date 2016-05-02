package sample;
import robocode.*;
import static robocode.util.Utils.normalRelativeAngleDegrees;
import java.awt.Color;
import java.awt.Point;
import java.util.HashSet;
import java.util.HashMap;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * OdoRobot - a robot by andre geraldes and paulo cardoso
 */
public class CavacoSilva extends TeamRobot
{
	private HashSet<String> sittingDucks;
	private HashMap<String, Point> teammates;
	
	public void run() {
		setColors(Color.white,Color.white,Color.white); // body,gun,radar
		setBulletColor(Color.white);
		sittingDucks = new HashSet<String>();
		teammates = new HashMap<String,Point>();

		// Robot main loop
		while(true) {
			//ahead(100);
        	//turnRight(90);
			turnGunLeft(360);
			
			try {
			broadcastMessage(new Point((int)getX(), (int)getY()));			
			}
			catch (Exception e) {}
			
			System.out.println("-------- Ducks ---------");
			if(sittingDucks.size() > 0){
				for(String p : sittingDucks)
					System.out.println(p);
			}
			
			System.out.println("-------- Teammates ---------");
			if(teammates.size() > 0){
				for(String name : teammates.keySet())
					System.out.println("Name: " + name + " Position: " + teammates.get(name).getX() + " " + teammates.get(name).getY());
			}
		}
	}

	public void smartFire(double robotDistance) {
		if (robotDistance > 200 || getEnergy() < 15) {
			fire(2);
		} else if (robotDistance > 50) {
			fire(2);
		} else {
			fire(3);
		}
	}

	public void onScannedRobot(ScannedRobotEvent e) {
		if(e.getName().contains("sample.SittingDuck") || e.getName().contains("sample.Rock")){
			// Calculate enemy bearing
			double enemyBearing = this.getHeading() + e.getBearing();
			// Calculate enemy's position
			double enemyX = getX() + e.getDistance() * Math.sin(Math.toRadians(enemyBearing));
			double enemyY = getY() + e.getDistance() * Math.cos(Math.toRadians(enemyBearing));
	
			try {
				broadcastMessage(new String(e.getName()+ " in x:" +(int)enemyX+" y:"+(int)enemyY));
			} catch (Exception ex) {
				ex.printStackTrace(out);
			}
			
			sittingDucks.add(new String(e.getName()+ " in x:" +(int)enemyX+" y:"+(int)enemyY));
		}
	
		if (isTeammate(e.getName()) || e.getName().contains("sample.SittingDuck") || e.getName().contains("sample.Rock")) {
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
		if (e.getMessage() instanceof Point) {
			Point p = (Point) e.getMessage();
			
			teammates.put(e.getSender(), p);
		}
		else if(e.getMessage() instanceof String){
			String r = (String) e.getMessage();
			sittingDucks.add(r);
		}
	}
	
	public void onRobotDeath(RobotDeathEvent e) {
		if(teammates.containsKey(e.getName())){
			teammates.remove(e.getName());
		}
	}
}
