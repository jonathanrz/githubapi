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

import java.util.List;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder> {
	private List<Language> languages;
	private Context context;

	static class ViewHolder extends RecyclerView.ViewHolder {
		private TextView name;

		ViewHolder(View itemView) {
			super(itemView);
			name = (TextView) itemView.findViewById(R.id.row_language_name);
		}

		public void setData(Language language) {
			name.setText(language.getName());
		}
	}

	public LanguageAdapter(final Context context) {
		this.context = context;

		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... voids) {
				languages = new LanguageRepository(new RepositoryImpl<Language>(new DatabaseHelper(context))).all();
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
		holder.setData(languages.get(position));
	}

	@Override
	public int getItemCount() {
		return languages != null ? languages.size() : 0;
	}
}
