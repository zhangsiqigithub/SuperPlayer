package com.dragon.superplayer.player.displayer.interfaces;

import com.dragon.superplayer.player.view.interfaces.IPlayerControllerViewInterface;

public interface IControllerViewDisplayer {

    public void displayControllerView(
            IPlayerControllerViewInterface iPlayerControllerViewInterface,
            boolean showOrHide, boolean useAnimation);

}
