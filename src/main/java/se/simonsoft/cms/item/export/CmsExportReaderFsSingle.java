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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CmsExportReaderFsSingle implements CmsExportReader {
	
	private final Path fsParent;
	
	private CmsImportJob importJob;
	private boolean ready = false;
	
	private static final Logger logger = LoggerFactory.getLogger(CmsExportReaderFsSingle.class);

	public CmsExportReaderFsSingle(File fsParent) {
		if (fsParent == null) {
			throw new IllegalArgumentException("The fsParent must not be null.");
		}
		
		Path fsParentPath = Paths.get(fsParent.getAbsolutePath());
		
		if (!Files.exists(fsParentPath)) {
			throw new IllegalStateException("The fsParent does not exist on file system");
		}
		
		if (!Files.isReadable(fsParentPath)) {
			throw new IllegalStateException("The fsParent exists but is not readable.");
		}
		
		this.fsParent = fsParentPath;
	}

	@Override
	public void prepare(CmsImportJob job) {
		logger.debug("Preparing reader...");
		
		if (importJob != null) {
			throw new IllegalStateException("Reader already consumed, initialize a new reader for each job.");
		}

		if (job == null) {
			throw new IllegalArgumentException("Can not prepare a null job.");
		}
		
		this.importJob = job;
		
		Path completePath = getCompletePath(job);
		if (!Files.exists(completePath)) {
			throw new IllegalStateException("Import path does not exist: " + completePath);
		}
		
		if (!Files.isReadable(completePath)) {
			throw new IllegalStateException("Import path does exist but is not readable: " + completePath);
		}
		
		this.ready = true;
		
		logger.debug("Reader is prepared.");
		
	}

	@Override
	public Map<CmsExportMetaKey, String> getMeta() {
		logger.warn("Reader does not support metadata, returning null");
		return null;
	}

	@Override
	public Map<CmsExportTagKey, CmsExportTagValue> getTagging() {
		logger.warn("Reader does not support tagging, returning null");
		return null;
	}

	@Override
	public InputStream getContents() {
		
		if (!isReady()) {
			throw new IllegalStateException("CmsExportFsReaderSingle has to be prepared before getting content.");
		}
		
		InputStream is = null;
		Path completePath = getCompletePath(importJob);
		try {
			is = Files.newInputStream(completePath);
		} catch (IOException e) {
			throw new RuntimeException("Could not read file at path: " + completePath, e);
		}
		
		return is;
	}

	@Override
	public void getContents(OutputStream receiver) throws IOException {
		byte[] buffer = new byte[1024 * 32];
		int length;
		final InputStream contents = getContents();
		while ((length = contents.read(buffer)) != -1) {
			receiver.write(buffer, 0, length);
		}

		contents.close();
	}
	
	private boolean isReady() {
		return this.ready;
	}
	
	private Path getCompletePath(CmsImportJob job) {
		String jobPath = job.getJobPath().replace("/", File.separator);
		Path completePath = fsParent.resolve(jobPath);
		return completePath;
	}
}
