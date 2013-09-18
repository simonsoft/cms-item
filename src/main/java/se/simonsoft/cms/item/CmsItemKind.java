/**
 * Copyright (C) 2009-2013 Simonsoft Nordic AB
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package se.simonsoft.cms.item;

public enum CmsItemKind {

	File,
	/**
	 * Often called "dir" in subversion "kind" info
	 */
	Folder,
	/**
	 * Special kind of folder, useful for styling etc, use {@link #isFolder()} to handle as folder
	 */
	Repository,
	/**
	 * Currently not supported in CMS.
	 */
	Symlink;
	
	/**
	 * Like {@link #valueOf(String)} but fixes case and supports svn's "dir"
	 * @param kind recognizable string
	 * @return cms kind
	 */
	public static CmsItemKind fromString(String kind) {
		if ("dir".equals(kind)) {
			return Folder;
		}
		return valueOf(kind.substring(0, 1).toUpperCase() + kind.substring(1).toLowerCase());
	}
	
	/**
	 * @return subversion's "kind" string (but maybe not for symlink)
	 */
	public String getKind() {
		if (isFolder()) {
			return "dir";
		}
		return toString().toLowerCase();
	}
	
	public boolean isFile() {
		return this == File;
	}
	
	public boolean isFolder() {
		return this == Folder || this == Repository;
	}
	
}
