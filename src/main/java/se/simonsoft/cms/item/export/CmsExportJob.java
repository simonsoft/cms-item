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
package se.simonsoft.cms.item.export;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;


public class CmsExportJob {

    private Logger logger = LoggerFactory.getLogger(CmsExportJob.class);
    private List<CmsExportItem> exportItems = new ArrayList<CmsExportItem>();
    private boolean isPrepared = false;
    private List<MetaTuple> metaList;
    private String jobName;
    private String jobPrefix;


    /**
     * Starts the preparation of the CmsExportItem's. Is only allowed to be called on once.
     */
    public void prepare() {

        if (isPrepared) {
            throw new IllegalStateException("The export job is prepared or in it's prepare phase.");
        }

        logger.info("Preparing: {} CmsExportItems for export.", getExportItems().size());

        isPrepared = true;
        for (CmsExportItem item : getExportItems()) {
            item.prepare();
        }
    }

    /**
     * Will traverse through List<CmsExportItem> and ask if they are ready to be exported.
     * @return Boolean set to true if al items is ready.
     */
    public Boolean isReady() {

        if (getExportItems().isEmpty()) {
            throw new IllegalArgumentException("There's no items in the export job to prepare");
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
            throw new IllegalArgumentException("Export item can't be null.");
        }

        if (isPrepared) {
            throw new ConcurrentModificationException("It's not allowed to modify the CmsExportItem list when preparation is started.");
        }

        if (this.getExportItems().contains(exportItem)) {
            throw new IllegalArgumentException("Duplicated CmsExportItem, this item is already in the list");
        }
        logger.info("Adding: {} to the CmsExportJob", exportItem);
        getExportItems().add(exportItem);
    }

    public List<CmsExportItem> getExportItems() {
        return this.exportItems;
    }


    public List<MetaTuple> getMetaList() {
        return this.metaList;
    }

    public void setMeta(String key, String value) {

        if (getMetaList() == null) {
            logger.debug("No meta data list exist creating new instance");
            this.metaList = new ArrayList<MetaTuple>();
        }

        MetaTuple metaTuple = new MetaTuple();
        metaTuple.setKey(key);

        if (value != null) {
            metaTuple.setValue(value);
        }

        //Should we allow 2 occurrence's of same meta values?
        if (getMetaList().contains(metaTuple)) {
            throw new IllegalArgumentException("The meta value is already in the list.");
        }

        for(MetaTuple meta: getMetaList()) {
            if (meta.getKey() == key ){
                //Should we allow meta to be overwritten?
                throw new IllegalArgumentException("Key already exists");
            }
        }

        metaList.add(metaTuple);

    }

    public void setMeta(String key) {

        if (key == null) {
            throw new IllegalArgumentException("Key already exist in the metalist.");
        }

        setMeta(key, null);
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobPrefix() {
        return jobPrefix;
    }

    public void setJobPrefix(String jobPrefix) {
        this.jobPrefix = jobPrefix;
    }

    public class MetaTuple {

        private String key;
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

    }

    public interface SingleItem {

        void getResultStream(OutputStream out);
    }
}
