package org.example;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;

import com.marklogic.junit.NamespaceProvider;
import com.marklogic.junit.spring.AbstractSpringTest;
import com.marklogic.junit.spring.ModulesLoaderTestExecutionListener;
import com.marklogic.junit.spring.ModulesPath;

/**
 * This is intended to be the base class for all of your tests that hit MarkLogic. It initializes a Spring container via
 * the TestConfig class, which extends ml-junit's BasicTestConfig class. New/modified modules are automatically loaded
 * via the listener.
 */
@ContextConfiguration(classes = { TestConfig.class })
@TestExecutionListeners(value = { ModulesLoaderTestExecutionListener.class })
@ModulesPath(baseDir = "src/main/ml-modules")
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
