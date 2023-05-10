package com.euhedral.game;

import com.euhedral.engine.Utility;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.net.URL;

public class SoundHandler {
    static int MAX_CLIP = 30;
    static Clip clip;
    static URL soundURL[] = new URL[MAX_CLIP];

    public Clip bgm_Main;
    public Clip bgm_Play;
    public Clip bgm_GameOver;

    private Clip current;

    private static int volume = 10;
    private final static int VOLUME_MAX = 10;
    private final static int VOLUME_MIN = 0;

    public static final int BGMMAINMENU = 0;
    public static final int BULLET = 1;
    public static final int PICKUP = 2;
    public static final int EXPLOSION = 3;
    public static final int BGMGAMEOVER = 4;
    public static final int BGMPLAY = 5;
    public static final int IMPACT = 6;

    public SoundHandler() {
        initializeSounds();
    }

    private void initializeSounds() {
        soundURL[0] = getClass().getResource("/bgmMain.wav");
        soundURL[1] = getClass().getResource("/bullet.wav");
        soundURL[2] = getClass().getResource("/pickup.wav");
        soundURL[3] = getClass().getResource("/explosion.wav");
        soundURL[4] = getClass().getResource("/bgmGameOver.wav");
        soundURL[5] = getClass().getResource("/bgm1.wav");
        soundURL[6] = getClass().getResource("/impact.wav");

        try {
            bgm_Main = AudioSystem.getClip();
            bgm_Main.open(AudioSystem.getAudioInputStream(soundURL[BGMMAINMENU]));

            bgm_Play = AudioSystem.getClip();
            bgm_Play.open(AudioSystem.getAudioInputStream(soundURL[BGMPLAY]));

            bgm_GameOver = AudioSystem.getClip();
            bgm_GameOver.open(AudioSystem.getAudioInputStream(soundURL[BGMGAMEOVER]));
        } catch (Exception e) {

        }
    }

    public static void setFile(int i) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception e) {

        }
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
        setVolume(volume);
    }

    private void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    // https://stackoverflow.com/questions/40514910/set-volume-of-java-clip

    public float getVolume() {
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        return (float) Math.pow(10f, gainControl.getValue() / 20f);
    }

    public static void setVolume(int volumeI) {
        float volume = (float) volumeI/10;
        if (volume < 0f || volume > 1f)
            throw new IllegalArgumentException("Volume not valid: " + volume);
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

        gainControl.setValue(20f * (float) Math.log10(volume));

        Utility.log("Volume set to " + (int) (volume*100) + "%");
    }

    public void playBGMMenu() {
        playBGM(bgm_Main);
    }

    public void playBGMPlay() {
        playBGM(bgm_Play);
    }

    public void playBGMGameOver() {
        playBGM(bgm_GameOver);
    }

    private void playBGM(Clip bgm) {
        if (current != null)
            if (current != bgm) {
                current.stop();
                bgm.setFramePosition(0);
            }
        bgm.start();
        clip = bgm;
        setVolume(volume);
        bgm.loop(Clip.LOOP_CONTINUOUSLY);
        current = bgm;
    }

    public boolean playingBGMMenu() {
        return clip == bgm_Main;
    }

    /******************
     * Volume Control *
     ******************/

    public static void toggleVolume() {
        if (isVolume()) {
            volume = 0;
        } else volume = 10;
        SaveLoad.saveSettings();
        setVolume(volume);
    }

    public static void volumeUp() {
        if (volume >= VOLUME_MAX) {
            return;
        }

        volume += 2;
        setVolume(volume);
    }

    public static void volumeDown() {
        if (volume <= VOLUME_MIN) {
            return;
        }

        volume -= 2;
        setVolume(volume);
    }

    public static boolean isVolume() {
        return volume == 10;
    }

}
