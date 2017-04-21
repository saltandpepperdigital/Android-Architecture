package co.saltandpepper.app_mvp.presenter;

/**
 * Created by Vlad on 21.04.2017.
 */

interface Presenter<V> {
    void attachView(V view);

    void detachView();

}
