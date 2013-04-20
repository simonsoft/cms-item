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
package se.simonsoft.cms.item.structure;

import java.util.regex.Pattern;

import se.simonsoft.cms.item.CmsItemId;

/**
 * Uses filename extensions to classify item, 
 * throwing {@link UnsupportedOperationException} on anything that would require lookup.
 */
public class CmsItemClassificationAdapterFiletypes implements
		CmsItemClassification {
	
	/**
	 * synchronized with cms_adapter.xml and javascript
	 */
	public static final String CMS_ADAPTER_XML = 
			"xml|dita|ditamap|dcf|pcf|xlf|sgm|sgml|htm|html|txt";

	/**
	 * synchronized with cms_adapter.xml and javascript
	 */
	public static final String CMS_ADAPTER_GRAPHICS = 
			"bmp|cgm|edz|pvz|eps|ps|gif|iso|isoz|jpg|jpeg|png|svg|tif|tiff";	
	
	private static final Pattern patternXml = Pattern.compile(".*\\.(" + CMS_ADAPTER_XML + ")$");
	private static final Pattern patternGraphics = Pattern.compile(".*\\.(" + CMS_ADAPTER_GRAPHICS + ")$");
	
	@Override
	public boolean isXml(CmsItemId item) {
		return patternXml.matcher(item.getRelPath().getName()).matches();
	}	
	
	@Override
	public boolean isGraphic(CmsItemId item) {
		return patternGraphics.matcher(item.getRelPath().getName()).matches();
	}

	@Override
	public boolean isPublishable(CmsItemId item) {
		// We should use a separate impl or subclass to implement this, keeping filetype logic isolated
		throw new UnsupportedOperationException("Detection of publishable documents requires lookup");
	}

}
