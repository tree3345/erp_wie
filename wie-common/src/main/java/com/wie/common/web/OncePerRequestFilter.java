/**   
 * @Title: OncePerRequestFilter.java 
 * @Package com.tgyt.common.web 
 * @Description: 
 * @author zhangfeng 13940488705@163.com 
 * @date 2011-8-9 上午10:27:07 
 * @version V1.0   
 */

package com.wie.common.web;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * Filter base class that guarantees to be just executed once per request,
 * on any servlet container. It provides a {@link #doFilterInternal}
 * method with HttpServletRequest and HttpServletResponse arguments.
 *
 * <p>The {@link #getAlreadyFilteredAttributeName} method determines how
 * to identify that a request is already filtered. The default implementation
 * is based on the configured name of the concrete filter instance.
 *
 * @author Juergen Hoeller
 * @since 06.12.2003
 */
public abstract class OncePerRequestFilter extends BaseFilter {

	/**
	 * Suffix that gets appended to the filter name for the
	 * "already filtered" request attribute.
	 * @see #getAlreadyFilteredAttributeName
	 */
	public static final String ALREADY_FILTERED_SUFFIX = ".FILTERED";


	/**
	 * This <code>doFilter</code> implementation stores a request attribute for
	 * "already filtered", proceeding without filtering again if the
	 * attribute is already there.
	 * @see #getAlreadyFilteredAttributeName
	 * @see #shouldNotFilter
	 * @see #doFilterInternal
	 */
	public final void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
			throw new ServletException("OncePerRequestFilter just supports HTTP requests");
		}
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		String alreadyFilteredAttributeName = getAlreadyFilteredAttributeName();
		if (request.getAttribute(alreadyFilteredAttributeName) != null || shouldNotFilter(httpRequest)) {
			// Proceed without invoking this filter...
			filterChain.doFilter(request, response);
		}
		else {
			// Do invoke this filter...
			request.setAttribute(alreadyFilteredAttributeName, Boolean.TRUE);
			doFilterInternal(httpRequest, httpResponse, filterChain);
		}
	}

	/**
	 * Return the name of the request attribute that identifies that a request
	 * is already filtered.
	 * <p>Default implementation takes the configured name of the concrete filter
	 * instance and appends ".FILTERED". If the filter is not fully initialized,
	 * it falls back to its class name.
	 * @see #getFilterName
	 * @see #ALREADY_FILTERED_SUFFIX
	 */
	protected String getAlreadyFilteredAttributeName() {
		String name = this.filterConfig.getFilterName();
		if (name == null) {
			name = getClass().getName();
		}
		return name + ALREADY_FILTERED_SUFFIX;
	}

	/**
	 * Can be overridden in subclasses for custom filtering control,
	 * returning <code>true</code> to avoid filtering of the given request.
	 * <p>The default implementation always returns <code>false</code>.
	 * @param request current HTTP request
	 * @return whether the given request should <i>not</i> be filtered
	 * @throws ServletException in case of errors
	 */
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return false;
	}


	/**
	 * Same contract as for <code>doFilter</code>, but guaranteed to be
	 * just invoked once per request. Provides HttpServletRequest and
	 * HttpServletResponse arguments instead of the default ServletRequest
	 * and ServletResponse ones.
	 */
	protected abstract void doFilterInternal(
			HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException;

}
