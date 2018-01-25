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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CmsExportFsReaderSingle implements CmsExportReader {
	
	private CmsImportJob importJob;
	private Path importPath;
	
	private final File fsParent;
	
	private static final Logger logger = LoggerFactory.getLogger(CmsExportFsReaderSingle.class);

	public CmsExportFsReaderSingle(File fsParent) {
		this.fsParent = fsParent;
	}

	@Override
	public void prepare(CmsImportJob job) {
		logger.debug("Preparing reader");
		
		if (importJob != null) {
			throw new IllegalStateException("Reader already consumed, initialize a new reader for each job.");
		}
		
		if (!(job instanceof CmsImportJobSingle)) {
			throw new IllegalArgumentException("Single readers expects CmsimportJobs that is of type CmsImportJobSingle");
		}
		
		createImportPath(job);
		
		this.importJob = job;
		
	}

	private void createImportPath(CmsImportJob job) {
		
		Path fsParentPath = Paths.get(fsParent.getAbsolutePath());
		StringBuilder sb = new StringBuilder();
		sb.append(fsParentPath.toString());
		
		if (!fsParentPath.endsWith(File.separator)) {
			sb.append(File.separator);
		}
		sb.append(job.getJobPath());
		
		Path completeImportPath = Paths.get(sb.toString());
		
		if (!Files.exists(completeImportPath)) {
			throw new IllegalStateException("Provided import path: " + completeImportPath + ", do not exist");
		}
		
		if (!Files.isReadable(completeImportPath)) {
			throw new IllegalStateException("Provided import path: " + completeImportPath + ", exists but is not readable.");
		}
		
		importPath = completeImportPath;
	}

	@Override
	public Map<CmsExportMetaKey, String> getMeta() {
		logger.warn("Reader do not support meta data returning null");
		return null;
	}

	@Override
	public InputStream getContents() {
		
		if (this.importPath == null) {
			throw new IllegalStateException("Reader is missing a valid import path, the reader has to be prepared");
		}
		
		InputStream is = null;
		try {
			is = Files.newInputStream(this.importPath);
		} catch (IOException e) {
			throw new RuntimeException("Could not read file at path: " + importPath, e);
		}
		
		return is;
	}

	@Override
	public void getContents(OutputStream receiver) throws IOException {
		byte[] buffer = new byte[1024];
		int length;
		final InputStream contents = getContents();
		while ((length = contents.read(buffer)) != -1) {
			receiver.write(buffer, 0, length);
		}

		contents.close();
	}

}
