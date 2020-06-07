/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openwms.core;

import org.ameba.app.SolutionApp;
import org.openwms.core.uaa.admin.UserService;
import org.openwms.core.uaa.admin.impl.Role;
import org.openwms.core.uaa.admin.impl.User;
import org.openwms.core.uaa.admin.impl.UserDetails;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.validation.Validator;
import java.time.ZonedDateTime;
import java.util.Optional;

import static java.util.Arrays.asList;

/**
 * An UAAStarter.
 *
 * @author Heiko Scherrer
 */
@SpringBootApplication(scanBasePackageClasses = {UAAStarter.class, SolutionApp.class})
public class UAAStarter {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(15);
    }

    @Bean
    MethodValidationPostProcessor methodValidationPostProcessor(Validator validator) {
        MethodValidationPostProcessor mvpp = new MethodValidationPostProcessor();
        mvpp.setValidator(validator);
        return mvpp;
    }

    /**
     * Boot up!
     *
     * @param args Some args
     */
    public static void main(String[] args) {
        SpringApplication.run(UAAStarter.class, args);
    }

    @Profile("DEMO")
    @Bean
    CommandLineRunner dataImporter(UserService service, PasswordEncoder encoder) {
        return args -> {
            Optional<User> tester = service.findByUsername("tester");
            if (tester.isEmpty()) {
                User user = new User("tester");
                user.setEnabled(true);
                user.setExpirationDate(ZonedDateTime.now().plusDays(1));
                user.setExternalUser(false);
                user.setFullname("Mister Jenkins");
                user.setLocked(false);
                user.setRoles(asList(new Role("ROLE_USER")));
                UserDetails ud = new UserDetails();
                ud.setComment("testing only");
                ud.setDepartment("Dep. 1");
                ud.setDescription("Just a test user");
                ud.setGender(UserDetails.Gender.FEMALE);
                ud.setIm("Skype:testee");
                ud.setOffice("Off. 815");
                ud.setPhoneNo("001-1234-56789");
                user.setUserDetails(ud);
                user.changePassword(encoder.encode("tester"), "tester", encoder);
                service.create(user);
            }
        };
    }
}
