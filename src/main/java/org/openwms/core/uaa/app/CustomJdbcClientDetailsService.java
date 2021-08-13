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
package org.openwms.core.uaa.app;

import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

import javax.sql.DataSource;

/**
 * A CustomJdbcClientDetailsService.
 *
 * @author Heiko Scherrer
 */
public class CustomJdbcClientDetailsService extends JdbcClientDetailsService {

    private static final String CLIENT_FIELDS_FOR_UPDATE = "C_RESOURCE_IDS, C_SCOPE, "
            + "C_AUTHORIZED_GRANT_TYPES, C_WEB_SERVER_REDIRECT_URI, C_AUTHORITIES, C_ACCESS_TOKEN_VALIDITY, "
            + "C_REFRESH_TOKEN_VALIDITY, C_ADDITIONAL_INFORMATION, C_AUTOAPPROVE";
    private static final String CLIENT_FIELDS = "C_CLIENT_SECRET, " + CLIENT_FIELDS_FOR_UPDATE;
    private static final String BASE_FIND_STATEMENT = "select C_CLIENT_ID, " + CLIENT_FIELDS + " from COR_UAA_CLIENT";

    public static final String INSERT_SQL = "insert into COR_UAA_CLIENT (" + CLIENT_FIELDS
            + ", C_CLIENT_ID) values (?,?,?,?,?,?,?,?,?,?,?)";
    public static final String DELETE_SQL = "delete from COR_UAA_CLIENT where C_CLIENT_ID = ?";
    private static final String FIND_SQL = BASE_FIND_STATEMENT + " order by C_CLIENT_ID";
    private static final String SELECT_SQL = BASE_FIND_STATEMENT + " where C_CLIENT_ID = ?";
    private static final String UPDATE_SQL = "update COR_UAA_CLIENT " + "set "
            + CLIENT_FIELDS_FOR_UPDATE.replace(", ", "=?, ") + "=? where C_CLIENT_ID = ?";
    private static final String UPDATE_SECRET_SQL = "update COR_UAA_CLIENT set C_CLIENT_SECRET = ? where C_CLIENT_ID = ?";

    public CustomJdbcClientDetailsService(DataSource dataSource) {
        super(dataSource);
        setInsertClientDetailsSql(INSERT_SQL);
        setDeleteClientDetailsSql(DELETE_SQL);
        setFindClientDetailsSql(FIND_SQL);
        setSelectClientDetailsSql(SELECT_SQL);
        setUpdateClientDetailsSql(UPDATE_SQL);
        setUpdateClientSecretSql(UPDATE_SECRET_SQL);
    }
}
