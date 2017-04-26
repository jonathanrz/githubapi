package com.jonathanzanella.githubapi.language;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jonathanzanella.githubapi.R;
import com.jonathanzanella.githubapi.database.DatabaseHelper;
import com.jonathanzanella.githubapi.database.RepositoryImpl;
import com.jonathanzanella.githubapi.projects.Project;
import com.jonathanzanella.githubapi.projects.ProjectRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder> {
	private List<Language> languages;
	private Map<Language, Long> repoCount = new HashMap<>();

	static class ViewHolder extends RecyclerView.ViewHolder {
		private TextView nameView;
		private TextView repoCountView;

		ViewHolder(View itemView) {
			super(itemView);
			nameView = (TextView) itemView.findViewById(R.id.row_language_name);
			repoCountView = (TextView) itemView.findViewById(R.id.row_language_repo_count);
		}

		void setData(Language language, long repoCount) {
			itemView.setTag(language.getId());
			nameView.setText(language.getName());
			repoCountView.setText(String.valueOf(repoCount));
		}
	}

	public LanguageAdapter(final Context context) {
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... voids) {
				DatabaseHelper databaseHelper = new DatabaseHelper(context);
				languages = new LanguageRepository(new RepositoryImpl<Language>(databaseHelper)).all();
				ProjectRepository projectRepository = new ProjectRepository(new RepositoryImpl<Project>(databaseHelper));
				for (Language language : languages) {
					repoCount.put(language, projectRepository.countProjectsOfLanguage(language.getId()));
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void aVoid) {
				super.onPostExecute(aVoid);
				notifyDataSetChanged();
			}
		}.execute();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_language, parent, false);
		return new ViewHolder(v);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		Language language = languages.get(position);
		holder.setData(language, repoCount.get(language));
	}

	@Override
	public int getItemCount() {
		return languages != null ? languages.size() : 0;
	}
}
