/**
 * Copyright (C) 2009-2016 Simonsoft Nordic AB
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

import se.simonsoft.cms.item.CmsItemId;


/**
 * Strict classification of XML that is expected to be parsable and valid. 
 * Non-XML items are considered graphic. 
 * Uses filename extensions to classify item, 
 * throwing {@link UnsupportedOperationException} on anything that would require lookup.
 */
public class CmsItemClassificationXml extends CmsItemClassificationExtension {


	public static final String VALID_XML_DEFAULT = 
			"xml|dita|ditamap" + 
			"|xliff|xlf" +
					"";	// dummy line - keep.

	
	
	public CmsItemClassificationXml() {
		this.patternXml = setPattern(VALID_XML_DEFAULT);
	}
	

	@Override
	public boolean isGraphic(CmsItemId item) {
		return !isXml(item);
	}
	
}
