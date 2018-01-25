package se.simonsoft.cms.item.export;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Named;

public class CmsExportProviderFsSingle implements CmsExportProvider {
	
	private File fsParent;
	
	@Inject
	public CmsExportProviderFsSingle(@Named("config:se.simonsoft.cms.export.fs.parent") File fsParent) {
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
