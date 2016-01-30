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

import java.sql.Timestamp;

/**
 * Représente un message écrit par un utilisateur.
 */
public class Message extends Jsonable {
	
	private Subscriber owner;
	private String content;
	private Timestamp writtenDate;
	
	public Message() {}
	
	public Message(Subscriber subscriber, String content, Timestamp writtenDate) {
		this.owner = subscriber;
		this.content = content;
		this.writtenDate = writtenDate;
	}
	
	/**
	 * @return	le propriétaire du message
	 */
	public Subscriber getOwner() {
		return owner;
	}

	/**
	 * Saisi le propriétaire du message
	 * 
	 * @param	owner
	 * 			le nouveau propriétaire du message
	 */
	public void setOwner(Subscriber owner) {
		this.owner = owner;
	}

	/**
	 * @return	le contenu du message
	 */
	public String getContent() {
		return content;
	}
	
	/**
	 * Saisi le contenu du message
	 * 
	 * @param	content
	 * 			le nouveau contenu du message
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return	la date d'écriture du message
	 */
	public Timestamp getWrittenDate() {
		return writtenDate;
	}

	/**
	 * Saisi la date d'écriture du message
	 * 
	 * @param	writtenDate
	 * 			la nouvelle date d'écriture du message
	 */
	public void setWrittenDate(Timestamp writtenDate) {
		this.writtenDate = writtenDate;
	}

}
