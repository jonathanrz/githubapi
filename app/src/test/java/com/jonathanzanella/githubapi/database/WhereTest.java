package com.jonathanzanella.githubapi.database;

import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class WhereTest {
	@Test
	public void generate_simple_query_successfully() throws Exception {
		Where where = new Where(Fields.ID).eq(1L);
		Select expected = new Select("id = ?", new String[]{"1"});
		Select query = where.query();
		assertThat(query, is(expected));
	}

	@Test
	public void generate_query_with_and_successfully() throws Exception {
		Where where = new Where(Fields.ID).eq(1L).and(Fields.NAME).eq("John Doe");
		Select expected = new Select("id = ? and name = ?", new String[] {"1", "John Doe"});
		Select query = where.query();
		assertThat(query, is(expected));
	}

	@Test
	public void generate_query_with_in() throws Exception {
		Where where = new Where(Fields.NAME).queryIn(Arrays.asList("John Doe", "John Doe2"));
		Select expected = new Select("name in (?,?)", new String[] {"John Doe", "John Doe2"});
		Select query = where.query();
		assertThat(query, is(expected));
	}

	@Test
	public void generate_query_with_order_by() throws Exception {
		Where where = new Where(Fields.ID).eq(1L).orderBy(Fields.ID);
		assertThat(where.orderBy(), is(Fields.ID.toString()));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void throw_exception_with_two_values_added() throws Exception {
		new Where(Fields.ID).eq("").eq("");
	}
}