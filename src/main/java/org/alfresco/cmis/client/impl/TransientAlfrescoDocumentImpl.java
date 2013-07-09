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
package org.alfresco.cmis.client.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.alfresco.cmis.client.TransientAlfrescoDocument;
import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.Property;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.runtime.TransientDocumentImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.Properties;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;
import org.apache.chemistry.opencmis.commons.enums.Updatability;

public class TransientAlfrescoDocumentImpl extends TransientDocumentImpl
		implements TransientAlfrescoDocument {
	protected TransientAlfrescoAspectsImpl aspects;

	@Override
	protected void initialize(Session session, CmisObject object) {
		super.initialize(session, object);
		aspects = new TransientAlfrescoAspectsImpl(session, object);
	}

    public ObjectType getTypeWithAspects()
    {
        return aspects.getTypeWithAspects();
    }

	@SuppressWarnings("unchecked")
	public <T> void setPropertyValue(String id, Object value) {
		ObjectType aspectType = aspects.findAspect(id);

		if (aspectType == null) {
			super.setPropertyValue(id, value);
			return;
		}

		PropertyDefinition<T> propertyDefinition = (PropertyDefinition<T>) aspectType
				.getPropertyDefinitions().get(id);
		if (propertyDefinition == null) {
			throw new IllegalArgumentException("Unknown property '" + id + "'!");
		}
		// check updatability
		if (propertyDefinition.getUpdatability() == Updatability.READONLY) {
			throw new IllegalArgumentException("Property is read-only!");
		}

		List<T> values = AlfrescoUtils.checkProperty(propertyDefinition,
				value);

		// create and set property
		Property<T> newProperty = getObjectFactory().createProperty(
				propertyDefinition, values);
		properties.put(id, newProperty);

		isPropertyUpdateRequired = true;
		isModified = true;
	}

	public boolean hasAspect(String id) {
		return aspects.hasAspect(id);
	}

	public boolean hasAspect(ObjectType type) {
		return aspects.hasAspect(type);
	}

	public Collection<ObjectType> getAspects() {
		return aspects.getAspects();
	}

	public ObjectType findAspect(String propertyId) {
		return aspects.findAspect(propertyId);
	}

	public CmisObject addAspect(String... id) {
		return aspects.addAspect(id);
	}

	public CmisObject addAspect(ObjectType... type) {
		return aspects.addAspect(type);
	}

	public CmisObject addAspect(ObjectType type, Map<String, ?> properties) {
		CmisObject cmisObject = aspects.addAspect(type);
		aspects.setPropertyValues(this, properties);
		return cmisObject;
	}

	public CmisObject addAspect(ObjectType[] type, Map<String, ?> properties) {
		CmisObject cmisObject = aspects.addAspect(type);
		aspects.setPropertyValues(this, properties);
		return cmisObject;
	}

	public CmisObject addAspect(String id, Map<String, ?> properties) {
		CmisObject cmisObject = aspects.addAspect(id);
		aspects.setPropertyValues(this, properties);
		return cmisObject;
	}

	public CmisObject addAspect(String[] id, Map<String, ?> properties) {
		CmisObject cmisObject = aspects.addAspect(id);
		aspects.setPropertyValues(this, properties);
		return cmisObject;
	}

	public CmisObject removeAspect(String... id) {
		return aspects.removeAspect(id);
	}

	public CmisObject removeAspect(ObjectType... type) {
		return aspects.removeAspect(type);
	}

	@Override
	public ObjectId save() {
		ObjectId id = super.save();
		if (!isMarkedForDelete) {
			aspects.save();
		}

		return id;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Properties prepareProperties() {
		ObjectType type = getType();
		PropertyDefinition<String> propDef = (PropertyDefinition<String>) type
				.getPropertyDefinitions().get(PropertyIds.OBJECT_TYPE_ID);
		String objectTypeIdValue = AlfrescoUtils
				.createObjectTypeIdValue(type, getAspects());
		Property<String> objectTypeIdProperty = getObjectFactory()
				.createProperty(propDef,
						Collections.singletonList(objectTypeIdValue));

		properties.put(PropertyIds.OBJECT_TYPE_ID, objectTypeIdProperty);

		return super.prepareProperties();
	}
}
