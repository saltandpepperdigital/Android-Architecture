package co.saltandpepper.app_mvp.view;

import java.util.List;

import co.saltandpepper.app_mvp.model.Repository;

/**
 * Created by Vlad on 21.04.2017.
 */

public interface MainMvpView extends MvpView {

    void showRepositories(List<Repository> repositories);

    void showMessage(int stringId);

    void showProgressIndicator();
}
