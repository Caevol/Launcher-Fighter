package Programming;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;

//Variable Timestep with experimental Rectangle!




public class MainClass implements Runnable{


	long startTime;
	long goalInterval;
	long interval;
	Boolean running;
	long currentTime;
	long previousTime;
	int counter;
	int launchStage = 1;
	int launchDistance = 20000;
	long lastfpsTime;
	double gameSpeed;
	boolean launching = true;
	boolean posRotate;
	boolean negRotate;
	boolean speedup;
	boolean slowdown;
	boolean firing;
	boolean transitioning = false;
	boolean firingMode = false;
	int fps;
	
	//items
	LinkedList<Artillery> weaponFire = new LinkedList();
	LinkedList<Artillery> enemyWeaponFire = new LinkedList();
	LinkedList<SmallCarrier> carriers = new LinkedList();
	LinkedList<Explosion> explosions = new LinkedList();
	LinkedList<SoundTest> sounds = new LinkedList();
	Rectangle shipShape = new Rectangle();
	
	//destroying items
	LinkedList<Artillery> weaponFireDestroy = new LinkedList();
	LinkedList<Artillery> enemyWeaponFireDestroy = new LinkedList();
	LinkedList<SmallCarrier> carriersDestroy = new LinkedList();
	LinkedList<Explosion> explosionsDestroy = new LinkedList();
	LinkedList<SoundTest> soundsDestroy = new LinkedList();
	
	//to be translated to a different class
	Rectangle soundButton = new Rectangle();
	int maxSpeed = 10;
	int reverseMaxSpeed = -5;
	int newCarrierX;
	int newCarrierY;
	boolean addnewCarrier;
	boolean soundEnabled = true;
	boolean musicEnabled = true;
	float timeForExp = 23;
	float shipRotation = 0;
	float shipX = 0;
	float shipY = 0;
	int worldX = 0;
	int worldY = 0;
	int edgeX = 2500; 
	int edgeY = 2500;
	int shipHealth = 3;
	float shipSpeed = 0;
	SoundTest music = null;
	SoundTest ambiance = null;
	BufferedImage ship = null;
	BufferedImage shipgo = null;
	BufferedImage shipback = null;
	BufferedImage weapon = null;
	BufferedImage weapon2 = null;
	BufferedImage carrierShip = null;
	BufferedImage carrierTop = null;
	BufferedImage carrierBottom = null;
	BufferedImage carrierTopHalf = null;
	BufferedImage carrierBotHalf = null;
	BufferedImage explosion = null;
	BufferedImage tinyEnemy = null;
	BufferedImage goodShip = null;
	BufferedImage hurtShip = null;
	BufferedImage dyingShip = null;
	BufferedImage deadShip = null;
	BufferedImage launchShip = null;
	int maxWeapons = 25;
	int weaponsFired = 0;
	int coolDown = 0;
	int coolDownTime = 25;
	float directionX;
	float directionY;
	float velocityX;
	float velocityY;
	float shipHandling = 2;

	//End

	//carrier is x + 10 and Y + 40 off from the polygon

	JFrame userFrame = new JFrame();

