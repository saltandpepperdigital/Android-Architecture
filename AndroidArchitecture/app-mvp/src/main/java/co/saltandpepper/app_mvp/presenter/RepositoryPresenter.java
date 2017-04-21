package co.saltandpepper.app_mvp.presenter;

import co.saltandpepper.app_mvp.ArchitectureApplication;
import co.saltandpepper.app_mvp.model.GitHubService;
import co.saltandpepper.app_mvp.model.User;
import co.saltandpepper.app_mvp.view.RepositoryMvpView;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Vlad on 21.04.2017.
 */

public class RepositoryPresenter implements Presenter<RepositoryMvpView> {
    private RepositoryMvpView mRepositoryMvpView;
    private Subscription mSubscription;

    @Override
    public void attachView(RepositoryMvpView view) {
        this.mRepositoryMvpView = view;
    }

    @Override
    public void detachView() {
        this.mRepositoryMvpView = null;
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    public void loadOwner(String userUrl) {
        ArchitectureApplication application = ArchitectureApplication.get(mRepositoryMvpView.getContext());
        GitHubService githubService = application.getGitHubService();
        mSubscription = githubService.userFromUrl(userUrl)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(application.defaultSubscribeScheduler())
                .subscribe(new Action1<User>() {
                    @Override
                    public void call(User user) {
                        mRepositoryMvpView.showOwner(user);
                    }
                });
    }
}
