/**
 * Copyright (C) 2009-2017 Simonsoft Nordic AB
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
package se.simonsoft.cms.item.structure;

public abstract class CmsLabelBase implements CmsLabel {
	
	protected String label;

	public CmsLabelBase(String label) {
		if (label == null) {
			throw new IllegalArgumentException("label can not be null");
		}
		if (label.length() == 0) {
			throw new IllegalArgumentException("label can not be an empty string");
		}		
		this.label = label;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public String toString() {
		return getLabel();
	}

	@Override
	public boolean equals(Object obj) {
		return obj != null && getLabel().equals(obj.toString());
	}

	@Override
	public int hashCode() {
		return getLabel().hashCode();
	}

}
