package com.dragon.superplayer.player.displayer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.dragon.superplayer.player.displayer.interfaces.IControllerViewDisplayer;
import com.dragon.superplayer.player.view.interfaces.IPlayerControllerViewInterface;

public class DefaultControllerViewDisplayer implements IControllerViewDisplayer {

    private static final int ANIMATION_DURATION = 200;

    @Override
    public void displayControllerView(
            IPlayerControllerViewInterface iPlayerControllerViewInterface,
            boolean showOrHide, boolean useAnimation) {
        if (iPlayerControllerViewInterface == null) {
            return;
        }
        final ViewGroup playerControllerUpperView = iPlayerControllerViewInterface
                .getPlayerControllerUpperView();
        final ViewGroup playerControllerBottomView = iPlayerControllerViewInterface
                .getPlayerControllerBottomView();

        if (showOrHide) {
            if (playerControllerUpperView.getVisibility() == View.VISIBLE) {
                return;
            }
            playerControllerUpperView.setVisibility(View.VISIBLE);
            playerControllerBottomView.setVisibility(View.VISIBLE);
            if (useAnimation) {
                float upperHeight = playerControllerUpperView.getHeight();
                ObjectAnimator objectAnimatorUpperTranslation = ObjectAnimator
                        .ofFloat(playerControllerUpperView, "translationY",
                                -1.0f * upperHeight, 0);
                objectAnimatorUpperTranslation
                        .setInterpolator(new AccelerateDecelerateInterpolator());
                ObjectAnimator objectAnimatorUpperAlpha = ObjectAnimator
                        .ofFloat(playerControllerUpperView, "alpha", 0.0f, 1.0f);

                float BottomHeight = playerControllerBottomView.getHeight();
                ObjectAnimator objectAnimatorBottomTranslation = ObjectAnimator
                        .ofFloat(playerControllerBottomView, "translationY",
                                1.0f * BottomHeight, 0);
                objectAnimatorBottomTranslation
                        .setInterpolator(new AccelerateDecelerateInterpolator());
                ObjectAnimator objectAnimatorBottomAlpha = ObjectAnimator
                        .ofFloat(playerControllerBottomView, "alpha", 0.0f,
                                1.0f);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(objectAnimatorUpperTranslation,
                        objectAnimatorUpperAlpha,
                        objectAnimatorBottomTranslation,
                        objectAnimatorBottomAlpha);
                animatorSet.setDuration(ANIMATION_DURATION);
                animatorSet.start();
            }
        } else {
            float upperHeight = playerControllerUpperView.getHeight();
            ObjectAnimator objectAnimatorUpperTranslation = ObjectAnimator
                    .ofFloat(playerControllerUpperView, "translationY", 0,
                            -1.0f * upperHeight);
            ObjectAnimator objectAnimatorUpperAlpha = ObjectAnimator.ofFloat(
                    playerControllerUpperView, "alpha", 1.0f, 0.0f);

            float bottomHeight = playerControllerBottomView.getHeight();
            ObjectAnimator objectAnimatorBottomTranslation = ObjectAnimator
                    .ofFloat(playerControllerBottomView, "translationY", 0,
                            1.0f * bottomHeight);
            ObjectAnimator objectAnimatorBottomAlpha = ObjectAnimator.ofFloat(
                    playerControllerBottomView, "alpha", 1.0f, 0.0f);

            final AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(objectAnimatorUpperTranslation,
                    objectAnimatorUpperAlpha, objectAnimatorBottomTranslation,
                    objectAnimatorBottomAlpha);
            animatorSet.setDuration(ANIMATION_DURATION);
            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    playerControllerUpperView.setVisibility(View.GONE);
                    playerControllerBottomView.setVisibility(View.GONE);
                }
            });
            animatorSet.start();
        }
    }

}
