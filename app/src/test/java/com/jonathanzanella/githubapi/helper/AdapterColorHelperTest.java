package com.jonathanzanella.githubapi.helper;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class AdapterColorHelperTest {
	@Test
	public void generate_colors_successfully() throws Exception {
		AdapterColorHelper adapterColorHelper = new AdapterColorHelper(1, 2);

		assertThat(adapterColorHelper.getColorForGridWithTwoColumns(0), is(1));
		assertThat(adapterColorHelper.getColorForGridWithTwoColumns(1), is(2));
		assertThat(adapterColorHelper.getColorForGridWithTwoColumns(2), is(2));
		assertThat(adapterColorHelper.getColorForGridWithTwoColumns(3), is(1));
		assertThat(adapterColorHelper.getColorForGridWithTwoColumns(4), is(1));
		assertThat(adapterColorHelper.getColorForGridWithTwoColumns(5), is(2));
		assertThat(adapterColorHelper.getColorForGridWithTwoColumns(6), is(2));
		assertThat(adapterColorHelper.getColorForGridWithTwoColumns(7), is(1));

		assertThat(adapterColorHelper.getColorForLinearLayout(0), is(1));
		assertThat(adapterColorHelper.getColorForLinearLayout(1), is(2));
		assertThat(adapterColorHelper.getColorForLinearLayout(2), is(1));
		assertThat(adapterColorHelper.getColorForLinearLayout(3), is(2));
	}
}