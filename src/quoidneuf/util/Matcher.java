package quoidneuf.util;

public final class Matcher {

	private Matcher() {}
	
	public static boolean isDigits(String str) {
		return str != null && str.matches("[0-9]+");
	}

}
