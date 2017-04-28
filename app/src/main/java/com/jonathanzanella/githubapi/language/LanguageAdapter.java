package com.jonathanzanella.githubapi.language;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jonathanzanella.githubapi.R;
import com.jonathanzanella.githubapi.database.DatabaseHelper;
import com.jonathanzanella.githubapi.database.RepositoryImpl;
import com.jonathanzanella.githubapi.helper.AdapterColorHelper;
import com.jonathanzanella.githubapi.projects.Project;
import com.jonathanzanella.githubapi.projects.ProjectRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder> {
	public interface OnLanguageSelectedListener {
		void onLanguageSelected(Language language);
	}

	class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		private TextView nameView;
		private TextView repoCountView;
		private AdapterColorHelper adapterColorHelper;

		ViewHolder(View itemView) {
			super(itemView);
			itemView.setOnClickListener(this);
			nameView = (TextView) itemView.findViewById(R.id.row_language_name);
			repoCountView = (TextView) itemView.findViewById(R.id.row_language_projects_count);

			int colorListEven = ContextCompat.getColor(itemView.getContext(), R.color.colorListEven);
			int colorListOdd = ContextCompat.getColor(itemView.getContext(), R.color.colorListOdd);

			adapterColorHelper = new AdapterColorHelper(colorListOdd, colorListEven);
		}

		void setData(Language language, long repoCount) {
			itemView.setTag(language.getId());
			itemView.setBackgroundColor(adapterColorHelper.getColorForGridWithTwoColumns(getAdapterPosition()));
			nameView.setText(language.getName());
			repoCountView.setText(String.valueOf(repoCount));
		}

		@Override
		public void onClick(View view) {
			if(onLanguageSelectedListener != null)
				onLanguageSelectedListener.onLanguageSelected(languages.get(getAdapterPosition()));
		}
	}

	private List<Language> languages;
	private Map<Language, Long> projectsCount = new HashMap<>();
	private OnLanguageSelectedListener onLanguageSelectedListener;

	public LanguageAdapter(Context context) {
		updateData(context);
	}

	public void updateData(final Context context) {
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... voids) {
				DatabaseHelper databaseHelper = new DatabaseHelper(context);
				languages = new LanguageRepository(new RepositoryImpl<Language>(databaseHelper)).all();
				ProjectRepository projectRepository = new ProjectRepository(new RepositoryImpl<Project>(databaseHelper));
				for (Language language : languages) {
					projectsCount.put(language, projectRepository.countProjectsOfLanguage(language.getId()));
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

	public void setOnLanguageSelectedListener(OnLanguageSelectedListener onLanguageSelectedListener) {
		this.onLanguageSelectedListener = onLanguageSelectedListener;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_language, parent, false);
		return new ViewHolder(v);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		Language language = languages.get(position);
		holder.setData(language, projectsCount.get(language));
	}

	@Override
	public int getItemCount() {
		return languages != null ? languages.size() : 0;
	}
}
