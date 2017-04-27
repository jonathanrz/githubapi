package com.jonathanzanella.githubapi.database;

import java.util.ArrayList;
import java.util.List;

public class Where {
	private class Query {
		private Fields field;
		private String operation;

		public Query(Fields field) {
			this.field = field;
		}

		@Override
		public String toString() {
			String query = field.toString() + " " + operation;
			if(!query.contains("in ("))
				query += " ?";
			return query;
		}
	}

	private List<Query> queries = new ArrayList<>();
	private List<String> values = new ArrayList<>();
	private String orderBy = Fields.ID.toString();

	public Where(Fields field) {
		if(field != null)
			queries.add(new Query(field));
	}

	private void setLastQueryOperation(String operation) {
		Query query = queries.get(queries.size() - 1);
		query.operation = operation;
	}

	public Where notEq(String s) {
		isExpectingFieldDefinition();
		setLastQueryOperation("!=");
		values.add(s);
		return this;
	}

	public Where eq(String s) {
		isExpectingFieldDefinition();
		setLastQueryOperation("=");
		values.add(s);
		return this;
	}

	public Where eq(Long l) {
		isExpectingFieldDefinition();
		setLastQueryOperation("=");
		values.add(l.toString());
		return this;
	}

	public Where eq(boolean b) {
		isExpectingFieldDefinition();
		setLastQueryOperation("=");
		values.add(b ? "1" : "0");
		return this;
	}

	public Where lessThanOrEq(Long l) {
		isExpectingFieldDefinition();
		setLastQueryOperation("<=");
		values.add(l.toString());
		return this;
	}

	public Where greaterThanOrEq(Long l) {
		isExpectingFieldDefinition();
		setLastQueryOperation(">=");
		values.add(l.toString());
		return this;
	}

	public Where queryIn(List<String> list) {
		String string = "in (";
		for (String s : list) {
			string = string + "?,";
			values.add(s);
		}

		string = string.substring(0, string.length() - 1) + ")";
		setLastQueryOperation(string);
		return this;
	}

	public Where and(Fields field) {
		queries.add(new Query(field));
		return this;
	}

	public Select query()  {
		StringBuilder query = new StringBuilder();
		for (int i = 0; i < queries.size(); i++) {
			query = query.append(queries.get(i).toString());
			if (i != queries.size() - 1) {
				query = query.append(" and ");
			}
		}
		return new Select(query.toString(), values.toArray(new String[0]));
	}

	public Where orderBy(Fields field) {
		orderBy = field.toString();
		return this;
	}

	public Where orderByDesc(Fields field) {
		orderBy = field.toString() + " desc";
		return this;
	}

	public String orderBy() {
		return orderBy;
	}

	private void isExpectingFieldDefinition() {
		if (queries.size() - 1 != values.size())
			throw new UnsupportedOperationException("More fields than values added");
	}
}
