package quoidneuf.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Json {
	
	private static Json jsonBuilder;

	private ObjectMapper mapper = new ObjectMapper();
	
	private Json() {}
	
	public <T> T read(String src, Class<T> valueType) {
		try {
			return (T) mapper.readValue(src, valueType);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String write(Object value) {
		try {
			return mapper.writeValueAsString(value);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Json getInstance() {
		if (jsonBuilder == null) {
			jsonBuilder = new Json();
		}
		return jsonBuilder;
	}

}
