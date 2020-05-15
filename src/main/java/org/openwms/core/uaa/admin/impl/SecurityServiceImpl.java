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
package org.openwms.core.uaa.admin.impl;

import org.ameba.annotation.TxService;
import org.ameba.i18n.Translator;
import org.openwms.core.annotation.FireAfterTransaction;
import org.openwms.core.event.UserChangedEvent;
import org.openwms.core.exception.ExceptionCodes;
import org.openwms.core.uaa.admin.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * A SecurityServiceImpl is a transactional Spring Service implementation.
 *
 * @author Heiko Scherrer
 * @version 0.2
 * @since 0.1
 */
@TxService
class SecurityServiceImpl implements SecurityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityServiceImpl.class);
    @Autowired
    private SecurityObjectRepository securityObjectRepository;
    @Autowired
    private Translator translator;

    /**
     * {@inheritDoc}
     * <p>
     * Triggers {@code UserChangedEvent} after completion.
     */
    @Override
    @FireAfterTransaction(events = {UserChangedEvent.class})
    public List<Grant> mergeGrants(String moduleName, List<Grant> grants) {
        Assert.notNull(moduleName, translator.translate(ExceptionCodes.MODULENAME_NOT_NULL));
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Merging grants of module:" + moduleName);
        }
        List<Grant> persisted = securityObjectRepository.findAllOfModule(moduleName + "%");
        List<Grant> result = new ArrayList<>(persisted);
        grants.forEach(g -> {
            if (persisted.contains(g)) persisted.remove(g);
            else result.add(securityObjectRepository.save(g));
        });
        result.removeAll(persisted);
        if (!persisted.isEmpty()) {
            securityObjectRepository.deleteAll(persisted);
        }
        return result;
    }
}