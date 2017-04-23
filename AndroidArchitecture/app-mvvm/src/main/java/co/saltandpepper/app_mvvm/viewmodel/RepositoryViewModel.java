package co.saltandpepper.app_mvvm.viewmodel;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import co.saltandpepper.app_mvvm.ArchitectureApplication;
import co.saltandpepper.app_mvvm.R;
import co.saltandpepper.app_mvvm.model.GitHubService;
import co.saltandpepper.app_mvvm.model.Repository;
import co.saltandpepper.app_mvvm.model.User;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Vlad on 23.04.2017.
 */

/**
 * ViewModel for the RepositoryActivity
 */
public class RepositoryViewModel implements ViewModel {
    private Repository mRepository;
    private Context mContext;
    private Subscription mSubscription;

    public ObservableField<String> ownerName;
    public ObservableField<String> ownerEmail;
    public ObservableField<String> ownerLocation;
    public ObservableInt ownerEmailVisibility;
    public ObservableInt ownerLocationVisibility;
    public ObservableInt ownerLayoutVisibility;

    public RepositoryViewModel(Context context, final Repository repository) {
        this.mRepository = repository;
        this.mContext = context;
        this.ownerName = new ObservableField<>();
        this.ownerEmail = new ObservableField<>();
        this.ownerLocation = new ObservableField<>();
        this.ownerLayoutVisibility = new ObservableInt(View.INVISIBLE);
        this.ownerEmailVisibility = new ObservableInt(View.VISIBLE);
        this.ownerLocationVisibility = new ObservableInt(View.VISIBLE);
        // Trigger loading the rest of the user data as soon as the view model is created.
        // It's odd having to trigger this from here. Cases where accessing to the data model
        // needs to happen because of a change in the Activity/Fragment lifecycle
        // (i.e. an activity created) don't work very well with this MVVM pattern.
        // It also makes this class more difficult to test. Hopefully a better solution will be found
        loadFullUser(repository.owner.url);
    }

    public String getDescription() {
        return mRepository.description;
    }

    public String getHomepage() {
        return mRepository.homepage;
    }

    public int getHomepageVisibility() {
        return mRepository.hasHomepage() ? View.VISIBLE : View.GONE;
    }

    public String getLanguage() {
        return mContext.getString(R.string.text_language, mRepository.language);
    }

    public int getLanguageVisibility() {
        return mRepository.hasLanguage() ? View.VISIBLE : View.GONE;
    }

    public int getForkVisibility() {
        return mRepository.isFork() ? View.VISIBLE : View.GONE;
    }

    public String getOwnerAvatarUrl() {
        return mRepository.owner.avatarUrl;
    }

    @Override
    public void destroy() {
        this.mContext = null;
        if (mSubscription != null && !mSubscription.isUnsubscribed()) mSubscription.unsubscribe();
    }

    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        Picasso.with(view.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)
                .into(view);
    }

    private void loadFullUser(String url) {
        ArchitectureApplication application = ArchitectureApplication.get(mContext);
        GitHubService githubService = application.getGitHubService();
        mSubscription = githubService.userFromUrl(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(application.defaultSubscribeScheduler())
                .subscribe(new Action1<User>() {
                    @Override
                    public void call(User user) {
                        ownerName.set(user.name);
                        ownerEmail.set(user.email);
                        ownerLocation.set(user.location);
                        ownerEmailVisibility.set(user.hasEmail() ? View.VISIBLE : View.GONE);
                        ownerLocationVisibility.set(user.hasLocation() ? View.VISIBLE : View.GONE);
                        ownerLayoutVisibility.set(View.VISIBLE);
                    }
                });
    }
}
