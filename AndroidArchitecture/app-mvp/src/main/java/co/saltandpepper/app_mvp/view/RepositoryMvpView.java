package co.saltandpepper.app_mvp.view;


import co.saltandpepper.app_mvp.model.User;

/**
 * Created by Vlad on 21.04.2017.
 */

public interface RepositoryMvpView extends MvpView {

    void showOwner(final User owner);

}
