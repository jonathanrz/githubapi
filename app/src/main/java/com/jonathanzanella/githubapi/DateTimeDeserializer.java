package com.jonathanzanella.githubapi;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.lang.reflect.Type;

class DateTimeDeserializer implements JsonDeserializer<DateTime> {
	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

	@Override
	public DateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		final String dateAsString = json.getAsString();
		if (dateAsString.length() == 0) {
			return null;
		} else {
			return DATE_FORMAT.parseDateTime(dateAsString);
		}
	}
}