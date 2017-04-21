package co.saltandpepper.app_mvp.view;

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

import co.saltandpepper.app_mvp.R;
import co.saltandpepper.app_mvp.model.Repository;
import co.saltandpepper.app_mvp.model.User;
import co.saltandpepper.app_mvp.presenter.RepositoryPresenter;

/**
 * Created by Vlad on 21.04.2017.
 */

public class RepositoryActivity extends AppCompatActivity implements RepositoryMvpView {
    private static final String EXTRA_REPOSITORY = "EXTRA_REPOSITORY";

    private TextView mTvDescription;
    private TextView mTvHomepage;
    private TextView mTvLanguage;
    private TextView mTvFork;
    private TextView mTvOwnerName;
    private TextView mTvOwnerEmail;
    private TextView mTvOwnerLocation;
    private ImageView mIvOwnerImage;
    private View mViewOwnerLayout;

    private RepositoryPresenter presenter;

    public static Intent newIntent(Context context, Repository repository) {
        Intent intent = new Intent(context, RepositoryActivity.class);
        intent.putExtra(EXTRA_REPOSITORY, repository);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new RepositoryPresenter();
        presenter.attachView(this);

        setContentView(R.layout.activity_repository);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mTvDescription = (TextView) findViewById(R.id.text_repo_description);
        mTvHomepage = (TextView) findViewById(R.id.text_homepage);
        mTvLanguage = (TextView) findViewById(R.id.text_language);
        mTvFork = (TextView) findViewById(R.id.text_fork);
        mTvOwnerName = (TextView) findViewById(R.id.text_owner_name);
        mTvOwnerEmail = (TextView) findViewById(R.id.text_owner_email);
        mTvOwnerLocation = (TextView) findViewById(R.id.text_owner_location);
        mIvOwnerImage = (ImageView) findViewById(R.id.image_owner);
        mViewOwnerLayout = findViewById(R.id.layout_owner);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Repository repository = getIntent().getParcelableExtra(EXTRA_REPOSITORY);
        bindRepositoryData(repository);
        presenter.loadOwner(repository.owner.url);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showOwner(final User owner) {
        mTvOwnerName.setText(owner.name);
        mTvOwnerEmail.setText(owner.email);
        mTvOwnerEmail.setVisibility(owner.hasEmail() ? View.VISIBLE : View.GONE);
        mTvOwnerLocation.setText(owner.location);
        mTvOwnerLocation.setVisibility(owner.hasLocation() ? View.VISIBLE : View.GONE);
        mViewOwnerLayout.setVisibility(View.VISIBLE);
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
                .into(mIvOwnerImage);
    }
}
