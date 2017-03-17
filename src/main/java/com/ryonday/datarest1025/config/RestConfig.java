package com.ryonday.datarest1025.config;

import com.ryonday.datarest1025.converter.CustomConverter;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

import java.util.Set;

import static org.slf4j.LoggerFactory.getLogger;

@Configuration
public class RestConfig extends RepositoryRestConfigurerAdapter {

    private static final Logger logger = getLogger(RestConfig.class);

    @Autowired
    @CustomConverter
    private Set<Converter<?, ?>> converters;

    /**
     * This is the SDR way of doing this. Other ways of registering custom converters are COMPLETELY DIFFERENT.
     */
    @Override
    public void configureConversionService(ConfigurableConversionService conversionService) {
        logger.info("Registering custom Converters: {}", converters);
        super.configureConversionService(conversionService);
        converters.forEach(conversionService::addConverter);
        logger.info("Converters registered: {}", conversionService);
    }
}
