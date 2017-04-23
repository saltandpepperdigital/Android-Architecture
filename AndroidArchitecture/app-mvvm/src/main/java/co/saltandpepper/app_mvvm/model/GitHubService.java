package co.saltandpepper.app_mvvm.model;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Vlad on 23.04.2017.
 */

public interface GitHubService {
    @GET("users/{username}/repos")
    Observable<List<Repository>> publicRepositories(@Path("username") String username);

    @GET
    Observable<User> userFromUrl(@Url String userUrl);


    class Factory {
        private static final String URL_GIT_HUB = "https://api.github.com/";
        public static GitHubService create() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(URL_GIT_HUB)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            return retrofit.create(GitHubService.class);
        }
    }
}
