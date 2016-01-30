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

public class SubscriberMeta extends Jsonable {

	private int id;
	private String pictureUri;
	private String description;
	private String email;
	private String phone;
	
	public SubscriberMeta() {}
	
	/**
	 * @return	l'identifiant des méta-données d'un utilisateur
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Saisi l'identifiant des méta-données d'un utilisateur
	 * 
	 * @param	id
	 * 			le nouvel identifiant des méta-données d'un utilisateur
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * @return	le chemin d'accès à la photo de profil de l'utilisateur
	 */
	public String getPictureUri() {
		if (pictureUri == null) {
			pictureUri = "";
		}
		return pictureUri;
	}

	/**
	 * Saisi le chemin d'accès à la photo de profil de l'utilisateur
	 * 
	 * @param	pictureUri
	 * 			le nouveau chemin d'accès à la photo de profil de l'utilisateur
	 */
	public void setPictureUri(String pictureUri) {
		this.pictureUri = pictureUri;
	}

	/**
	 * @return	la description du profil de l'utilisateur
	 */
	public String getDescription() {
		if (description == null) {
			description = "";
		}
		return description;
	}

	/**
	 * Saisi la description du profil de l'utilisateur
	 * 
	 * @param	description
	 * 			la nouvelle description du profil de l'utilisateur
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return	l'email de l'utilisateur
	 */
	public String getEmail() {
		if (email == null) {
			email = "";
		}
		return email;
	}

	/**
	 * Saisi l'email de l'utilisateur
	 * 
	 * @param	email
	 * 			le nouvel email de l'utilisateur
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return	le numéro de téléphone de l'utilisateur
	 */
	public String getPhone() {
		if (phone == null) {
			phone = "";
		}
		return phone;
	}

	/**
	 * Saisi le numéro de téléphone de l'utilisateur
	 * 
	 * @param	phone
	 * 			le nouveau numéro de téléphone de l'utilisateur
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}
