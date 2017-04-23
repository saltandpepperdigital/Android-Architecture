package co.saltandpepper.app_mvvm.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.view.View;

import co.saltandpepper.app_mvvm.R;
import co.saltandpepper.app_mvvm.model.Repository;
import co.saltandpepper.app_mvvm.view.RepositoryActivity;

/**
 * Created by Vlad on 23.04.2017.
 */

/**
 * View model for each item in the repositories RecyclerView
 */
public class ItemRepoViewModel extends BaseObservable implements ViewModel {
    private Repository mRepository;
    private Context mContext;

    public ItemRepoViewModel(Context context, Repository repository) {
        this.mRepository = repository;
        this.mContext = context;
    }

    public String getName() {
        return mRepository.name;
    }

    public String getDescription() {
        return mRepository.description;
    }

    public String getStars() {
        return mContext.getString(R.string.text_stars, mRepository.stars);
    }

    public String getWatchers() {
        return mContext.getString(R.string.text_watchers, mRepository.watchers);
    }

    public String getForks() {
        return mContext.getString(R.string.text_forks, mRepository.forks);
    }

    public void onItemClick(View view) {
        mContext.startActivity(RepositoryActivity.newIntent(mContext, mRepository));
    }

    // Allows recycling ItemRepoViewModels within the recyclerview adapter
    public void setmRepository(Repository mRepository) {
        this.mRepository = mRepository;
        notifyChange();
    }

    @Override
    public void destroy() {
        //In this case destroy doesn't need to do anything because there is not async calls
    }

}
