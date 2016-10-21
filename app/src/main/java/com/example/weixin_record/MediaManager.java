package com.example.weixin_record;


import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

public class MediaManager {

	private static MediaPlayer mPlayer;

	private static boolean isPause;
//    private static boolean isReset = true;//是否被重置了 如果false 则表示正在播放

	public static void playSound(final String filePathString,
								 MediaPlayer.OnCompletionListener onCompletionListener) {
		// TODO Auto-generated method stub
		if (mPlayer == null) {
			mPlayer = new MediaPlayer();
			//保险起见，设置报错监听
			mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					// TODO Auto-generated method stub
					mPlayer.reset();
					return false;
				}
			});
		} else {
			mPlayer.reset();//就恢复
		}

//        if(!isReset){
//            stop();
//            isReset=true;
//            return;
//        }

		try {
			mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mPlayer.setOnCompletionListener(onCompletionListener);
			mPlayer.setDataSource(filePathString);
			mPlayer.prepareAsync();
			mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mp) {
					mPlayer.start();
//                    isReset = false;
				}
			});
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//停止函数
	public static void pause() {
		if (mPlayer != null && mPlayer.isPlaying()) {
			mPlayer.pause();
			isPause = true;
		}
	}

	//停止函数
	public static void stop() {
		if (mPlayer != null && mPlayer.isPlaying()) {
			mPlayer.reset();
//            isReset = true;
		}
	}

	//继续
	public static void resume() {
		if (mPlayer != null && isPause) {
			mPlayer.start();
			isPause = false;
		}
	}


	public static void release() {
		if (mPlayer != null) {
			mPlayer.release();
			mPlayer = null;
		}
	}
}