package quoidneuf.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import quoidneuf.entity.Credential;

public final class Matcher {
	
	public static final String DATE_PATTERN = "yyyy-MM-dd";

	private Matcher() {}
	
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}
	
	public static boolean isDigits(String str) {
		return str != null && str.matches("[0-9]+");
	}
	
	public static boolean isDate(String str) {
		Date date = null;
		try {
			date = new SimpleDateFormat(DATE_PATTERN).parse(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date != null;
	}
	
	public static boolean isEmail(String email) {
		return email != null && email.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
	}
	
	public static boolean isPhone(String phone) {
		return phone != null && phone.matches("[0-9]{10}");
	}
	
	public static boolean isPassword(String password) {
		return password != null && password.length() > Credential.MIN_PASSWORD_LENGTH;
	}
	
	public static Integer convertInt(String str) {
		if (isDigits(str)) {
			return Integer.parseInt(str);
		}
		return null;
	}
	
	public static Boolean convertBoolean(String str) {
		if (str == null) {
			return null;
		}
		return Boolean.valueOf(str.toLowerCase());
	}
	
	public static Date convertDate(String str) {
		Date date = null;
		try {
			date = new SimpleDateFormat(DATE_PATTERN).parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

}
