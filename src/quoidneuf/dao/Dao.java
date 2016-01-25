package quoidneuf.dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public abstract class Dao<T> {
	
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
