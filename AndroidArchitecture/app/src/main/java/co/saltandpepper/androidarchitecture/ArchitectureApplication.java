package co.saltandpepper.androidarchitecture;

import android.app.Application;
import android.content.Context;

import co.saltandpepper.androidarchitecture.model.GitHubService;
import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * Created by Vlad on 21.04.2017.
 */

public class ArchitectureApplication extends Application {
    private GitHubService mGitHubService;
    private Scheduler mDefaultSubscribeScheduler;

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
        if (mDefaultSubscribeScheduler == null) {
            mDefaultSubscribeScheduler = Schedulers.io();
        }
        return mDefaultSubscribeScheduler;
    }
}
