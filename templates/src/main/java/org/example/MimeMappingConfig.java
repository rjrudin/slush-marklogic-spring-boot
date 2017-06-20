package org.example;


import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.MimeMappings;

import org.springframework.context.annotation.Configuration;
/**
 * Configuration to map file extensions to mime type header in http responses
 */
@Configuration
public class MimeMappingConfig implements EmbeddedServletContainerCustomizer {

	/**
	 * Customizes the container configuration programmatically (replacement for web.xml)
	 *
	 * @return
	 */
	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
		mappings.add("woff2", "font/woff2");
		container.setMimeMappings(mappings);
	}


}
