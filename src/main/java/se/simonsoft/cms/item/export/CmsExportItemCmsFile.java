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

import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.simonsoft.cms.item.CmsItem;

public class CmsExportItemCmsFile implements CmsExportItem {

    private final CmsItem item;
    private Logger logger = LoggerFactory.getLogger(CmsExportItemCmsFile.class);
    private Boolean ready = false;
    private CmsExportPath exportPath;


    public CmsExportItemCmsFile(CmsItem item) {
    	this(item, null);
    }

    
    public CmsExportItemCmsFile(CmsItem item, CmsExportPath exportPath) {

        if (item == null) {
            throw new IllegalArgumentException("The export item must not be null");
        }
        
        this.item = item;
        this.exportPath = exportPath;
    }

    
    @Override
    public void prepare() {

        if (ready) {
            throw new IllegalStateException("Item: " + item.getId().getLogicalId() + " is already prepared for export");
        }

        if (item.getKind().isFolder()) {
            throw new IllegalArgumentException("Item has to be a file. " + item.getId().getLogicalId());
        }

        logger.info("Prepared export of item: {}", this.item.getId().getLogicalId());
        this.ready = true;
    }

    @Override
    public Boolean isReady() {
        return this.ready;
    }

    @Override
    public void getResultStream(OutputStream stream) {
        if (!ready) {
            throw new IllegalStateException("Export item: " + this.item.getId().getLogicalId() + ", is not ready for export");
        }
            this.item.getContents(stream);
    }

    @Override
    public CmsExportPath getResultPath() {
        return this.exportPath;
    }


}
