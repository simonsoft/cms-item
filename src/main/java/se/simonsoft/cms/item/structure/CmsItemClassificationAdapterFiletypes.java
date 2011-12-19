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
