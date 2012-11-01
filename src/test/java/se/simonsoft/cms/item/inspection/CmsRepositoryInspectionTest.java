package se.simonsoft.cms.item.inspection;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

public class CmsRepositoryInspectionTest {

	@Test
	public void testWihtoutHost() {
		File p = new File("/tmp/repo");
		CmsRepositoryInspection repo = new CmsRepositoryInspection("/svn", "repo", p);
		assertEquals(p, repo.getAdminPath());
	}

}
