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

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Système de Dao simplifié.
 * 
 * @param	<T>
 * 			le type de la clé d'intégrité référentielle
 */
public abstract class Dao<T extends Serializable> {
	
	/**
	 * @return	retourne une connexion à la base de données via le pool de connexion
	 * @throws	NamingException
	 * 			erreur dans le nom du pool
	 * @throws	SQLException
	 * 			erreur SQL
	 */
	public Connection getConnection() throws NamingException, SQLException {
		Context initCtx = new InitialContext();
		Context envCtx = (Context) initCtx.lookup("java:comp/env");
		DataSource ds = (DataSource) envCtx.lookup("qdnPool");
		return ds.getConnection();
	}
	
	/**
	 * Vérifie l'existence d'un objet en base
	 * 
	 * @param	id
	 * 			l'identifiant de l'objet
	 * 
	 * @return	<true> s'il est trouvé, <false> sinon
	 */
	public abstract boolean exist(T id);

}
