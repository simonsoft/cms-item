package se.simonsoft.cms.item.export;

import org.junit.Test;

import static junit.framework.Assert.fail;
import static junit.framework.TestCase.assertEquals;

public class CmsExportMetaKeyTest {


    @Test
    public void testEmptyKeyShouldThrow() {

        try {
            new CmsExportMetaKey("");
            fail("Should fail");
        } catch (IllegalArgumentException e) {
            assertEquals("Not a valid MetaKey: ", e.getMessage());
        }

    }

    @Test
    public void testNullKeyShouldThrow() {

        try {
            new CmsExportMetaKey(null);
            fail("Should fail");
        } catch (IllegalArgumentException e) {
            assertEquals("MetaKey must not be null.", e.getMessage());
        }

    }

    @Test
    public void testNoSpecialCharsExceptDash() {

        try {
            new CmsExportMetaKey("fail*");
            fail("Should fail");
        } catch (IllegalArgumentException e) {
            assertEquals("Not a valid MetaKey: fail*", e.getMessage());
        }

        try {
            new CmsExportMetaKey("fail&");
            fail("Should fail");
        } catch (IllegalArgumentException e) {
            assertEquals("Not a valid MetaKey: fail&", e.getMessage());
        }

        try {
            new CmsExportMetaKey("fail%");
            fail("Should fail");
        } catch (IllegalArgumentException e) {
            assertEquals("Not a valid MetaKey: fail%", e.getMessage());
        }

    }

    @Test
    public void testAlphaNumericsWithDashShouldPass() {
        CmsExportMetaKey cmsExportMetaKey = new CmsExportMetaKey("cms-pass");
        assertEquals("cms-pass", cmsExportMetaKey.toString());

    }
}
