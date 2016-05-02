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
	private HashMap<String, String> sittingDucks;
	private HashMap<String, Point> teammates;
	
	public void run() {
		setColors(Color.white,Color.white,Color.white); // body,gun,radar
		setBulletColor(Color.white);
		sittingDucks = new HashMap<String, String>();
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
			System.out.println("-------- BattleField Info ---------");
			System.out.println("> SittingDucks:");
			if(sittingDucks.size() > 0){
				for(String p : sittingDucks.keySet())
					System.out.println("Name: " + p + " Position: " + sittingDucks.get(p));
			}
			
			System.out.println("> My position: " + (int)getX() + "," + (int)getY());
			System.out.println("> Teammates:");
			if(teammates.size() > 0){
				for(String name : teammates.keySet())
					System.out.println("Name: " + name + " Position: " + (int)teammates.get(name).getX() + "," + (int)teammates.get(name).getY());				
			}
			System.out.println("------------------------------------");
			
			recalcularRota();
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
		if(e.getName().contains("sample.SittingDuck")){
			// Calculate enemy bearing
			double enemyBearing = this.getHeading() + e.getBearing();
			// Calculate enemy's position
			double enemyX = getX() + e.getDistance() * Math.sin(Math.toRadians(enemyBearing));
			double enemyY = getY() + e.getDistance() * Math.cos(Math.toRadians(enemyBearing));
	
			try {
				broadcastMessage(new String(e.getName()+ "|" +(int)enemyX+","+(int)enemyY));
			} catch (Exception ex) {
				ex.printStackTrace(out);
			}
			
			sittingDucks.put(e.getName(), new String((int)enemyX+","+(int)enemyY));
		}
	
		if (isTeammate(e.getName()) || e.getName().contains("sample.SittingDuck")) {
			return;
		}	

		double absoluteBearing = getHeading() + e.getBearing();
		double bearingFromGun = normalRelativeAngleDegrees(absoluteBearing - getGunHeading());

		if (Math.abs(bearingFromGun) <= 3) {
			turnGunRight(bearingFromGun);

			if (getGunHeat() == 0 && safeToShoot()) {
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
			String[] p = r.split("\\|");
			sittingDucks.put(p[0],p[1]);
		}
	}
	
	public void onRobotDeath(RobotDeathEvent e) {
		if(teammates.containsKey(e.getName())){
			teammates.remove(e.getName());
		}
	}
	
	public void onBulletHit(BulletHitEvent event) {
       if(event.getName().contains("sample.SittingDuck")){
		   turnRight(120);
		   ahead(200);
	   }
   }
	
	public void recalcularRota(){
		if(teammates.size() > 0){
				for(String name : teammates.keySet()){
					double x = teammates.get(name).getX();
					double y = teammates.get(name).getY();
					double dist = Math.sqrt(Math.pow(this.getX()-x,2) + Math.pow(this.getY()-y,2));	
					if(dist < 200){
						turnRight(150);
						ahead(100);
					}
				}
		}
	}
	
	public boolean safeToShoot(){
		double ang = this.getHeading();	
		boolean safe = true;
		String[] c = null;
		
		if(sittingDucks.size() > 0){
				for(String p : sittingDucks.keySet())
					c = sittingDucks.get(p).split(",");
					int x = Integer.parseInt(c[0]);
					int y = Integer.parseInt(c[1]);
			}

		return safe;
	}
}
