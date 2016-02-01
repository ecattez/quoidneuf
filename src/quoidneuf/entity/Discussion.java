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

import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Représente une discussion entre plusieurs utilisateurs.
 */
public class Discussion extends Jsonable {

	private String id;
	private String name;
	private List<Subscriber> subscribers = new LinkedList<>();
	private List<Message> messages = new LinkedList<>();

	public Discussion() {}
	
	public Discussion(String name) {
		this.name = name;
	}
	
	public Discussion(String id, String name) {
		this.id = id;
		this.name = name;
	}
	
	/**
	 * @return	l'identifiant de la discussion
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Saisi l'identifiant de la discussion
	 * 
	 * @param	id
	 * 			le nouvel identifiant de la discussion
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * @return	le nom de la discussion
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Saisi le nom de la discussion
	 * 
	 * @param	name
	 * 			le nouveau nom de la discussion
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return	la liste des abonnés à la discussion
	 */
	@JsonProperty("current_subscribers")
	public List<Subscriber> getSubscribers() {
		return subscribers;
	}
	
	/**
	 * Saisi la liste des abonnés à la discussion
	 * 
	 * @param	subscribers
	 * 			la nouvelle liste d'abonnés
	 */
	public void setSubscribers(List<Subscriber> subscribers) {
		this.subscribers = subscribers;
	}
	
	/**
	 * Ajoute un abonné à la discussion
	 * 
	 * @param	sbr
	 * 			le nouvel abonné à ajouter
	 */
	public void push(Subscriber sbr) {
		subscribers.add(sbr);
	}
	
	/**
	 * @return	la liste des messages de la discussion
	 */
	public List<Message> getMessages() {
		return messages;
	}
	
	/**
	 * Ajoute un message à la discussion
	 * 
	 * @param	msg
	 * 			le nouveau message à ajouter
	 */
	public void push(Message msg) {
		messages.add(msg);
	}

}
