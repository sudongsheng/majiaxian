package cn.pocdoc.majiaxian.helper;

import android.os.CountDownTimer;

/**
 * Created by Administrator on 2015/1/19 0019.
 */
public class CountTimerHelper extends CountDownTimer {
    private boolean isRunning = false;
    private MyCountDownTimerListener listener;

    public CountTimerHelper(long millisInFuture, long countDownInterval, MyCountDownTimerListener listener) {
        super(millisInFuture, countDownInterval);
        isRunning = false;
        if (listener != null) {
            this.listener = listener;
        }
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if (listener != null) {
            listener.onTick((int) (millisUntilFinished / 1000));
        }
    }

    @Override
    public void onFinish() {
        isRunning = false;
        listener.onFinish();
    }

    public void TimeStart() {
        if (!isRunning) {
            isRunning = true;
            this.start();
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void TimeCacel() {
        if (isRunning)
            this.cancel();
    }

    public interface MyCountDownTimerListener {

        public void onTick(int count);

        public void onFinish();

    }
}
