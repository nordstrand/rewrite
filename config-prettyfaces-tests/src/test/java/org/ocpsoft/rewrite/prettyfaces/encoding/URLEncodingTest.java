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
package org.ocpsoft.rewrite.prettyfaces.encoding;

import junit.framework.Assert;

import org.apache.http.client.methods.HttpGet;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ocpsoft.rewrite.prettyfaces.PrettyFacesTestBase;
import org.ocpsoft.rewrite.test.HttpAction;
import org.ocpsoft.rewrite.test.RewriteTestBase;

@RunWith(Arquillian.class)
public class URLEncodingTest extends RewriteTestBase
{
   @Deployment(testable = false)
   public static WebArchive createDeployment()
   {
      return PrettyFacesTestBase.getDeployment()
               .addClass(EncodingBean.class)
               .addAsWebResource("encoding/encoding.xhtml", "encoding.xhtml")
               .addAsWebInfResource("encoding/encoding-pretty-config.xml", "pretty-config.xml");
   }

   /**
    * Test a rewrite rule using the 'substitute' attribute to modify the URL.
    * 
    * @see http://code.google.com/p/prettyfaces/issues/detail?id=76
    */
   @Test
   public void testRewriteEncodingSubstitute() throws Exception
   {
      String target = "/virtual/rewrite/substitute";
      String expected = "/virtuální";

      HttpAction<HttpGet> action = get(target);

      Assert.assertTrue(action.getResponseContent().contains(action.getContextPath() + expected));
   }

   /**
    * Test a rewrite rule using the 'url' attribute to create a completely new URL.
    * 
    * @see http://code.google.com/p/prettyfaces/issues/detail?id=76
    */
   @Test
   public void testRewriteEncodingUrl() throws Exception
   {
      String target = "/virtual/rewrite/url";
      String expected = "/virtuální";

      HttpAction<HttpGet> action = get(target);

      Assert.assertTrue(action.getCurrentURL().endsWith(expected));
      Assert.assertTrue(action.getResponseContent().contains(expected));
   }

   @Test
   public void testPrettyFacesFormActionURLEncodesProperly() throws Exception
   {
      String expected = "/custom/form";

      HttpAction<HttpGet> action = get(expected);

      Assert.assertTrue(action.getCurrentURL().endsWith(expected));
      Assert.assertTrue(action.getResponseContent().contains(expected));
   }

   @Test
   // http://code.google.com/p/prettyfaces/issues/detail?id=64
   public void testPrettyFacesFormActionURLEncodesProperlyWithCustomRegexAndMultiplePathSegments() throws Exception
   {
      String expected = "/foo/bar/baz/car/";

      HttpAction<HttpGet> action = get(expected);

      Assert.assertTrue(action.getCurrentURL().endsWith(expected));
      Assert.assertTrue(action.getResponseContent().contains(expected));

      Assert.assertTrue(action.getResponseContent().contains("beanPathText=foo/bar/baz/car"));
   }

   @Test
   public void testNonMappedRequestRendersRewrittenURL() throws Exception
   {
      HttpAction<HttpGet> action = get("/encoding.jsf");

      Assert.assertTrue(action.getCurrentURL().endsWith("/encoding.jsf"));
      Assert.assertTrue(action.getResponseContent().contains("/custom/form"));
   }

   @Test
   public void testURLDecoding() throws Exception
   {
      HttpAction<HttpGet> action = get("/encoding/Vračar?dis=Fooo Bar");

      Assert.assertTrue(action.getCurrentURL().endsWith("/encoding/Vračar?dis=Fooo Bar"));
      Assert.assertTrue(action.getResponseContent().contains("/custom/form"));

      // Test a managed bean
      Assert.assertTrue(action.getResponseContent().contains("beanPathText=Vračar"));
   }

   @Test
   public void testQueryDecoding() throws Exception
   {
      HttpAction<HttpGet> action = get("/encoding/Vračar?dis=Fooo%20Bar");

      Assert.assertTrue(action.getCurrentURL().endsWith("/encoding/Vračar?dis=Fooo%20Bar"));
      Assert.assertTrue(action.getResponseContent().contains("/custom/form"));

      // Test a managed bean
      Assert.assertTrue(action.getResponseContent().contains("beanQueryText=Fooo Bar"));
   }

   @Test
   public void testQueryWithGermanUmlaut() throws Exception
   {
      // query parameter contains a German 'ü' encoded with UTF8
      HttpAction<HttpGet> action = get("/encoding/Vračar?dis=%C3%BC");

      Assert.assertTrue(action.getCurrentURL().endsWith("/encoding/Vračar?dis=%C3%BC"));
      Assert.assertTrue(action.getResponseContent().contains("/rewrite-test/encoding/Vračar?dis=%C3%BC"));

      // Test a managed bean
      Assert.assertTrue(action.getResponseContent().contains("beanQueryText=\u00fc"));
   }

   @Test
   public void testPatternDecoding() throws Exception
   {
      HttpAction<HttpGet> action = get("/hard encoding/Vračar");
      Assert.assertEquals(200, action.getStatusCode());
   }

   @Test
   public void testEncodedURLMatchesNonEncodedPattern() throws Exception
   {
      HttpAction<HttpGet> action = get("/URL%20ENCODED");
      Assert.assertEquals(200, action.getStatusCode());
   }

   // public void testNoDecodeOnSubmitDoesNotCrash() throws Exception
   // {
   // JSFSession jsfSession = new JSFSession("/decodequery");
   // JSFServerSession server = jsfSession.getJSFServerSession();
   // JSFClientSession client = jsfSession.getJSFClientSession();
   // assertEquals("encoding.jsf", server.getCurrentViewID());
   //
   // client.setValue("input1", "%");
   // client.click("submit");
   // }
}
