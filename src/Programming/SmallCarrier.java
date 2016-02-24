package Programming;

import java.awt.Polygon;

public class SmallCarrier {
	
	Polygon hitBox = new Polygon();
	int shipX;
	int shipY;
	float directionX, directionY;
	int velocityX, velocityY;
	int health;
	int totalHealth;
	int timeToLaunch;
	float cooldownEnemy = 0;
	public SmallCarrier(int X, int Y){
		

		
		shipX = X - 105;
		shipY = Y - 235;
		
		health = 70;
		totalHealth = health;
		setUpHitBox();
		hitBox.translate(shipX -10, shipY -40);
	}
	
	public void setUpHitBox(){
		hitBox.addPoint(110, 35);
		hitBox.addPoint(66, 67);
		hitBox.addPoint(18, 251);
		hitBox.addPoint(16, 403);
		hitBox.addPoint(18, 469);
		hitBox.addPoint(73, 455);
		hitBox.addPoint(71, 150);
		hitBox.addPoint(128, 79);
		hitBox.addPoint(141, 265);
		hitBox.addPoint(162, 419);
		hitBox.addPoint(151, 471);
		hitBox.addPoint(210, 473);
		hitBox.addPoint(209, 265);
		hitBox.addPoint(194, 147);
		hitBox.addPoint(166, 67);
		hitBox.addPoint(118, 31);
	}
	
	public void updateHitBox(int changeX, int changeY){
		hitBox.translate(changeX, changeY);
	}
	
	public Boolean checkCollision(float weapX, float weapY){
		if(hitBox.contains(weapX, weapY)){
			health --;
			return true;
		}
		else{
			return false;
		}
	}
	
	public Boolean checkCollision(float weapX, float weapY, int healthLost){
		if(hitBox.contains(weapX, weapY)){
			health -= healthLost;
			return true;
		}
		else{
			return false;
		}
	}
	
	public void moveShip(){
		shipX += velocityX;
		shipY += velocityY;
		hitBox.translate(velocityX, velocityY);
		
	}
	
}
