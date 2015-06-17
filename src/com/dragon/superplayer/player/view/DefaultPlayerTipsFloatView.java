package com.dragon.superplayer.player.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dragon.superplayer.R;
import com.dragon.superplayer.player.view.interfaces.IPlayerTipsFloatViewInterface;

public class DefaultPlayerTipsFloatView implements
        IPlayerTipsFloatViewInterface {

    private RelativeLayout mTipsFloatLayout;
    private RelativeLayout mLoadingLayout;// 缓冲布局
    private TextView mLoadingTextView;

    public DefaultPlayerTipsFloatView(Context context) {
        this.init(context);
    }

    private void init(Context context) {
        this.mTipsFloatLayout = (RelativeLayout) View.inflate(context,
                R.layout.player_tips_float_layout, null);

        this.mLoadingLayout = (RelativeLayout) this.mTipsFloatLayout
                .findViewById(R.id.player_tips_float_loading_layout);
        this.mLoadingTextView = (TextView) this.mTipsFloatLayout
                .findViewById(R.id.player_tips_float_loading_textview);
    }

    @Override
    public ViewGroup getPlayerTipsFloatView() {
        return this.mTipsFloatLayout;
    }

    @Override
    public void showLoading(String loadingText) {
        if (loadingText != null) {
            this.mLoadingTextView.setText(loadingText);
        }
        this.mTipsFloatLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        this.mTipsFloatLayout.setVisibility(View.GONE);
    }

    @Override
    public void showBrightness(int value) {
        // TODO Auto-generated method stub

    }

    @Override
    public void hideBrightness() {
        // TODO Auto-generated method stub

    }

    @Override
    public void showVolume(int value) {
        // TODO Auto-generated method stub

    }

    @Override
    public void hideVolume() {
        // TODO Auto-generated method stub

    }

}
