package com.ryonday.datarest1025.converter;

import com.ryonday.datarest1025.domain.FooId;
import org.slf4j.Logger;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import static java.util.Objects.requireNonNull;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Spring Boot Gotcha:
 * BackEndIdConverter will be auto-registered (I suppose it's an SDR thing), but regular Converters will not be, unless you enable
 * WebMVCAutoConfiguration.
 *
 * For SDR you have to extend RepositoryRestConfigurerAdapter (as I did)
 */
@Component
@CustomConverter
public class StringToFooIdConverter implements Converter<String, FooId> {

    private static final Logger logger = getLogger(StringToFooIdConverter.class);

    @Override
    public FooId convert(String source) {
        int splitIdx = requireNonNull(source, "Cannot convert null String to SocialConnectionId.").indexOf('-');

        FooId id = new FooId();

        if (splitIdx == -1) {
            logger.warn("Invalid FooId format received: {}", source);
        } else if (splitIdx == 0) {
            logger.warn("FooId requires a hash key: {}", source);
        } else if (splitIdx >= source.length() - 1) {
            logger.warn("FooId requires a range key: {}", source);
        } else {
            String hash = source.substring(0, splitIdx);
            String range = source.substring(splitIdx + 1);
            id.setHash(hash);
            id.setRange(range);
        }

        return id;
    }
}
