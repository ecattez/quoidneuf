package quoidneuf.util;

public final class Matcher {

	private Matcher() {}
	
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}
	
	public static boolean isDigits(String str) {
		return str != null && str.matches("[0-9]+");
	}
	
	public static Integer convert(String str) {
		if (isDigits(str)) {
			return Integer.parseInt(str);
		}
		return null;
	}

}
