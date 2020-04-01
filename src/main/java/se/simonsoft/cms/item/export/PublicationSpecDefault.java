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
package se.simonsoft.cms.item.export;

import se.simonsoft.cms.item.CmsItemId;

/**
 * Represents publication metadata such as document name and output profile.
 * 
 * Identification of these fields differs between subclasses.
 */
public class PublicationSpecDefault implements PublicationSpec {

	private CmsItemId id;
//	private String outputExtension = PublicationRequest.Type.pdf.toString();
	private String outputExtension = ".pdf";
	private String locale = null;
	private String profile = null;
	private String style = null;
	
	public PublicationSpecDefault(CmsItemId logicalId) {
		this.id = logicalId;
	}

	/**
	 * @return logical id of the publication source
	 */
	public CmsItemId getId() {
		return id;
	}
	
	@Override
	public String getName() {
		return id.getRelPath().getNameBase();
	}

	@Override
	public String getOutputExtension() {
		return outputExtension;
	}

	@Override
	public String getProfile() {
		return profile;
	}

	@Override
	public String getLocale() {
		return locale;
	}

	@Override
	public String getStyle() {
		return style;
	}

	@Override
	public String toString() {
		StringBuffer s = new StringBuffer();
		s.append(id);
		if (getLocale() != null) s.append(",lang=").append(getLocale());
		if (getProfile() != null) s.append(",profile=").append(getProfile());
		if (getStyle() != null) s.append(",style=").append(getStyle());
		s.append(',').append(outputExtension);
		return s.toString();
	}

	void setId(CmsItemId id) {
		this.id = id;
	}

	void setOutputExtension(String outputExtension) {
		this.outputExtension = outputExtension;
	}

	void setLocale(String locale) {
		this.locale = locale;
	}

	void setProfile(String profile) {
		this.profile = profile;
	}

	void setStyle(String style) {
		this.style = style;
	}

}
