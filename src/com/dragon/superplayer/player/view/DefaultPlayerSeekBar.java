package com.dragon.superplayer.player.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.dragon.superplayer.R;
import com.dragon.superplayer.player.util.LogUtil;
import com.dragon.superplayer.player.util.TimeUtil;

public class DefaultPlayerSeekBar extends RelativeLayout implements
        SeekBar.OnSeekBarChangeListener {

    protected TextView mBeginTextView;
    protected TextView mEndTextView;
    protected SeekBar mSeekBar;
    protected RelativeLayout timeRelative;
    protected Drawable mThumb;
    protected Context mContext;
    protected String endTimes = "00:00";
    protected int mEndTextViewLeftMargin = 0;

    protected long mEndTime = 0;

    protected OnSeekBarTouchListener mSeekBarTouchListener;

    public Drawable getThumb() {
        return this.mThumb;
    }

    public interface OnSeekBarTouchListener {
        void onSeekBarTouch(MotionEvent motionEvent);
    }

    public void setOnSeekBarTouchListener(OnSeekBarTouchListener listener) {
        this.mSeekBarTouchListener = listener;
    }

    protected OnSeekBarChangeListener mOnSeekBarChangeListener;

    public DefaultPlayerSeekBar(Context context) {
        this(context, null);
    }

    public DefaultPlayerSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DefaultPlayerSeekBar(Context context, AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
        this.init(context, attrs, defStyle);
    }

    public void initBeginTextView() {
        this.mBeginTextView.invalidate();
    }

    protected void init(Context context, AttributeSet attrs, int defStyle) {
        this.mContext = context;
        View.inflate(context, R.layout.player_seekbar_layout, this);
        this.mSeekBar = (SeekBar) this
                .findViewById(R.id.player_progress_seekbar);
        this.mBeginTextView = (TextView) this
                .findViewById(R.id.seek_start_time);
        this.mEndTextView = (TextView) this.findViewById(R.id.seek_end_time);
        this.timeRelative = (RelativeLayout) this
                .findViewById(R.id.player_seekbar_time_container);

        this.mSeekBar.setOnSeekBarChangeListener(this);
        this.mThumb = this.mSeekBar.getThumb();
        this.mSeekBar.setThumbOffset(0);
        this.mSeekBar.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    if (DefaultPlayerSeekBar.this.mSeekBarTouchListener != null) {
                        DefaultPlayerSeekBar.this.mSeekBarTouchListener
                                .onSeekBarTouch(motionEvent);
                    }
                    break;
                }
                return false;
            }
        });
    }

    public int getMax() {
        return this.mSeekBar.getMax();
    }

    public int getProgress() {
        return this.mSeekBar.getProgress();
    }

    public void setEnable(boolean enabled) {
        this.mSeekBar.setEnabled(enabled);
    }

    public void setMax(int max) {
        LogUtil.d("setMax-->max:" + max);
        this.mSeekBar.setMax(max);
        this.endTimes = TimeUtil.getStringTime(max);
    }

    public void setSeekBarVisiAble(int visiAble) {
        this.mSeekBar.setVisibility(visiAble);
    }

    public void reLayout() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) this.mBeginTextView
                .getLayoutParams();
        params.leftMargin = 0;
        this.mBeginTextView.setLayoutParams(params);
    }

    public void setProgress(int progress) {
        this.mSeekBar.setProgress(progress);
        this.mBeginTextView.setText(TimeUtil.getStringTime(progress));
    }

    public void setSecondaryProgress(int secondaryProgress) {
        this.mSeekBar.setSecondaryProgress(secondaryProgress);
    }

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener l) {
        this.mOnSeekBarChangeListener = l;
    }

    private int mEndTextViewW = -1;// 记录首次测量的mEndTextViewW的宽度
    private boolean isChangeShow;

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
            boolean fromUser) {
        if (this.isEnabled()) {
            // add libo 此处必须在onProgressChanged中每次都执行 否则会影响看点的弹出
            if (this.mOnSeekBarChangeListener != null) {
                this.mOnSeekBarChangeListener.onProgressChanged(seekBar,
                        progress, fromUser);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        this.refreshViews(this.getProgress());
    }

    protected void refreshViews(int progress) {
        View parent = (View) DefaultPlayerSeekBar.this.getParent();
        if (DefaultPlayerSeekBar.this.getVisibility() == VISIBLE
                && (parent != null && parent.getVisibility() == VISIBLE)
                && !"00:00:00".equals(this.endTimes)) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) this.mBeginTextView
                    .getLayoutParams();
            if (this.mEndTextViewW == -1) {
                this.mEndTextViewW = this.mEndTextView.getMeasuredWidth();
            }
            this.mEndTextViewLeftMargin = this.getMeasuredWidth()
                    - this.mEndTextViewW;
            if (this.mEndTextViewLeftMargin > 0
                    && this.mThumb.getBounds().right >= this.mEndTextViewLeftMargin) {
                this.isChangeShow = true;
                this.mBeginTextView.setVisibility(View.INVISIBLE);
                params.leftMargin = 0;
                this.mBeginTextView.setLayoutParams(params);
            } else {
                this.isChangeShow = false;
                this.mEndTextViewW = -1;
                this.mBeginTextView.setVisibility(View.VISIBLE);
                int offset = this.mThumb.getBounds().left
                        - this.mBeginTextView.getMeasuredWidth();
                params.leftMargin = offset < 0 ? 0 : offset;
                this.mBeginTextView.setText(TimeUtil.getStringTime(progress));
                this.mBeginTextView.setLayoutParams(params);
            }
            if (this.mBeginTextView.getVisibility() == View.INVISIBLE) {
                this.mEndTextView.setText(TimeUtil.getStringTime(progress)
                        + "/" + this.endTimes);
            } else {
                this.mBeginTextView.setText(TimeUtil.getStringTime(progress));
                this.mEndTextView.setText(this.endTimes);
            }
        }
    }

    public int getThumbWidth() {
        if (this.mThumb == null) {
            return 0;
        }
        return this.mThumb.getBounds().right - this.mThumb.getBounds().left;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (this.isEnabled()) {
            if (this.mOnSeekBarChangeListener != null) {
                this.mOnSeekBarChangeListener
                        .onStartTrackingTouch(this.mSeekBar);
            }
        }

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (this.isEnabled()) {
            if (this.mOnSeekBarChangeListener != null) {
                this.mOnSeekBarChangeListener
                        .onStopTrackingTouch(this.mSeekBar);
            }
        }
    }

    public void setBeginTime(long timeMs) {
        this.mBeginTextView.setVisibility(View.VISIBLE);
        this.mBeginTextView.setText(TimeUtil.getStringTime((int) timeMs));
    }

    public void setEndTime(long timeMs) {
        // 用于控制结束TextView的显示样式
        this.mEndTextView.setVisibility(View.VISIBLE);
        this.endTimes = TimeUtil.getStringTime((int) timeMs);
        this.mEndTextView.setText(this.endTimes);
        this.mEndTextViewLeftMargin = this.getMeasuredWidth()
                - this.mEndTextView.getMeasuredWidth();
    }

    public void hideOrShowTime(boolean isShow) {
        this.mBeginTextView.setVisibility(isShow ? View.VISIBLE
                : View.INVISIBLE);
        this.mEndTextView.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
    }

    public SeekBar getSeekBar() {
        return this.mSeekBar;
    }
}
