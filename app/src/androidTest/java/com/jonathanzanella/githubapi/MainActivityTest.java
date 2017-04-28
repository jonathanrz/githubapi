package com.jonathanzanella.githubapi;

import android.support.annotation.IdRes;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import com.jonathanzanella.githubapi.database.DatabaseHelper;
import com.jonathanzanella.githubapi.database.RepositoryImpl;
import com.jonathanzanella.githubapi.language.Language;
import com.jonathanzanella.githubapi.language.LanguageRepository;
import com.jonathanzanella.githubapi.projects.Project;
import com.jonathanzanella.githubapi.projects.ProjectRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withTagValue;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.jonathanzanella.githubapi.helper.UIHelper.matchToolbarTitle;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.AllOf.allOf;

public class MainActivityTest {
	private List<Language> languages;
	@Rule
	public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

	@Before
	public void setUp() throws Exception {
		languages = new LanguageRepository(new RepositoryImpl<Language>(new DatabaseHelper(getTargetContext()))).all();
	}

	@Test
	public void showSavedLanguagesOnScreen() throws Exception {
		for (Language language : languages) {
			onLanguageRowView(R.id.row_language_name, language)
					.check(matches(withText(language.getName())));
		}
	}

	@Test
	public void showProjectCountForFirstLanguage() throws Exception {
		ProjectRepository projectRepository = new ProjectRepository(new RepositoryImpl<Project>(new DatabaseHelper(getTargetContext())));
		Language language = languages.get(0);
		long count = projectRepository.countProjectsOfLanguage(language.getId());
		onLanguageRowView(R.id.row_language_projects_count, language)
				.check(matches(withText(String.valueOf(count))));
	}

	@Test
	public void showLanguageScreenWhenClickingIntoLanguageView() throws Exception {
		Language language = languages.get(0);
		onView(withTagValue(is((Object)language.getId()))).perform(click());

		matchToolbarTitle(getTargetContext().getString(R.string.main_activity_language) + " " + language.getName());
	}

	private ViewInteraction onLanguageRowView(@IdRes int id, Language language) {
		return onView(allOf(
				withId(id),
				isDescendantOfA(withTagValue(is((Object)language.getId())))));
	}
}
