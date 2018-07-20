package cn.pocdoc.majiaxian.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import cn.pocdoc.majiaxian.MainApplication;
import cn.pocdoc.majiaxian.config.Config;

/**
 * Created by Administrator on 2015/1/22 0022.
 */
public class SoundPlayer {
    private MediaPlayer player;
    private boolean isPause = false;
    private int currentVolume;

    public SoundPlayer() {
        this.init();
    }

    private void init() {
        player = MediaPlayer.create(MainApplication.getInstance(), Config.soundResource[1]);
        player.setLooping(false);
        currentVolume = PreferencesUtils.getInt(MainApplication.getInstance(), "currentVolume", 0);
    }

    public void play() {
        if (player != null && !player.isPlaying()) {
            LogUtil.d("sound", "mediaplayer start");
            player.start();
        }
    }

    public void pause() {
        if (!isPause && player != null && player.isPlaying()) {
            isPause = true;
            player.pause();
        }
    }

    public boolean isPlaying() {
        return player != null && player.isPlaying();
    }

    public void rePlay() {
        if (player != null && isPause) {
            player.start();
            isPause = false;
        }
    }

    public void stop() {
        if (player != null) {
            player.stop();
            player.release();
        }
    }

    public void setVolumn(boolean b) {
        if (player != null) {
            AudioManager audioManager = (AudioManager) MainApplication.getInstance().getSystemService(Context.AUDIO_SERVICE);
            if (b) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
            } else {
                currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                PreferencesUtils.putInt(MainApplication.getInstance(), "currentVolume", currentVolume);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
            }
        }
    }

    public int getVolume() {
        AudioManager audioManager = (AudioManager) MainApplication.getInstance().getSystemService(Context.AUDIO_SERVICE);
        return audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }
}
