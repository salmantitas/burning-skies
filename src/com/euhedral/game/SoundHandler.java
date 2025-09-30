package com.euhedral.game;

import com.euhedral.engine.Engine;
import com.euhedral.engine.GameState;
import com.euhedral.engine.Utility;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.net.URL;
import java.util.HashMap;

public class SoundHandler {

    static int volTest = 0;

    public static final int BGMMAINMENU = 0;
    public static final int BULLET_PLAYER = 1;
    public static final int PICKUP = 2;
    public static final int EXPLOSION = 3;
    public static final int EXPLOSION_PLAYER = 4;
    public static final int BGMGAMEOVER = 5;
    public static final int IMPACT = 6;
    public static final int BULLET_ENEMY = 7;
    public static final int UI1 = 8;
    public static final int UI2 = 9;
    public static final int SHIELD_1 = 10;
    public static final int SHIELD_2 = 11;
    public static final int SHIELD_3 = 12;
    public static final int RING = 13;

    static int MAX_CLIP = RING + 1;

//    public static final int BGMPLAY1 = 0;
//    public static final int BGMPLAY2 = 1;

    private static final int BGM_MAX = 2;

    static Clip clip;
    public static Clip effect;
    private static Clip bgm;
    static Clip newClip = null;
    static URL soundURL[] = new URL[MAX_CLIP];
    static Clip clips[] = new Clip[MAX_CLIP];
    static URL BGM_URL[] = new URL[BGM_MAX];
    static Clip BGMs[] = new Clip[BGM_MAX];

    private static int currentBGM = -1;

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

    private static int bgmID = -1;
    private static int bgmGameID = -1;

    static boolean noBgmPlaying;
    static boolean sameBgmPlaying;

    private static String[] songInfo = new String[BGM_MAX];

    public SoundHandler() {
        initializeSounds();
    }

    private void initializeSounds() {
        soundURL[BGMMAINMENU] = getClass().getResource("/bgmMain.wav");
        soundURL[BULLET_PLAYER] = getClass().getResource("/bullet_player.wav");
        soundURL[BULLET_ENEMY] = getClass().getResource("/bullet_enemy.wav");
        soundURL[PICKUP] = getClass().getResource("/pickup.wav");
        soundURL[EXPLOSION] = getClass().getResource("/explosion.wav");
        soundURL[EXPLOSION_PLAYER] = getClass().getResource("/explosion_player.wav");
        soundURL[BGMGAMEOVER] = getClass().getResource("/bgmGameOver.wav");
        soundURL[IMPACT] = getClass().getResource("/impact.wav");
        soundURL[UI1] = getClass().getResource("/clip_ui_1.wav");
        soundURL[UI2] = getClass().getResource("/clip_ui_2.wav");
        soundURL[SHIELD_1] = getClass().getResource("/shield_1.wav");
        soundURL[SHIELD_2] = getClass().getResource("/shield_2.wav");
        soundURL[SHIELD_3] = getClass().getResource("/shield_3.wav");
        soundURL[RING] = getClass().getResource("/ringOfFire.wav");


        for (int i = 0; i < MAX_CLIP; i ++) {
            clips[i] = setClip(i);
        }

        songInfo[0] = "Mountain Trails - Joshua McLean";
        songInfo[1] = "Pixel Wars 1 - Abstraction";
//        songInfo[2] = "Battle Commences - Not Jam";

        for (int i = 0; i < BGM_MAX; i ++) {
            BGM_URL[i] = getClass().getResource("/bgm" + (i + 1 + ".wav"));
        }

        for (int i = 0; i < BGM_MAX; i ++) {
            BGMs[i] = setClipBGM(i);
        }

        volumeEffects = VOLUME_MAX;
        volumeMusic = VOLUME_MAX;

//        Utility.log("Initializing sounds");
    }

    public void update() {
//        No point
//        for (int i = 0; i < MAX_CLIP; i ++) {
//            if (!clips[i].isRunning()) {
//
//            }
//        }

//        if (Engine.stateIs(GameState.Game)) {
//            if (!BGMs[currentBGM].isRunning()) {
//                BGMUp();
//            }
//        }
    }

    // Assumes all clips have already been loaded
    public static void setFile(int i) {
        clip = clips[i];
        clip.setFramePosition(0);
    }

    // Assumes all clips have already been loaded
    public static void setFileBGM(int i) {
        clip = BGMs[i];
        clip.setFramePosition(0);
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

    public static Clip setClipBGM(int i) {
        newClip = null;
        try {
            ais = AudioSystem.getAudioInputStream(BGM_URL[i]);
            newClip = AudioSystem.getClip();
            newClip.open(ais);
        } catch (Exception e) {

        }
//        Utility.log("Set Clip");
        return newClip;
    }

//    public void playMusic(int i) {
//        setFile(i);
//        play();
//        loop();
//    }

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
//        clip.
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

    public static void playBGMPlay() {
        if (Engine.previousState == GameState.Pause) {
            return;
        }

        if (currentBGM == -1) {
            currentBGM = Utility.randomRangeInclusive(0, BGM_MAX-1);
        } else {
            currentBGM = (currentBGM + 1) % (BGM_MAX);
        }

        sameBgmPlaying = bgmGameID == currentBGM;

        if (sameBgmPlaying) {

        }
        else {
            bgm.stop();
            bgmPlayHelper(currentBGM);
        }
    }

    public static void playBGMMenu() {
        playBGM(BGMMAINMENU);
//        Utility.log("Play BGM Menu");
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
        setFile(soundID);
        bgm = clip;
        gainControlVolumeMaster();
        bgm.start();
        bgm.loop(Clip.LOOP_CONTINUOUSLY);
        bgmID = soundID;
        bgmGameID = -1;
//        Utility.log("BGM Helper");
    }

    private static void bgmPlayHelper(int soundID) {
        bgm.stop();
        setFileBGM(soundID);
        bgm = clip;
        gainControlVolumeMaster();
        bgm.start();
        bgm.loop(Clip.LOOP_CONTINUOUSLY);
        bgmGameID = soundID;
        bgmID = -1;
//        Utility.log("BGM Helper");
    }

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
        gainControlVolumeMusic();
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

    public static void BGMUp() {
        currentBGM = (currentBGM + 1) % (BGM_MAX);
        bgmPlayHelper(currentBGM);
    }

    public static void BGMDown() {
        currentBGM = (currentBGM - 1) % (BGM_MAX);
        if (currentBGM < 0) {
            currentBGM += BGM_MAX;
        }
        bgmPlayHelper(currentBGM);
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

    public static String getSongName() {
        if (currentBGM == -1)
            return "";
        else return songInfo[currentBGM];
    }

    public static boolean gameBGMRunning() {
        return bgmGameID > -1;
    }
}
