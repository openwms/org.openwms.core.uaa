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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Client is the representation of an OAuth2 Client.
 *
 * @author Heiko Scherrer
 */
@Entity
@Table(name = "COR_UAA_CLIENT")
public class Client extends ApplicationEntity implements Serializable {

    @Column(name = "C_RESOURCE_IDS")
    private String resourceIds;
    @Column(name = "C_CLIENT_ID", nullable = false)
    private String clientId;
    @Column(name = "C_CLIENT_SECRET")
    private String clientSecret;
    @Column(name = "C_SCOPE")
    private String scope;
    @Column(name = "C_AUTHORIZED_GRANT_TYPES")
    private String authorizedGrantTypes;
    @Column(name = "C_WEB_SERVER_REDIRECT_URI")
    private String webServerRedirectUri;
    @Column(name = "C_AUTHORITIES")
    private String authorities;
    @Column(name = "C_ACCESS_TOKEN_VALIDITY")
    private int accessTokenValidity;
    @Column(name = "C_REFRESH_TOKEN_VALIDITY")
    private int refreshTokenValidity;
    @Column(name = "C_ADDITIONAL_INFORMATION")
    private String additionalInformation;
    @Column(name = "C_AUTOAPPROVE")
    private String autoapprove;

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

    public String getResourceIds() {
        return resourceIds;
    }

    public void setResourceIds(String resourceIds) {
        this.resourceIds = resourceIds;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getAuthorizedGrantTypes() {
        return authorizedGrantTypes;
    }

    public void setAuthorizedGrantTypes(String authorizedGrantTypes) {
        this.authorizedGrantTypes = authorizedGrantTypes;
    }

    public String getWebServerRedirectUri() {
        return webServerRedirectUri;
    }

    public void setWebServerRedirectUri(String webServerRedirectUri) {
        this.webServerRedirectUri = webServerRedirectUri;
    }

    public String getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String authorities) {
        this.authorities = authorities;
    }

    public int getAccessTokenValidity() {
        return accessTokenValidity;
    }

    public void setAccessTokenValidity(int accessTokenValidity) {
        this.accessTokenValidity = accessTokenValidity;
    }

    public int getRefreshTokenValidity() {
        return refreshTokenValidity;
    }

    public void setRefreshTokenValidity(int refreshTokenValidity) {
        this.refreshTokenValidity = refreshTokenValidity;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public String getAutoapprove() {
        return autoapprove;
    }

    public void setAutoapprove(String autoapprove) {
        this.autoapprove = autoapprove;
    }

    /**
     * {@inheritDoc}
     *
     * Use all fields.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Client client = (Client) o;
        return accessTokenValidity == client.accessTokenValidity &&
                refreshTokenValidity == client.refreshTokenValidity &&
                Objects.equals(resourceIds, client.resourceIds) &&
                Objects.equals(clientId, client.clientId) &&
                Objects.equals(clientSecret, client.clientSecret) &&
                Objects.equals(scope, client.scope) &&
                Objects.equals(authorizedGrantTypes, client.authorizedGrantTypes) &&
                Objects.equals(webServerRedirectUri, client.webServerRedirectUri) &&
                Objects.equals(authorities, client.authorities) &&
                Objects.equals(additionalInformation, client.additionalInformation) &&
                Objects.equals(autoapprove, client.autoapprove);
    }

    /**
     * {@inheritDoc}
     *
     * Use all fields.
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), resourceIds, clientId, clientSecret, scope, authorizedGrantTypes, webServerRedirectUri, authorities, accessTokenValidity, refreshTokenValidity, additionalInformation, autoapprove);
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
