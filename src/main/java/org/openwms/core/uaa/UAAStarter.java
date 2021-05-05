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
package org.openwms.core.uaa;

import org.ameba.app.SolutionApp;
import org.openwms.core.uaa.admin.UserService;
import org.openwms.core.uaa.admin.impl.Email;
import org.openwms.core.uaa.admin.impl.Role;
import org.openwms.core.uaa.admin.impl.User;
import org.openwms.core.uaa.admin.impl.UserDetails;
import org.openwms.core.uaa.auth.Client;
import org.openwms.core.uaa.auth.ClientService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Optional;

import static java.util.Arrays.asList;

/**
 * An UAAStarter.
 *
 * @author Heiko Scherrer
 */
@SpringBootApplication(scanBasePackageClasses = {UAAStarter.class, SolutionApp.class})
public class UAAStarter {

    @ConditionalOnExpression("'${owms.security.useEncoder}'=='true'")
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(15);
    }

    @ConditionalOnExpression("'${owms.security.useEncoder}'=='false'")
    @Bean
    PasswordEncoder nopPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    /**
     * Boot up!
     *
     * @param args Some args
     */
    public static void main(String[] args) {
        SpringApplication.run(UAAStarter.class, args);
    }

    @Profile({"DEMO"})
    @Bean
    CommandLineRunner dataImporter(UserService service, PasswordEncoder encoder, ClientService clientService,
            @Value("${owms.security.useEncoder}") boolean useEncoder) {
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
                user.setEmailAddresses(new HashSet<>(asList(new Email(user, "tester.tester@example.com", true))));
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
            Client client = new Client();
            client.setClientId("gateway");
            client.setScope("gateway");
            client.setAuthorizedGrantTypes("password,authorization_code,refresh_token,implicit");
            client.setWebServerRedirectUri("http://localhost:8086/login/oauth2/code/gateway");
            client.setRefreshTokenValidity(3600);
            client.setAccessTokenValidity(3600);
            client.setAutoapprove("true");
            client.setClientSecret(useEncoder ? encoder.encode("secret") : "secret");
            clientService.create(client);
        };
    }
}
