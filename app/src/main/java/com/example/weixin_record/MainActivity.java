package com.example.weixin_record;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nickming.view.AudioRecordButton;
import com.nickming.view.AudioRecordButton.AudioFinishRecorderListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    AudioRecordButton button;

    private ListView mlistview;
    private ArrayAdapter<Recorder> mAdapter;
    private View viewanim;
    private View currentView;
    private List<Recorder> mDatas = new ArrayList<MainActivity.Recorder>();
    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mlistview = (ListView) findViewById(R.id.listview);

        button = (AudioRecordButton) findViewById(R.id.recordButton);
        button.setAudioFinishRecorderListener(new AudioFinishRecorderListener() {

            @Override
            public void onFinished(float seconds, String filePath) {
                // TODO Auto-generated method stub
                Recorder recorder = new Recorder(seconds, filePath);
                mDatas.add(recorder);
                mAdapter.notifyDataSetChanged();
                mlistview.setSelection(mDatas.size() - 1);
            }
        });

        mAdapter = new RecorderAdapter(this, mDatas);
        mlistview.setAdapter(mAdapter);

        mlistview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // 播放动画
                if (viewanim != null) {//让第二个播放的时候第一个停止播放
                    currentView = viewanim;
                    viewanim.setBackgroundResource(R.drawable.adj);
                    viewanim = null;
                    MediaManager.stop();
                }
                viewanim = view.findViewById(R.id.id_recorder_anim);
                if (currentView == viewanim) {
                    if (isPlaying){
                        isPlaying = false;
                        return;
                    }
                }
                viewanim.setBackgroundResource(R.drawable.play);
                AnimationDrawable drawable = (AnimationDrawable) viewanim
                        .getBackground();
                drawable.start();
                isPlaying = true;
                // 播放音频
                MediaManager.playSound(mDatas.get(position).filePathString,
                        new MediaPlayer.OnCompletionListener() {

                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                isPlaying = false;
                                viewanim.setBackgroundResource(R.drawable.adj);
                            }
                        });
            }
        });
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MediaManager.pause();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MediaManager.resume();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        MediaManager.release();
    }

    class Recorder {
        float time;
        String filePathString;

        public Recorder(float time, String filePathString) {
            super();
            this.time = time;
            this.filePathString = filePathString;
        }

        public float getTime() {
            return time;
        }

        public void setTime(float time) {
            this.time = time;
        }

        public String getFilePathString() {
            return filePathString;
        }

        public void setFilePathString(String filePathString) {
            this.filePathString = filePathString;
        }

    }

}
