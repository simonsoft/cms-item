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

import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Named;

import se.simonsoft.cms.item.CmsItemId;

/**
 * Uses filename extensions to classify item, 
 * throwing {@link UnsupportedOperationException} on anything that would require lookup.
 */
public class CmsItemClassificationExtension implements
		CmsItemClassification {
	
	protected Pattern patternXml;
	protected Pattern patternGraphics;
	
	
	public CmsItemClassificationExtension() {}
	

	@Inject 
	public void setFiletypesXml(
			@Named("config:se.simonsoft.cms.item.filetypes.xml") String filetypesXml) {

		this.patternXml = setPattern(filetypesXml);
	}
	
	@Inject 
	public void setFiletypesGraphic(
			@Named("config:se.simonsoft.cms.item.filetypes.graphic") String filetypesGraphic)  {

		this.patternGraphics = setPattern(filetypesGraphic);
	}
	
	@Inject 
	public CmsItemClassificationExtension withFiletypesXml(String filetypesXml) {

		this.patternXml = setPattern(filetypesXml);
		return this;
	}
	
	@Inject 
	public CmsItemClassificationExtension withFiletypesGraphic(String filetypesGraphic)  {

		this.patternGraphics = setPattern(filetypesGraphic);
		return this;
	}
	
	
	protected Pattern setPattern(String config) {
		// Matching whole filename is more flexible, can likely support multi-extensions like '.tar.gz'
		return Pattern.compile(".*\\.(" + config + ")$");
	};
	
	
	@Override
	public boolean isXml(CmsItemId item) {
		// Still case-sensitive for XML, keep the strict approach for now.
		return patternXml != null && patternXml.matcher(item.getRelPath().getName()).matches();
	}	
	
	@Override
	public boolean isGraphic(CmsItemId item) {
		// Relaxed to case-insensitive matching for graphics due to practical reasons (interop with some Windows applications).
		return patternGraphics != null && patternGraphics.matcher(item.getRelPath().getName().toLowerCase()).matches();
	}

	@Override
	public boolean isPublishable(CmsItemId item) {
		// We should use a separate impl or subclass to implement this, keeping filetype logic isolated
		throw new UnsupportedOperationException("Detection of publishable documents requires lookup");
	}

}
