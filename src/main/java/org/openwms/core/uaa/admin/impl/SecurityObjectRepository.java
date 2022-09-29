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

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * A SecurityObjectRepository is used to find, modify and delete {@link SecurityObject}s.
 *
 * @author Heiko Scherrer
 */
interface SecurityObjectRepository extends JpaRepository<SecurityObject, Long> {

    Optional<Grant> findBypKey(String pKey);

    Optional<Grant> findByName(String name);

    @Query("select g from Grant g where g.name like :moduleName")
    List<Grant> findAllOfModule(String moduleName);

    @Query("select g from Grant g where g.name like :moduleName")
    List<Grant> findAllForUser(String moduleName);

    @Query("select g from Grant g")
    List<Grant> findAllGrants();
}