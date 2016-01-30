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

import java.sql.Date;

/**
 * Représentation d'un utilisateur de l'application
 */
public class Subscriber extends Jsonable {
	
	private int id;
	private String firstName;
	private String lastName;
	private Date birthday;
	private SubscriberMeta meta;
	
	public Subscriber() {}
	
	public Subscriber(int id, String firstName, String lastName, Date birthday) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthday = birthday;
	}

	/**
	 * @return	l'identifiant de l'utilisateur
	 */
	public int getId() {
		return id;
	}

	/**
	 * Saisi l'identifiant de l'utilisateur
	 * 
	 * @param	id
	 * 			le nouvel identifiant de l'utilisateur
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return	le prénom de l'utilisateur
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Saisi le prénom de l'utilisateur
	 * 
	 * @param	firstName
	 * 			le nouveau prénom de l'utilisateur
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return	le nom de famille de l'utilisateur
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Saisi le nom de famille de l'utilisateur
	 * 
	 * @param	lastName
	 * 			le nouveau nom de famille de l'utilisateur
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return	la date d'anniversaire de l'utilisateur
	 */
	public Date getBirthday() {
		return birthday;
	}
	
	/**
	 * Saisi la date d'anniversaire de l'utilisateur
	 * 
	 * @param	birthday
	 * 			la nouvelle date d'anniversaire de l'utilisateur
	 */
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
	/**
	 * @return	les méta-données de l'utilisateur
	 */
	public SubscriberMeta getMeta() {
		return meta;
	}
	
	/**
	 * Saisi les méta-données de l'utilisateur
	 * 
	 * @param	meta
	 * 			les nouvelles méta-données de l'utilisateur
	 */
	public void setMeta(SubscriberMeta meta) {
		this.meta = meta;
	}
	
}
