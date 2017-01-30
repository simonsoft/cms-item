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

import java.io.OutputStream;

public class CmsExportJobSingle extends CmsExportJob implements CmsExportJob.SingleItem {
	
	public CmsExportJobSingle(CmsExportPrefix jobPrefix, String jobName, String jobExtension) {
		super(jobPrefix, jobName, jobExtension);
	}

    @Override
    public void getResultStream(OutputStream out) {
        getExportItems().get(0).getResultStream(out);
    }

    public void addExportItem(CmsExportItem exportItem) {

        if (getExportItems().size() > 1) {
            throw new IllegalArgumentException(this.getClass().getName()
                    .concat(" The maximum number of items allowed is already reached: " + getExportItems().size()));
        }

        super.addExportItem(exportItem);
    }
}
