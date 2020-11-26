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
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates a collection of {@link Tag}s and is mapped to the XML type "tags" in the ui-actions-schema.xsd. <p> <a
 * href="http://www.openwms.org/schema/ui-actions-schema.xsd">http://www. openwms.org/schema/ui-actions-schema.xsd</a> </p>
 *
 * @author Heiko Scherrer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tags", propOrder = {"tag"})
public class Tags implements Serializable {

    /**
     * A List of {@code Tag}s.
     */
    private List<Tag> tag;

    /**
     * Gets the value of the tag property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list
     * will be present inside the JAXB object. This is why there is not a {@code set} method for the tag property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <p>
     * <pre>
     * getTag().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list {@link Tag }
     *
     * @return The List of Tags wrapped by this instance
     */
    public List<Tag> getTag() {
        if (tag == null) {
            tag = new ArrayList<Tag>();
        }
        return tag;
    }
}