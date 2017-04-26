package com.jonathanzanella.githubapi.projects;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jonathanzanella.githubapi.R;
import com.jonathanzanella.githubapi.database.DatabaseHelper;
import com.jonathanzanella.githubapi.database.RepositoryImpl;
import com.jonathanzanella.githubapi.language.Language;

import java.util.List;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {
	private List<Project> projects;

	static class ViewHolder extends RecyclerView.ViewHolder {
		private TextView viewName;

		ViewHolder(View itemView) {
			super(itemView);
			viewName = (TextView) itemView.findViewById(R.id.row_project_name);
		}

		public void setData(Project project) {
			itemView.setTag(project.getId());
			viewName.setText(project.getName());
		}
	}

	public ProjectAdapter(Context context, Language language) {
		ProjectRepository projectRepository = new ProjectRepository(new RepositoryImpl<Project>(new DatabaseHelper(context)));
		this.projects = projectRepository.projectsOfLanguage(language.getId());
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_project, parent, false);
		return new ViewHolder(v);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		Project project = projects.get(position);
		holder.setData(project);
	}

	@Override
	public int getItemCount() {
		return projects.size();
	}
}
