package org.example;

import org.junit.Test;

import com.marklogic.client.io.Format;
import com.marklogic.client.io.StringHandle;
import com.marklogic.junit.Fragment;

/**
 * Sample ml-junit test. Note that the database is cleared at the start of the test, but not at the end, so that you can
 * manually inspect documents after the test has completed.
 */
public class WriteAndReadDocumentTest extends AbstractAppTest {

    /**
     * This is a basic test - it loads an XML document using the MarkLogic Java API, and then it reads that document
     * back as a String using the MarkLogic Java API. That String is then parsed as an XML fragment, and with the
     * Fragment object, we can easily make a number of assertions.
     */
    @Test
    public void writeAndReadDocument() {
        String xml = "<person xmlns='http://marklogic.com/sample'><name>Jane</name><description>This is a test</description></person>";
        getClient().newXMLDocumentManager().write("/jane.xml", new StringHandle(xml).withFormat(Format.XML));

        xml = getClient().newXMLDocumentManager().read("/jane.xml", new StringHandle()).get();
        Fragment frag = parse(xml);
        frag.assertElementValue("This is a basic assertion on the text node of a particular element",
                "/sample:person/sample:name", "Jane");
        frag.assertElementExists("Can also assert on the presence of an element as expressed via XPath",
                "//sample:description[. = 'This is a test']");

        // Example of using prettyPrint for debugging
        frag.prettyPrint();
    }
}
