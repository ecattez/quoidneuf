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
package quoidneuf.entity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Représente les identifiants d'un utilisateur
 */
public class Credential extends Jsonable {
	
	public static final int MAX_LOGIN_LENGTH = 16;
	public static final int MIN_PASSWORD_LENGTH = 8;
	public static final String DEFAULT_ROLE = "user";
	
	private String login;
	private String password;
	
	public Credential(String login, String password) {
		this.login = login;
		this.password = password;
	}
	
	/**
	 * @return	le login de l'utilisateur
	 */
	public String getLogin() {
		return login;
	}
	
	/**
	 * Saisi le login de l'utilisateur
	 * 
	 * @param	login
	 * 			le nouveau login de l'utilisateur
	 */
	public void setLogin(String login) {
		this.login = login;
	}
	
	/**
	 * @return	le mot de passe de l'utilisateur
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Saisi le mot de passe de l'utilisateur
	 * 
	 * @param	password
	 * 			le nouveau mot de passe de l'utilisateur
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * Crypte un password en MD5
	 * 
	 * @param	password
	 * 			le mot de passe à crypté
	 * 
	 * @return	le mot de passe crypté
	 */
	public static String encode(String password) {
		byte[] unique = password.getBytes();
		byte[] hash = null;

		try {
			hash = MessageDigest.getInstance("MD5").digest(unique);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		StringBuilder builder = new StringBuilder();
		String hex;
		for (int i=0; i < hash.length; i++) {
			hex = Integer.toHexString(hash[i]);
			if (hex.length() == 1) {
				builder.append('0').append(hex);
			}
			else {
				builder.append(hex.substring(hex.length() - 2));
			}
		}
		return builder.toString();
	}
	
	/**
	 * @return	un mot de passe généré aléatoirement à 8 caractères
	 */
	public static String randomPassword() {
		return encode(String.valueOf(new Random().nextInt())).substring(0, 8);
	}

}
