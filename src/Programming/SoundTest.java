package Programming;

import java.net.URL;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


public class SoundTest {
	
int loopCount = 1;
	
	 URL url;
	 AudioInputStream ais;
	 Clip clip;
	 // simple audio file name like snick7.wav works if in same folder as this.class
	 // but full filenames like /Volumes/PENDRIVE/groovy/snick7.wav don't 
	 public SoundTest(String fn)
	 {
	   try
	   {
	     url = SoundTest.class.getResource(fn);
	     ais = AudioSystem.getAudioInputStream(url);
	     clip = AudioSystem.getClip();
	     clip.open(ais);
	    clip.start(); 
	   // clip.loop(3);
	   } 
	   catch(Exception e){e.printStackTrace();}
	 } // end of constructor

	 // one command line argument must be simple audio file name in one of the formats noted above (mp3 NOT supported)

	 
	 public Boolean isRunning(){
		 if(clip.isRunning()){
			 return true;
		 }
		 else{
			 return false;
		 }
	 }
	 
	 
	 
	 
	 
}


	 
/*	 
	 
	 public static void main(String args[]) throws Exception
	 {
	 SoundTest se = new SoundTest("Sounds/New Lazer 2.WAV");
	 Scanner scan = new Scanner(System.in);
	 scan.nextLine();
	 
	 
	 }
	}
	
	
\*/

