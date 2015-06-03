package com.dragon.superplayer.view;

import android.content.Context;
import android.view.Gravity;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.dragon.superplayer.R;
import com.dragon.superplayer.interfaces.IPlayerControllerViewInterface;
import com.dragon.superplayer.interfaces.IPlayerViewInterface;
import com.dragon.superplayer.listener.PlayerControllerViewButtonClickListener;

/**
 * 整个播放界面的layout，仅供IViewController的实现类持有
 * @author yeguolong
 */
public class PlayerView implements IPlayerViewInterface {

    private Context mContext;
    private RelativeLayout mPlayerView;
    private SurfaceView mSurfaceView;
    private IPlayerControllerViewInterface mIPlayerControllerViewInterface;

    public PlayerView(Context context) {
        this.init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        this.initView(context);
    }

    private void initView(Context context) {
        this.mPlayerView = (RelativeLayout) View.inflate(context,
                R.layout.player_view, null);
        this.mPlayerView.setGravity(Gravity.CENTER);
        this.mSurfaceView = (SurfaceView) this.mPlayerView
                .findViewById(R.id.player_surfaceview);
        this.initPlayerControllerView(context);
    }

    private void initPlayerControllerView(Context context) {
        this.mIPlayerControllerViewInterface = new PlayerControllerView(context);
        this.mPlayerView.addView(this.mIPlayerControllerViewInterface
                .getPlayerControllerView());
    }

    @Override
    public SurfaceView getSurfaceView() {
        return this.mSurfaceView;
    }

    @Override
    public void showPlayerControllerView() {
        this.mIPlayerControllerViewInterface.setVisibility(View.VISIBLE);
    }

    @Override
    public void hidePlayerControllerView() {
        this.mIPlayerControllerViewInterface.setVisibility(View.GONE);
    }

    @Override
    public ViewGroup getPlayerView() {
        return this.mPlayerView;
    }

    @Override
    public void setPlayerControllerViewButtonClickListener(
            PlayerControllerViewButtonClickListener playerControllerViewButtonClickListener) {
        if (this.mIPlayerControllerViewInterface != null) {
            this.mIPlayerControllerViewInterface
                    .setPlayerControllerViewButtonClickListener(playerControllerViewButtonClickListener);
        }
    }

}
