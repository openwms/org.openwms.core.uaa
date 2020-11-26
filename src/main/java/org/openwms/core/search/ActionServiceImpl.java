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
package org.openwms.core.search;

import org.ameba.annotation.TxService;
import org.openwms.core.uaa.admin.impl.User;

import java.util.Collection;
import java.util.HashSet;

/**
 * An ActionServiceImpl is a Spring managed transaction service used by the UI to work with actions.
 * 
 * @author Heiko Scherrer
 */
@TxService
class ActionServiceImpl implements ActionService {

    private Collection<Action> actions = new HashSet<>();
    private Collection<Tag> tags = new HashSet<>();

    /**
     * {@inheritDoc}
     * 
     * Returns a HashSet with all Actions.
     */
    @Override
    public Collection<Action> findAllActions() {
        return actions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public Collection<Action> findAllActions(User user) {
        /*
        Collection<UserPreference> userPrefs = confSrv.findByType(UserPreference.class, user.getUsername());
        PreferenceKey pKey = new PreferenceKey(PropertyScope.USER, user.getUsername(), "lastSearchActions");
        for (UserPreference pref : userPrefs) {
            if (pref.getPrefKey().equals(pKey)) {
                return (Collection<Action>) pref.getBinValue();
            }
        }
        */
        return new HashSet<>(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Tag> findAllTags(User user) {
        return tags;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public Collection<Action> save(User user, Collection<Action> actions) {
        /*
        Collection<UserPreference> userPrefs = confSrv.findByType(UserPreference.class, user.getUsername());
        PreferenceKey pKey = new PreferenceKey(PropertyScope.USER, user.getUsername(), "lastSearchActions");
        UserPreference result = null;
        for (UserPreference pref : userPrefs) {
            if (pref.getPrefKey().equals(pKey)) {
                result = pref;
                break;
            }
        }
        if (result == null) {
            result = new UserPreference(user.getUsername(), "lastSearchActions");
            result.setDescription("All UI actions the User has previously chosen");
            Set<Action> all = new HashSet<Action>();
            all.addAll(actions);
            result.setBinValue((Serializable) all);
        } else {
            result.setBinValue((Serializable) actions);
        }
        result = confSrv.save(result);
        return (Collection<Action>) result.getBinValue();
        */
        return null;
    }
}