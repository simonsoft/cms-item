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


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CmsExportItemString implements CmsExportItem {

    private final ByteArrayInputStream content; // IOUtils uses ByteArrayInputStream internally
    private final CmsExportPath exportPath;

    private Boolean ready = false;
    private Logger logger = LoggerFactory.getLogger(CmsExportItemString.class);

    
    public CmsExportItemString(String content) {
    	this(content, null);
    }
    
    public CmsExportItemString(String content, CmsExportPath exportPath) {

        if (content == null) {
            throw new IllegalArgumentException("The export content string must not be null");
        }

        this.content = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        this.exportPath = exportPath;
    }


    @Override
    public Long prepare() {

        if (isReady()) {
            throw new IllegalStateException("InputStream is already prepared for export");
        }

        logger.trace("Starting preparation for export of InputStream");
        this.ready = true;
        return content.available() + 0L; // ensure Long
    }

    @Override
    public Boolean isReady() {
        return this.ready;
    }

    @Override
    public void getResultStream(OutputStream stream) {
    	
        try {
        	stream.write(content.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to export OutputStream: ", e);
        } 
    }

    @Override
    public CmsExportPath getResultPath() {
        return this.exportPath;
    }


}
