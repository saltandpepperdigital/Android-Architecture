package co.saltandpepper.androidarchitecture;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import co.saltandpepper.androidarchitecture.model.GitHubService;
import co.saltandpepper.androidarchitecture.model.Repository;
import co.saltandpepper.androidarchitecture.model.User;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Vlad on 21.04.2017.
 */

public class RepositoryActivity extends AppCompatActivity {
    private static final String EXTRA_REPOSITORY = "EXTRA_REPOSITORY";

    private TextView mTvDescription;
    private TextView mTvHomepage;
    private TextView mTvLanguage;
    private TextView mTvFork;
    private TextView mTvOwnerName;
    private TextView mTvOwnerEmail;
    private TextView mTvOwnerLocation;
    private ImageView mIvOwner;
    private View mVOwner;

    private Subscription mSubscription;

    public static Intent newIntent(Context context, Repository repository) {
        Intent intent = new Intent(context, RepositoryActivity.class);
        intent.putExtra(EXTRA_REPOSITORY, repository);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mTvDescription = (TextView) findViewById(R.id.text_repo_description);
        mTvHomepage = (TextView) findViewById(R.id.text_homepage);
        mTvLanguage = (TextView) findViewById(R.id.text_language);
        mTvFork = (TextView) findViewById(R.id.text_fork);
        mTvOwnerName = (TextView) findViewById(R.id.text_owner_name);
        mTvOwnerEmail = (TextView) findViewById(R.id.text_owner_email);
        mTvOwnerLocation = (TextView) findViewById(R.id.text_owner_location);
        mIvOwner = (ImageView) findViewById(R.id.image_owner);
        mVOwner = findViewById(R.id.layout_owner);

        Repository repository = getIntent().getParcelableExtra(EXTRA_REPOSITORY);
        bindRepositoryData(repository);
        loadFullUser(repository.owner.url);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    private void bindRepositoryData(final Repository repository) {
        setTitle(repository.name);
        mTvDescription.setText(repository.description);
        mTvHomepage.setText(repository.homepage);
        mTvHomepage.setVisibility(repository.hasHomepage() ? View.VISIBLE : View.GONE);
        mTvLanguage.setText(getString(R.string.text_language, repository.language));
        mTvLanguage.setVisibility(repository.hasLanguage() ? View.VISIBLE : View.GONE);
        mTvFork.setVisibility(repository.isFork() ? View.VISIBLE : View.GONE);
        //Preload image for user because we already have it before loading the full user
        Picasso.with(this)
                .load(repository.owner.avatarUrl)
                .placeholder(R.drawable.placeholder)
                .into(mIvOwner);
    }

    private void bindOwnerData(final User owner) {
        mTvOwnerName.setText(owner.name);
        mTvOwnerEmail.setText(owner.email);
        mTvOwnerEmail.setVisibility(owner.hasEmail() ? View.VISIBLE : View.GONE);
        mTvOwnerLocation.setText(owner.location);
        mTvOwnerLocation.setVisibility(owner.hasLocation() ? View.VISIBLE : View.GONE);
    }


    private void loadFullUser(String url) {
        ArchitectureApplication application = ArchitectureApplication.get(this);
        GitHubService gitHubService = application.getGitHubService();
        mSubscription = gitHubService.userFromUrl(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(application.defaultSubscribeScheduler())
                .subscribe(new Action1<User>() {
                    @Override
                    public void call(User user) {
                        bindOwnerData(user);
                        mVOwner.setVisibility(View.VISIBLE);
                    }
                });
    }
}
