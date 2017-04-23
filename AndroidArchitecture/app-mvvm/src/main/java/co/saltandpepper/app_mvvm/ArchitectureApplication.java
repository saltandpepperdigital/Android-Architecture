package co.saltandpepper.app_mvvm;

import android.app.Application;
import android.content.Context;

import co.saltandpepper.app_mvvm.model.GitHubService;
import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * Created by Vlad on 23.04.2017.
 */

public class ArchitectureApplication extends Application {
    private GitHubService mGitHubService;
    private Scheduler defaultSubscribeScheduler;

    public static ArchitectureApplication get(Context context) {
        return (ArchitectureApplication) context.getApplicationContext();
    }

    public GitHubService getGitHubService() {
        if (mGitHubService == null) {
            mGitHubService = GitHubService.Factory.create();
        }
        return mGitHubService;
    }

    public Scheduler defaultSubscribeScheduler() {
        if (defaultSubscribeScheduler == null) {
            defaultSubscribeScheduler = Schedulers.io();
        }
        return defaultSubscribeScheduler;
    }
}
