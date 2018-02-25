package com.drsaina.mars.testnotification.features.Login;

import com.drsaina.mars.testnotification.features.Base.IPresenter;
import com.drsaina.mars.testnotification.features.Base.IView;
import com.drsaina.mars.testnotification.Data.remot.Model.CoreUser;

/**
 * Created by mars on 2/12/2018.
 */
public interface LoginContract {

    interface View extends IView<Presenter> {
        void showLoader ();
        void hideLoader();
        void goChatRoom();
    }

    interface Presenter extends IPresenter<View> {
        void registerUser ( CoreUser user );
    }
}
