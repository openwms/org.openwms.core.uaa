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
package org.openwms.core.uaa.admin.impl;

import org.ameba.annotation.Measured;
import org.ameba.annotation.TxService;
import org.ameba.exception.NotFoundException;
import org.ameba.exception.ResourceExistsException;
import org.ameba.exception.ServiceLayerException;
import org.ameba.i18n.Translator;
import org.ameba.mapping.BeanMapper;
import org.openwms.core.annotation.FireAfterTransaction;
import org.openwms.core.event.UserChangedEvent;
import org.openwms.core.exception.InvalidPasswordException;
import org.openwms.core.uaa.admin.RoleService;
import org.openwms.core.uaa.admin.UserService;
import org.openwms.core.uaa.api.UserVO;
import org.openwms.core.uaa.api.ValidationGroups;
import org.openwms.core.uaa.configuration.ConfigurationService;
import org.openwms.core.uaa.configuration.UserPreference;
import org.openwms.core.uaa.events.UserEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.ameba.system.ValidationUtil.validate;
import static org.openwms.core.uaa.MessageCodes.USER_ALREADY_EXISTS;
import static org.openwms.core.uaa.MessageCodes.USER_PW_INVALID;
import static org.openwms.core.uaa.MessageCodes.USER_SAVE_NOT_BE_NULL;
import static org.openwms.core.uaa.MessageCodes.USER_WITH_NAME_NOT_EXIST;
import static org.openwms.core.uaa.MessageCodes.USER_WITH_PKEY_NOT_EXIST;
import static org.openwms.core.uaa.MessageCodes.USER_WITH_PK_NOT_EXIST;

/**
 * An UserServiceImpl is a Spring managed transactional implementation of the {@link UserService}. Using Spring 2 annotation support
 * autowires collaborators, therefore XML configuration becomes obsolete. This class is marked with Amebas {@link TxService} annotation to
 * benefit from Springs exception translation interceptor. Traditional CRUD operations are delegated to an {@link UserRepository}.
 * <p>
 * This implementation exists since Spring 2.0.
 *
 * @author Heiko Scherrer
 */
