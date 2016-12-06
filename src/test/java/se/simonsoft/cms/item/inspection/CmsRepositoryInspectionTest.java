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
package se.simonsoft.cms.item.inspection;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import se.simonsoft.cms.item.CmsRepository;

@SuppressWarnings({"deprecation", "serial"}) // tests to be moved to backend-svnkit
public class CmsRepositoryInspectionTest {

	@Test
	public void testWihtoutHost() {
		File p = new File("/tmp/repo");
		CmsRepositoryInspection repo = new CmsRepositoryInspection("/svn", "repo", p) {};
		assertEquals(p, repo.getAdminPath());
	}
	
	@Test
	public void testGetPublic() {
		File p = new File("/tmp/repo");
		CmsRepositoryInspection r1 = new CmsRepositoryInspection("http://host:1234/svn/repo", p) {};
		CmsRepository r1p = r1.getPublic();
		assertFalse(r1p instanceof CmsRepositoryInspection);
		assertEquals(new CmsRepository("http://host:1234/svn/repo"), r1p);
		
		CmsRepositoryInspection repo = new CmsRepositoryInspection("/svn", "repo", p) {};
		try {
			repo.getPublic();
		} catch (IllegalStateException e) {
			// expected
		}
	}
	
	@Test
	public void testFromHostname() {
		File p = new File("/tmp/repo2");
		CmsRepositoryInspection r1 = new CmsRepositoryInspection("https", "a.host:222", "/parent", "reponame", p) {};
		assertEquals("https://a.host:222/parent/reponame", r1.getUrl());
	}

}
