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
package org.openwms.core.uaa.auth;

import org.ameba.integration.jpa.ApplicationEntity;
import org.ameba.integration.jpa.StringListConverter;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static javax.persistence.FetchType.EAGER;

/**
 * A Client is the representation of an OAuth2 Client.
 *
 * @author Heiko Scherrer
 */
@Entity
@Table(name = "COR_UAA_CLIENT")
public class Client extends ApplicationEntity implements Serializable {

    /** Client Id, <a target="_blank" href="https://tools.ietf.org/html/rfc6749">RFC 6749</a>. */
    @Column(name = "C_CLIENT_ID", nullable = false)
    @NotBlank
    private String clientId;

    /** Client Id, <a target="_blank" href="https://tools.ietf.org/html/rfc6749">RFC 6749</a>. */
    @Column(name = "C_CLIENT_ID_ISSUED_AT")
    private Instant clientIdIssuedAt;

    /** Client Secret, <a target="_blank" href="https://tools.ietf.org/html/rfc6749">RFC 6749</a>. */
    @Column(name = "C_CLIENT_SECRET")
    private String clientSecret;

    /** Client Id, <a target="_blank" href="https://tools.ietf.org/html/rfc6749">RFC 6749</a>. */
    @Column(name = "C_CLIENT_SECRET_EXPIRES_AT")
    private Instant clientSecretExpiresAt;

    /** Client Id, <a target="_blank" href="https://tools.ietf.org/html/rfc6749">RFC 6749</a>. */
    @Column(name = "C_CLIENT_NAME")
    private String clientName;

    /** Scopes, <a target="_blank" href="https://tools.ietf.org/html/rfc6749">RFC 6749</a>. */
    @Convert(converter = StringListConverter.class)
    @Column(name = "C_SCOPES", length = 1024)
    @Size(max = 1024)
    private List<String> scopes;

    /** Client Id, <a target="_blank" href="https://tools.ietf.org/html/rfc6749">RFC 6749</a>. */
    @Convert(converter = StringListConverter.class)
    @Column(name = "C_AUTHENTICATION_METHODS", length = 1024)
    @Size(max = 1024)
    private List<String> clientAuthenticationMethods;

    /** Client Id, <a target="_blank" href="https://tools.ietf.org/html/rfc6749">RFC 6749</a>. */
    @Convert(converter = StringListConverter.class)
    @Column(name = "C_AUTHORIZED_GRANT_TYPES", length = 1024)
    @Size(max = 1024)
    private List<String> authorizedGrantTypes;

    /** Redirect URIs, <a target="_blank" href="https://tools.ietf.org/html/rfc6749">RFC 6749</a>. */
    @Convert(converter = StringListConverter.class)
    @Column(name = "C_WEB_SERVER_REDIRECT_URIS", length = StringListConverter.STRING_LIST_LENGTH)
    @Size(max = StringListConverter.STRING_LIST_LENGTH)
    private List<String> webServerRedirectUris;

    /** All other client settings stored as a map. */
    @ElementCollection(fetch = EAGER)
    @CollectionTable(name = "CORE_UAA_CLIENT_SETTING",
            joinColumns = {
                    @JoinColumn(name = "C_CLIENT_PK", referencedColumnName = "C_PK")
            },
            foreignKey = @ForeignKey(name = "FK_CLIENT_SETTING_CLIENT")
    )
    @MapKeyColumn(name = "C_KEY")
    @Column(name = "C_VALUE")
    private Map<String, String> clientSettings = new HashMap<>();

    /** All other token settings stored as a map. */
    @ElementCollection(fetch = EAGER)
    @CollectionTable(name = "CORE_UAA_TOKEN_SETTING",
            joinColumns = {
                    @JoinColumn(name = "C_CLIENT_PK", referencedColumnName = "C_PK")
            },
            foreignKey = @ForeignKey(name = "FK_CLIENT_TOKEN_CLIENT")
    )
    @MapKeyColumn(name = "C_KEY")
    @Column(name = "C_VALUE")
    private Map<String, String> tokenSettings = new HashMap<>();

    @Override
    public void setPersistentKey(String pKey) {
        super.setPersistentKey(pKey);
    }

    @Override
    public long getOl() {
        return super.getOl();
    }

    @Override
    public void setOl(long ol) {
        super.setOl(ol);
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Instant getClientIdIssuedAt() {
        return clientIdIssuedAt;
    }

    public void setClientIdIssuedAt(Instant clientIdIssuedAt) {
        this.clientIdIssuedAt = clientIdIssuedAt;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public Instant getClientSecretExpiresAt() {
        return clientSecretExpiresAt;
    }

    public void setClientSecretExpiresAt(Instant clientSecretExpiresAt) {
        this.clientSecretExpiresAt = clientSecretExpiresAt;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }

    public List<String> getClientAuthenticationMethods() {
        return clientAuthenticationMethods;
    }

    public void setClientAuthenticationMethods(List<String> clientAuthenticationMethods) {
        this.clientAuthenticationMethods = clientAuthenticationMethods;
    }

    public List<String> getAuthorizedGrantTypes() {
        return authorizedGrantTypes;
    }

    public void setAuthorizedGrantTypes(List<String> authorizedGrantTypes) {
        this.authorizedGrantTypes = authorizedGrantTypes;
    }

    public List<String> getWebServerRedirectUris() {
        return webServerRedirectUris;
    }

    public void setWebServerRedirectUris(List<String> webServerRedirectUris) {
        this.webServerRedirectUris = webServerRedirectUris;
    }

    public Map<String, String> getClientSettings() {
        return clientSettings;
    }

    public void setClientSettings(Map<String, String> clientSettings) {
        this.clientSettings = clientSettings;
    }

    public Map<String, String> getTokenSettings() {
        return tokenSettings;
    }

    public void setTokenSettings(Map<String, String> tokenSettings) {
        this.tokenSettings = tokenSettings;
    }

    /**
     * {@inheritDoc}
     *
     * All fields.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;
        if (!super.equals(o)) return false;
        Client client = (Client) o;
        return Objects.equals(clientId, client.clientId) && Objects.equals(clientIdIssuedAt, client.clientIdIssuedAt) && Objects.equals(clientSecret, client.clientSecret) && Objects.equals(clientSecretExpiresAt, client.clientSecretExpiresAt) && Objects.equals(clientName, client.clientName) && Objects.equals(scopes, client.scopes) && Objects.equals(clientAuthenticationMethods, client.clientAuthenticationMethods) && Objects.equals(authorizedGrantTypes, client.authorizedGrantTypes) && Objects.equals(webServerRedirectUris, client.webServerRedirectUris) && Objects.equals(clientSettings, client.clientSettings) && Objects.equals(tokenSettings, client.tokenSettings);
    }

    /**
     * {@inheritDoc}
     *
     * All fields.
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), clientId, clientIdIssuedAt, clientSecret, clientSecretExpiresAt, clientName, scopes, clientAuthenticationMethods, authorizedGrantTypes, webServerRedirectUris, clientSettings, tokenSettings);
    }

    /**
     * {@inheritDoc}
     *
     * Just return the {@code clientId}.
     */
    @Override
    public String toString() {
        return clientId;
    }
}
