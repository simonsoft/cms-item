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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.simonsoft.cms.item.export.CmsExportJob;
import se.simonsoft.cms.item.export.CmsExportWriter;

public class CmsExportWriterFsSingle implements CmsExportWriter, CmsExportWriter.LocalFileSystem {

    private Path fsParent;
    private CmsExportJob exportJob;
    private boolean ready = false;
    
    private static final Logger logger = LoggerFactory.getLogger(CmsExportWriterFsSingle.class);

    public CmsExportWriterFsSingle(File fsParent) {
    	if (fsParent == null) {
			throw new IllegalArgumentException("The fsParent must not be null.");
		}
		
		this.fsParent = Paths.get(fsParent.getAbsolutePath());
		
    	if (!Files.isWritable(this.fsParent)) {
    		throw new RuntimeException("Can not write to directory: " + fsParent.toString());
    	}
        
    }

    @Override
    public void prepare(CmsExportJob job) {
    	
    	logger.debug("Preparing writer");
    	
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

        logger.debug("Creating parent directory for the export job...");
        createFolder();
        
        Path completePath = getCompletePath(job);
        
        if (Files.exists(completePath)) {
    		logger.warn("File: {} already exists it will be overwritten", completePath.toString());
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
        
        final Path completePath = getCompletePath(exportJob);
        
        if (completePath == null) {
        	throw new IllegalStateException("Export path must not be null. Writer has to be prepared.");
        }

        logger.info("Writing file: {} ", completePath.toString());
        try {
        	        	
            FileOutputStream stream = new FileOutputStream(completePath.toFile());
            CmsExportJob.SingleItem jobSingle = (CmsExportJob.SingleItem) this.exportJob;
            jobSingle.getResultStream(stream);

            stream.close();

        } catch (FileNotFoundException e) {
            logger.error("Failed when trying to initialize a outputStream with path: {}, {}", completePath, e.getMessage());
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.error("Failed to close stream", e.getMessage());
            throw new RuntimeException(e);
        }
    }


    protected void createFolder() {
    	
    	final Path parentPath = getCompletePath(exportJob).getParent();
    	
    	try {
			Files.createDirectories(parentPath);
		} catch (IOException e) {
			logger.error("Could not create directories in: {}, even though it passed isWritable check.", fsParent.toString());
			throw new RuntimeException("Could not create directories.",e);
		}
    }
    
	private Path getCompletePath(CmsExportJob job) {
		String jobPath = job.getJobPath().replaceAll("/", File.separator);
		Path completePath = fsParent.resolve(jobPath);
		return completePath;
	}

    @Override
    public Path getExportPath() {
    	if (!isReady()) {
            throw new IllegalStateException("The writer is not prepared to write.");
        }
        return getCompletePath(exportJob);
    }
}
