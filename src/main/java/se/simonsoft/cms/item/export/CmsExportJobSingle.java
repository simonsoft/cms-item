package se.simonsoft.cms.item.export;

import java.io.OutputStream;

public class CmsExportJobSingle extends CmsExportJob implements CmsExportJob.SingleItem {

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
