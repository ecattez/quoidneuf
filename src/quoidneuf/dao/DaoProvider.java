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
package quoidneuf.dao;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Edouard
 *
 */
public class DaoProvider {
	
	private static DaoProvider daoProvider;
	
	public static <T extends Dao<?>> T getDao(Class<T> clazz) {
		if (daoProvider == null) {
			daoProvider = new DaoProvider();
		}
		return daoProvider.accessDao(clazz);
	}
	
	private Map<Class<? extends Dao<?>>, Dao<?>> map;
	
	private DaoProvider() {
		map = new HashMap<>();
		map.put(AuthenticationDao.class, new AuthenticationDao());
		map.put(DiscussionDao.class, new DiscussionDao());
		map.put(FriendDao.class, new FriendDao());
		map.put(MessageDao.class, new MessageDao());
		map.put(SubscriberDao.class, new SubscriberDao());
		map.put(SubscriberMetaDao.class, new SubscriberMetaDao());
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Dao<?>> T accessDao(Class<T> clazz) {
		return map.containsKey(clazz) ? (T) map.get(clazz) : null;
	}
	
}
