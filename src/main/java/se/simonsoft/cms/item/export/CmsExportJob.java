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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.util.*;


public class CmsExportJob extends CmsExportJobBase {

    private Logger logger = LoggerFactory.getLogger(CmsExportJob.class);
    private List<CmsExportItem> exportItems = new ArrayList<>();
    private boolean isPrepared = false;
    private Map<CmsExportMetaKey, String> metaMap = new HashMap<>();


	public CmsExportJob(CmsExportPrefix jobPrefix, String jobName, String jobExtension) {
		super(jobPrefix, jobName, jobExtension);
	}

    
	public boolean isEmpty() {
		return exportItems.isEmpty();
	}
	
    /**
     * Starts the preparation of the CmsExportItem's. Is only allowed to be called on once.
     */
    public void prepare() {

        if (isPrepared) {
            throw new IllegalStateException("The export job is prepared or in it's prepare phase.");
        }

        logger.info("Preparing: {} CmsExportItems for export.", getExportItems().size());
        
        if (getExportItems().isEmpty()) {
            throw new IllegalArgumentException("There are no items in the export job.");
        }

        for (CmsExportItem item : getExportItems()) {
            item.prepare();
        }
        
        isPrepared = true;
    }

    /**
     * Will traverse through List<CmsExportItem> and ask if they are ready to be exported.
     * @return Boolean set to true if al items is ready.
     */
    public Boolean isReady() {

        if (getExportItems().isEmpty()) {
            throw new IllegalArgumentException("There are no items in the export job.");
        }

        if (isPrepared) {
            return isPrepared;
        }

        //TODO: We should remember if an ExportItem is ready, so we don't have to check again do it again.
        for (CmsExportItem item: getExportItems()) {
            isPrepared = item.isReady();
            if (!isPrepared) {
                break;
            }
        }

        return isPrepared;
    }

    /**
     * Will add a CmsExportItem, dose not allow duplicates or modification
     * @param exportItem
     */
    public void addExportItem(CmsExportItem exportItem) {

    	if (exportItem == null) {
            throw new IllegalArgumentException("Export item must not be null.");
        }
        
        if (exportItem.getResultPath() == null) {
            throw new IllegalArgumentException("The export path must not be null");
        }
        
    	addExportItemInternal(exportItem);
    }
    
    protected void addExportItemInternal(CmsExportItem exportItem) {

    	if (exportItem == null) {
            throw new IllegalArgumentException("Export item must not be null.");
        }
    	
        if (isPrepared) {
            throw new ConcurrentModificationException("It's not allowed to modify the CmsExportItem list when preparation is started.");
        }

        if (this.getExportItems().contains(exportItem)) {
            throw new IllegalArgumentException("Duplicated CmsExportItem, this item is already in the list");
        }
        logger.debug("Adding: {} to the CmsExportJob", exportItem);
        getExportItems().add(exportItem);
    }

    public List<CmsExportItem> getExportItems() {
        return this.exportItems;
    }


    public Map<CmsExportMetaKey, String> getMeta() {
        return Collections.unmodifiableMap(this.metaMap);
    }

    public CmsExportJob withMeta(String key, String value) {

        CmsExportMetaKey cmsExportMetaKey = new CmsExportMetaKey(key);

        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Can not add empty or null value as meta");
        }

        if (metaMap.containsKey(cmsExportMetaKey)) {
            throw new IllegalArgumentException("The meta key already exist in map. Key: " + key);
        }

        metaMap.put(cmsExportMetaKey, value);

        return this;

    }

    public interface SingleItem {

        void getResultStream(OutputStream out);
    }



}
