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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.alfresco.cmis.client.AlfrescoAspects;
import org.alfresco.cmis.client.AlfrescoDocument;
import org.alfresco.cmis.client.AlfrescoFolder;
import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.TransientCmisObject;
import org.apache.chemistry.opencmis.commons.data.CmisExtensionElement;
import org.apache.chemistry.opencmis.commons.enums.ExtensionLevel;

public class TransientAlfrescoAspectsImpl implements AlfrescoAspects {
	private Session session;
	private CmisObject object;
	private Map<String, ObjectType> aspectTypes;
	private Map<String, ObjectType> addAspectTypes;
	private Map<String, ObjectType> removeAspectTypes;

	public TransientAlfrescoAspectsImpl(Session session, CmisObject object) {
		this.session = session;
		this.object = object;

		List<CmisExtensionElement> alfrescoExtensions = AlfrescoUtils
				.findAlfrescoExtensions(object
						.getExtensions(ExtensionLevel.PROPERTIES));

		if (alfrescoExtensions == null) {
			aspectTypes = Collections.emptyMap();
		} else {
			aspectTypes = new HashMap<String, ObjectType>();
			for (ObjectType type : AlfrescoUtils.getAspectTypes(session,
					alfrescoExtensions)) {
				if (type != null) {
					aspectTypes.put(type.getId(), type);
				}
			}
		}

		addAspectTypes = new HashMap<String, ObjectType>();
		removeAspectTypes = new HashMap<String, ObjectType>();
	}

    public ObjectType getTypeWithAspects() {
		if (object instanceof AlfrescoDocument) {
			return new AlfrescoDocumentTypeImpl((AlfrescoDocument) object);
		} else if (object instanceof AlfrescoFolder) {
			return new AlfrescoFolderTypeImpl((AlfrescoFolder) object);
		} else {
			return object.getType();
		}
	}

	public boolean hasAspect(String id) {
		return (aspectTypes.containsKey(id) || addAspectTypes.containsKey(id))
				&& (!removeAspectTypes.containsKey(id));
	}

	public boolean hasAspect(ObjectType type) {
		return type == null ? false : hasAspect(type.getId());
	}

	public Collection<ObjectType> getAspects() {
		Collection<ObjectType> result = new ArrayList<ObjectType>();
		Set<String> addTypes = new HashSet<String>(addAspectTypes.keySet());

		for (String typeId : aspectTypes.keySet()) {
			if (!removeAspectTypes.containsKey(typeId)) {
				result.add(aspectTypes.get(typeId));
			}
			addTypes.remove(typeId);
		}

		for (String typeId : addTypes) {
			result.add(addAspectTypes.get(typeId));
		}

		return result;
	}

	public ObjectType findAspect(String propertyId) {
		return AlfrescoUtils.findAspect(getAspects(), propertyId);
	}

	public CmisObject addAspect(String... id) {
		if (id == null || id.length == 0) {
			throw new IllegalArgumentException("Id must be set!");
		}

		ObjectType[] types = new ObjectType[id.length];
		for (int i = 0; i < id.length; i++) {
			types[i] = session.getTypeDefinition(id[i]);
		}

		return addAspect(types);
	}

	public CmisObject addAspect(ObjectType... type) {
		if (type == null || type.length == 0) {
			throw new IllegalArgumentException("Type must be set!");
		}

		for (ObjectType t : type) {
			if (t != null) {
				addAspectTypes.put(t.getId(), t);
				removeAspectTypes.remove(t.getId());
			}
		}
		
		return object;
	}

	public void setPropertyValues(TransientCmisObject object,
			Map<String, ?> properties) {
		if (properties == null || properties.isEmpty()) {
			return;
		}

		for (Map.Entry<String, ?> property : properties.entrySet()) {
			object.setPropertyValue(property.getKey(), property.getValue());
		}
	}

	public CmisObject addAspect(ObjectType type, Map<String, ?> properties) {
		return addAspect(type);
	}

	public CmisObject addAspect(ObjectType[] type, Map<String, ?> properties) {
		return addAspect(type);
	}

	public CmisObject addAspect(String id, Map<String, ?> properties) {
		return addAspect(id);
	}

	public CmisObject addAspect(String[] id, Map<String, ?> properties) {
		return addAspect(id);
	}

	public CmisObject removeAspect(String... id) {
		if (id == null || id.length == 0) {
			throw new IllegalArgumentException("Id must be set!");
		}

		ObjectType[] types = new ObjectType[id.length];
		for (int i = 0; i < id.length; i++) {
			types[i] = session.getTypeDefinition(id[i]);
		}

		return removeAspect(types);
	}

	public CmisObject removeAspect(ObjectType... type) {
		if (type == null || type.length == 0) {
			throw new IllegalArgumentException("Type must be set!");
		}

		for (ObjectType t : type) {
			if (t != null) {
				addAspectTypes.remove(t.getId());
				removeAspectTypes.put(t.getId(), t);
			}
		}
		return object;
	}

	public void save() {
		if (addAspectTypes.isEmpty() && removeAspectTypes.isEmpty()) {
			return;
		}

		AlfrescoUtils.updateAspects(session, object,
				addAspectTypes.values().toArray(new ObjectType[0]),
				removeAspectTypes.values().toArray(new ObjectType[0]), null);
	}
}
