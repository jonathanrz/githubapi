package com.jonathanzanella.githubapi.mock;

import com.jonathanzanella.githubapi.Model;
import com.jonathanzanella.githubapi.database.Fields;
import com.jonathanzanella.githubapi.database.Repository;
import com.jonathanzanella.githubapi.database.Select;
import com.jonathanzanella.githubapi.database.Table;
import com.jonathanzanella.githubapi.database.Where;

import java.util.ArrayList;
import java.util.List;

public class RepositoryMock<T extends Model> implements Repository<T> {
	private ArrayList<T> items = new ArrayList<>();
	private long lastId = 1L;

	@Override
	public List<T> query(Table<T> table, Where where) {
		return query(where);
	}

	@Override
	public long count(Table<T> table, Where where) {
		return query(where).size();
	}

	private ArrayList<T> query(Where where) {
		ArrayList<T> queriedItems = (ArrayList<T>)items.clone();
		Select select = where.query();
		String[] queries = select.getWhere().split("=");
		String[] parameters = select.getParameters();
		for (int i = 0; i < queries.length; i++) {
			String query = queries[i].trim();
			if(query.equals(Fields.ID.toString())) {
				filter(queriedItems, Fields.ID, parameters[i]);
			} else if(query.equals(Fields.VALID.toString())) {
				filter(queriedItems, Fields.VALID, parameters[i]);
			} else if(query.equals(Fields.NAME.toString())) {
				filter(queriedItems, Fields.NAME, parameters[i]);
			} else {
				if(query.length() > 0 && !query.equals("?"))
					throw new UnsupportedOperationException(query + " is not mapped");
			}
		}
		return queriedItems;
	}

	private void filter(List<T> items, final Fields field, String data) {
		for (int i = 0; i < items.size(); i++) {
			T model = items.get(i);
			boolean valid;
			switch (field) {
				case ID: {
					valid = (model.getId() == Integer.parseInt(data));
					break;
				}
				case VALID: {
					valid = (model.isValid() == Boolean.parseBoolean(data));
					break;
				}
				case NAME: {
					valid = model.getName().equals(data);
					break;
				}
				default: {
					throw new UnsupportedOperationException(field.toString() + " is not mapped");
				}
			}
			if(!valid) {
				items.remove(i);
				i--;
			}
		}
	}

	@Override
	public void delete(Table<T> table, Where where) {
		List<T> dataToDelete = query(where);
		for (T modelToDelete : dataToDelete) {
			for (int i = 0; i < items.size(); i++) {
				T localItem = items.get(i);
				if(localItem.getId().equals(modelToDelete.getId())) {
					items.remove(i);
					i--;
				}
			}
		}
	}

	@Override
	public void save(Table<T> table, T data) {
		for (int i = 0; i < items.size(); i++) {
			T localItem = items.get(i);
			if(localItem.getId().equals(data.getId())) {
				items.set(i, data);
				return;
			}
		}

		data.setId(lastId++);
		items.add(data);
	}
}
