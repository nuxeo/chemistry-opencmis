/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.chemistry.opencmis.inmemory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.commons.bindings.AccessControlEntry;
import org.apache.chemistry.opencmis.commons.bindings.AccessControlList;
import org.apache.chemistry.opencmis.commons.bindings.AllowableActionsData;
import org.apache.chemistry.opencmis.commons.bindings.ChangeEventInfoData;
import org.apache.chemistry.opencmis.commons.bindings.ObjectData;
import org.apache.chemistry.opencmis.commons.bindings.PolicyIdListData;
import org.apache.chemistry.opencmis.commons.bindings.RenditionData;
import org.apache.chemistry.opencmis.commons.enums.IncludeRelationships;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.AccessControlListImpl;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.AllowableActionsDataImpl;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ChangeEventInfoDataImpl;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.PolicyIdListDataImpl;
import org.apache.chemistry.opencmis.inmemory.server.RuntimeContext;
import org.apache.chemistry.opencmis.inmemory.storedobj.api.Content;
import org.apache.chemistry.opencmis.inmemory.storedobj.api.Folder;
import org.apache.chemistry.opencmis.inmemory.storedobj.api.ObjectStore;
import org.apache.chemistry.opencmis.inmemory.storedobj.api.StoredObject;
import org.apache.chemistry.opencmis.inmemory.storedobj.api.Version;
import org.apache.chemistry.opencmis.inmemory.storedobj.api.VersionedDocument;
import org.apache.chemistry.opencmis.server.spi.CallContext;

/**
 * @author Jens A collection of utility functions to fill the data objects used as return values for
 *         the service object calls
 */
public class DataObjectCreator {

