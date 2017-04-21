package co.saltandpepper.app_mvp.presenter;

import java.util.List;

import co.saltandpepper.app_mvp.ArchitectureApplication;
import co.saltandpepper.app_mvp.R;
import co.saltandpepper.app_mvp.model.GitHubService;
import co.saltandpepper.app_mvp.model.Repository;
import co.saltandpepper.app_mvp.view.MainMvpView;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Vlad on 21.04.2017.
 */

public class MainPresenter implements Presenter<MainMvpView> {
    private MainMvpView mMainMvpView;
    private Subscription mSubscription;
    private List<Repository> mRepositories;

    @Override
    public void attachView(MainMvpView view) {
        this.mMainMvpView = view;
    }

    @Override
    public void detachView() {
        this.mMainMvpView = null;
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    public void loadRepositories(String usernameEntered) {
        String username = usernameEntered.trim();
        if (username.isEmpty()) return;

        mMainMvpView.showProgressIndicator();
        if (mSubscription != null) mSubscription.unsubscribe();
        ArchitectureApplication application = ArchitectureApplication.get(mMainMvpView.getContext());
        GitHubService githubService = application.getGitHubService();
        mSubscription = githubService.publicRepositories(username)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(application.defaultSubscribeScheduler())
                .subscribe(new Subscriber<List<Repository>>() {
                    @Override
                    public void onCompleted() {
                        if (!mRepositories.isEmpty()) {
                            mMainMvpView.showRepositories(mRepositories);
                        } else {
                            mMainMvpView.showMessage(R.string.text_empty_repos);
                        }
                    }

                    @Override
                    public void onError(Throwable error) {
                        if (isHttp404(error)) {
                            mMainMvpView.showMessage(R.string.error_username_not_found);
                        } else {
                            mMainMvpView.showMessage(R.string.error_loading_repos);
                        }
                    }

                    @Override
                    public void onNext(List<Repository> repositories) {
                        MainPresenter.this.mRepositories = repositories;
                    }
                });
    }

    private static boolean isHttp404(Throwable error) {
        return error instanceof HttpException && ((HttpException) error).code() == 404;
    }

}
