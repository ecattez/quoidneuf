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

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author Edouard
 *
 */
public class AppServletListener implements ServletContextListener {
	
	public static final String SUBSCRIBER_PATH_KEY = "subscriberPath";
	public static final String DISCUSSION_PATH_KEY = "discussionPath";
	
	@Override
	public void contextDestroyed(ServletContextEvent e) {
		// Nothing TODO
	}

	@Override
	public void contextInitialized(ServletContextEvent e) {
		System.out.println("Démarrage de l'application");
		ServletContext ctx = e.getServletContext();
		ctx.setAttribute(SUBSCRIBER_PATH_KEY, ctx.getRealPath(ctx.getInitParameter("subscribers")));
		ctx.setAttribute(DISCUSSION_PATH_KEY, ctx.getRealPath(ctx.getInitParameter("discussions")));
	}

}
