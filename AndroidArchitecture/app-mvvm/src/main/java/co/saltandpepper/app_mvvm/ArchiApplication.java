package co.saltandpepper.app_mvvm;

import android.app.Application;
import android.content.Context;

import co.saltandpepper.app_mvvm.model.GitHubService;
import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * Created by Vlad on 23.04.2017.
 */

public class ArchiApplication extends Application {

    private GitHubService githubService;
    private Scheduler defaultSubscribeScheduler;

    public static ArchiApplication get(Context context) {
        return (ArchiApplication) context.getApplicationContext();
    }

    public GitHubService getGithubService() {
        if (githubService == null) {
            githubService = GitHubService.Factory.create();
        }
        return githubService;
    }

    //For setting mocks during testing
    public void setGithubService(GitHubService githubService) {
        this.githubService = githubService;
    }

    public Scheduler defaultSubscribeScheduler() {
        if (defaultSubscribeScheduler == null) {
            defaultSubscribeScheduler = Schedulers.io();
        }
        return defaultSubscribeScheduler;
    }

    //User to change scheduler from tests
    public void setDefaultSubscribeScheduler(Scheduler scheduler) {
        this.defaultSubscribeScheduler = scheduler;
    }
}
