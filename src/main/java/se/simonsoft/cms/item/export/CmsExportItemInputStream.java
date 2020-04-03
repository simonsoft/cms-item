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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CmsExportItemInputStream implements CmsExportItem {

    private final InputStream inputStream;
    private final CmsExportPath exportPath;

    private Boolean ready = false;
    private Logger logger = LoggerFactory.getLogger(CmsExportItemInputStream.class);

    
    public CmsExportItemInputStream(InputStream inputStream) {
    	this(inputStream, null);
    }
    
    public CmsExportItemInputStream(InputStream inputStream, CmsExportPath exportPath) {

        if (inputStream == null) {
            throw new IllegalArgumentException("The export InputStream must not be null");
        }

        this.inputStream = inputStream;
        this.exportPath = exportPath;
    }


    @Override
    public void prepare() {

        if (isReady()) {
            throw new IllegalStateException("InputStream is already prepared for export");
        }

        logger.info("Starting preparation for export of InputStream");
        this.ready = true;
    }

    @Override
    public Boolean isReady() {
        return this.ready;
    }

    @Override
    public void getResultStream(OutputStream stream) {

        byte[] buffer = new byte[1024 * 32];
        int length;
        final InputStream content = this.inputStream;
        try {
            while ((length = content.read(buffer)) != -1) {
                stream.write(buffer, 0, length);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not read InputStream", e);
        }
    }

    @Override
    public CmsExportPath getResultPath() {
        return this.exportPath;
    }


}
