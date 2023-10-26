package com.euhedral.game;

import com.euhedral.engine.Utility;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.net.URL;

public class SoundHandler {

    static int volTest = 0;

    static int MAX_CLIP = 30;
    static Clip clip;
    public static Clip effect;
    private static Clip bgm;
    static URL soundURL[] = new URL[MAX_CLIP];

    private static int volumeMaster;
    private static int volumeEffects;
    private static int volumeMusic;
    private final static int VOLUME_DELTA = 1;
    private final static int VOLUME_MAX = 10;
    private final static int VOLUME_MIN = 0;
    private static boolean volumeMasterOn = true;
    private static boolean volumeEffectsOn = true;
    private static boolean volumeMusicOn = true;

    public static final int BGMMAINMENU = 0;
    public static final int BULLET = 1;
    public static final int PICKUP = 2;
    public static final int EXPLOSION = 3;
    public static final int BGMGAMEOVER = 4;
    public static final int BGMPLAY = 5;
    public static final int IMPACT = 6;

    private static int bgmID = -1;

    public SoundHandler() {
        initializeSounds();
    }

    private void initializeSounds() {
        soundURL[BGMMAINMENU] = getClass().getResource("/bgmMain.wav");
        soundURL[BULLET] = getClass().getResource("/bullet.wav");
        soundURL[PICKUP] = getClass().getResource("/pickup.wav");
        soundURL[EXPLOSION] = getClass().getResource("/explosion.wav");
        soundURL[BGMGAMEOVER] = getClass().getResource("/bgmGameOver.wav");
        soundURL[BGMPLAY] = getClass().getResource("/bgm1.wav");
        soundURL[IMPACT] = getClass().getResource("/impact.wav");
    }

    public static void setFile(int i) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception e) {

        }
    }

    public static Clip setClip(int i) {
        Clip newClip = null;
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            newClip = AudioSystem.getClip();
            newClip.open(ais);
        } catch (Exception e) {

        }
        return newClip;
    }

    public void playMusic(int i) {
        setFile(i);
        play();
        loop();
    }

    public void stopMusic() {
        clip.stop();
    }

    public static void playSound(int i) {
        setFile(i);
        play();
    }

    static private void play() {
        clip.start();
        gainControlVolumeMaster();
    }

    private void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    // https://stackoverflow.com/questions/40514910/set-volume-of-java-clip

//    public float getVolume() {
//        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
//        return (float) Math.pow(10f, gainControl.getValue() / 20f);
//    }

    public static void gainControlVolumeMaster() {
        if (clip == null)
            return;
        float volumeF = (float) volumeMaster/10;
        if (!volumeMasterOn) {
            volumeF = 0;
        }
        if (volumeF < 0f || volumeF > 1f)
            throw new IllegalArgumentException("Volume not valid: " + volumeF);
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        FloatControl bgmGainControl = (FloatControl) bgm.getControl(FloatControl.Type.MASTER_GAIN);

        gainControl.setValue(20f * (float) Math.log10(volumeF));
        bgmGainControl.setValue(20f * (float) Math.log10(volumeF));

//        Utility.log("Master Volume set to " + (int) (volumeF*100) + "%");
    }

    public static void gainControlVolumeMusic() {
        if (bgm == null)
            return;
        float volumeF = (float) volumeMusic/10;
        if (!volumeMusicOn) {
            volumeF = 0;
        }
        if (volumeF < 0f || volumeF > 1f)
            throw new IllegalArgumentException("Volume not valid: " + volumeF);
        FloatControl gainControl = (FloatControl) bgm.getControl(FloatControl.Type.MASTER_GAIN);

        gainControl.setValue(20f * (float) Math.log10(volumeF));

        Utility.log("Music Volume set to " + (int) (volumeF*100) + "%");
    }

    public static void gainControlVolumeEffects() {
        // stub
    }

    public static void playBGMMenu() {
        playBGM(BGMMAINMENU);
    }

    public static void playBGMPlay() {
        playBGM(BGMPLAY);
    }

    public static void playBGMGameOver() {
        playBGM(BGMGAMEOVER);
    }

    private static void playBGM(int soundID) {
        boolean noBgmPlaying = bgm == null;
        boolean sameBgmPlaying = bgmID == soundID;

        if (noBgmPlaying) {
            bgmHelper(soundID);
        } else if (sameBgmPlaying) {

        } else {
            bgm.stop();
            bgmHelper(soundID);
        }
    }

    private static void bgmHelper(int soundID) {
        bgm = setClip(soundID);
        clip = bgm;
        gainControlVolumeMaster();
        bgm.start();
        bgm.loop(Clip.LOOP_CONTINUOUSLY);
        bgmID = soundID;
    }

//    public boolean playingBGMMenu() {
//        return clip == bgm_Main;
//    }

    /******************
     * Volume Control *
     ******************/

    public static void toggleVolumeMaster() {
        if (volTest == 1) {
            System.out.println("Testing");
        }
        volumeMasterOn = !volumeMasterOn;
        gainControlVolumeMaster();
    }

    public static void toggleVolumeMusic() {
        volumeMusicOn = !volumeMusicOn;
        // gainControlVolume
    }

    public static void toggleVolumeEffects() {
        volumeEffectsOn = !volumeEffectsOn;
        // gainControlVolume
    }

    public static void volumeMasterUp() {
        if (volumeMaster >= VOLUME_MAX) {
            return;
        }

        setVolumeMaster(volumeMaster + VOLUME_DELTA);
        gainControlVolumeMaster();
    }

    public static void volumeMasterDown() {
        if (volumeMaster <= VOLUME_MIN) {
            return;
        }

        setVolumeMaster(volumeMaster - VOLUME_DELTA);
        gainControlVolumeMaster();

        volTest += 1;
    }

    public static void volumeMusicUp() {
        if (volumeMusic >= VOLUME_MAX) {
            return;
        }

        setVolumeMusic(volumeMusic + VOLUME_DELTA);
        gainControlVolumeMusic();
    }

    public static void volumeMusicDown() {
        if (volumeMusic <= VOLUME_MIN) {
            return;
        }

        setVolumeMusic(volumeMusic - VOLUME_DELTA);
        gainControlVolumeMusic();
    }

    public static void volumeEffectsUp() {
        if (volumeEffects >= VOLUME_MAX) {
            return;
        }

        setVolumeEffects(volumeEffects + VOLUME_DELTA);
        gainControlVolumeMaster();
    }

    public static void volumeEffectsDown() {
        if (volumeEffects <= VOLUME_MIN) {
            return;
        }

        setVolumeEffects(volumeEffects - VOLUME_DELTA);
        gainControlVolumeMaster();
    }

    /*****************
     * Getter/Setter *
     *****************/

    public static boolean isVolumeMaster() {
        return volumeMasterOn;
    }

    public static boolean isVolumeMusic() {
        return volumeMusicOn;
    }

    public static boolean isVolumeEffects() {
        return volumeEffectsOn;
    }

    public static int getVolumeMaster() {return volumeMaster;}
    public static int getVolumeMusic() {
        return volumeMusic;
    }

    public static int getVolumeEffects() {
        return volumeEffects;
    }

    public static void setVolumeMaster(int volume) {
        volumeMaster = volume;
        Utility.log(Integer.toString(volumeMaster));
    }

    public static void setVolumeMusic(int volume) {
        volumeMusic = volume;
        Utility.log(Integer.toString(volumeMusic));
    }

    public static void setVolumeEffects(int volume) {
        volumeEffects = volume;
        Utility.log(Integer.toString(volumeEffects));
    }

}
