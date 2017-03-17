package com.ryonday.datarest1025.converter;

import com.google.common.base.Joiner;
import com.ryonday.datarest1025.domain.Foo;
import com.ryonday.datarest1025.domain.FooId;
import org.slf4j.Logger;
import org.springframework.data.rest.webmvc.spi.BackendIdConverter;
import org.springframework.stereotype.Component;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * A {@link BackendIdConverter} is responsible for the representation of an entity ID in generated URIs.
 *
 * This is different from a {@link org.springframework.core.convert.converter.Converter}, which in
 * Spring-Data-REST is what takes what's on the URL and translates it to the Id itself.
 *
 * Necessary for composite keys.
 */
@Component
public class FooIdBackendConverter implements BackendIdConverter {


    private static final Logger logger = getLogger(FooIdBackendConverter.class);

    private static final Joiner j = Joiner.on('-').skipNulls();

    @Override
    public Serializable fromRequestId(String id, Class<?> entityType) {
        return id;
    }

    @Override
    @SuppressWarnings("unchecked")
    public String toRequestId(Serializable id, Class<?> entityType) {
        checkNotNull(id, "Cannot serialize a null id.");
        checkArgument(supports(entityType), "Cannot convert non-CompositeId-implementing id '%s'.", entityType.getName());

        FooId fooId = (FooId) id;

        Object hashKey = fooId.getHash();
        Object rangeKey = fooId.getRange();

        return j.join(hashKey, rangeKey);
    }

    @Override
    public boolean supports(Class<?> delimiter) {
        logger.info("Checking for support for {}", delimiter);
        return Foo.class
            .isAssignableFrom(checkNotNull(delimiter, "Cannot check whether null class type is supported."));
    }

}
