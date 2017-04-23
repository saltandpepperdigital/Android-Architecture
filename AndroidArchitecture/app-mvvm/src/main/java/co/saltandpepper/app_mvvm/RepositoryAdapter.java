package co.saltandpepper.app_mvvm;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import co.saltandpepper.app_mvvm.databinding.ItemRepoBinding;
import co.saltandpepper.app_mvvm.model.Repository;
import co.saltandpepper.app_mvvm.viewmodel.ItemRepoViewModel;

/**
 * Created by Vlad on 23.04.2017.
 */

public class RepositoryAdapter extends RecyclerView.Adapter<RepositoryAdapter.RepositoryViewHolder> {
    private List<Repository> mRepositories;

    public RepositoryAdapter() {
        this.mRepositories = Collections.emptyList();
    }

    public void setRepositories(List<Repository> mRepositories) {
        this.mRepositories = mRepositories;
    }

    @Override
    public RepositoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemRepoBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_repo,
                parent,
                false);
        return new RepositoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(RepositoryViewHolder holder, int position) {
        holder.bindRepository(mRepositories.get(position));
    }

    @Override
    public int getItemCount() {
        return mRepositories.size();
    }

    public static class RepositoryViewHolder extends RecyclerView.ViewHolder {
        final ItemRepoBinding binding;

        public RepositoryViewHolder(ItemRepoBinding binding) {
            super(binding.cardView);
            this.binding = binding;
        }

        void bindRepository(Repository repository) {
            if (binding.getViewModel() == null) {
                binding.setViewModel(new ItemRepoViewModel(itemView.getContext(), repository));
            } else {
                binding.getViewModel().setmRepository(repository);
            }
        }
    }
}
