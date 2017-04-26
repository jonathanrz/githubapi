package com.jonathanzanella.githubapi;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import com.jonathanzanella.githubapi.database.DatabaseHelper;
import com.jonathanzanella.githubapi.database.RepositoryImpl;
import com.jonathanzanella.githubapi.language.Language;
import com.jonathanzanella.githubapi.language.LanguageActivity;
import com.jonathanzanella.githubapi.language.LanguageRepository;
import com.jonathanzanella.githubapi.projects.Project;
import com.jonathanzanella.githubapi.projects.ProjectRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withTagValue;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

public class LanguageActivityTest {
	private List<Language> languages;

	@Rule
	public ActivityTestRule<LanguageActivity> activityTestRule = new ActivityTestRule<>(LanguageActivity.class);

	@Before
	public void setUp() throws Exception {
		languages = new LanguageRepository(new RepositoryImpl<Language>(new DatabaseHelper(getTargetContext()))).all();
	}

	@Test
	public void showProjectsFromFirstScreen() throws Exception {
		Language language = languages.get(0);

		Intent i = new Intent();
		i.putExtra(LanguageActivity.KEY_LANGUAGE_ID, language.getId());
		activityTestRule.launchActivity(i);

		ProjectRepository projectRepository = new ProjectRepository(new RepositoryImpl<Project>(new DatabaseHelper(getTargetContext())));
		List<Project> projectsOfLanguage = projectRepository.projectsOfLanguage(language.getId());

		for (Project project : projectsOfLanguage) {
			onProjectRowView(R.id.row_project_name, project)
					.check(matches(withText(project.getName())));
		}
	}

	private ViewInteraction onProjectRowView(@IdRes int id, Project project) {
		return onView(allOf(
				withId(id),
				isDescendantOfA(withTagValue(is((Object)project.getId())))));
	}
}
