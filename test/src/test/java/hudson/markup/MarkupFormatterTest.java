/*
 * The MIT License
 *
 * Copyright (c) 2010, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package hudson.markup;

import hudson.security.AuthorizationStrategy.Unsecured;
import hudson.security.HudsonPrivateSecurityRealm;
import org.jvnet.hudson.test.HudsonTestCase;
import org.jvnet.hudson.test.TestExtension;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;
import java.io.Writer;

/**
 * @author Kohsuke Kawaguchi
 */
public class MarkupFormatterTest extends HudsonTestCase {
    public void testConfigRoundtrip() throws Exception {
        jenkins.setSecurityRealm(new HudsonPrivateSecurityRealm(false));
        jenkins.setAuthorizationStrategy(new Unsecured());
        jenkins.setMarkupFormatter(new DummyMarkupImpl("hello"));
        configRoundtrip();

        assertEquals("hello", ((DummyMarkupImpl) jenkins.getMarkupFormatter()).prefix);
    }

    public static class DummyMarkupImpl extends MarkupFormatter {
        public final String prefix;
        @DataBoundConstructor
        public DummyMarkupImpl(String prefix) {
            this.prefix = prefix;
        }

        @Override
        public void translate(String markup, Writer output) throws IOException {
            output.write(prefix+"["+markup+"]");
        }

        @TestExtension
        public static class DescriptorImpl extends MarkupFormatterDescriptor {
            @Override
            public String getDisplayName() {
                return "dummy";
            }
        }
    }
}