	//this label draws the game, treat it well
	JLabel world = new JLabel(){

		protected void paintComponent(Graphics g){
			super.paintComponent(g);


			
			//ForeGround	
			g.setColor(Color.black);
			g.fillRect(0, 0, world.getWidth(), world.getHeight());
			
			if(launching){
			
			
			if(isVisibleMain(0, launchDistance)){
				g.drawImage(launchShip, -500 - worldX, launchDistance - 530 - worldY, null);
				g.drawImage(launchShip, -1500 - worldX, launchDistance - 530 - worldY, null);
				g.drawImage(launchShip, 500 - worldX, launchDistance - 530 - worldY, null);
			}
			
			switch(launchStage){
			
			case 1:	
					g.setColor(Color.DARK_GRAY);
					g.fillRect(userFrame.getWidth() / 2 - 310, userFrame.getHeight() / 2 -25, 160, 100);
					g.setColor(Color.RED);
					g.drawString("Press W to launch", userFrame.getWidth() / 2 - 300, userFrame.getHeight() / 2);
					g.drawString("A / D to rotate", userFrame.getWidth() / 2 - 300, userFrame.getHeight() / 2 + 20);
					g.drawString("S to slow down", userFrame.getWidth() / 2 - 300, userFrame.getHeight() / 2 + 40);
					g.drawString("T to switch firing modes", userFrame.getWidth() / 2 - 300, userFrame.getHeight() / 2 + 60);
					g.setColor(Color.BLACK);
					g.drawRect(userFrame.getWidth() / 2 - 310, userFrame.getHeight() / 2 -25, 160, 100);
					break;
			case 2:
					g.setColor(Color.red);
					g.drawString("Distance to battlefield:    " + (int) (shipY - 1500), userFrame.getWidth() / 3 + 150, 300);
			}
			
			
			}
			
			g.drawImage(tinyEnemy, 200 - worldX, 400 - worldY, null);
			AffineTransform at = new AffineTransform();
			// 4. translate it to the center of the component
			at.translate(world.getWidth()/2, world.getHeight()/2);
			// 3. do the actual rotation
			at.rotate(Math.toRadians(shipRotation));
			at.scale(0.5, 0.5);
			// 1. translate the object so that you rotate it around the 
			//    center (easier :))
			at.translate(-ship.getWidth()/2, -ship.getHeight()/2);
			// draw the image
			Graphics2D g2d = (Graphics2D) g;


			for(SmallCarrier c: carriers){
				if(isVisibleMain(c.shipX,c.shipY)){
					if(c.health > c.totalHealth/3){
						g.drawImage(carrierBottom, c.shipX - worldX, c.shipY - worldY, null);
					}
					else{
						g.drawImage(carrierBotHalf, c.shipX - worldX, c.shipY - worldY, null);
					}
				}
			}

			if(speedup){
				g2d.drawImage(shipgo, at, null);
			}
			else if(slowdown){

				g2d.drawImage(shipback, at, null);
			}

			else{
				g2d.drawImage(ship, at, null);
			}


			//g.drawImage(ship, (int) shipX, 5, null);

			//menu items above the rest of it

			//Firing mode display

			for(SmallCarrier c: carriers){
				if(isVisibleMain(c.shipX, c.shipY)){
					if(c.health > 50){
						g.drawImage(carrierTop, c.shipX - worldX, c.shipY - worldY, null);
					}
					else{
						g.drawImage(carrierTopHalf, c.shipX - worldX, c.shipY - worldY, null);
					}
					g.setColor(Color.blue);
					g.drawString(c.health +"/" + c.totalHealth, c.shipX - worldX, c.shipY - worldY);
				}
			}


			g.setColor(Color.darkGray);
			g.fillRect(10, 10, 100, 20);
			g.setColor(Color.gray);
			g.drawRect(10, 10, 100, 20);

			if(firingMode){
				g.setColor(Color.red);
				g.drawString("Firing Mode", 20, 25);
			}
			else{
				g.setColor(Color.cyan);
				g.drawString("Flying Mode", 20, 25);
			}
			//g.fillPolygon(polyTest);

			for(Explosion e: explosions){
				if(isVisibleFloat(e.explodeX, e.explodeY)){
					AffineTransform at1 = new AffineTransform();

					at1.translate(e.explodeX - worldX, e.explodeY - worldY);
					//at1.rotate(Math.toRadians(e.weapRotation));
					at1.scale(.1 * e.getElapsedTime()/10, .1 * e.getElapsedTime()/10);
					at1.translate(-explosion.getWidth()/2, -explosion.getHeight()/2);

					Graphics2D g2d1 = (Graphics2D) g;
					g2d1.drawImage(explosion, at1, null);
				}
			}

			for(Artillery a: weaponFire){
				if(isVisibleFloat(a.weapX, a.weapY)){
					AffineTransform at1 = new AffineTransform();
					at1.translate(a.weapX - worldX, a.weapY - worldY);
					at1.rotate(Math.toRadians(a.weapRotation));
					at1.translate(-weapon.getWidth()/2, -weapon.getHeight()/2);
					Graphics2D g2d1 = (Graphics2D) g;

					switch(a.type){

					case 1:
						g2d1.drawImage(weapon, at1, null);
						break;

					case 2:
						g2d1.drawImage(weapon2, at1, null);
						break;
					default:
						g2d1.drawImage(weapon, at1, null);
					}

				}



			}

			for(Artillery a: enemyWeaponFire){
				if(isVisibleFloat(a.weapX, a.weapY)){
					AffineTransform at1 = new AffineTransform();
					// 4. translate it to the center of the component
					at1.translate(a.weapX - worldX, a.weapY - worldY);
					// 3. do the actual rotation
					at1.rotate(Math.toRadians(a.weapRotation));
					//at.scale(0.5, 0.5);
					// 1. translate the object so that you rotate it around the 
					//    center (easier :))
					at1.translate(-weapon.getWidth()/2, -weapon.getHeight()/2);
					// draw the image
					Graphics2D g2d1 = (Graphics2D) g;

					switch(a.type){

					case 1:
						g2d1.drawImage(weapon, at1, null);
						break;

					case 2:
						g2d1.drawImage(weapon2, at1, null);
						break;
					default:
						g2d1.drawImage(weapon2, at1, null);
					}
				}
			}

			g.setColor(Color.RED);
			g.drawRect(-edgeX - worldX, -edgeY - worldY, 5000, 5000);

			//draw the Minimap!!!!!!!!
			g.setColor(Color.DARK_GRAY);
			g.fillRect(0, userFrame.getHeight() - 250, 250, 250);

			//g.setColor(Color.blue);
			//g.fillRect(shipShape.x, shipShape.y, shipShape.width, shipShape.height);
			for(int x = -25; x <25; x++){
				for(int y = -25; y < 25; y++){
					
					
					switch(minimapSquare(x, y)){
					/*
					 * 1 User
					 * 2 Carrier
					 * 3 Smaller Ship
					 * 4 weapon
					 */
					case 1: g.setColor(Color.WHITE); g.fillRect((x + 25) * 5, userFrame.getHeight() - 250 + ((y + 25) * 5), 5, 5);
						break;
					case 2: g.setColor(Color.MAGENTA); g.fillRect((x + 25) * 5, userFrame.getHeight() - 250 + ((y + 25) * 5), 5, 5);
						break;
					case 3:  g.setColor(Color.YELLOW); g.fillRect((x + 25) * 5, userFrame.getHeight() - 250 + ((y + 25) * 5), 5, 5);
						break;
					case 4: g.setColor(Color.RED); g.fillRect((x + 25) * 5, userFrame.getHeight() - 250 + ((y + 25) * 5), 5, 5);
						break;
					
					default: //nothing, its just space
						break;
					
					}
					
				}
				
				
			}
		
			
			g.setColor(Color.gray);
			g.drawString("Status:", userFrame.getWidth() - 100, userFrame.getHeight() - 110);
			g.drawRect(userFrame.getWidth() - 125, userFrame.getHeight() - 125, 100, 75);
			switch(shipHealth){
			case 0: g.drawImage(deadShip, userFrame.getWidth() - 125, userFrame.getHeight() - 125, null);
				break;
			case 1: g.drawImage(dyingShip, userFrame.getWidth() - 125, userFrame.getHeight() - 125, null);
				break;
			case 2: g.drawImage(hurtShip, userFrame.getWidth() - 125, userFrame.getHeight() - 125, null);
				break;
			case 3: g.drawImage(goodShip, userFrame.getWidth() - 125,  userFrame.getHeight() - 125, null);
				break;
			}
			
			if(soundEnabled){
				g.setColor(Color.blue);
				
			}
			else{
				g.setColor(Color.red);
			}
			g.fillRect(soundButton.x, soundButton.y - soundButton.height, soundButton.width, soundButton.height);
			
		};


	};
	

