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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.ameba.http.AbstractBase;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Objects;

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
    @NotEmpty
    @JsonProperty("clientId")
    private String clientId;
    @JsonProperty("clientSecret")
    private String clientSecret;
    @JsonProperty("scope")
    private String scope;
    @JsonProperty("authorizedGrantTypes")
    private String authorizedGrantTypes;
    @JsonProperty("webServerRedirectUri")
    private String webServerRedirectUri;
    @JsonProperty("authorities")
    private String authorities;
    @JsonProperty("accessTokenValidity")
    private int accessTokenValidity;
    @JsonProperty("refreshTokenValidity")
    private int refreshTokenValidity;
    @JsonProperty("additionalInformation")
    private String additionalInformation;
    @JsonProperty("autoapprove")
    private String autoapprove;

    public ClientVO() {
    }

    private ClientVO(Builder builder) {
        setpKey(builder.pKey);
        setResourceIds(builder.resourceIds);
        setClientId(builder.clientId);
        setClientSecret(builder.clientSecret);
        setScope(builder.scope);
        setAuthorizedGrantTypes(builder.authorizedGrantTypes);
        setWebServerRedirectUri(builder.webServerRedirectUri);
        setAuthorities(builder.authorities);
        setAccessTokenValidity(builder.accessTokenValidity);
        setRefreshTokenValidity(builder.refreshTokenValidity);
        setAdditionalInformation(builder.additionalInformation);
        setAutoapprove(builder.autoapprove);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getpKey() {
        return pKey;
    }

    public void setpKey(String pKey) {
        this.pKey = pKey;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ClientVO clientVO = (ClientVO) o;
        return accessTokenValidity == clientVO.accessTokenValidity &&
                refreshTokenValidity == clientVO.refreshTokenValidity &&
                Objects.equals(pKey, clientVO.pKey) &&
                Objects.equals(resourceIds, clientVO.resourceIds) &&
                Objects.equals(clientId, clientVO.clientId) &&
                Objects.equals(clientSecret, clientVO.clientSecret) &&
                Objects.equals(scope, clientVO.scope) &&
                Objects.equals(authorizedGrantTypes, clientVO.authorizedGrantTypes) &&
                Objects.equals(webServerRedirectUri, clientVO.webServerRedirectUri) &&
                Objects.equals(authorities, clientVO.authorities) &&
                Objects.equals(additionalInformation, clientVO.additionalInformation) &&
                Objects.equals(autoapprove, clientVO.autoapprove);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), pKey, resourceIds, clientId, clientSecret, scope, authorizedGrantTypes, webServerRedirectUri, authorities, accessTokenValidity, refreshTokenValidity, additionalInformation, autoapprove);
    }

    public static final class Builder {
        private String pKey;
        private String resourceIds;
        private @NotEmpty String clientId;
        private String clientSecret;
        private String scope;
        private String authorizedGrantTypes;
        private String webServerRedirectUri;
        private String authorities;
        private int accessTokenValidity;
        private int refreshTokenValidity;
        private String additionalInformation;
        private String autoapprove;

        private Builder() {
        }

        public Builder pKey(String val) {
            pKey = val;
            return this;
        }

        public Builder resourceIds(String val) {
            resourceIds = val;
            return this;
        }

        public Builder clientId(@NotEmpty String val) {
            clientId = val;
            return this;
        }

        public Builder clientSecret(String val) {
            clientSecret = val;
            return this;
        }

        public Builder scope(String val) {
            scope = val;
            return this;
        }

        public Builder authorizedGrantTypes(String val) {
            authorizedGrantTypes = val;
            return this;
        }

        public Builder webServerRedirectUri(String val) {
            webServerRedirectUri = val;
            return this;
        }

        public Builder authorities(String val) {
            authorities = val;
            return this;
        }

        public Builder accessTokenValidity(int val) {
            accessTokenValidity = val;
            return this;
        }

        public Builder refreshTokenValidity(int val) {
            refreshTokenValidity = val;
            return this;
        }

        public Builder additionalInformation(String val) {
            additionalInformation = val;
            return this;
        }

        public Builder autoapprove(String val) {
            autoapprove = val;
            return this;
        }

        public ClientVO build() {
            return new ClientVO(this);
        }
    }
}
