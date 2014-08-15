package com.adonnai.vitamiotestproject;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnInfoListener;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity implements OnInfoListener, 
			OnBufferingUpdateListener {
	private Uri uri;
	private VideoView mVideoView;
	private boolean isStart;
	private ProgressBar pb;
	private String mVideoPath = "";
	private String mVideoName = "";
	private MediaController mMediaController;
	private TextView downloadRateView, loadRateView;

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (!LibsChecker.checkVitamioLibs(this))
			return;

		setContentView(R.layout.activity_video_view);

		mVideoView = (VideoView) findViewById(R.id.buffer);
		pb = (ProgressBar) findViewById(R.id.probar);

		downloadRateView = (TextView) findViewById(R.id.download_rate);
		loadRateView = (TextView) findViewById(R.id.load_rate);

		mVideoName = "This is a dummy name!";
		mVideoPath  = "rtmp://cp269609.edgefcs.net/ondemand/mp4:207011/mm/flvmedia/2627/n/3/a/n3a210me_g26264gg_h264_1328K.mp4?cid=2627&aid=2191717&afid=4152437";

		if(mVideoPath.trim().length() > 0) {
			/*
			 * Alternatively,for streaming media you can use
			 * mVideoView.setVideoURI(Uri.parse(URLstring));
			 */
			uri = Uri.parse(mVideoPath);
			
			// commenting the line below,since only 
			// audio sounds but the video cannot be seen
			// mVideoView.setHardwareDecoder(true);
			// changes end here
			
			mVideoView.setVideoURI(uri);
			mMediaController = new MediaController(this);
			mVideoView.setMediaController(mMediaController);
			mMediaController.setFileName(mVideoName);
			mVideoView.requestFocus();
			mVideoView.setOnInfoListener(this);
			mVideoView.setOnBufferingUpdateListener(this);
			mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mediaPlayer) {
					// optional need Vitamio 4.0
					mediaPlayer.setPlaybackSpeed(1.0f);
					mMediaController.setFileName(mVideoName);
				}
			});
		}
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		loadRateView.setText(percent + "%");
	}

	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		switch (what) {
		case MediaPlayer.MEDIA_INFO_BUFFERING_START:
			if (mVideoView.isPlaying()) {
				mVideoView.pause();
				isStart = true;
				pb.setVisibility(View.VISIBLE);
				downloadRateView.setVisibility(View.VISIBLE);
				loadRateView.setVisibility(View.VISIBLE);

			}
			break;
		case MediaPlayer.MEDIA_INFO_BUFFERING_END:
			if (isStart) {
				mVideoView.start();
				pb.setVisibility(View.GONE);
				downloadRateView.setVisibility(View.GONE);
				loadRateView.setVisibility(View.GONE);
			}
			break;
		case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
			downloadRateView.setText("" + extra + "kb/s" + "  ");
			break;
		}
		return true;
	}

}

