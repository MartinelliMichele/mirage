package com.miragesql.miragesql.filter;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.miragesql.miragesql.session.Session;
import com.miragesql.miragesql.session.SessionFactory;
import com.miragesql.miragesql.util.ExceptionUtil;

/**
 * This filter implements the  <em>Open Session in View</em> pattern in Mirage-SQL.
 * <p>
 * This filter has to be used with {@link Session} and manages transaction automatically through methods of that.
 *
 * @author Naoki Takezoe
 */
public class OpenSessionInViewFilter implements Filter {

	private static final Logger logger = Logger.getLogger(OpenSessionInViewFilter.class.getName());

//	@Override
	public void destroy() {
	}

//	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		Session session = SessionFactory.getSession();

		// begin
		try {
			session.begin();

		} catch(Exception ex){
			logger.severe("Failed to begin Session.");
			logger.severe(ExceptionUtil.toString(ex));
			throw new RuntimeException(ex);
		}

		try {
			chain.doFilter(request, response);

			// commit
			if(!session.isRollbackOnly()){
				try {
					session.commit();

				} catch(Exception ex){
					logger.severe("Failed to commit Session.");
					logger.severe(ExceptionUtil.toString(ex));

					if(ex instanceof RuntimeException){
						throw (RuntimeException) ex;
					}

					throw new RuntimeException(ex);
				}
			}
		} catch(Exception ex){
			session.setRollbackOnly();

		} finally {
			// rollback
			if(session.isRollbackOnly()){
				try {
					session.rollback();

				} catch (Exception e) {
					logger.severe("Failed to rollback Session.");
					logger.severe(ExceptionUtil.toString(e));

					if (e instanceof RuntimeException) {
						throw (RuntimeException) e;
					}

					throw new RuntimeException(e);
				}
			}

			// release
			try {
				session.release();

			} catch(Exception ex){
				logger.severe("Failed to release Session.");
				logger.severe(ExceptionUtil.toString(ex));

				if(ex instanceof RuntimeException){
					throw (RuntimeException) ex;
				}

				throw new RuntimeException(ex);
			}
		}
	}

//	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

}
