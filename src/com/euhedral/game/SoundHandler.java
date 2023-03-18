package com.euhedral.game;

import com.euhedral.game.Entities.Enemy.Enemy;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.net.URL;

public class SoundHandler {
    int MAX_CLIP = 30;
    Clip clip;
    URL soundURL[] = new URL[MAX_CLIP];

    public Clip BGM_Menu;
    public Clip BGM_GameOver;

    public static final int BGM = 0;
    public static final int BULLET = 1;
    public static final int EXPLOSION = 3;
    public static final int BGM_Game_Over = 4;

    public SoundHandler() {
        initializeSounds();
    }

    private void initializeSounds() {
        soundURL[0] = getClass().getResource("/bgm1.wav");
        soundURL[1] = getClass().getResource("/bullet.wav");
        soundURL[2] = getClass().getResource("/collision.wav");
        soundURL[3] = getClass().getResource("/explosion.wav");
        soundURL[4] = getClass().getResource("/bgmGameOver.wav");

        try {
            BGM_Menu = AudioSystem.getClip();
            BGM_Menu.open(AudioSystem.getAudioInputStream(soundURL[BGM]));

            BGM_GameOver = AudioSystem.getClip();
            BGM_GameOver.open(AudioSystem.getAudioInputStream(soundURL[BGM_Game_Over]));
        } catch (Exception e) {

        }
    }

    public void setFile(int i) {
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

    public void playSound(int i) {
        setFile(i);
        play();
    }

    private void play() {
        clip.start();
        setVolume(VariableHandler.getVolume());
    }

    private void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    // https://stackoverflow.com/questions/40514910/set-volume-of-java-clip

    public float getVolume() {
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        return (float) Math.pow(10f, gainControl.getValue() / 20f);
    }

    public void setVolume(float volume) {
        if (volume < 0f || volume > 1f)
            throw new IllegalArgumentException("Volume not valid: " + volume);
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(20f * (float) Math.log10(volume));
    }

    public void playBGMMenu() {
        playBGM(BGM_Menu);
    }

    public void playBGMGameOver() {
        playBGM(BGM_GameOver);
    }

    private void playBGM(Clip bgm) {
        bgm.setFramePosition(0);
        bgm.start();
        clip = bgm;
        setVolume(VariableHandler.getVolume());
        bgm.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public boolean playingBGMMenu() {
        return clip == BGM_Menu;
    }

}
