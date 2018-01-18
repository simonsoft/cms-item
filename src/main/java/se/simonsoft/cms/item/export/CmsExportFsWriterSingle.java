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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.simonsoft.cms.item.export.CmsExportJob;
import se.simonsoft.cms.item.export.CmsExportWriter;

public class CmsExportFsWriterSingle implements CmsExportWriter, CmsExportWriter.LocalFileSystem {

    private File fsParent;
    private CmsExportJob exportJob;
    private File folder;
    private boolean ready = false;
    private File exportPath;
    private Logger logger = LoggerFactory.getLogger(CmsExportFsWriterSingle.class);


    public CmsExportFsWriterSingle(File fsParent) {
        this.fsParent = fsParent;
    }

    @Override
    public void prepare(CmsExportJob job) {
    	
    	logger.debug("Prepearing writer");
    	
    	if (exportJob != null) {
			throw new IllegalStateException("Writer already consumed, initialize a new reader for each job.");
		}

        if (!(job instanceof CmsExportJob.SingleItem)) {
            throw new IllegalArgumentException("Single writers expects CmsExportJobs that implements SingleItem");
        }

        if (!job.isReady()) {
            throw new IllegalStateException("The export job is not prepared to be written");
        }

        this.exportJob = job;


        try {
        	logger.debug("Creating parent directory...");
            folder = getFolder();
        } catch (IOException e) {
            logger.error("Error when trying to create folder at Fs: {}", fsParent.getAbsolutePath());
        }
        
        File parentFolder = this.exportPath.getParentFile();
    	if (!parentFolder.exists()) {
    		logger.debug("Complete export path: {} do no exist, creating it.", exportPath);
    		parentFolder.mkdirs();
    	} else {
    		logger.debug("A job with the same name already exists, it will be overwritten, path: {}", this.exportPath);
    	}

        this.ready = true;
    }

    @Override
    public boolean isReady() {
        return this.ready;
    }

    @Override
    public void write() {

        if (!isReady()) {
            throw new IllegalStateException("The writer is not prepared to write.");
        }

        logger.info("Writing file: {} ", this.exportPath);
        try {
        	        	
            FileOutputStream stream = new FileOutputStream(this.exportPath);
            CmsExportJob.SingleItem jobSingle = (CmsExportJob.SingleItem) this.exportJob;
            jobSingle.getResultStream(stream);

            stream.close();

        } catch (FileNotFoundException e) {
            logger.error("Failed when trying to initialize a outputStream with path: {}, {}", this.exportPath, e.getMessage());
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.error("Failed to close stream", e.getMessage());
            throw new RuntimeException(e);
        }
    }


    protected File getFolder() throws IOException {
    	
    	String completeJobPath = fsParent.getPath().concat("/").concat(this.exportJob.getJobPath());
        exportPath = new File(completeJobPath);
    	
    	boolean mkdirs = exportPath.getParentFile().mkdirs();
    	if (mkdirs) {
    		logger.debug("Created missing directories {}", fsParent.getPath());
    	} else {
    		logger.debug("Parent folder {} already exists.", fsParent.getAbsolutePath());
    	}
    	
    	return exportPath; 
    }

    protected boolean mkdir(File dir) {
        boolean ok = dir.mkdir();
        return ok;
    }

    @Override
    public Path getExportPath() {
    	if (!isReady()) {
            throw new IllegalStateException("The writer is not prepared to write.");
        }
        return this.exportPath.toPath();
    }
}
