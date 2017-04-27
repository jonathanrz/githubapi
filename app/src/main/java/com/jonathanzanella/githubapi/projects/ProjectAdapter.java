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

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {
	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormat.forPattern(Project.DATE_FORMAT);
	private List<Project> projects;

	static class ViewHolder extends RecyclerView.ViewHolder {
		private TextView viewName;
		private TextView viewCreatedAt;
		private TextView viewUpdatedAt;
		private TextView viewOpenIssues;

		ViewHolder(View itemView) {
			super(itemView);
			viewName = (TextView) itemView.findViewById(R.id.row_project_name);
			viewCreatedAt = (TextView) itemView.findViewById(R.id.row_project_created_at);
			viewUpdatedAt = (TextView) itemView.findViewById(R.id.row_project_updated_at);
			viewOpenIssues = (TextView) itemView.findViewById(R.id.row_project_open_issues);
		}

		public void setData(Project project) {
			itemView.setTag(project.getId());
			viewName.setText(project.getName());
			viewCreatedAt.setText(DATE_FORMAT.print(project.getCreatedAt()));
			viewUpdatedAt.setText(DATE_FORMAT.print(project.getUpdatedAt()));
			viewOpenIssues.setText(String.valueOf(project.getOpenIssues()));
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
