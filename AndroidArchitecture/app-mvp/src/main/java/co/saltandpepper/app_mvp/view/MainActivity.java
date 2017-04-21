package co.saltandpepper.app_mvp.view;

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

import co.saltandpepper.app_mvp.R;
import co.saltandpepper.app_mvp.RepositoryAdapter;
import co.saltandpepper.app_mvp.model.Repository;
import co.saltandpepper.app_mvp.presenter.MainPresenter;

/**
 * Created by Vlad on 21.04.2017.
 */

public class MainActivity extends AppCompatActivity implements MainMvpView {
    private MainPresenter presenter;
    private RecyclerView mRvRepos;
    private EditText mEtUsername;
    private ProgressBar mProgressBar;
    private TextView mInfo;
    private ImageButton mSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set up presenter
        presenter = new MainPresenter();
        presenter.attachView(this);

        setContentView(R.layout.activity_main);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        mInfo = (TextView) findViewById(R.id.text_info);
        //Set up ToolBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Set up RecyclerView
        mRvRepos = (RecyclerView) findViewById(R.id.repos_recycler_view);
        setupRecyclerView(mRvRepos);
        // Set up search button
        mSearch = (ImageButton) findViewById(R.id.button_search);
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.loadRepositories(mEtUsername.getText().toString());
            }
        });
        //Set up username EditText
        mEtUsername = (EditText) findViewById(R.id.edit_text_username);
        mEtUsername.addTextChangedListener(mHideShowButtonTextWatcher);
        mEtUsername.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    presenter.loadRepositories(mEtUsername.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    // MainMvpView interface methods implementation

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showRepositories(List<Repository> repositories) {
        RepositoryAdapter adapter = (RepositoryAdapter) mRvRepos.getAdapter();
        adapter.setRepositories(repositories);
        adapter.notifyDataSetChanged();
        mRvRepos.requestFocus();
        hideSoftKeyboard();
        mProgressBar.setVisibility(View.INVISIBLE);
        mInfo.setVisibility(View.INVISIBLE);
        mRvRepos.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMessage(int stringId) {
        mProgressBar.setVisibility(View.INVISIBLE);
        mInfo.setVisibility(View.VISIBLE);
        mRvRepos.setVisibility(View.INVISIBLE);
        mInfo.setText(getString(stringId));
    }

    @Override
    public void showProgressIndicator() {
        mProgressBar.setVisibility(View.VISIBLE);
        mInfo.setVisibility(View.INVISIBLE);
        mRvRepos.setVisibility(View.INVISIBLE);
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
            mSearch.setVisibility(charSequence.length() > 0 ? View.VISIBLE : View.GONE);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
}
