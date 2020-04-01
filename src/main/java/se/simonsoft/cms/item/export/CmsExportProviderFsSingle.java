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

import javax.inject.Inject;
import javax.inject.Named;

public class CmsExportProviderFsSingle implements CmsExportProvider {
	
	private File fsParent;
	
	@Inject
	public CmsExportProviderFsSingle(@Named("config:se.simonsoft.cms.export.fs.parent") File fsParent) {
		
		if (fsParent == null) {
			throw new IllegalStateException("Filesystem export is not configured");
		}
		
		this.fsParent = fsParent;
	}

	@Override
	public CmsExportReader getReader() {
		return new CmsExportReaderFsSingle(fsParent);
	}

	@Override
	public CmsExportWriter getWriter() {
		return new CmsExportWriterFsSingle(fsParent);
	}

}
