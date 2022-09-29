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
import org.openwms.core.annotation.FireAfterTransaction;
import org.openwms.core.event.UserChangedEvent;
import org.openwms.core.uaa.admin.InvalidPasswordException;
import org.openwms.core.uaa.admin.RoleService;
import org.openwms.core.uaa.admin.UserMapper;
import org.openwms.core.uaa.admin.UserService;
import org.openwms.core.uaa.api.UserVO;
import org.openwms.core.uaa.api.ValidationGroups;
import org.openwms.core.uaa.configuration.ConfigurationService;
import org.openwms.core.uaa.configuration.UserPreference;
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
import javax.validation.constraints.NotBlank;
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
    private final UserMapper userMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final String systemUsername;
    private final String systemPassword;

    UserServiceImpl(UserRepository repository, SecurityObjectRepository securityObjectDao, ConfigurationService confSrv,
            @Lazy RoleService roleService, PasswordEncoder enc, Translator translator, Validator validator, UserMapper userMapper,
            ApplicationEventPublisher eventPublisher, @Value("${owms.security.system.username}") String systemUsername,
            @Value("${owms.security.system.password}") String systemPassword) {
        this.repository = repository;
        this.securityObjectDao = securityObjectDao;
        this.confSrv = confSrv;
        this.roleService = roleService;
        this.enc = enc;
        this.translator = translator;
        this.validator = validator;
        this.userMapper = userMapper;
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
    public @NotNull User save(@NotNull(groups = ValidationGroups.Modify.class) @Valid User user, List<String> roleNames) {
        validate(validator, user, ValidationGroups.Modify.class);
        if (roleNames != null && !roleNames.isEmpty()) {
            user.setRoles(roleService.findByNames(roleNames));
        }
        var saved = repository.save(user);
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
    public void uploadImageFile(@NotBlank String pKey, @NotNull byte[] image) {
        var user = findByPKeyInternal(pKey);
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
        var saved = repository.save(entity);
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
    public @NotNull User getTemplate(@NotBlank String username) {
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
    public @NotNull SystemUser createSystemUser() {
        var sys = new SystemUser(systemUsername, systemPassword);
        var role = new Role.Builder(SystemUser.SYSTEM_ROLE_NAME).withDescription("SuperUsers Role").asImmutable().build();
        role.setGrants(new HashSet<>(securityObjectDao.findAll()));
        sys.addRole(role);
        return sys;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    @Validated(ValidationGroups.Create.class)
    public @NotNull User create(@NotNull(groups = ValidationGroups.Create.class) @Valid User user, List<String> roleNames) {
        var optUser = repository.findByUsername(user.getUsername());
        if (optUser.isPresent()) {
            throw new ResourceExistsException(translator.translate(USER_ALREADY_EXISTS, user.getUsername()),
                    USER_ALREADY_EXISTS,
                    user.getUsername());
        }
        user.getEmailAddresses().forEach(e -> e.setUser(user));
        if (roleNames != null) {
            var byNames = roleService.findByNames(roleNames);
            byNames.forEach(role -> role.addUser(user));
            user.setRoles(byNames);
            LOGGER.debug("Assigned roles [{}] to User [{}]", byNames, user.getUsername());
        }
        var created = repository.save(user);
        eventPublisher.publishEvent(new UserEvent(created, UserEvent.EventType.CREATED));
        return created;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public @NotNull Optional<User> findByUsername(@NotBlank String username) {
        return repository.findByUsername(username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public @NotNull User findByPKey(@NotBlank String pKey) {
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
    public void remove(@NotBlank String username) {
        var user = findInternal(username);
        repository.delete(user);
        eventPublisher.publishEvent(new UserEvent(user, UserEvent.EventType.DELETED));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public void delete(@NotBlank String pKey) {
        var user = findByPKeyInternal(pKey);
        repository.delete(user);
        eventPublisher.publishEvent(new UserEvent(user, UserEvent.EventType.DELETED));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public @NotNull UserVO updatePassword(@NotBlank String pKey, @NotNull CharSequence newPassword) throws InvalidPasswordException {
        var saved = findByPKey(pKey);
        saved.changePassword(enc.encode(newPassword), newPassword.toString(), enc);
        eventPublisher.publishEvent(new UserEvent(saved, UserEvent.EventType.MODIFIED));
        return userMapper.convertToVO(saved);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FireAfterTransaction(events = {UserChangedEvent.class})
    @Measured
    public void changeUserPassword(@NotNull UserPassword userPassword) {
        var entity = findInternal(userPassword.getUser().getUsername());
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
    public @NotNull User saveUserProfile(
            @NotNull(groups = ValidationGroups.Modify.class) @Valid User user,
            @NotNull UserPassword userPassword,
            UserPreference... prefs) {
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