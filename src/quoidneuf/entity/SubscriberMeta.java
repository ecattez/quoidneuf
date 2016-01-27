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
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getPictureUri() {
		return pictureUri;
	}

	public void setPictureUri(String pictureUri) {
		this.pictureUri = pictureUri;
	}

	public String getDescription() {
		if (description == null) {
			description = "";
		}
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEmail() {
		if (email == null) {
			email = "";
		}
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		if (phone == null) {
			phone = "";
		}
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}
