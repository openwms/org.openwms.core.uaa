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
package org.openwms.core.search;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java element interface generated in the
 * org.openwms.core.domain.search package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the Java representation for XML content. The Java
 * representation of XML content can consist of schema derived interfaces and classes representing the binding of schema type definitions,
 * element declarations and model groups. Factory methods for each of these are provided in this class.
 *
 * @author Heiko Scherrer
 */
@XmlRegistry
public class ObjectFactory {

    private static final QName ACTIONS_QNAME = new QName("http://www.openwms.org/schema/ui-actions-schema", "actions");

    /**
     * Create an instance of {@link Tag }.
     *
     * @return A new Tag
     */
    public Tag createTag() {
        return new Tag();
    }

    /**
     * Create an instance of {@link Tags }.
     *
     * @return A new Tags
     */
    public Tags createTags() {
        return new Tags();
    }

    /**
     * Create an instance of {@link Actions }.
     *
     * @return A new Actions
     */
    public Actions createActions() {
        return new Actions();
    }

    /**
     * Create an instance of {@link Action }.
     *
     * @return A new Action
     */
    public Action createAction() {
        return new Action();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Actions }{@code >} .
     *
     * @param value An Actions object
     * @return A wrapped Actions as JAXBElement
     */
    @XmlElementDecl(namespace = "http://www.openwms.org/schema/ui-actions-schema", name = "actions")
    public JAXBElement<Actions> createActions(Actions value) {
        return new JAXBElement<Actions>(ACTIONS_QNAME, Actions.class, null, value);
    }
}