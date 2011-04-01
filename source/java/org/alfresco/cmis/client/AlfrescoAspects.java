/*
 * Copyright 2005-2011 Alfresco Software Limited.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.alfresco.cmis.client;

import java.util.Collection;

import org.apache.chemistry.opencmis.client.api.ObjectType;

/**
 * Alfresco aspects interface.
 */
public interface AlfrescoAspects
{
    /**
     * Returns if the given aspect is applied to this object.
     * 
     * @param id
     *            the aspect id
     * 
     * @return <code>true</code> if the aspect is applied, <code>false</code>
     *         otherwise
     */
    boolean hasAspect(String id);

    /**
     * Returns if the given aspect is applied to this object.
     * 
     * @param type
     *            the aspect object type
     * 
     * @return <code>true</code> if the aspect is applied, <code>false</code>
     *         otherwise
     */
    boolean hasAspect(ObjectType type);

    /**
     * Returns all applied aspects. If no aspect is applied, an empty collection
     * is returned.
     * 
     * @return collection of the applied aspects
     */
    Collection<ObjectType> getAspects();

    /**
     * Returns the aspect type that defines the given property.
     * 
     * @param propertyId
     *            the property id
     * 
     * @return the aspect type if the property id is defined in an applied
     *         aspect, <code>null</code> otherwise
     */
    ObjectType findAspect(String propertyId);

    /**
     * Adds one or more aspects to the object.
     * 
     * @param id
     *            the aspect id or ids
     */
    void addAspect(String... id);

    /**
     * Adds one or more aspects to the object.
     * 
     * @param id
     *            the aspect type or types
     */
    void addAspect(ObjectType... type);

    /**
     * Removes one or more aspects from the object.
     * 
     * @param id
     *            the aspect id or ids
     */
    void removeAspect(String... id);

    /**
     * Removes one or more aspects from the object.
     * 
     * @param id
     *            the aspect type or types
     */
    void removeAspect(ObjectType... type);
}