@Validated
@TxService
class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository repository;
    private final SecurityObjectRepository securityObjectDao;
    private final ConfigurationService confSrv;
    private final RoleService roleService;
    private final PasswordEncoder enc;
    private final Translator translator;
    private final Validator validator;
    private final BeanMapper mapper;
    private final ApplicationEventPublisher eventPublisher;
    private final String systemUsername;
    private final String systemPassword;

    UserServiceImpl(UserRepository repository, SecurityObjectRepository securityObjectDao, ConfigurationService confSrv,
            @Lazy RoleService roleService, PasswordEncoder enc, Translator translator, Validator validator, BeanMapper mapper, ApplicationEventPublisher eventPublisher, @Value("${owms.security.system.username}") String systemUsername,
            @Value("${owms.security.system.password}") String systemPassword) {
        this.repository = repository;
        this.securityObjectDao = securityObjectDao;
        this.confSrv = confSrv;
        this.roleService = roleService;
        this.enc = enc;
        this.translator = translator;
        this.validator = validator;
        this.mapper = mapper;
        this.eventPublisher = eventPublisher;
        this.systemUsername = systemUsername;
        this.systemPassword = systemPassword;
    }

    /**
     * {@inheritDoc}
     *
     * @throws EntityNotFoundException when no User with {@code id} found
     */
    @Override
    @FireAfterTransaction(events = {UserChangedEvent.class})
    @Measured
    public User save(@NotNull User user, List<String> roleNames) {
        validate(validator, user, ValidationGroups.Modify.class);
        if (roleNames != null && !roleNames.isEmpty()) {
            user.setRoles(roleService.findByNames(roleNames));
        }
        User saved = repository.save(user);
        eventPublisher.publishEvent(new UserEvent(saved, UserEvent.EventType.MODIFIED));
        return saved;
    }

    /**
     * {@inheritDoc}
     *
     * @throws EntityNotFoundException when no User with {@code id} found
     */
    @Override
    @FireAfterTransaction(events = {UserChangedEvent.class})
    @Measured
    public void uploadImageFile(String pKey, byte[] image) {
        User user = findByPKeyInternal(pKey);
        user.getUserDetails().setImage(image);
        repository.save(user);
        eventPublisher.publishEvent(new UserEvent(user, UserEvent.EventType.MODIFIED));
    }

    /**
     * {@inheritDoc}
     * <p>
     * Triggers {@code UserChangedEvent} after completion.
     *
     * @throws ServiceLayerException if the {@code entity} argument is {@literal null}
     */
    @Override
    @Measured
    public User save(User entity) {
        Assert.notNull(entity, translator.translate(USER_SAVE_NOT_BE_NULL));
        validate(validator, entity, ValidationGroups.Modify.class);
        User saved = repository.save(entity);
        eventPublisher.publishEvent(new UserEvent(saved, UserEvent.EventType.MODIFIED));
        return saved;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Marked as read-only transactional method.
     */
    @Override
    @Transactional(readOnly = true)
    @Measured
    public User getTemplate(String username) {
        return new User(username);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Marked as read-only transactional method.
     */
    @Override
    @Transactional(readOnly = true)
    @Measured
    public SystemUser createSystemUser() {
        SystemUser sys = new SystemUser(systemUsername, systemPassword);
        Role role = new Role.Builder(SystemUser.SYSTEM_ROLE_NAME).withDescription("SuperUsers Role").asImmutable().build();
        role.setGrants(new HashSet<>(securityObjectDao.findAll()));
        sys.addRole(role);
        return sys;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public User create(@NotNull @Valid User user, List<String> roleNames) {
        Optional<User> optUser = repository.findByUsername(user.getUsername());
        if (optUser.isPresent()) {
            throw new ResourceExistsException(translator.translate(USER_ALREADY_EXISTS, user.getUsername()),
                    USER_ALREADY_EXISTS,
                    user.getUsername());
        }
        user.getEmailAddresses().forEach(e -> e.setUser(user));
        if (roleNames != null) {
            List<Role> byNames = roleService.findByNames(roleNames);
            byNames.forEach(role -> role.addUser(user));
            user.setRoles(byNames);
            LOGGER.debug("Assigned roles [{}] to User [{}]", byNames, user.getUsername());
        }
        User created = repository.save(user);
        eventPublisher.publishEvent(new UserEvent(created, UserEvent.EventType.CREATED));
        return created;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public Optional<User> findByUsername(@NotEmpty String username) {
        return repository.findByUsername(username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public @NotNull User findByPKey(@NotEmpty String pKey) {
        return findByPKeyInternal(pKey);
    }

    private User findByPKeyInternal(String pKey) {
        return repository.findBypKey(pKey).orElseThrow(() -> new NotFoundException(
                translator.translate(USER_WITH_PKEY_NOT_EXIST, pKey),
                USER_WITH_PKEY_NOT_EXIST,
                pKey
        ));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public void remove(String username) {
        User user = findInternal(username);
        repository.delete(user);
        eventPublisher.publishEvent(new UserEvent(user, UserEvent.EventType.DELETED));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public void delete(String pKey) {
        User user = findByPKeyInternal(pKey);
        repository.delete(user);
        eventPublisher.publishEvent(new UserEvent(user, UserEvent.EventType.DELETED));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public UserVO updatePassword(String pKey, CharSequence newPassword) throws InvalidPasswordException {
        User saved = findByPKey(pKey);
        saved.changePassword(enc.encode(newPassword), newPassword.toString(), enc);
        eventPublisher.publishEvent(new UserEvent(saved, UserEvent.EventType.MODIFIED));
        return mapper.map(saved, UserVO.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FireAfterTransaction(events = {UserChangedEvent.class})
    @Measured
    public void changeUserPassword(@NotNull UserPassword userPassword) {
        User entity = findInternal(userPassword.getUser().getUsername());
        try {
            entity.changePassword(enc.encode(userPassword.getPassword()), userPassword.getPassword(), enc);
            repository.save(entity);
            eventPublisher.publishEvent(new UserEvent(entity, UserEvent.EventType.MODIFIED));
        } catch (InvalidPasswordException ipe) {
            LOGGER.error(ipe.getMessage());
            throw new ServiceLayerException(translator.translate(USER_PW_INVALID, userPassword.getUser().getUsername()),
                    USER_PW_INVALID);
        }
    }

    private User findInternal(String username) {
        return repository.findByUsername(username).orElseThrow(
                () -> new NotFoundException(
                        translator.translate(USER_WITH_NAME_NOT_EXIST, username),
                        USER_WITH_NAME_NOT_EXIST,
                        username
                )
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FireAfterTransaction(events = {UserChangedEvent.class})
    @Measured
    public User saveUserProfile(@NotNull User user, @NotNull UserPassword userPassword, UserPreference... prefs) {
        try {
            user.changePassword(enc.encode(userPassword.getPassword()), userPassword.getPassword(), enc);
        } catch (InvalidPasswordException ipe) {
            LOGGER.error(ipe.getMessage());
            throw new ServiceLayerException(translator.translate(USER_PW_INVALID, userPassword.getPassword()),
                    USER_PW_INVALID);
        }
        Arrays.stream(prefs).forEach(confSrv::save);
        return save(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public Collection<User> findAll() {
        return repository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public User findById(Long pk) {
        return repository.findById(pk).orElseThrow(() -> new NotFoundException(
                translator, USER_WITH_PK_NOT_EXIST, new Long[]{pk}, pk
        ));
    }
}