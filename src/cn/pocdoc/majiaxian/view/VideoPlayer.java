package cn.pocdoc.majiaxian.view;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;

import java.io.FileInputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by pengwei on 15/1/16.
 * 自定义视频播放器
 */
public class VideoPlayer extends VideoView {

    private Context context;
    //    private MediaController mediaController;
    private onControlListener callback;
    private boolean isError;

    FileInputStream fileInputStream = null;

    public interface onControlListener {
        void onCompletionListener(MediaPlayer mediaPlayer);

        void onErrorListener(MediaPlayer mediaPlayer);
    }

    public VideoPlayer(Context context) {
        super(context);
        this.init(context);
    }

    public VideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context);
    }

    public VideoPlayer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.init(context);
    }

    private void init(Context context) {
        this.context = context;
        isError = false;
    }

    public void play(int resourceId) {
        this.setVideoURI(Uri.parse("android.resource://" + context.getPackageName() + "/" + resourceId));
        play();
    }

    /**
     * Attention: remember to release resource yourself!!
     * @param fileName
     */
    public void play(String fileName) {
        FileDescriptor fd = null;
        try {
            String path = context.getFilesDir() + "/videos/" + fileName;
            fileInputStream = new FileInputStream(path);
            fd = fileInputStream.getFD();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (IOException e){
            e.printStackTrace();
            return;
        }
        setVideoFD(fd);
        play();
    }

    public void play(FileInputStream fis){
        try {
            setVideoFD(fis.getFD());
        } catch (IOException e) {
            e.printStackTrace();
        }
        play();
    }

    private void play() {
//        mediaController = new MediaController(context);
//        mediaController.setVisibility(View.INVISIBLE);
//        mediaController.setEnabled(false);
//        mediaController.setMediaPlayer(this);
        this.setMediaController(null);
        this.requestFocus();
        this.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                       @Override
                                       public void onPrepared(MediaPlayer mediaPlayer) {
                                           mediaPlayer.start();
                                       }
                                   }

        );
        this.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                         @Override
                                         public void onCompletion(MediaPlayer mp) {
                                             if (callback != null && !isError)
                                                 callback.onCompletionListener(mp);
                                         }
                                     }

        );
        this.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                                    @Override
                                    public boolean onError(MediaPlayer mp, int what, int extra) {
                                        if (callback != null) {
                                            isError = true;
                                            callback.onErrorListener(mp);
                                        }
                                        return true;
                                    }
                                }

        );
    }

    /**
     * 暂停后重新播放
     */

    public void reStart() {
        if (!isPlaying()) {
            super.start();
        }
    }

    /**
     * 暂停播放
     */
    public void pause() {
        if (isPlaying())
            super.pause();
    }

    /**
     * 是否正在播放
     *
     * @return
     */
    public boolean isPlaying() {
        return super.isPlaying();
    }

    public void setOnControlListener(onControlListener callback) {
        this.callback = callback;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    /**
     * release resource, such as close the inputstream
     */
    public void relase(){
        try {
            if (fileInputStream != null)
                fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
