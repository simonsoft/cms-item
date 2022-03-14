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

import java.net.URL;
import java.time.Duration;

public interface CmsExportUrlPresigner {

	// CmsExportImportJob.getJobPath() is relative to any other prefixes (CmsExportPrefix, CloudId)
	// The CmsExportUrlPresigner implementation must originate from the same CmsExportProvider (or identical config) as the Reader/Writer.
	
	public URL getUrl(CmsExportImportJob job);
	
	public URL getPresignedGet(CmsExportImportJob job, Duration duration);
	
	public URL getPresignedPut(CmsExportImportJob job, Duration duration);
	
}
