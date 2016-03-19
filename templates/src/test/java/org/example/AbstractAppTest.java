package org.example;

import org.springframework.test.context.ContextConfiguration;

import com.marklogic.junit.NamespaceProvider;
import com.marklogic.junit.spring.AbstractSpringTest;

/**
 * This is intended to be the base class for all of your tests that hit MarkLogic. It initializes a Spring container via
 * the TestConfig class, which extends ml-junit's BasicTestConfig class.
 */
@ContextConfiguration(classes = { TestConfig.class })
public abstract class AbstractAppTest extends AbstractSpringTest {

    /**
     * Returns an app-specific namespace provider so that app-specific namespace prefixes can be used in XPath
     * assertions.
     */
    @Override
    protected NamespaceProvider getNamespaceProvider() {
        return new AppNamespaceProvider();
    }

}
