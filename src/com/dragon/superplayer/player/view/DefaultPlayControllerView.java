package com.dragon.superplayer.player.view;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.dragon.superplayer.R;
import com.dragon.superplayer.player.view.callback.PlayControllerViewCallback;
import com.dragon.superplayer.player.view.interfaces.IPlayerControllerViewInterface;

/**
 * 播放界面控制层的layout
 * @author yeguolong
 */
public class DefaultPlayControllerView implements
        IPlayerControllerViewInterface, OnClickListener {

    private PlayControllerViewCallback mPlayerControllerViewButtonClickListener;// 控制面板点击回调接口

    private Context mContext;
    private RelativeLayout mPlayerControllerUpperView;
    private RelativeLayout mPlayerControllerBottomView;

    // 上面的控制栏：
    private TextView mTitle;// 标题

    // 下面的控制栏：
    private ImageView mPlayPauseBtn;// 播放暂停按钮
    private Button mOrientationSwitchButton;// 横竖屏切换按钮
    private DefaultPlayerSeekBar mPlayerSeekBar;// 播放进度条

    public enum PlayPauseBtnStatus {
        PLAY,
        PAUSE,
        FAST_RUN,
        FAST_BACK,
        STOP
    }

    public DefaultPlayControllerView(Context context) {
        this.init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        this.initView();
        this.setListener();
    }

    private void initView() {
        this.initUpperView();
        this.initBottomView();
    }

    private void initUpperView() {
        this.mPlayerControllerUpperView = (RelativeLayout) View.inflate(
                this.mContext, R.layout.player_controller_upper_view, null);
        this.mTitle = (TextView) this.mPlayerControllerUpperView
                .findViewById(R.id.player_controller_tv_title);
    }

    private void initBottomView() {
        this.mPlayerControllerBottomView = (RelativeLayout) View.inflate(
                this.mContext, R.layout.player_controller_bottom_view, null);

        this.mPlayPauseBtn = (ImageView) this.mPlayerControllerBottomView
                .findViewById(R.id.player_btn_play_pause);
        this.mOrientationSwitchButton = (Button) this.mPlayerControllerBottomView
                .findViewById(R.id.player_orientation_switch_btn);
        this.mPlayerSeekBar = (DefaultPlayerSeekBar) this.mPlayerControllerBottomView
                .findViewById(R.id.player_seekbar_layout);
    }

    private void setListener() {
        this.mPlayPauseBtn.setOnClickListener(this);
        this.mOrientationSwitchButton.setOnClickListener(this);
    }

    @Override
    public ViewGroup getPlayerControllerUpperView() {
        return this.mPlayerControllerUpperView;
    }

    @Override
    public ViewGroup getPlayerControllerBottomView() {
        return this.mPlayerControllerBottomView;
    }

    @Override
    public void setVisibility(int visible) {
        this.mPlayerControllerUpperView.setVisibility(visible);
        this.mPlayerControllerBottomView.setVisibility(visible);
    }

    @Override
    public void setTitle(String title) {
        if (this.mTitle != null && title != null) {
            this.mTitle.setText(title);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.player_btn_play_pause:// 播放暂停按钮
            if (this.mPlayerControllerViewButtonClickListener != null) {
                this.mPlayerControllerViewButtonClickListener
                        .onPlayPauseButtonClick();
            }
            break;
        case R.id.player_orientation_switch_btn:// 横竖屏切换按钮
            if (this.mPlayerControllerViewButtonClickListener != null) {
                this.mPlayerControllerViewButtonClickListener
                        .onOrientationSwitchButtonClick();
            }
            break;
        }
    }

    @Override
    public void setPlayerControllerViewButtonClickListener(
            PlayControllerViewCallback playerControllerViewButtonClickListener) {
        this.mPlayerControllerViewButtonClickListener = playerControllerViewButtonClickListener;
    }

    @Override
    public boolean isShowing() {
        if (this.mPlayerControllerUpperView != null) {
            return this.mPlayerControllerUpperView.isShown();
        }
        return false;
    }

    @Override
    public void setPlayerSeekbarProgress(int currentPostion) {
        if (this.mPlayerSeekBar != null) {
            this.mPlayerSeekBar.setProgress(currentPostion);
        }
    }

    @Override
    public int getPlayerSeekbarCurrentProgress() {
        if (this.mPlayerSeekBar != null) {
            return this.mPlayerSeekBar.getProgress();
        }
        return 0;
    }

    @Override
    public void setPlayerSeekbarMax(int max) {
        if (this.mPlayerSeekBar != null) {
            this.mPlayerSeekBar.setMax(max);
        }
    }

    @Override
    public void setPlayerSeekBarChangeListener(OnSeekBarChangeListener l) {
        this.mPlayerSeekBar.setOnSeekBarChangeListener(l);
    }

    @Override
    public void updatePlayPauseBtn(PlayPauseBtnStatus playPauseBtnStatus) {
        switch (playPauseBtnStatus) {
        case PLAY:
            this.mPlayPauseBtn
                    .setImageResource(R.drawable.player_play_btn_selector);
            break;
        case PAUSE:
            this.mPlayPauseBtn
                    .setImageResource(R.drawable.player_pause_btn_selector);
            break;
        case FAST_RUN:
            this.mPlayPauseBtn.setImageResource(R.drawable.player_icon_forward);
            break;
        case FAST_BACK:
            this.mPlayPauseBtn.setImageResource(R.drawable.player_icon_rewind);
            break;
        case STOP:
            this.mPlayPauseBtn.setImageResource(R.drawable.btn_play_disabled);
            break;
        default:
            break;
        }
    }

}
