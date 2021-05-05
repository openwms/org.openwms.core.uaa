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
package org.openwms.core.uaa;

import org.ameba.aop.ServiceLayerAspect;
import org.ameba.exception.ServiceLayerException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolationException;
import java.util.Optional;

/**
 * A ServiceLayerExceptionTranslator.
 *
 * @author Heiko Scherrer
 */
@Component
@Order(16)
class ServiceLayerExceptionTranslator extends ServiceLayerAspect {

    /**
     * Override method to handle the transaction yourself and skip to the default exception handling .
     *
     * @param ex Exception to handle
     * @return An empty Optional to use the default exception handling or an Exception to skip default handling
     */
    @Override
    protected Optional<Exception> doTranslateException(Exception ex) {
        if (ex instanceof ConstraintViolationException) {
            return Optional.of(ex);
        }
        if (ex instanceof ServiceLayerException) {
            return Optional.of(ex);
        }
        return Optional.of(new ServiceLayerException(ex.getMessage()));
    }
}
