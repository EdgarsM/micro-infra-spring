package com.ofg.infrastructure.web.correlationid;

import com.ofg.infrastructure.correlationid.UuidGenerator;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.regex.Pattern;

/**
 * Registers beans that add correlation id to requests
 *
 * @see CorrelationIdAspect
 * @see CorrelationIdFilter
 */
@Configuration
public class CorrelationIdConfiguration {

    @Value("${rest.correlationId.skipPattern:}")
    private String skipPattern;

    @Bean
    public CorrelationIdAspect correlationIdAspect() {
        return new CorrelationIdAspect();
    }

    @Bean
    public FilterRegistrationBean correlationHeaderFilter(UuidGenerator uuidGenerator) {
        Pattern pattern = StringUtils.isNotBlank(skipPattern) ? Pattern.compile(skipPattern) : CorrelationIdFilter.DEFAULT_SKIP_PATTERN;
        return new FilterRegistrationBean(new CorrelationIdFilter(uuidGenerator, pattern));
    }

    @Bean
    public UuidGenerator uuidGenerator() {
        return new UuidGenerator();
    }
}
