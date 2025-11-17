package se.simonsoft.cms.item.naming;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

import se.simonsoft.cms.item.CmsItem;
import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.CmsRepository;
import se.simonsoft.cms.item.info.CmsItemLookup;

/**
 * Experimental
 */
@Singleton
public class CmsItemNameFactory {
	
	private final Map<CmsRepository, CmsItemLookup> lookup;
	
	private static final String CMS_CLASS_SHARDPARENT = "shardparent";
	private static final String PROPNAME_CONFIG_ITEMNAMEPATTERN = "cmsconfig:ItemNamePattern";
	
	private static final Logger logger = LoggerFactory.getLogger(CmsItemNameFactory.class);
	
	
	@Inject
	public CmsItemNameFactory(Map<CmsRepository, CmsItemLookup> lookup) {
		this.lookup = lookup;
	}
	
	
	public CmsItemPath getItemPath(CmsItem folder, String extension) {
		if (!folder.isCmsClass(CMS_CLASS_SHARDPARENT)) {
        	throw new IllegalArgumentException(MessageFormatter.format("Not a shard parent folder: {}", folder).getMessage());
		}
		CmsItemNamePattern pattern = getItemNamePattern(folder);
		
		CmsItemNaming itemNaming = new CmsItemNamingShard1K(folder.getId().getRepository(), lookup.get(folder.getId().getRepository()));
		return itemNaming.getItemPath(folder.getId().getRelPath(), pattern, extension);
	}
	
	
    public static boolean isShardParent(CmsItem folder) {

        if (!folder.isCmsClass(CMS_CLASS_SHARDPARENT)) {
        	return false;
		}
        
        // Test the configured pattern, throws exception if invalid
        CmsItemNamePattern pattern = getItemNamePattern(folder);
        logger.trace("Shard parent folder: {} with pattern: {}", folder.getId(), pattern);

        return true;
    }
    
    public static CmsItemNamePattern getItemNamePattern(CmsItem folder) {

    	if (!folder.getProperties().containsProperty(PROPNAME_CONFIG_ITEMNAMEPATTERN)) {
        	throw new IllegalArgumentException(MessageFormatter.format("Location does not define a name pattern: {}", folder).getMessage());
        }

        // Test the configured pattern, throws exception if invalid
        return new CmsItemNamePattern(folder.getProperties().getString(PROPNAME_CONFIG_ITEMNAMEPATTERN));
    }
}
