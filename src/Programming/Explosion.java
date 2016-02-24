package Programming;

public class Explosion {

	
	int explodeX, explodeY;
	float timeRemaining;
	float totalTime;
	
	public Explosion(float weapX, float weapY, float totTime){
	explodeX = (int) weapX;
	explodeY = (int) weapY;
	totalTime = totTime;
	timeRemaining = totTime;
		
		
	}
	
	
	public float getElapsedTime(){
		
		return timeRemaining - totalTime;
		
	}
	
	
}
