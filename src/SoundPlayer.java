import java.io.File;
import java.util.EnumMap;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundPlayer {

	private static SoundPlayer instance;
	//data
	
	public static SoundPlayer getInstance() {
		if(instance == null) {
			instance = new SoundPlayer();	
		}
		return instance;
	} //getInstance()

	
	public enum SoundList {
		Jjangu, MapleMain, Ellinia, Curning, Ludibriam, ButtonSound01
	} //bgm enum
	
	private Map<SoundList, String> soundPath; //경로
	private Map<SoundList, Clip> clips; //clip
	private SoundList nowPlay; //현재 bgm
	//data 
	
	public SoundPlayer() {
		
		/*
		 * EnumMap 참고
		 * https://stackoverflow.com/questions/12669497/using-enum-as-key-for-map
		 * */
		soundPath = new EnumMap<SoundList, String>(SoundList.class);
		clips = new EnumMap<SoundList, Clip>(SoundList.class);
		
		
		/* 사운드 경로 초기화 */
		soundPath.put(SoundList.MapleMain, 	"메이플메인.wav");
		soundPath.put(SoundList.Jjangu, 	"짱구.wav");
		soundPath.put(SoundList.Ellinia, 	"엘리니아.wav");
		soundPath.put(SoundList.Curning, 	"커닝시티.wav");
		soundPath.put(SoundList.Ludibriam,  "루디브리엄.wav");
		soundPath.put(SoundList.ButtonSound01,  "btn_sound.wav");
		
		clips.clear();
	} //SoundPlayer()
	
	private final String bgmDir = "res/bgm/";
	
	public void play(SoundList s) {

		
		if(clips.get(s) == null) {

				File f = new File(bgmDir + soundPath.get(s));
				try {
				AudioInputStream stream = AudioSystem.getAudioInputStream(f);
				Clip clip = AudioSystem.getClip();
				clip.open(stream);
				clips.put(s, clip);
					//set bgm clip
			} catch(Exception e) {
				e.printStackTrace();
			}
		} //

		
		if(nowPlay != null) {
			this.stop(nowPlay);
		}		
		//clips.get(s).setFramePosition(10);		
		clips.get(s).loop(Clip.LOOP_CONTINUOUSLY); //loop play
		
		nowPlay = s;
	} //play()

	public void playOnce(SoundList s) {
		File f = new File(bgmDir + soundPath.get(s));
		try {
		AudioInputStream stream = AudioSystem.getAudioInputStream(f);
		Clip clip = AudioSystem.getClip();
		clip.open(stream);
		clip.start(); //play once
		} catch(Exception e) {
			e.printStackTrace();
		}
	}//playOnce()
	
	public void stop(SoundList s) {
		if(clips.get(s) != null) {
			clips.get(s).stop(); //bgm stop
		}
	}//stop()

} //SoundPlayer class
