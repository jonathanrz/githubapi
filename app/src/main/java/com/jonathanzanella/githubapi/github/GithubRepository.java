package com.jonathanzanella.githubapi.github;

public class GithubRepository {
	String name;
	String language;

	public String getName() {
		return name;
	}

	public String getLanguage() {
		if(language == null)
			return "Undefined";
		return language;
	}
}