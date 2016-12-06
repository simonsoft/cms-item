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
import se.simonsoft.cms.item.CmsItem;
import se.simonsoft.cms.item.CmsItemId;

import java.io.OutputStream;

public class CmsExportItemCmsFile implements CmsExportItem {

    private final CmsItem translationItem;
    private Logger logger = LoggerFactory.getLogger(CmsExportItemCmsFile.class);
    private Boolean ready = false;
    private CmsExportPath resultPath;


    public CmsExportItemCmsFile(CmsItem translationItem, String exportPath) {

        if (translationItem == null) {
            throw new IllegalArgumentException("Can't prepare null item for export");
        }
        this.translationItem = translationItem;

        if (exportPath != null) {
            setResultPath(exportPath);
        }
    }

    @Override
    public void prepare() {

        if (ready) {
            throw new IllegalStateException("Item: " + translationItem.getId().getLogicalId() + " is already prepared for export");
        }

        if (translationItem.getKind().isFolder()) {
            throw new IllegalArgumentException("Item has to be a file. " + translationItem.getId().getLogicalId());
        }

        logger.info("Starting preperation for export of item: {}", this.translationItem.getId().getLogicalId());
        setReady(true);
    }

    @Override
    public Boolean isReady() {
        return this.ready;
    }

    @Override
    public void getResultStream(OutputStream stream) {
        if (!ready) {
            throw new IllegalStateException("Export item: " + this.translationItem.getId().getLogicalId() + ", is not ready for export");
        }
            this.translationItem.getContents(stream);
    }

    @Override
    public CmsExportPath getResultPath() {

        CmsExportPath path = this.resultPath;
        if (path == null) {
            path = new CmsExportPath("/".concat(this.translationItem.getId().getRelPath().getName()));
        }

        return path;
    }

    public void setResultPath(String path) {
        CmsItemId id = this.translationItem.getId();
        this.resultPath =  new CmsExportPath(path.concat(id.getRelPath().getName()));
    }


    private void setReady(Boolean ready) {
        logger.info("Export item's ready flag is set to: {}", ready);
        this.ready = ready;
    }

    private boolean isTranslation(CmsItem translation) {
        return translation.getProperties().containsProperty("abx:TranslationLocale");
    }
}
