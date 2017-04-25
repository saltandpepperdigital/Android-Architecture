package co.saltandpepper.androidarchitecture;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import co.saltandpepper.androidarchitecture.model.GitHubService;
import co.saltandpepper.androidarchitecture.model.Repository;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity {
    private Subscription mSubscription;
    private RecyclerView mRvRepos;
    private EditText mEtUsername;
    private ProgressBar mProgressBar;
    private TextView mTvInfo;
    private ImageButton ibSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        mTvInfo = (TextView) findViewById(R.id.text_info);
        //Set up ToolBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Set up RecyclerView
        mRvRepos = (RecyclerView) findViewById(R.id.repos_recycler_view);
        setupRecyclerView(mRvRepos);
        // Set up search button
        ibSearch = (ImageButton) findViewById(R.id.button_search);
        ibSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadGitHubRepos(mEtUsername.getText().toString());
            }
        });
        //Set up username EditText
        mEtUsername = (EditText) findViewById(R.id.edit_text_username);
        mEtUsername.addTextChangedListener(mHideShowButtonTextWatcher);
        mEtUsername.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String username = mEtUsername.getText().toString();
                    if (username.length() > 0) loadGitHubRepos(username);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    public void loadGitHubRepos(String username) {
        mProgressBar.setVisibility(View.VISIBLE);
        mRvRepos.setVisibility(View.GONE);
        mTvInfo.setVisibility(View.GONE);
        ArchitectureApplication application = ArchitectureApplication.get(this);
        GitHubService gitHubService = application.getGitHubService();
        mSubscription = gitHubService.publicRepositories(username)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(application.defaultSubscribeScheduler())
                .subscribe(new Subscriber<List<Repository>>() {
                    @Override
                    public void onCompleted() {
                        mProgressBar.setVisibility(View.GONE);
                        if (mRvRepos.getAdapter().getItemCount() > 0) {
                            mRvRepos.requestFocus();
                            hideSoftKeyboard();
                            mRvRepos.setVisibility(View.VISIBLE);
                        } else {
                            mTvInfo.setText(R.string.text_empty_repos);
                            mTvInfo.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError(Throwable error) {
                        mProgressBar.setVisibility(View.GONE);
                        if (error instanceof HttpException
                                && ((HttpException) error).code() == 404) {
                            mTvInfo.setText(R.string.error_username_not_found);
                        } else {
                            mTvInfo.setText(R.string.error_loading_repos);
                        }
                        mTvInfo.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(List<Repository> repositories) {
                        RepositoryAdapter adapter =
                                (RepositoryAdapter) mRvRepos.getAdapter();
                        adapter.setmRepositories(repositories);
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        RepositoryAdapter adapter = new RepositoryAdapter();
        adapter.setCallback(new RepositoryAdapter.Callback() {
            @Override
            public void onItemClick(Repository repository) {
                startActivity(RepositoryActivity.newIntent(MainActivity.this, repository));
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEtUsername.getWindowToken(), 0);
    }

    private TextWatcher mHideShowButtonTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            ibSearch.setVisibility(charSequence.length() > 0 ? View.VISIBLE : View.GONE);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

}
