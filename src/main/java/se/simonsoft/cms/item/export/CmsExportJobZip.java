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
package se.simonsoft.cms.item.export;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class CmsExportJobZip {

    private ZipOutputStream zipOutputStream;
    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private CmsExportJob exportJob;
    private List<CmsExportItem> exportItems;
    private Boolean isReady;

    public CmsExportJobZip(CmsExportJob exportJob) {

        if (exportJob == null) {
            throw new IllegalArgumentException("CmsExportJob must not be null");
        }

        if (!exportJob.isReady()) {
            throw new IllegalStateException("The export job is not prepared to be sent to a writer");
        }

        this.exportJob = exportJob;
        this.isReady = false;
    }

    public void prepare() {

        this.exportItems = exportJob.getExportItems();

        if (exportItems.size() < 1) {
            throw new IllegalArgumentException("There's nothing to export in the exportJob, need at least one prepared CmsExportItem");
        }

        this.isReady = true;

    }

    public boolean isReady() {
        return this.isReady;
    }

    public void getResultStream(OutputStream out) {

        if (!isReady) {
            throw new IllegalStateException("The writer is not prepared to write.");
        }

        if (out == null) {
            throw new IllegalArgumentException("OutputStream");
        }

        try {
            zipOutputStream = new ZipOutputStream(out);

            for (CmsExportItem item: getExportItems()) {
                String path = item.getResultPath().getPath();
                //Path's first "/" must be removed for zips on windows should work.
                ZipEntry entry = new ZipEntry(path.substring(1, path.length()));
                zipOutputStream.putNextEntry(entry);
                item.getResultStream(zipOutputStream);
                zipOutputStream.closeEntry();
            }

            zipOutputStream.finish();
            zipOutputStream.close();

        } catch (IOException e) {
            logger.error("Could not compress to file: {}", e.getMessage());
        }
    }

    private List<CmsExportItem> getExportItems() {
        return this.exportItems;
    }

}



