package com.dragon.superplayer.view;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dragon.superplayer.R;
import com.dragon.superplayer.interfaces.IPlayerControllerViewInterface;
import com.dragon.superplayer.listener.PlayerControllerViewButtonClickListener;

/**
 * 播放界面控制层的layout，仅供PlayerView持有
 * @author yeguolong
 */
public class PlayerControllerView implements IPlayerControllerViewInterface,
        OnClickListener {

    private PlayerControllerViewButtonClickListener mPlayerControllerViewButtonClickListener;

    private Context mContext;
    private RelativeLayout mPlayerControllerView;

    // 上面的控制栏：
    private TextView mTitle;// 标题

    // 下面的控制栏：
    private Button mPlayPauseBtn;// 播放暂停按钮
    private Button mOrientationSwitchButton;// 横竖屏切换按钮
    private PlayerSeekBar mPlayerSeekBar;

    public PlayerControllerView(Context context) {
        this.init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        this.initView();
        this.setListener();
    }

    private void initView() {
        this.mPlayerControllerView = (RelativeLayout) View.inflate(
                this.mContext, R.layout.player_controller_view, null);
        this.initUpperView();
        this.initBottomView();
    }

    private void initUpperView() {
        this.mTitle = (TextView) this.mPlayerControllerView
                .findViewById(R.id.player_controller_textview_title);
    }

    private void initBottomView() {
        this.mPlayPauseBtn = (Button) this.mPlayerControllerView
                .findViewById(R.id.player_btn_play_pause);
        this.mOrientationSwitchButton = (Button) this.mPlayerControllerView
                .findViewById(R.id.player_orientation_switch_btn);
        this.mPlayerSeekBar = (PlayerSeekBar) this.mPlayerControllerView
                .findViewById(R.id.player_seekbar_layout);
    }

    private void setListener() {
        this.mPlayPauseBtn.setOnClickListener(this);
        this.mOrientationSwitchButton.setOnClickListener(this);
    }

    @Override
    public ViewGroup getPlayerControllerView() {
        return this.mPlayerControllerView;
    }

    @Override
    public void setVisibility(int visible) {
        this.mPlayerControllerView.setVisibility(visible);
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
            PlayerControllerViewButtonClickListener playerControllerViewButtonClickListener) {
        this.mPlayerControllerViewButtonClickListener = playerControllerViewButtonClickListener;
    }

}
