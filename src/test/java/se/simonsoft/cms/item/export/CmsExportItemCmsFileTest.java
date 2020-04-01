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

import org.junit.Test;
import se.simonsoft.cms.item.CmsItem;
import se.simonsoft.cms.item.CmsItemId;
import se.simonsoft.cms.item.CmsItemKind;
import se.simonsoft.cms.item.impl.CmsItemIdArg;
import se.simonsoft.cms.item.properties.CmsItemPropertiesMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CmsExportItemCmsFileTest {

    private CmsItem translation;

    @Test
    public void instantiatePrepareReadyResultTest() {

        setUpValidCmsItem();

        CmsExportItemCmsFile exportItem = new CmsExportItemCmsFile(translation, new CmsExportPath("/path/chapter1.xml"));

        exportItem.prepare();
        assertTrue(exportItem.isReady());
        assertTrue(exportItem.getResultPath() instanceof CmsExportPath);
        assertEquals("/path/chapter1.xml", exportItem.getResultPath().getPath());
    }

    @Test (expected = IllegalArgumentException.class)
    public void nullShouldThrowError() {
        new CmsExportItemCmsFile(null, null);
    }

    @Test
    public void itemHasToBeAFile() {

        setUpValidCmsItemAsFolder();

        CmsExportItemCmsFile exportItem = new CmsExportItemCmsFile(translation, new CmsExportPath("/some/path"));

        try {
        exportItem.prepare();

        } catch (IllegalArgumentException e) {
            assertEquals("Item has to be a file. x-svn:///svn/demo1^/vvab/xml/documents", e.getMessage());
        }
    }

    @Test
    public void cantBePreparedTwice() {

        setUpValidCmsItem();

        CmsExportItemCmsFile exportItem = new CmsExportItemCmsFile(translation, new CmsExportPath("/some/path"));
        exportItem.prepare();
        assertTrue(exportItem.isReady());

        try {
            exportItem.prepare();
        } catch (IllegalStateException e) {
            assertEquals("Item: x-svn:///svn/demo1^/vvab/xml/documents/chapter1.xml is already prepared for export", e.getMessage());
        }

    }

    @Test
    public void getResultStreamWithoutPreperation() {
        setUpValidCmsItem();

        CmsExportItemCmsFile exportItem = new CmsExportItemCmsFile(translation, new CmsExportPath("/some/path"));

        try {
            exportItem.getResultStream(null);
        } catch (IllegalStateException e) {
            assertEquals("Export item: x-svn:///svn/demo1^/vvab/xml/documents/chapter1.xml, is not ready for export", e.getMessage());
        }
    }


    private void setUpCmsItemNotTranslation() {

        translation = mock(CmsItem.class);
        when(translation.getKind()).thenReturn(CmsItemKind.File);

        CmsItemPropertiesMap props = new CmsItemPropertiesMap();
        when(translation.getProperties()).thenReturn(props);

        CmsItemId itemId = new CmsItemIdArg("x-svn:///svn/demo1^/vvab/xml/documents/chapter1.xml");
        when(translation.getId()).thenReturn(itemId);
    }

    public void setUpValidCmsItemAsFolder() {

        translation = mock(CmsItem.class);
        when(translation.getKind()).thenReturn(CmsItemKind.Folder);

        CmsItemPropertiesMap props = new CmsItemPropertiesMap();
        props.and("abx:TranslationLocale", "se_SV");
        when(translation.getProperties()).thenReturn(props);

        CmsItemId itemId = new CmsItemIdArg("x-svn:///svn/demo1^/vvab/xml/documents/");
        when(translation.getId()).thenReturn(itemId);
    }

    public void setUpValidCmsItem() {

        translation = mock(CmsItem.class);
        when(translation.getKind()).thenReturn(CmsItemKind.File);

        CmsItemPropertiesMap props = new CmsItemPropertiesMap();
        props.and("abx:TranslationLocale", "se_SV");
        when(translation.getProperties()).thenReturn(props);

        CmsItemId itemId = new CmsItemIdArg("x-svn:///svn/demo1^/vvab/xml/documents/chapter1.xml");
        when(translation.getId()).thenReturn(itemId);


    }
}
