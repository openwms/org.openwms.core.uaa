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
 * A Tag represents a search keyword in the OpenWMS.org ActionSearch concept. Each {@link Action} consists of multiple Tags.
 *
 * @author Heiko Scherrer
 * @GlossaryTerm
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tag")
public class Tag implements Serializable {

    /**
     * The unique name of this Tag.
     */
    @XmlAttribute(required = true)
    private String name;

    /**
     * Other possible names for this tag. Used for internationalization.
     */
    private List<String> aliases = new ArrayList<>();

    /* ----------------------------- constructors ------------------- */

    /**
     * Accessed by JAXB.
     */
    public Tag() {
        super();
    }

    /**
     * Create a new Tag.
     *
     * @param name The initial name
     */
    protected Tag(String name) {
        super();
        this.name = name;
    }

    /* ----------------------------- methods ------------------- */

    /**
     * Get the aliases.
     *
     * @return the aliases.
     */
    public List<String> getAliases() {
        return aliases;
    }

    /**
     * Set the aliases.
     *
     * @param aliases The aliases to set.
     */
    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    /**
     * Get the name.
     *
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name.
     *
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Find the given String in the name of the Tag.
     *
     * @param word The String to search for
     * @return 0 if {@code name} is not part of the tag name, otherwise some positive value
     */
    public int matches(String word) {
        return name.toLowerCase().indexOf(word.toLowerCase()) + 1;
    }
}