  public static AllowableActionsData fillAllowableActions(ObjectStore objStore, StoredObject so) {

    boolean isFolder = so instanceof Folder;
    boolean isDocument = so instanceof Content;
    boolean isCheckedOut = false; 
    boolean canCheckOut = false;
    boolean canCheckIn = false;
    boolean isVersioned = so instanceof Version || so instanceof VersionedDocument;
    boolean hasContent = so instanceof Content && ((Content) so).hasContent();
      
    String user = RuntimeContext.getRuntimeConfigValue(CallContext.USERNAME);
    if (so instanceof Version) {
      isCheckedOut = ((Version)so).isPwc();
      canCheckIn = isCheckedOut && ((Version)so).getParentDocument().getCheckedOutBy().equals(user);
    } else if (so instanceof VersionedDocument) {
      isCheckedOut = ((VersionedDocument)so).isCheckedOut();
      canCheckOut = !((VersionedDocument)so).isCheckedOut();
      canCheckIn = isCheckedOut && ((VersionedDocument)so).getCheckedOutBy().equals(user);
    }
          
    AllowableActionsDataImpl allowableActions = new AllowableActionsDataImpl();
    Map<String, Boolean> actions = new HashMap<String, Boolean>();
    actions.put(AllowableActionsData.ACTION_CAN_DELETE_OBJECT, Boolean.TRUE);
    actions.put(AllowableActionsData.ACTION_CAN_UPDATE_PROPERTIES, Boolean.TRUE);
    
    actions.put(AllowableActionsData.ACTION_CAN_APPLY_POLICY, Boolean.FALSE);
    actions.put(AllowableActionsData.ACTION_CAN_GET_APPLIED_POLICIES, Boolean.FALSE);
    actions.put(AllowableActionsData.ACTION_CAN_REMOVE_POLICY, Boolean.FALSE);

    actions.put(AllowableActionsData.ACTION_CAN_GET_ACL, Boolean.FALSE);
    actions.put(AllowableActionsData.ACTION_CAN_APPLY_ACL, Boolean.FALSE);

    if (isFolder || isDocument) {
      actions.put(AllowableActionsData.ACTION_CAN_CREATE_RELATIONSHIP, Boolean.FALSE);
      actions.put(AllowableActionsData.ACTION_CAN_GET_PROPERTIES, Boolean.TRUE);
      actions.put(AllowableActionsData.ACTION_CAN_GET_OBJECT_RELATIONSHIPS, Boolean.FALSE);
      actions.put(AllowableActionsData.ACTION_CAN_GET_OBJECT_PARENTS, !so.equals(objStore.getRootFolder()));
      actions.put(AllowableActionsData.ACTION_CAN_MOVE_OBJECT, Boolean.TRUE);      
    }

    if (isFolder) {
      actions.put(AllowableActionsData.ACTION_CAN_GET_FOLDER_PARENT, !so.equals(objStore.getRootFolder()));
      actions.put(AllowableActionsData.ACTION_CAN_GET_FOLDER_TREE, true);
      actions.put(AllowableActionsData.ACTION_CAN_GET_DESCENDANTS, true);
      actions.put(AllowableActionsData.ACTION_CAN_ADD_OBJECT_TO_FOLDER, true);
      actions.put(AllowableActionsData.ACTION_CAN_REMOVE_OBJECT_FROM_FOLDER, true);
      actions.put(AllowableActionsData.ACTION_CAN_CREATE_DOCUMENT, true);
      actions.put(AllowableActionsData.ACTION_CAN_CREATE_FOLDER, true);
      actions.put(AllowableActionsData.ACTION_CAN_GET_CHILDREN, true);
      actions.put(AllowableActionsData.ACTION_CAN_DELETE_TREE, isFolder);
    }
    
    if (hasContent) { 
      actions.put(AllowableActionsData.ACTION_CAN_DELETE_CONTENT_STREAM, isDocument);
      actions.put(AllowableActionsData.ACTION_CAN_GET_CONTENT_STREAM, isDocument);
      actions.put(AllowableActionsData.ACTION_CAN_GET_RENDITIONS, Boolean.FALSE);
    }        

   if (isVersioned) { 
      actions.put(AllowableActionsData.ACTION_CAN_CHECK_OUT, canCheckOut);
      actions.put(AllowableActionsData.ACTION_CAN_CANCEL_CHECK_OUT, isCheckedOut);
      actions.put(AllowableActionsData.ACTION_CAN_CHECK_IN, canCheckIn);
      actions.put(AllowableActionsData.ACTION_CAN_GET_ALL_VERSIONS, so instanceof VersionedDocument);
    }
    
    if (isDocument) {
      actions.put(AllowableActionsData.ACTION_CAN_SET_CONTENT_STREAM, isVersioned ? canCheckIn: isDocument);      
    }
    
    allowableActions.setAllowableActions(actions);
    return allowableActions;
  }

  public static AccessControlList fillACL(StoredObject so) {
    AccessControlListImpl acl = new AccessControlListImpl();
    List<AccessControlEntry> aces = new ArrayList<AccessControlEntry>();
    // TODO to be completed if ACLs are implemented
    acl.setAces(aces);
    return acl;
  }

  public static PolicyIdListData fillPolicyIds(StoredObject so) {
    // TODO: to be completed if policies are implemented
    PolicyIdListDataImpl polIds = new PolicyIdListDataImpl();
    // polIds.setPolicyIds(...);
    return polIds;
  }

  public static List<ObjectData> fillRelationships(IncludeRelationships includeRelationships,
      StoredObject so) {
    // TODO: to be completed if relationships are implemented
    List<ObjectData> relationships = new ArrayList<ObjectData>();
    return relationships;
  }

  public static List<RenditionData> fillRenditions(StoredObject so) {
    // TODO: to be completed if renditions are implemented
    List<RenditionData> renditions = new ArrayList<RenditionData>();
    return renditions;
  }

  public static ChangeEventInfoData fillChangeEventInfo(StoredObject so) {
    // TODO: to be completed if change information is implemented
    ChangeEventInfoData changeEventInfo = new ChangeEventInfoDataImpl();
    return changeEventInfo;
  }
}
