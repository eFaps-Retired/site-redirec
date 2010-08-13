/*
 * Copyright 2003 - 2009 The eFaps Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Revision:        $Rev$
 * Last Changed:    $Date$
 * Last Changed By: $Author$
 */

package org.efaps.site_redirect;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

/**
 * Depending on the selected sub domain the related path from eFaps in the
 * Google Code is selected.
 *
 * @author The eFaps Team
 * @version $Id$
 */
public class RedirectServlet
    extends HttpServlet
{
    /**
     * Generated serial version UID of the eFaps redirect servlet.
     */
    private static final long serialVersionUID = -3515249212260192796L;

    /**
     * Default URL where to redirect if no defined server name in
     * {@link #REDIRECTS} is found.
     */
    private static final String DEFAULT_URL = "http://efaps.googlecode.com";

    /**
     * Map defining all redirects from given server name to the http URL.
     */
    private static final Map<String, String> REDIRECTS = new HashMap<String, String>(8);
    static {
        RedirectServlet.REDIRECTS.put("efaps.org",                          RedirectServlet.DEFAULT_URL);
        RedirectServlet.REDIRECTS.put("source.efaps.org",                   "http://code.google.com/p/efaps/source/browse");
        RedirectServlet.REDIRECTS.put("wiki.efaps.org",                     "http://code.google.com/p/efaps/wiki");
        RedirectServlet.REDIRECTS.put("issues.efaps.org",                   "http://code.google.com/p/efaps/issues");

        RedirectServlet.REDIRECTS.put("efaps-kernel.efaps.org",             "http://efaps.googlecode.com/svn/site/eFaps-Kernel/index.html");
        RedirectServlet.REDIRECTS.put("maven-efaps-jetty.efaps.org",        "http://efaps.googlecode.com/svn/site/maven-efaps-jetty/index.html");
        RedirectServlet.REDIRECTS.put("maven-efaps-plugin.efaps.org",       "http://efaps.googlecode.com/svn/site/maven-eFaps-PlugIn/index.html");
        RedirectServlet.REDIRECTS.put("maven-java5.efaps.org",              "http://efaps.googlecode.com/svn/site/maven-java5/index.html");
        RedirectServlet.REDIRECTS.put("maven-slf4jlogger.efaps.org",        "http://efaps.googlecode.com/svn/site/maven-slf4jlogger/index.html");
        RedirectServlet.REDIRECTS.put("number2words.efaps.org",             "http://efaps.googlecode.com/svn/site/number2words/index.html");
        RedirectServlet.REDIRECTS.put("efaps-webapp.efaps.org",             "http://efaps.googlecode.com/svn/site/eFaps-Webapp/index.html");
    }

    /**
     * @param _request      http request
     * @param _response     http response (to redirect)
     * @throws IOException if redirect failed
     */
    @Override()
    public void doGet(final HttpServletRequest _request,
                      final HttpServletResponse _response)
        throws IOException
    {
        // the www. is removed...
        final String serverName = _request.getServerName().startsWith("www.")
                                  ? _request.getServerName().substring(4)
                                  : _request.getServerName();
        final String newUrl = RedirectServlet.REDIRECTS.get(serverName);

        if ("efaps.org".equals(serverName) && "/xsd/eFaps_1.0.xsd".equals(_request.getPathInfo()))  {

            final InputStream in = this.getClass().getClassLoader().getResourceAsStream("xsd/eFaps_1.0.xsd");
            final OutputStream out = _response.getOutputStream();

            IOUtils.copy(in, out);

            in.close();

        } else if (newUrl != null)  {
            _response.sendRedirect(newUrl);
        } else  {
            _response.sendRedirect(RedirectServlet.DEFAULT_URL);
        }
    }

}
