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

import java.io.Serializable;

/**
 * A VerbalTag represents a verb.
 *
 * @author Heiko Scherrer
 * @GlossaryTerm
 */
public class VerbTag extends Tag implements Serializable {

    /* ----------------------------- constructors ------------------- */

    /**
     * Create a new VerbTag.
     */
    public VerbTag() {
        super();
    }

    /**
     * Create a new VerbTag.
     *
     * @param name The initial name
     */
    public VerbTag(String name) {
        super(name);
    }

    /* ----------------------------- methods ------------------- */

    /**
     * A Builder class for {@code VerbTag}s.
     *
     * @author Heiko Scherrer
     * @version $Revision$
     * @since 0.2
     */
    public static class Builder {

        private VerbTag tag;

        /**
         * Constructor.
         *
         * @param name Tags name
         */
        public Builder(String name) {
            tag = new VerbTag(name);
        }

        /**
         * Add an alias to the {@code VerbTag}.
         *
         * @param alias The alias to add
         * @return The builder
         */
        public Builder withAlias(String alias) {
            tag.getAliases().add(alias);
            return this;
        }

        /**
         * Build and return the {@code VerbTag}.
         *
         * @return The {@code VerbTag}
         */
        public VerbTag build() {
            return tag;
        }
    }
}