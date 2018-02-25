package com.drsaina.mars.testnotification.features.Splash;

import com.drsaina.mars.testnotification.features.Base.IPresenter;
import com.drsaina.mars.testnotification.features.Base.IView;

/**
 * Created by mars on 2/12/2018.
 */
public interface SplashContract {

    interface View extends IView<Presenter> {
        void openChatRoomActivity ( );
        void openLoginActivity ( );
    }

    interface Presenter extends IPresenter<View> {
        void init ();
    }
}
