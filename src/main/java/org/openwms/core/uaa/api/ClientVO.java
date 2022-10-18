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
package org.openwms.core.uaa.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.ameba.http.AbstractBase;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * A ClientVO.
 *
 * @author Heiko Scherrer
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientVO extends AbstractBase<ClientVO> implements Serializable {

    /** HTTP media type representation. */
    public static final String MEDIA_TYPE = "application/vnd.openwms.uaa.client-v1+json";

    @JsonProperty("pKey")
    private String pKey;
    @JsonProperty("resourceIds")
    private String resourceIds;
    @NotBlank
    @JsonProperty("clientId")
    private String clientId;
    @JsonProperty("clientIdIssuedAt")
    private Instant clientIdIssuedAt;
    @JsonProperty("clientSecret")
    private String clientSecret;
    @JsonProperty("clientSecretExpiresAt")
    private Instant clientSecretExpiresAt;
    @JsonProperty("clientName")
    private String clientName;
    @JsonProperty("scopes")
    private List<String> scopes;
    @JsonProperty("clientAuthenticationMethods")
    private List<String> clientAuthenticationMethods;
    @JsonProperty("authorizedGrantTypes")
    private List<String> authorizedGrantTypes;
    @JsonProperty("webServerRedirectUris")
    private List<String> webServerRedirectUris;
    @JsonProperty("clientSettings")
    private String clientSettings;
    @JsonProperty("tokenSettings")
    private String tokenSettings;

    /*~-------------------- constructors --------------------*/
    @JsonCreator
    public ClientVO() {
        // For Jackson and MapStruct usage
    }

    /*~-------------------- accessors --------------------*/
    public String getpKey() {
        return pKey;
    }

    public void setpKey(String pKey) {
        this.pKey = pKey;
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

    public String getClientSettings() {
        return clientSettings;
    }

    public void setClientSettings(String clientSettings) {
        this.clientSettings = clientSettings;
    }

    public String getTokenSettings() {
        return tokenSettings;
    }

    public void setTokenSettings(String tokenSettings) {
        this.tokenSettings = tokenSettings;
    }

    /*~-------------------- methods --------------------*/
    /**
     * {@inheritDoc}
     *
     * All fields.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClientVO)) return false;
        if (!super.equals(o)) return false;
        var clientVO = (ClientVO) o;
        return Objects.equals(pKey, clientVO.pKey) && Objects.equals(clientId, clientVO.clientId) && Objects.equals(clientIdIssuedAt, clientVO.clientIdIssuedAt) && Objects.equals(clientSecret, clientVO.clientSecret) && Objects.equals(clientSecretExpiresAt, clientVO.clientSecretExpiresAt) && Objects.equals(clientName, clientVO.clientName) && Objects.equals(scopes, clientVO.scopes) && Objects.equals(clientAuthenticationMethods, clientVO.clientAuthenticationMethods) && Objects.equals(authorizedGrantTypes, clientVO.authorizedGrantTypes) && Objects.equals(webServerRedirectUris, clientVO.webServerRedirectUris) && Objects.equals(clientSettings, clientVO.clientSettings) && Objects.equals(tokenSettings, clientVO.tokenSettings);
    }

    /**
     * {@inheritDoc}
     *
     * All fields.
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), pKey, clientId, clientIdIssuedAt, clientSecret, clientSecretExpiresAt, clientName, scopes, clientAuthenticationMethods, authorizedGrantTypes, webServerRedirectUris, clientSettings, tokenSettings);
    }

    /**
     * {@inheritDoc}
     *
     * All fields.
     */
    @Override
    public String toString() {
        return new StringJoiner(", ", ClientVO.class.getSimpleName() + "[", "]")
                .add("pKey='" + pKey + "'")
                .add("resourceIds='" + resourceIds + "'")
                .add("clientId='" + clientId + "'")
                .add("clientIdIssuedAt=" + clientIdIssuedAt)
                .add("clientSecret='" + clientSecret + "'")
                .add("clientSecretExpiresAt=" + clientSecretExpiresAt)
                .add("clientName='" + clientName + "'")
                .add("scopes=" + scopes)
                .add("clientAuthenticationMethods=" + clientAuthenticationMethods)
                .add("authorizedGrantTypes=" + authorizedGrantTypes)
                .add("webServerRedirectUris=" + webServerRedirectUris)
                .add("clientSettings='" + clientSettings + "'")
                .add("tokenSettings='" + tokenSettings + "'")
                .toString();
    }

    /*~-------------------- builder --------------------*/
    public static final class Builder {
        private String pKey;
        private String clientId;
        private Instant clientIdIssuedAt;
        private String clientSecret;
        private Instant clientSecretExpiresAt;
        private String clientName;
        private List<String> scopes;
        private List<String> clientAuthenticationMethods;
        private List<String> authorizedGrantTypes;
        private List<String> webServerRedirectUris;
        private String clientSettings;
        private String tokenSettings;

        private Builder() {
        }

        public static Builder aClientVO() {
            return new Builder();
        }

        public Builder pKey(String pKey) {
            this.pKey = pKey;
            return this;
        }

        public Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder clientIdIssuedAt(Instant clientIdIssuedAt) {
            this.clientIdIssuedAt = clientIdIssuedAt;
            return this;
        }

        public Builder clientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
        }

        public Builder clientSecretExpiresAt(Instant clientSecretExpiresAt) {
            this.clientSecretExpiresAt = clientSecretExpiresAt;
            return this;
        }

        public Builder clientName(String clientName) {
            this.clientName = clientName;
            return this;
        }

        public Builder scopes(List<String> scopes) {
            this.scopes = scopes;
            return this;
        }

        public Builder clientAuthenticationMethods(List<String> clientAuthenticationMethods) {
            this.clientAuthenticationMethods = clientAuthenticationMethods;
            return this;
        }

        public Builder authorizedGrantTypes(List<String> authorizedGrantTypes) {
            this.authorizedGrantTypes = authorizedGrantTypes;
            return this;
        }

        public Builder webServerRedirectUris(List<String> webServerRedirectUris) {
            this.webServerRedirectUris = webServerRedirectUris;
            return this;
        }

        public Builder clientSettings(String clientSettings) {
            this.clientSettings = clientSettings;
            return this;
        }

        public Builder tokenSettings(String tokenSettings) {
            this.tokenSettings = tokenSettings;
            return this;
        }

        public ClientVO build() {
            var clientVO = new ClientVO();
            clientVO.setClientId(clientId);
            clientVO.setClientIdIssuedAt(clientIdIssuedAt);
            clientVO.setClientSecret(clientSecret);
            clientVO.setClientSecretExpiresAt(clientSecretExpiresAt);
            clientVO.setClientName(clientName);
            clientVO.setScopes(scopes);
            clientVO.setClientAuthenticationMethods(clientAuthenticationMethods);
            clientVO.setAuthorizedGrantTypes(authorizedGrantTypes);
            clientVO.setWebServerRedirectUris(webServerRedirectUris);
            clientVO.setClientSettings(clientSettings);
            clientVO.setTokenSettings(tokenSettings);
            clientVO.pKey = this.pKey;
            return clientVO;
        }
    }
}
