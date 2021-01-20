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

/**
 * Uses filename extensions to classify item,
 * throwing {@link UnsupportedOperationException} on anything that would require lookup.
 */
public class CmsItemClassificationGraphicWeb extends CmsItemClassificationExtension {

	/**
	 * graphics compatible with both web browsers and traditional XML authoring environments
	 * https://developer.mozilla.org/en-US/docs/Web/Media/Formats/Image_types
	 */
	public static final String GRAPHICS_WEB_DEFAULT = 
			"bmp" + // keeping bmp, used for embedded system screenshots
			"|gif" +
			"|jpg|jpeg" +
			"|png" +
			"|svg" +
			// tiff is too old with too much variation and limited browser support
			// Consider adding WebP in the future if non-browsers implement support
			"|mp4" + // #1338 Adding support for audio / video in CMS 5.0 / Arbortext 8.1.
			""; // dummy line - keep.

	public CmsItemClassificationGraphicWeb() {
		this.patternXml = setPattern(CmsItemClassificationXml.VALID_XML_DEFAULT); // Use CmsItemClassificationXml, basic default
		this.patternGraphics = setPattern(GRAPHICS_WEB_DEFAULT);
	}

}
