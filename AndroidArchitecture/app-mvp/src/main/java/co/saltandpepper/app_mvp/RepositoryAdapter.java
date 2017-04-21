package co.saltandpepper.app_mvp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import co.saltandpepper.app_mvp.model.Repository;

/**
 * Created by Vlad on 21.04.2017.
 */

public class RepositoryAdapter extends RecyclerView.Adapter<RepositoryAdapter.RepositoryViewHolder> {

    private List<Repository> repositories;
    private Callback callback;

    public RepositoryAdapter() {
        this.repositories = Collections.emptyList();
    }

    public void setRepositories(List<Repository> repositories) {
        this.repositories = repositories;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public RepositoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_repo, parent, false);
        final RepositoryViewHolder viewHolder = new RepositoryViewHolder(itemView);
        viewHolder.contentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onItemClick(viewHolder.repository);
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RepositoryViewHolder holder, int position) {
        Repository repository = repositories.get(position);
        Context context = holder.titleTextView.getContext();
        holder.repository = repository;
        holder.titleTextView.setText(repository.name);
        holder.descriptionTextView.setText(repository.description);
        holder.watchersTextView.setText(
                context.getResources().getString(R.string.text_watchers, repository.watchers));
        holder.starsTextView.setText(
                context.getResources().getString(R.string.text_stars, repository.stars));
        holder.forksTextView.setText(
                context.getResources().getString(R.string.text_forks, repository.forks));
    }

    @Override
    public int getItemCount() {
        return repositories.size();
    }

    static class RepositoryViewHolder extends RecyclerView.ViewHolder {
        View contentLayout;
        TextView titleTextView;
        TextView descriptionTextView;
        TextView watchersTextView;
        TextView starsTextView;
        TextView forksTextView;
        public Repository repository;

        RepositoryViewHolder(View itemView) {
            super(itemView);
            contentLayout = itemView.findViewById(R.id.layout_content);
            titleTextView = (TextView) itemView.findViewById(R.id.text_repo_title);
            descriptionTextView = (TextView) itemView.findViewById(R.id.text_repo_description);
            watchersTextView = (TextView) itemView.findViewById(R.id.text_watchers);
            starsTextView = (TextView) itemView.findViewById(R.id.text_stars);
            forksTextView = (TextView) itemView.findViewById(R.id.text_forks);
        }
    }

    public interface Callback {
        void onItemClick(Repository repository);
    }
}
