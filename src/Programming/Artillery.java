package Programming;

import java.awt.image.BufferedImage;

import javax.swing.JLabel;

public class Artillery {

	float weapRotation = 0;
	float weapY;
	float weapX;
	float velocityX;
	float velocityY;
	int weaponSpeed = 15;
	int type;
	
	

	
	public Artillery(float x, float y, float rotation){
		weapX = x;
		weapY = y;
		weapRotation = rotation;

		velocityX = (float) Math.cos(Math.toRadians(rotation));
		velocityY = (float) Math.sin(Math.toRadians(rotation));
		type = 1;
		
		
	}
	
	public Artillery(float x, float y, float rotation, int Wtype){
		velocityX = (float) Math.cos(Math.toRadians(rotation));
		velocityY = (float) Math.sin(Math.toRadians(rotation));
		weapX = x + velocityX * 20;
		weapY = y + velocityY * 20;
		weapRotation = rotation;
		type = Wtype;
		switch(type){
		case 1: weaponSpeed = 15; break;
		case 2: weaponSpeed = 10; break;
		
		}
		
		
	}
	
	public Artillery(float x, float y, float velocX, float velocY, int Wtype){
		velocityX = velocX;
		velocityY = velocY;
		weapX = x + velocityX * 20;
		weapY = y + velocityY * 20;
		weapRotation = (float) Math.toDegrees(Math.atan2(velocityY, velocityX));;
		type = Wtype;
		switch(type){
		case 1: weaponSpeed = 15; break;
		case 2: weaponSpeed = 10; break;
		
		}
		
		
	}
	
	public void moveWeapon(double gameSpeed){
		weapX += velocityX*weaponSpeed * gameSpeed;
		weapY += velocityY*weaponSpeed * gameSpeed;
		
	}
	
	public Boolean checkForRemove(int edgeX, int edgeY){
		if(weapX > edgeX){
			return true;
		}
		else if(weapX < -edgeX){
			return true;
		}
		else if(weapY > edgeY + 50){
			return true;
		}
		else if(weapY < -edgeY){
			return true;
		}
		else{
			return false;
		}
	}
	
}
