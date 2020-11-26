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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates a collection of {@link Action}s and is mapped to the XML type "actions" in the ui-actions-schema.xsd. <p> <a
 * href="http://www.openwms.org/schema/ui-actions-schema.xsd">http://www. openwms.org/schema/ui-actions-schema.xsd</a> </p>
 *
 * @author Heiko Scherrer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "actions", propOrder = {"action"})
public class Actions implements Serializable {

    private static final long serialVersionUID = -8134374894573948880L;
    /**
     * A List of all {@link Action}s.
     */
    private List<Action> action;
    /**
     * The owning module of the set of {@link Action}s.
     */
    @XmlAttribute
    protected String owner;

    /**
     * Gets the value of the action property.
     * <p>
     * <p>
     * This acessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list
     * will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for the action property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <p>
     * <pre>
     * getAction().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list {@link Action }
     *
     * @return The list of {@link Action}s
     */
    public List<Action> getAction() {
        if (action == null) {
            action = new ArrayList<Action>();
        }
        return action;
    }

    /**
     * Gets the owner of this property.
     *
     * @return possible object is {@link String }
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Sets the owner of this property.
     *
     * @param owner allowed object is {@link String }
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }
}