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
package org.apache.chemistry.opencmis.fit.runtime;

import java.util.Map;

public enum FixtureData {
	FOLDER_TYPE_ID("org.apache.chemistry.opencmis.fit.runtime.folder.type.id",
			"cmis:folder"), DOCUMENT_TYPE_ID(
			"org.apache.chemistry.opencmis.fit.runtime.document.type.id",
			"cmis:document"), QUERY(
			"org.apache.chemistry.opencmis.fit.runtime.query",
			"SELECT * FROM cmis:document"), PROPERTY_FILTER(
			"org.apache.chemistry.opencmis.fit.runtime.property.filter", "*"), FOLDER1_NAME(
			"org.apache.chemistry.opencmis.fit.runtime.folder1.name", "folder1"), FOLDER2_NAME(
			"org.apache.chemistry.opencmis.fit.runtime.folder2.name", "folder2"), DOCUMENT1_NAME(
			"org.apache.chemistry.opencmis.fit.runtime.document1.name",
			"document1.txt"), DOCUMENT2_NAME(
			"org.apache.chemistry.opencmis.fit.runtime.document2.name",
			"document2.txt"), 
			PROPERTY_NAME_STRING_MULTI_VALUED("org.apache.chemistry.opencmis.fit.runtime.multi.valued.property.name", null);

    private final String key;
	private String value;

	FixtureData(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String value() {
		return this.value;
	}

	public static void changeValues(Map<String, String> map) {
		for (FixtureData fd : FixtureData.values()) {
			String v = map.get(fd.key);
			if (v != null) {
				fd.changeValue(v);
			}
		}
	}

	public static FixtureData get(String key) {
		for (FixtureData fd : FixtureData.values()) {
			if (fd.key.equals(key)) {
				return fd;
			}
		}
		throw new IllegalArgumentException(key);
	}

	void changeValue(String newValue) {
		this.value = newValue;
	}

	public String toString() {
		return this.value();
	}

	public String key() {
		return this.key;
	}
}