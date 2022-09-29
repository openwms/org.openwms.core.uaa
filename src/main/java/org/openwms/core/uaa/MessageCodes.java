/*
 * Copyright 2005-2021 the original author or authors.
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

/**
 * A MessageCodes.
 *
 * @author Heiko Scherrer
 */
public final class MessageCodes {

    private MessageCodes() { }

    /** Thrown if an User to persist already exists. */
    public static final String USER_ALREADY_EXISTS = "user.already.exists";
    /** Thrown if an User has been looked up but hasn't been found. */
    public static final String USER_WITH_PKEY_NOT_EXIST = "user.pkey.not.exist";
    /** Thrown if an User has been looked up but hasn't been found. */
    public static final String USER_WITH_PK_NOT_EXIST = "user.pk.not.exist";
    /** Thrown if an User has been looked up but hasn't been found. */
    public static final String USER_WITH_NAME_NOT_EXIST = "user.name.not.exist";
    /** Thrown if saving a User was requested with {@code null} argument. */
    public static final String USER_SAVE_NOT_BE_NULL = "user.save.null.argument";
    /** Thrown if changing a User's password was requested, but the new password does not match the defined password rules. */
    public static final String USER_PW_INVALID = "user.password.invalid";

    /** Thrown if a Role has been looked up by persistent key but hasn't been found. */
    public static final String ROLE_WITH_PKEY_NOT_EXIST = "role.pkey.not.exist";

    /** Thrown if a SecurityObject has been looked up by persistent key but hasn't been found. */
    public static final String SO_WITH_PKEY_NOT_EXIST = "so.pkey.not.exist";

    /** Thrown if a service method was called with expected modulename but that was {@code null}. */
    public static final String MODULENAME_NOT_NULL = "module.modulename.null.argument";
}
