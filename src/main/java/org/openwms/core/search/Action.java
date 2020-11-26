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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * An Action represents a possible UI action an User can take. Each Action has a resulting URL to a webpage and a descriptive text that is
 * displayed in the UI. Additionally a field {@code weight} is used to count how many times the User has chosen this Action.
 *
 * @author Heiko Scherrer
 * @GlossaryTerm
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "action", propOrder = {"tags"})
public class Action implements Serializable {

    /**
     * The unique name of this Action.
     */
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    private String id;

    @XmlAttribute(required = true)
    private String url;

    /**
     * Text full text of the Action that is displayed in the UI.
     */
    @XmlAttribute(required = true)
    private String text;

    /**
     *
     */
    @XmlTransient
    private int weight = 0;

    /**
     * An Action has one ore more Tags. With Tags an Action can be found.
     */
    @XmlElementWrapper(name = "tags")
    @XmlElement(name = "tag")
    // FIXME [scherrer] : This list is not resolved by JAXB
    private List<Tag> tags = new ArrayList<>();

    /* ----------------------------- constructors ------------------- */

    /**
     * Create a new Action.
     */
    public Action() {
        super();
    }

    /* ----------------------------- methods ------------------- */

    /**
     * Calculate and return a rating for a list of words. Every match between a word and one of the tags, increases the rating. In case none
     * of the tags matches, 0 is returned.
     *
     * @param words The Array of words to calculate the rating
     * @return 0 when none of the words matches, or any other positive value in case of matches
     */
    public int rate(String... words) {
        int result = 0;
        for (String word : words) {
            for (Tag tag : tags) {
                if (tag.matches(word) > 0) {
                    result++;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Get the name.
     *
     * @return the name.
     */
    public String getName() {
        return getId();
    }

    /**
     * Set the name.
     *
     * @param name The name to set.
     */
    public void setName(String name) {
        this.id = name;
    }

    /**
     * Get the name.
     *
     * @return the name.
     */
    public String getId() {
        return id;
    }

    /**
     * Set the name.
     *
     * @param id The name to set.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get the url.
     *
     * @return the url.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Set the url.
     *
     * @param url The url to set.
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Get the text.
     *
     * @return the text.
     */
    public String getText() {
        return text;
    }

    /**
     * Set the text.
     *
     * @param text The text to set.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Get the weight.
     *
     * @return the weight.
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Set the weight.
     *
     * @param weight The weight to set.
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }

    /**
     * Increase weight by 1 and return the new weight.
     *
     * @return The weight increased by 1
     */
    public int increaseWeight() {
        return weight++;
    }

    /**
     * Decrease weight by 1 and return the new weight.
     *
     * @return The weight decreased by 1
     */
    public int decreaseWeight() {
        return --weight;
    }

    /**
     * Get the tags.
     *
     * @return the tags.
     */
    public List<Tag> getTags() {
        return tags;
    }

    /**
     * Set the tags.
     *
     * @param tags The tags to set.
     */
    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getName() + getText() + getUrl();
    }

    /**
     * A Builder class for <code>Action</code>s.
     *
     * @author Heiko Scherrer
     * @version $Revision$
     * @since 0.2
     */
    public static class Builder {

        private Action action;

        /**
         * Create a new Action.Builder.
         *
         * @param name An initial name
         */
        public Builder(String name) {
            action = new Action();
            action.setName(name);
        }

        /**
         * Add a text to the Action.
         *
         * @param text The text
         * @return The Builder
         */
        public Builder withText(String text) {
            action.setText(text);
            return this;
        }

        /**
         * Add a Tag to the Action.
         *
         * @param tag The Tag
         * @return The Builder
         */
        public Builder withTag(Tag tag) {
            action.getTags().add(tag);
            return this;
        }

        /**
         * Build and return the Action.
         *
         * @return The Action
         */
        public Action build() {
            return action;
        }
    }
}