package com.jonathanzanella.githubapi.database;

import java.util.Arrays;

public class Select {
	private String where;
	private String [] parameters;

	public Select(String where, String [] parameters) {
		this.where = where;
		this.parameters = parameters;
	}

	public String getWhere() {
		return where;
	}

	public String[] getParameters() {
		return parameters;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Select)) return false;

		Select select = (Select) o;

		if (where != null ? !where.equals(select.where) : select.where != null) return false;
		return Arrays.deepEquals(parameters, select.parameters);
	}

	@Override
	public int hashCode() {
		int result = where != null ? where.hashCode() : 0;
		result = 31 * result + Arrays.hashCode(parameters);
		return result;
	}
}
