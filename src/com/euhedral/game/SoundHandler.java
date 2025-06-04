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
    static Clip newClip = null;
    static URL soundURL[] = new URL[MAX_CLIP];

    static FloatControl gainControl;
    static FloatControl bgmGainControl;

    private static int volumeMaster;
    private static int volumeEffects;
    private static int volumeMusic;
    private final static int VOLUME_DELTA = 1;
    private final static int VOLUME_MAX = 10;
    private final static int VOLUME_MIN = 0;
    private static boolean volumeMasterOn = true;
    private static boolean volumeEffectsOn = true;
    private static boolean volumeMusicOn = true;

    static float volumeF;
    static AudioInputStream ais;

    public static final int BGMMAINMENU = 0;
    public static final int BULLET_PLAYER = 1;
    public static final int PICKUP = 2;
    public static final int EXPLOSION = 3;
    public static final int BGMGAMEOVER = 4;
    public static final int BGMPLAY = 5;
    public static final int IMPACT = 6;
    public static final int BULLET_ENEMY = 7;
    public static final int UI1 = 8;
    public static final int UI2 = 9;

    private static int bgmID = -1;

    static boolean noBgmPlaying;
    static boolean sameBgmPlaying;

    public SoundHandler() {
        initializeSounds();
    }

    private void initializeSounds() {
        soundURL[BGMMAINMENU] = getClass().getResource("/bgmMain.wav");
        soundURL[BULLET_PLAYER] = getClass().getResource("/bullet_player.wav");
        soundURL[BULLET_ENEMY] = getClass().getResource("/bullet_enemy.wav");
        soundURL[PICKUP] = getClass().getResource("/pickup.wav");
        soundURL[EXPLOSION] = getClass().getResource("/explosion.wav");
        soundURL[BGMGAMEOVER] = getClass().getResource("/bgmGameOver.wav");
        soundURL[BGMPLAY] = getClass().getResource("/bgm1.wav");
        soundURL[IMPACT] = getClass().getResource("/impact.wav");
        soundURL[UI1] = getClass().getResource("/clip_ui_1.wav");
        soundURL[UI2] = getClass().getResource("/clip_ui_2.wav");

        volumeEffects = VOLUME_MAX;
        volumeMusic = VOLUME_MAX;

//        Utility.log("Initializing sounds");
    }

    public static void setFile(int i) {
        try {
            ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception e) {

        }
//        Utility.log("Set File");
    }

    public static Clip setClip(int i) {
        newClip = null;
        try {
            ais = AudioSystem.getAudioInputStream(soundURL[i]);
            newClip = AudioSystem.getClip();
            newClip.open(ais);
        } catch (Exception e) {

        }
//        Utility.log("Set Clip");
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
//        Utility.log("Play Sound");
        setFile(i);
        play();
    }

    static private void play() {
//        Utility.log("Play");
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
        volumeF = (float) volumeMaster/10;
        if (!volumeMasterOn) {
            volumeF = 0;
        }
//        if (!volumeMusicOn) {
//            volumeF = 0;
//        }
//        if (!volumeEffectsOn) {
//            volumeF = 0;
//        }

        if (volumeF < 0f || volumeF > 1f)
            throw new IllegalArgumentException("Volume not valid: " + volumeF);
        gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        bgmGainControl = (FloatControl) bgm.getControl(FloatControl.Type.MASTER_GAIN);
//        Utility.log("gainControl: " + gainControl);
//        Utility.log("bgmGainControl: " + bgmGainControl);

        gainControl.setValue(20f * (float) Math.log10(volumeF));
        if (!isVolumeMusic()) {
            volumeF = 0;
        }
        bgmGainControl.setValue(20f * (float) Math.log10(volumeF));

//        Utility.log("Master Volume set to " + (int) (volumeF*100) + "%");
    }

    public static void gainControlVolumeMusic() {
        if (bgm == null)
            return;
        volumeF = (float) volumeMusic/10;
        if (!volumeMusicOn) {
            volumeF = 0;
        }
        if (volumeF < 0f || volumeF > 1f)
            throw new IllegalArgumentException("Volume not valid: " + volumeF);
        gainControl = (FloatControl) bgm.getControl(FloatControl.Type.MASTER_GAIN);

        gainControl.setValue(20f * (float) Math.log10(volumeF));

//        Utility.log("Music Volume set to " + (int) (volumeF*100) + "%");
    }

    public static void gainControlVolumeEffects() {
        // stub
    }

    public static void playBGMMenu() {
        playBGM(BGMMAINMENU);
//        Utility.log("Play BGM Menu");
    }

    public static void playBGMPlay() {
        playBGM(BGMPLAY);
//        Utility.log("Play BGM Game");
    }

    public static void playBGMGameOver() {
        playBGM(BGMGAMEOVER);
//        Utility.log("Play BGM GameOver");
    }

    private static void playBGM(int soundID) {
        noBgmPlaying = bgm == null;
        sameBgmPlaying = bgmID == soundID;

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
//        Utility.log("BGM Helper");
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
//        Utility.log(Integer.toString(volumeMaster));
    }

    public static void setVolumeMusic(int volume) {
        volumeMusic = volume;
//        Utility.log(Integer.toString(volumeMusic));
    }

    public static void setVolumeEffects(int volume) {
        volumeEffects = volume;
//        Utility.log(Integer.toString(volumeEffects));
    }
}
