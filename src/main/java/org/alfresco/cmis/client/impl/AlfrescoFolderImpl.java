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
import java.util.Map;

import org.alfresco.cmis.client.AlfrescoFolder;
import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.TransientCmisObject;
import org.apache.chemistry.opencmis.client.runtime.FolderImpl;
import org.apache.chemistry.opencmis.client.runtime.SessionImpl;
import org.apache.chemistry.opencmis.commons.data.ObjectData;

public class AlfrescoFolderImpl extends FolderImpl implements AlfrescoFolder
{
    private static final long serialVersionUID = 1L;

    protected AlfrescoAspectsImpl aspects;

    public AlfrescoFolderImpl(SessionImpl session, ObjectType objectType, ObjectData objectData,
            OperationContext context)
    {
        super(session, objectType, objectData, context);
    }

    @Override
    protected void initialize(SessionImpl session, ObjectType objectType, ObjectData objectData,
            OperationContext context)
    {
        super.initialize(session, objectType, objectData, context);
        aspects = new AlfrescoAspectsImpl(session, this);
    }

    @Override
    protected TransientCmisObject createTransientCmisObject()
    {
        TransientAlfrescoFolderImpl td = new TransientAlfrescoFolderImpl();
        td.initialize(getSession(), this);

        return td;
    }

    public ObjectType getTypeWithAspects()
    {
        readLock();
        try
        {
            return aspects.getTypeWithAspects();
        } finally
        {
            readUnlock();
        }
    }

    @Override
    public ObjectId updateProperties(Map<String, ?> properties, boolean refresh)
    {
        return super.updateProperties(
                AlfrescoAspectsUtils.preparePropertiesForUpdate(properties, getType(), getAspects()), refresh);
    }

    public boolean hasAspect(String id)
    {
        readLock();
        try
        {
            return aspects.hasAspect(id);
        } finally
        {
            readUnlock();
        }
    }

    public boolean hasAspect(ObjectType type)
    {
        readLock();
        try
        {
            return aspects.hasAspect(type);
        } finally
        {
            readUnlock();
        }
    }

    public Collection<ObjectType> getAspects()
    {
        readLock();
        try
        {
            return aspects.getAspects();
        } finally
        {
            readUnlock();
        }
    }

    public ObjectType findAspect(String propertyId)
    {
        readLock();
        try
        {
            return aspects.findAspect(propertyId);
        } finally
        {
            readUnlock();
        }
    }

    public void addAspect(String... id)
    {
        readLock();
        try
        {
            aspects.addAspect(id);
        } finally
        {
            readUnlock();
        }
        refresh();
    }

    public void addAspect(ObjectType... type)
    {
        readLock();
        try
        {
            aspects.addAspect(type);
        } finally
        {
            readUnlock();
        }
        refresh();
    }

    public void addAspect(ObjectType type, Map<String, ?> properties)
    {
        readLock();
        try
        {
            aspects.addAspect(type, properties);
        } finally
        {
            readUnlock();
        }
        refresh();
    }

    public void addAspect(ObjectType[] type, Map<String, ?> properties)
    {
        readLock();
        try
        {
            aspects.addAspect(type, properties);
        } finally
        {
            readUnlock();
        }
        refresh();
    }

    public void addAspect(String id, Map<String, ?> properties)
    {
        readLock();
        try
        {
            aspects.addAspect(id, properties);
        } finally
        {
            readUnlock();
        }
        refresh();
    }

    public void addAspect(String[] id, Map<String, ?> properties)
    {
        readLock();
        try
        {
            aspects.addAspect(id, properties);
        } finally
        {
            readUnlock();
        }
        refresh();
    }

    public void removeAspect(String... id)
    {
        readLock();
        try
        {
            aspects.removeAspect(id);
        } finally
        {
            readUnlock();
        }
        refresh();
    }

    public void removeAspect(ObjectType... type)
    {
        readLock();
        try
        {
            aspects.removeAspect(type);
        } finally
        {
            readUnlock();
        }
        refresh();
    }
}