	public MainClass(int frames){

		setupPanels();
		setupTimer(frames);
	}

	//Basic Timer code, once running
	public void run() {

		while(running){



			currentTime = System.nanoTime();
			interval = currentTime - previousTime;
			previousTime = currentTime;


			lastfpsTime += interval;
			fps ++;


			
			

			if(lastfpsTime > 1000000000){

				//System.out.println("FPS = " +fps + " Game speed = " + gameSpeed);
				lastfpsTime = 0;
				fps = 0;
			}


			gameLogic();
			gameSpeed = interval / ((double)goalInterval);
			world.repaint();

			while(interval < goalInterval){
				currentTime = System.nanoTime();
				interval = currentTime - previousTime;
			}


		}
	}


	public int minimapSquare(int x, int y){
		
		//user
		if(shipX >= x * 100 && shipX < x * 100 + 100 && shipY >= y * 100 && shipY < y * 100 + 100){
			return 1;
		}
		//carrier
		
			for(SmallCarrier c: carriers){
			if(c.shipX + 100 >= x * 100 && c.shipX + 100 < x * 100 + 100 && c.shipY + 230 >= y * 100 && c.shipY + 230 < y * 100 + 100){
		return 2;		
		
		}
			}
		//enemy
		//TO BE WRITTEN
		//weapon
			for(Artillery a: weaponFire.toArray(new Artillery[0])){
				if(a.weapX >= x*100 && a.weapX < x*100 + 100 && a.weapY >= y * 100 && a.weapY < y * 100 + 100){
					return 3;
				}
			}
			for(Artillery e: enemyWeaponFire.toArray(new Artillery[0])){
				if(e.weapX >= x*100 && e.weapX < x*100 + 100 && e.weapY >= y * 100 && e.weapY < y * 100 + 100){
					return 4;
				}
			} 
			
		return 5;
	}
	
