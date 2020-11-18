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
package org.openwms.core;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An AbstractMockitoTests initializes mocks on startup.
 *
 * @author Heiko Scherrer
 */
public abstract class AbstractMockitoTests {

    /**
     * Logger instance can be used by subclasses.
     */
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Setting up some test data.
     */
    @BeforeEach
    public void onSuperBefore() {
        doBefore();
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Clean up, clear lists.
     */
    @AfterEach
    public void onSuperAfter() {
        doAfter();
    }

    /**
     * Do something before the mock objects are initialized.
     */
    protected void doBefore() {
    }

    /**
     * Do something after each test run.
     */
    protected void doAfter() {
    }
}
