package sample;
import robocode.*;
import java.awt.Color;

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
			turnGunRight(360);
			scan();
		}
	}
	
	public void onScannedRobot(ScannedRobotEvent e) {
		if(isTeammate(e.getName()) || e.getName().contains("SittingDuck") || e.getName().contains("Rock")){
		}
		else {
			if(e.getEnergy() > 199.0){
				System.out.println("lider " + e.getName());
				try	{
					broadcastMessage(e.getName());
				}
				catch(Exception error){
					System.out.println("ERROR: " + error);
				}
				fire(1);
			}
			fire(1);
		}
	}

	public void onHitByBullet(HitByBulletEvent e) {
		back(10);
	}

	public void onHitWall(HitWallEvent e) {
		back(20);
	}
}