	//this code sets up all the jframes and panelling before the code starts
	public void setupPanels(){




		//setUp image for test
		try {
			ship = ImageIO.read(this.getClass().getResource("Images/Ship2.png"));
			shipgo = ImageIO.read(this.getClass().getResource("Images/Shipgo2.png"));
			shipback = ImageIO.read(this.getClass().getResource("Images/Shipbackv2.png"));
			weapon = ImageIO.read(this.getClass().getResource("Images/Weapon.png"));
			carrierShip = ImageIO.read(this.getClass().getResource("Images/Corvettev7.png"));
			carrierTop = ImageIO.read(this.getClass().getResource("Images/CorvetteTop.png"));
			carrierBottom = ImageIO.read(this.getClass().getResource("Images/CorvetteBottom.png"));
			carrierTopHalf = ImageIO.read(this.getClass().getResource("Images/CorvetteTopHalfHealth.png"));
			carrierBotHalf = ImageIO.read(this.getClass().getResource("Images/CorvetteBottomHalfHealth.png"));
			weapon2 = ImageIO.read(this.getClass().getResource("Images/Weapon2.png"));
			explosion = ImageIO.read(this.getClass().getResource("Images/Explosion.png"));
			tinyEnemy = ImageIO.read(this.getClass().getResource("Images/Tiny Enemy2.png"));
			goodShip = ImageIO.read(this.getClass().getResource("Images/GoodShip.png"));
			hurtShip = ImageIO.read(this.getClass().getResource("Images/HurtShip.png"));
			dyingShip = ImageIO.read(this.getClass().getResource("Images/DyingShip.png"));
			deadShip = ImageIO.read(this.getClass().getResource("Images/DeadShip.png"));
			launchShip = ImageIO.read(this.getClass().getResource("Images/Launch Shipv.png"));
		} catch (IOException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(soundEnabled){
			ambiance = new SoundTest("Sounds/SpaceSoundsLoud.WAV");
			ambiance.clip.loop(-1);
		}
		userFrame.setSize(1500, 1000);
		userFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		userFrame.add(world);
		world.setSize(userFrame.getSize()); 
		userFrame.addKeyListener(rotateSquare);
		userFrame.addMouseListener(polyCoordinates);
		userFrame.setVisible(true);
		
		resetShip();
		carriers.add(new SmallCarrier(-500, 1500));
		carriers.add(new SmallCarrier(-230, 7000));
		carriers.add(new SmallCarrier(-1200, 800));
		carriers.add(new SmallCarrier(800, -100));
		
	}



	//the timer is prepared here.
	public void setupTimer(int frames){

		startTime = System.nanoTime();
		previousTime = System.nanoTime();
		goalInterval = 1000000000 / frames;
		counter = 0;
		lastfpsTime = 0;
		running = true;
	}

	//all basic game code will go in here
	public void gameLogic(){

		
		
		
		
		soundButton.setBounds(userFrame.getWidth() - 100, 100, 20, 20);
		
		
		if(addnewCarrier){
			carriers.add(new SmallCarrier(newCarrierX  + worldX, newCarrierY + worldY));
			addnewCarrier = false;
		}

		if(launching){
			switch(launchStage){
			case 1:
				//ready for launch
				if(speedup){
					launchStage ++;
					if(musicEnabled && music == null){
						ambiance.clip.stop();
						music = new SoundTest("Sounds/ambiance.WAV");
					}
					if(soundEnabled){
					sounds.add(new SoundTest("Sounds/LaunchTemp.WAV"));
					}
					//anything else?
				}
				
				
				
				break;
			case 2:
				//launching
				if(shipSpeed < 15){
					shipSpeed += .1 * gameSpeed;
				}
				speedup = true;
				shipY -= shipSpeed * gameSpeed;
				
				break;
			
			
			}
			
			if(shipY < 1500){
				launching = false;
				launchStage = 1;
				launchDistance = 8000;
				
			}
			
		}
		else if(firingMode){
			controlsLogicFiringMode();
		}
		else{
			controlsLogicFlyingMode();
		}


		//Update the position based on our current speed
		//This is basic s = vt physics

		for(Artillery a2: enemyWeaponFire){
			
			
			if(shipShape.contains(a2.weapX, a2.weapY)){
				shipHit();
				//explosions.add(new Explosion(a2.weapX, a2.weapY, timeForExp));
				enemyWeaponFireDestroy.add(a2);

			}

		}


		//update weapons
		for(Artillery a: weaponFire){

			if(a.checkForRemove(edgeX, edgeY)){
				weaponFireDestroy.add(a);
				weaponsFired --;
			}

			else{
				a.moveWeapon(gameSpeed);
			}
		}

		for(Artillery a: enemyWeaponFire){

			if(a.checkForRemove(edgeX, edgeY)){

				enemyWeaponFireDestroy.add(a);
			}

			else{
				a.moveWeapon(gameSpeed);
			}
		}


		for(SmallCarrier c: carriers){	
			for(Artillery a: weaponFire){

				if(c.checkCollision(a.weapX, a.weapY)){

					explosions.add(new Explosion(a.weapX, a.weapY, timeForExp));
					weaponFireDestroy.add(a);
					weaponsFired --;
				}
			}
		}



		//carrier checks *Move to new method for simplicity!


		for(SmallCarrier c: carriers){
			// Ship Colliding with Carrier
			if(c.checkCollision(shipX, shipY, 20)){
				shipCollide();
			}

		}

		//check for out of health
		for(SmallCarrier c: carriers){
			if(c.health <= 0){
				explosions.add(new Explosion(c.shipX, c.shipY, 70));
				
				explosions.add(new Explosion(c.shipX + 200, c.shipY + 290, 90));
				carriersDestroy.add(c);

			
			}
		}


		//ship AI shooting
		for(SmallCarrier c: carriers){
			float distance = ((c.shipX + 105 - shipX) * (c.shipX + 105 - shipX)) + ((c.shipY + 235 - shipY) * (c.shipY + 235 - shipY));

			if(distance < 160000){

			}
			else if(distance < 3200000  && c.cooldownEnemy <= 0){

				//within Range
				/*
				 * 1. solve rates
				 * 2. apply Rates
				 * 
				 * 
				 */
				double totDist = Math.abs(c.shipX + 105 - shipX) + Math.abs(c.shipY + 235 - shipY);
				double distX = c.shipX + 105 - shipX;
				double distY = (c.shipY + 235 - shipY);
				float rateX = (float) ( -1 * (distX / totDist));
				float rateY = (float) ( -1 * (distY / totDist));

				enemyWeaponFire.add(new Artillery(c.shipX + 105, c.shipY + 235, rateX, rateY, 2));
				c.cooldownEnemy = coolDownTime;
				if(isVisibleMain(c.shipX, c.shipY) && soundEnabled){
					sounds.add(new SoundTest("Sounds/DownLazer.WAV"));
				}
				//sounds.add(new SoundTest("Sounds/DownLazer.WAV"));
				
			/*	float angle = (float) Math.toDegrees(Math.atan2(c.shipX - shipX + 105, c.shipY + 235 - shipY));
				angle *= -1;
				if(angle < 0){
					angle = 360 + angle;
				}
				if(angle > 360){
					angle = angle % 360;
				}
				c.cooldownEnemy = coolDownTime;


				enemyWeaponFire.add(new Artillery(c.shipX + 105, c.shipY + 235, angle - 90, 2));
				sounds.add(new SoundTest("Sounds/DownLazer.WAV"));
				*/
			}

			c.cooldownEnemy -= 1 * gameSpeed;

		}

		for(SoundTest s: sounds){
			if(!s.isRunning()){
				soundsDestroy.add(s);
			}
		}

		if(!launching){
		outofBounds();
		}
		
		for(Explosion t: explosions){
			t.timeRemaining -= 1 * gameSpeed;
			if(t.timeRemaining <= 0){
				explosionsDestroy.add(t);
				break;
			}
		}

		weaponFire.removeAll(weaponFireDestroy);
		enemyWeaponFire.removeAll(enemyWeaponFireDestroy);
		explosions.removeAll(explosionsDestroy);
		sounds.removeAll(soundsDestroy);
		carriers.removeAll(carriersDestroy);
		
		
		
		coolDown -= 1 * gameSpeed;
		worldX = (int) (shipX - world.getWidth()/2);
		worldY = (int) (shipY - world.getHeight()/2);
		shipShape.setBounds((int)(shipX - 15),(int) (shipY - 15), 30, 30);
	}

	public void toofast(){
		//prevents the ship from outrunning its own weapons and breaking the game
		if(firingMode){
			if(velocityX > maxSpeed){
				velocityX = maxSpeed;
			}
			else if(velocityX < reverseMaxSpeed){
				velocityX = reverseMaxSpeed;
			}

			if(velocityY > maxSpeed){
				velocityY = maxSpeed;
			}
			else if(velocityY < reverseMaxSpeed){
				velocityY = reverseMaxSpeed;
			}
		}
		else{
			if(shipSpeed > maxSpeed){
				shipSpeed = maxSpeed;
			}
			else if(shipSpeed < reverseMaxSpeed){
				shipSpeed = reverseMaxSpeed;
			}
		}
	}

	public void shipHit(){
		explosions.add(new Explosion(shipX, shipY, timeForExp));
		//shipX -= velocityX * 30;
		//shipY -= velocityY * 30;
		//velocityX = 0;
		//velocityY = 0;
		//shipSpeed = 0;
		shipHealth --;
		if(shipHealth < 1){
			resetShip();
		}
		
	};
	
	public void shipCollide(){
		explosions.add(new Explosion(shipX, shipY, 65));
		//shipX -= velocityX * 30;
		//shipY -= velocityY * 30;
		//velocityX = 0;
		//velocityY = 0;
		//shipSpeed = 0;
		//shipHealth --;
		//if(shipHealth < 0){
		//	shipHealth = 3;
		//}
		resetShip();
		
		
	}
	
	

	public void controlsLogicFlyingMode(){

		/* 												FLYING MODE 
		 * In this mode, the ship's movement directly follows the rotation of the ship, acceleration having no effect
		 * 
		 */

		//rotates the ship around to match momentum, so it doesnt go crazy
		if(transitioning){			
			//double tranRadius = Math.hypot(velocityX, velocityY);
			float goalAngle = (float) Math.toDegrees(Math.atan2(velocityY, velocityX));


			if(goalAngle < 0){
				goalAngle = 360 + goalAngle;
			}
			else if(goalAngle > 360){
				goalAngle = goalAngle % 360;
			}
			if(goalAngle > shipRotation){
				shipRotation += shipHandling * gameSpeed;
			}
			else if(goalAngle < shipRotation){
				shipRotation -= shipHandling * gameSpeed;
			}

			if(goalAngle - shipRotation < 1 && goalAngle - shipRotation > -1){
				shipRotation = goalAngle;
				transitioning = false;
			}
			rotation360();
		}

		else{
			//rotating the ship
			if(posRotate){
				shipRotation += shipHandling * gameSpeed;

			}

			//rotating the ship
			else if(negRotate){
				shipRotation -= shipHandling * gameSpeed;
			}

			if(speedup){

				shipSpeed += .1 * gameSpeed;




			}

			//reverse
			else if(slowdown){

				shipSpeed -= .03 * gameSpeed;


			}



			//weaponFire
			if(firing && weaponsFired < maxWeapons && coolDown <= 0){
				if(soundEnabled){
				sounds.add(new SoundTest("Sounds/New Lazer 3.WAV"));
				}
				weaponFire.add(new Artillery(shipX, shipY, shipRotation));
				coolDown = coolDownTime;
				weaponsFired ++;
			}

			rotation360();

			//first get the direction the entity is pointed
			directionX = (float) Math.cos(Math.toRadians(shipRotation));
			directionY = (float) Math.sin(Math.toRadians(shipRotation));

			//Then scale it by the current speed to get the velocity
			velocityX = (float) (directionX * shipSpeed);
			velocityY = (float) (directionY * shipSpeed);
		}		
		shipX += velocityX * gameSpeed;
		shipY += velocityY * gameSpeed;


		toofast();

	}

	public void rotation360(){
		//resets angle so it doesn't go crazy
		if(shipRotation > 360){
			shipRotation = shipRotation % 360;
		}
		else if(shipRotation < 0){
			shipRotation = 360 + shipRotation;
		}
	}

	public void controlsLogicFiringMode(){

		/*										FIRING MODE:
		 * In this mode, the ship will fly more like a real space ship, only changing direction when accelerating
		 * 
		 * 
		 */

		//rotating the ship
		if(posRotate){
			shipRotation += shipHandling * gameSpeed;

		}

		//rotating the ship
		else if(negRotate){
			shipRotation -= shipHandling * gameSpeed;
		}

		if(speedup){


			//first get the direction the entity is pointed
			directionX = (float) Math.cos(Math.toRadians(shipRotation));
			directionY = (float) Math.sin(Math.toRadians(shipRotation));

			//Then scale it by the current speed to get the velocity
			velocityX += (float) (directionX * gameSpeed * .1);
			velocityY += (float) (directionY * gameSpeed * .1);



		}

		//reverse, still rotates the ship slightly
		else if(slowdown){
			//deceleration
			/*if( shipSpeed > reverseMaxSpeed){
					shipSpeed -= .05 * gameSpeed;
				}*/

			directionX = (float) Math.cos(Math.toRadians(shipRotation));
			directionY = (float) Math.sin(Math.toRadians(shipRotation));

			//Then scale it by the current speed to get the velocity
			velocityX += (float) (directionX * gameSpeed * -.05);
			velocityY += (float) (directionY * gameSpeed * -.05);

		}

		/*********  Slow down effect
		else if(!speedup && !slowdown){
			if(velocityX > 5){
				velocityX -= .03 * gameSpeed;
			}
			else if(velocityX < -3){
				velocityX += .03 * gameSpeed;
			}
			if(velocityY > 5){
				velocityY -= .03 * gameSpeed;
			}
			else if(velocityY < -3){
				shipSpeed += .03 * gameSpeed;
			}
		}
		 ***********/

		///////////////////////////                 FIRES THE WEAPONS
		if(firing && weaponsFired < maxWeapons && coolDown <= 0){
			weaponFire.add(new Artillery(shipX + (directionX ), shipY + (directionY ), shipRotation));
			if(soundEnabled){
			sounds.add(new SoundTest("Sounds/New Lazer 3.WAV"));
			}
			coolDown = coolDownTime;
			weaponsFired ++;
		}

		//resets angle so it doesn't go crazy
		rotation360();

		shipX += velocityX * gameSpeed;
		shipY += velocityY * gameSpeed;

		toofast();
	}

	KeyListener rotateSquare = new KeyListener(){

		@Override
		public void keyPressed(KeyEvent arg0) {
			char key = arg0.getKeyChar();
			if(key == 'd'){
				posRotate = true;
			}
			else if(key == 'a'){
				negRotate = true;
			}
			else if(key == 'w'){
				speedup = true;
			}
			else if(key == 's'){
				slowdown = true;
			}
			else if(key == ' '){
				firing = true;
			}

		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			char key = arg0.getKeyChar();
			if(key == 'd'){
				posRotate = false;
			}
			else if(key == 'a'){
				negRotate = false;
			}
			else if(key == 'w'){
				speedup = false;
			}
			else if(key == 's'){
				slowdown = false;
			}
			else if(key == ' '){
				firing = false;
			}
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			char key = arg0.getKeyChar();
			if(key == 't'){

				firingMode = !firingMode;
				if(soundEnabled){
				sounds.add(new SoundTest("Sounds/No Ammo.WAV"));
				}
				if(!firingMode){
					transitioning = true;

				}
			}

		}



	};




	MouseListener polyCoordinates = new MouseListener(){

		@Override
		public void mouseClicked(MouseEvent arg0) {
			if(soundButton.contains(arg0.getX(), arg0.getY())){
				soundEnabled = !soundEnabled;
				System.out.println("Sound");
			}
			else{
				if(arg0.getX() + worldX < 2500 && arg0.getX() + worldX > -2500 && arg0.getY() + worldY < 2500 && arg0.getY() + worldY > -2500){
			newCarrierX = arg0.getX();
			newCarrierY = arg0.getY();
			//System.out.println(newCarrierX + " " + newCarrierY + ": " + soundButton.x + " " + soundButton.y);
			addnewCarrier = true;
				}
			}
			//carriers.add(new SmallCarrier(mouX + worldX, mouY + worldY));
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}


	};

	public Boolean isVisibleMain(int locationX, int locationY){
		if((locationX > shipX - world.getWidth() && locationX < shipX + world.getWidth()) &&
				(locationY > shipY - world.getHeight() && locationY < shipY + world.getHeight())){
			return true;
		}
		else{
			return false;
		}
	}

	public Boolean isVisibleFloat(float locationX, float locationY){
		if((locationX > shipX - world.getWidth() && locationX < shipX + world.getWidth()) &&
				(locationY > shipY - world.getHeight() && locationY < shipY + world.getHeight())){
			return true;
		}
		else{
			return false;
		}
	}

	public void outofBounds(){
		if(shipX > edgeX){
			shipX = -edgeX;
		}
		else if(shipX < -edgeX){
			shipX = edgeX;
		}
		if(shipY > edgeY){
			shipY = -edgeY;
		}
		else if(shipY < -edgeY){
			shipY = edgeY;
		}

	}

	public void resetShip(){
		launching = true;
		launchStage = 1;
		shipX = 0;
		shipY = launchDistance;
		shipRotation = 270;
		shipSpeed = 0;
		shipHealth = 3;
		firingMode = false;
	}
	
	
	public static void main(String[] args) {

		new Thread(new MainClass(60)).start();


	}

}
