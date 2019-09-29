/*
 * openwms.org, the Open Warehouse Management System.
 * Copyright (C) 2014 Heiko Scherrer
 *
 * This file is part of openwms.org.
 *
 * openwms.org is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * openwms.org is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this software. If not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.openwms.core;

import org.junit.After;
import org.junit.Before;
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
    @Before
    public void onSuperBefore() {
        doBefore();
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Clean up, clear lists.
     */
    @After
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
