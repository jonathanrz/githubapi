package com.jonathanzanella.githubapi.language;

import com.jonathanzanella.githubapi.database.Fields;
import com.jonathanzanella.githubapi.database.Repository;
import com.jonathanzanella.githubapi.database.Where;

import java.util.List;

public class LanguageRepository {
	private final Repository<Language> repository;
	private final LanguageTable table = new LanguageTable();

	public LanguageRepository(Repository<Language> repository) {
		this.repository = repository;
	}

	public List<Language> all() {
		return repository.query(table, new Where(null).orderBy(Fields.NAME));
	}

	Language findById(long id) {
		List<Language> languages = repository.query(table, new Where(Fields.ID).eq(id));
		if(languages.size() > 0)
			return languages.get(0);
		return null;
	}

	public Language findByName(String name) {
		List<Language> languages = repository.query(table, new Where(Fields.NAME).eq(name));
		if(languages.size() > 0)
			return languages.get(0);
		return null;
	}

	public void deleteInvalid() {
		repository.delete(table, new Where(Fields.VALID).eq(false));
	}

	public void save(Language language) {
		repository.save(table, language);
	}
}