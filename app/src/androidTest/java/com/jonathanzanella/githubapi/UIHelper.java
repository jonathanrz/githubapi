package com.jonathanzanella.githubapi;

import android.support.annotation.NonNull;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.internal.util.Checks.checkNotNull;
import static org.hamcrest.Matchers.is;

public class UIHelper {
	public static ViewInteraction matchToolbarTitle(CharSequence title) {
		return onView(isAssignableFrom(Toolbar.class))
				.check(matches(withToolbarTitle(is(title))));
	}

	private static Matcher<Object> withToolbarTitle(final Matcher<CharSequence> textMatcher) {
		return new BoundedMatcher<Object, Toolbar>(Toolbar.class) {
			@Override public boolean matchesSafely(Toolbar toolbar) {
				return textMatcher.matches(toolbar.getTitle());
			}
			@Override public void describeTo(Description description) {
				description.appendText("with toolbar title: ");
				textMatcher.describeTo(description);
			}
		};
	}

	public static Matcher<View> atPosition(final int position, @NonNull final Matcher<View> itemMatcher) {
		checkNotNull(itemMatcher);
		return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
			@Override
			public void describeTo(Description description) {
				description.appendText("has item at position " + position + ": ");
				itemMatcher.describeTo(description);
			}

			@Override
			protected boolean matchesSafely(final RecyclerView view) {
				RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
				if (viewHolder == null) {
					// has no item on such position
					return false;
				}
				return itemMatcher.matches(viewHolder.itemView);
			}
		};
	}
}
