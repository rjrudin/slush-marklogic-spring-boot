package org.example;

import java.util.List;

import org.jdom2.Namespace;

import com.marklogic.junit.MarkLogicNamespaceProvider;

public class AppNamespaceProvider extends MarkLogicNamespaceProvider {

    /**
     * Add namespaces to this list so that you can use the prefixes in assertions in your tests.
     */
    @Override
    protected List<Namespace> buildListOfNamespaces() {
        List<Namespace> list = super.buildListOfNamespaces();
        list.add(Namespace.getNamespace("sample", "http://marklogic.com/sample"));
        return list;
    }
}
