/**
 * This file is part of quoidneuf.
 *
 * quoidneuf is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * quoidneuf is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.				 
 * 
 * You should have received a copy of the GNU General Public License
 * along with quoidneuf.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * @author Edouard CATTEZ <edouard.cattez@sfr.fr> (La 7 Production)
 */
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
	
	/**
	 * Vérifie si une chaîne n'est composée que de chiffres
	 * 
	 * @param	str
	 * 			la chaîne de caractères à vérifier
	 * 
	 * @return	<true> si la chaîne de caractères n'est composée que de chiffres, <false> sinon
	 */
	public static boolean isDigits(String str) {
		return str != null && str.matches("[0-9]+");
	}
	
	/**
	 * Vérifie si une chaîne de caractères est une date au format DATE_PATTERN
	 * 
	 * @param	str
	 * 			la chaîne de caractères à vérifier
	 * 
	 * @return	<true> si la chaîne de caractères est une date, <false> sinon
	 */
	public static boolean isDate(String str) {
		Date date = null;
		try {
			date = new SimpleDateFormat(DATE_PATTERN).parse(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date != null;
	}
	
	/**
	 * Vérifie si une chaîne de caractères est un email
	 * 
	 * @param	str
	 * 			la chaîne de caractères à vérifier
	 * 
	 * @return	<true> si la chaîne de caractères est un email, <false> sinon
	 */
	public static boolean isEmail(String str) {
		return str != null && str.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
	}
	
	/**
	 * Vérifie si une chaîne de caractères un numéro de téléphone (FR)
	 * 
	 * @param	str
	 * 			la chaîne de caractères à vérifier
	 * 
	 * @return	<true> si la chaîne de caractères est un numéro de téléphone, <false> sinon
	 */
	public static boolean isPhone(String str) {
		return str != null && str.matches("[0-9]{10}");
	}
	
	/**
	 * Vérifie si une chaîne de caractères est un mot de passe valide (taille > Credential.MIN_PASSWORD_LENGTH)
	 * 
	 * @param	str
	 * 			la chaîne de caractères à vérifier
	 * 
	 * @return	<true> si la chaîne de caractères est un mot de passe valide, <false> sinon
	 */
	public static boolean isPassword(String str) {
		return str != null && str.length() >= Credential.MIN_PASSWORD_LENGTH;
	}
	
	/**
	 * Converti une chaîne de caractères en entier
	 * 
	 * @param	str
	 * 			la chaîne de caractères à convertir
	 * 
	 * @return	une instance d'Integer en cas de succès, null sinon
	 */
	public static Integer convertInt(String str) {
		if (isDigits(str)) {
			return Integer.parseInt(str);
		}
		return null;
	}
	
	/**
	 * Converti une chaîne de caractères en booléen
	 * 
	 * @param	str
	 * 			la chaîne de caractères à convertir
	 * 
	 * @return	une instance de Boolean en cas de succès, null sinon
	 */
	public static Boolean convertBoolean(String str) {
		if (str == null) {
			return null;
		}
		return Boolean.valueOf(str.toLowerCase());
	}
	
	/**
	 * Converti une chaîne de caractères en date
	 * 
	 * @param	str
	 * 			la chaîne de caractères à convertir
	 * 
	 * @return	une instance de Date en cas de succès, null sinon
	 */
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
