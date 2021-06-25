/*
 * Copyright 2005-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openwms.core.uaa.app;

import io.micrometer.core.instrument.MeterRegistry;
import org.ameba.annotation.EnableAspects;
import org.ameba.app.BaseConfiguration;
import org.ameba.app.SpringProfiles;
import org.ameba.http.PermitAllCorsConfigurationSource;
import org.ameba.i18n.AbstractTranslator;
import org.ameba.i18n.Translator;
import org.ameba.mapping.BeanMapper;
import org.ameba.mapping.DozerMapperImpl;
import org.ameba.system.NestedReloadableResourceBundleMessageSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.Filter;
import javax.validation.Validator;

/**
 * A UAAModuleConfiguration.
 *
 * @author Heiko Scherrer
 */
@Configuration
@EnableJpaAuditing
@EnableAspects(propagateRootCause = true)
@EnableTransactionManagement
@Import(BaseConfiguration.class)
public class UAAModuleConfiguration {

    @Profile(SpringProfiles.DEVELOPMENT_PROFILE)
    public @Bean
    Filter corsFiler() {
        return new CorsFilter(new PermitAllCorsConfigurationSource());
    }

    @Bean
    MeterRegistryCustomizer<MeterRegistry> metricsCommonTags(@Value("${spring.application.name}") String applicationName) {
        return registry -> registry.config().commonTags("application", applicationName);
    }

    @Bean
    MethodValidationPostProcessor methodValidationPostProcessor(Validator validator) {
        MethodValidationPostProcessor mvpp = new MethodValidationPostProcessor();
        mvpp.setValidator(validator);
        return mvpp;
    }

    public
    @Bean
    Translator translator() {
        return new AbstractTranslator() {
            @Override
            protected MessageSource getMessageSource() {
                return messageSource();
            }
        };
    }

    public
    @Bean LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver slr = new AcceptHeaderLocaleResolver();
        return slr;
    }

    public
    @Bean
    MessageSource messageSource() {
        NestedReloadableResourceBundleMessageSource nrrbm = new NestedReloadableResourceBundleMessageSource();
        nrrbm.setBasename("classpath*:/META-INF/i18n");
        nrrbm.setDefaultEncoding("UTF-8");
        return nrrbm;
    }

    public
    @Bean
    BeanMapper beanMapper() {
        return new DozerMapperImpl("META-INF/dozer/bean-mappings.xml");
    }
}
