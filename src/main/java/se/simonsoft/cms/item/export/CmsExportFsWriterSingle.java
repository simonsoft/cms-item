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

public class CmsExportFsWriterSingle implements CmsExportWriter, CmsExportWriter.LocalFileSystem {

    private File fsParent;
    private CmsExportJob exportJob;
    private boolean ready = false;
    private Path exportPath;
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

        logger.debug("Creating parent directory for the export job...");
        createFolder();
        
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
        
        if (this.exportPath == null) {
        	throw new IllegalStateException("Export path must not be null. Writer has to be prepared.");
        }

        logger.info("Writing file: {} ", this.exportPath);
        try {
        	        	
            FileOutputStream stream = new FileOutputStream(this.exportPath.toFile());
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


    protected void createFolder() {
    	
    	final String pathStr = fsParent.getPath().concat(File.separator).concat(this.exportJob.getJobPath());
    	final Path completePath = Paths.get(pathStr);
    	final Path parentPath = completePath.getParent();
    	
    	if (Files.exists(completePath)) {
    		logger.warn("File with name: {} at path: {} already exists, it will be overwritten", completePath.getFileName(), parentPath.toString());
    	}
    	
    	boolean writable = Files.isWritable(Paths.get(fsParent.getAbsolutePath()));
    	if (!writable) {
    		throw new RuntimeException("Can not write to directory: " + fsParent.getAbsolutePath());
    	}
    	
    	try {
			Files.createDirectories(parentPath);
		} catch (IOException e) {
			logger.error("Could not create directories in: {}, even though it passed isWritable check.", fsParent.getAbsolutePath());
			throw new RuntimeException("Could not create directories.",e);
		}
    	
    	this.exportPath = completePath;
    }

    @Override
    public Path getExportPath() {
    	if (!isReady()) {
            throw new IllegalStateException("The writer is not prepared to write.");
        }
        return this.exportPath;
    }
}
