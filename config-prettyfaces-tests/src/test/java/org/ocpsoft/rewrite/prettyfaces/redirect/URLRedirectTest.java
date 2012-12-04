/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ocpsoft.rewrite.prettyfaces.redirect;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.runner.RunWith;
import org.ocpsoft.rewrite.prettyfaces.PrettyFacesTestBase;
import org.ocpsoft.rewrite.test.RewriteTestBase;

@RunWith(Arquillian.class)
public class URLRedirectTest extends RewriteTestBase
{
   @Deployment(testable = false)
   public static WebArchive createDeployment()
   {
      return PrettyFacesTestBase.getDeployment()
               .addClass(RedirectBean.class)
               .addAsWebResource("redirect/redirect.xhtml", "redirect.xhtml")
               .addAsWebInfResource("redirect/redirect-pretty-config.xml", "pretty-config.xml");
   }

   // TODO FIXME these tests need to be rewritten, probably using arquillian Drone
   // @Test
   // public void testRefreshEncodesValuesPropertly() throws Exception
   // {
   // String expected = "/1 1/2 2";
   //
   // HttpAction<HttpGet> action = get(expected);
   //
   // JSFClientSession client = jsfSession.getJSFClientSession();
   //
   // FacesContext context = server.getFacesContext();
   // PrettyContext prettyContext = PrettyContext.getCurrentInstance(context);
   //
   // client.click("refresh");
   //
   // String actual = prettyContext.getRequestURL().toString();
   // assertEquals(expected, actual);
   // }
   //
   // @Test
   // public void testRedirectEncodesValuesPropertly() throws Exception
   // {
   // String requestURL = "/foo/%20%3F%20?que=ora.+es";
   //
   // JSFSession jsfSession = new JSFSession(requestURL);
   // JSFServerSession server = jsfSession.getJSFServerSession();
   //
   // JSFClientSession client = jsfSession.getJSFClientSession();
   //
   // FacesContext context = server.getFacesContext();
   // PrettyContext prettyContext = PrettyContext.getCurrentInstance(context);
   //
   // String attribute = client.getElement("form").getAttribute("action");
   // client.click("redirect");
   //
   // // doesn't seem to work. But I think our code is correct. Perhaps a HTTPUnit problem?
   // // String browserURL = client.getContentPage().getUrl().toString();
   // // assertTrue(browserURL.contains(requestURL));
   //
   // String expected = "/foo/" + RedirectBean.PATH_VALUE;
   // String actual = prettyContext.getRequestURL().toString();
   // assertEquals(expected, actual);
   //
   // // TODO QueryString should probably separate encoding from default behavior
   // // String expectedQuery = "?que=" + RedirectBean.QUERY_VALUE;
   // // String actualQuery = prettyContext.getRequestQueryString().toString();
   // // assertEquals(expectedQuery, actualQuery);
   // }
}
