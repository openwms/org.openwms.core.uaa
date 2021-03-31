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
package org.openwms.core.uaa.impl;

import com.github.dozermapper.core.DozerConverter;

import java.util.Base64;

/**
 * A Base64Converter.
 *
 * @author Heiko Scherrer
 */
public class Base64Converter extends DozerConverter<byte[], String> {

    /**
     * {@inheritDoc}
     */
    public Base64Converter() {
        super(byte[].class, String.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String convertTo(byte[] source, String destination) {
        if (source == null) {
            return null;
        }
        return Base64.getEncoder().encodeToString(source);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] convertFrom(String source, byte[] destination) {
        if (source == null) {
            return null;
        }
        return Base64.getDecoder().decode(source);
    }
}