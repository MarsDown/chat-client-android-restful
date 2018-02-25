package com.drsaina.mars.testnotification.features.Base;

/**
 * Created by mars on 2/12/2018.
 */

public interface IPresenter<T> {

    void subscribe();

    void unsubscribe();

    void onViewAttached(T view);

}
