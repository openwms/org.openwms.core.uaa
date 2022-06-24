/*
 * Copyright 2005-2022 the original author or authors.
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
package org.openwms.core.uaa.auth.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openwms.core.uaa.auth.Client;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ClientSettings;
import org.springframework.security.oauth2.server.authorization.config.TokenSettings;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A JpaRegisteredClientRepository.
 *
 * @author Heiko Scherrer
 */
@Repository
class JpaRegisteredClientRepository implements RegisteredClientRepository {

    private final ClientRepository clientRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    JpaRegisteredClientRepository(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
        ClassLoader classLoader = JpaRegisteredClientRepository.class.getClassLoader();
        List<Module> securityModules = SecurityJackson2Modules.getModules(classLoader);
        this.objectMapper.registerModules(securityModules);
        this.objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
    }

    @Override
    public void save(RegisteredClient registeredClient) {
        clientRepository.save(to(registeredClient));
    }

    @Override
    public RegisteredClient findById(String id) {
        return clientRepository.findBypKey(id).map(this::to).orElse(null);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        return clientRepository.findByClientId(clientId).map(this::to).orElse(null);
    }

    private static ClientAuthenticationMethod resolveClientAuthenticationMethod(String clientAuthenticationMethod) {
        if (ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
        } else if (ClientAuthenticationMethod.CLIENT_SECRET_POST.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.CLIENT_SECRET_POST;
        } else if (ClientAuthenticationMethod.CLIENT_SECRET_JWT.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.CLIENT_SECRET_JWT;
        } else if (ClientAuthenticationMethod.PRIVATE_KEY_JWT.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.PRIVATE_KEY_JWT;
        } else if (ClientAuthenticationMethod.NONE.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.NONE;
        }
        // Custom client authentication method
        return new ClientAuthenticationMethod(clientAuthenticationMethod);
    }

    private static AuthorizationGrantType resolveAuthorizationGrantType(String authorizationGrantType) {
        if (AuthorizationGrantType.AUTHORIZATION_CODE.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.AUTHORIZATION_CODE;
        } else if (AuthorizationGrantType.REFRESH_TOKEN.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.REFRESH_TOKEN;
        } else if (AuthorizationGrantType.CLIENT_CREDENTIALS.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.CLIENT_CREDENTIALS;
        } else if (AuthorizationGrantType.PASSWORD.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.PASSWORD;
        } else if (AuthorizationGrantType.JWT_BEARER.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.JWT_BEARER;
        }
        // Custom authorization grant type
        return new AuthorizationGrantType(authorizationGrantType);
    }

    private RegisteredClient to(Client client) {
        var clientAuthenticationMethods = new HashSet<>(client.getClientAuthenticationMethods());
        var authorizationGrantTypes = new HashSet<>(client.getAuthorizedGrantTypes());
        var redirectUris = new HashSet<>(client.getWebServerRedirectUris());
        var clientScopes = new HashSet<>(client.getScopes());

        RegisteredClient.Builder builder = RegisteredClient
                .withId(client.getPersistentKey())
                .clientId(client.getClientId())
                .clientIdIssuedAt(client.getClientIdIssuedAt())
                .clientSecret(client.getClientSecret())
                .clientSecretExpiresAt(client.getClientSecretExpiresAt())
                .clientName(client.getClientName())
                .clientAuthenticationMethods(ams ->
                        clientAuthenticationMethods.forEach(am -> ams.add(resolveClientAuthenticationMethod(am))))
                .authorizationGrantTypes((gts) ->
                        authorizationGrantTypes.forEach(grantType -> gts.add(resolveAuthorizationGrantType(grantType))))
                .redirectUris((uris) -> uris.addAll(redirectUris))
                .scopes((scopes) -> scopes.addAll(clientScopes));

        Map<String, String> clientSettingsMap = client.getClientSettings();
        if (clientSettingsMap != null && !clientSettingsMap.isEmpty()) {
            builder.clientSettings(ClientSettings.withSettings(toObjectMap(clientSettingsMap)).build());
        }

        Map<String, String> tokenSettingsMap = client.getTokenSettings();
        if (tokenSettingsMap != null && !tokenSettingsMap.isEmpty()) {
            builder.tokenSettings(TokenSettings.withSettings(toObjectMap(tokenSettingsMap)).build());
        }

        return builder.build();
    }

    private Client to(RegisteredClient registeredClient) {
        var entity = new Client();
        entity.setPersistentKey(registeredClient.getId());
        entity.setClientId(registeredClient.getClientId());
        entity.setClientIdIssuedAt(registeredClient.getClientIdIssuedAt());
        entity.setClientSecret(registeredClient.getClientSecret());
        entity.setClientSecretExpiresAt(registeredClient.getClientSecretExpiresAt());
        entity.setClientName(registeredClient.getClientName());
        entity.setClientAuthenticationMethods(registeredClient.getClientAuthenticationMethods().stream().map(ClientAuthenticationMethod::getValue).collect(Collectors.toList()));
        entity.setAuthorizedGrantTypes(registeredClient.getAuthorizationGrantTypes().stream().map(AuthorizationGrantType::getValue).collect(Collectors.toList()));
        entity.setWebServerRedirectUris(new ArrayList<>(registeredClient.getRedirectUris()));
        entity.setScopes(new ArrayList<>(registeredClient.getScopes()));
        entity.setClientSettings(toStringMap(registeredClient.getClientSettings().getSettings()));
        entity.setTokenSettings(toStringMap(registeredClient.getTokenSettings().getSettings()));
        return entity;
    }

    private Map<String, Object> toObjectMap(Map<String, String> data) {
        if (data == null || data.isEmpty()) {
            return new HashMap<>();
        }
        try {
            Map<String, String> input = new HashMap<>(data);
            String str = this.objectMapper.writeValueAsString(input);
            var result = this.objectMapper.readValue(str, new TypeReference<Map<String, Object>>() {});
            return result;
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }

    private Map<String, String> toStringMap(Map<String, Object> data) {
        try {
            String str = this.objectMapper.writeValueAsString(data);
            return this.objectMapper.readValue(str, new TypeReference<>() {
            });
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }

}
