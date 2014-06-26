/**
 * Copyright (C) 2009-2013 Simonsoft Nordic AB
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
package se.simonsoft.cms.item.indexing;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import se.simonsoft.cms.item.CmsItemId;
import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.CmsRepository;
import se.simonsoft.cms.item.RepoRevision;
import se.simonsoft.cms.item.impl.CmsItemIdArg;

public class IdStrategyDefaultTest {

	@Test
	public void testRepository() {
		IdStrategy strategy = new IdStrategyDefault();
		CmsRepository repo = new CmsRepository("http://some.host:123/svn/repo1");
		RepoRevision rev = new RepoRevision(1, new Date());
		assertEquals("some.host:123/svn/repo1/a/b.txt@0000000001", strategy.getId(repo, rev, new CmsItemPath("/a/b.txt")));
		// Do we ever use the repository ID directly? //assertNotEquals("repository id must be distinguished from root item id",
		//		strategy.getIdRepository(repo), strategy.getIdHead(repo, null));
		assertTrue("repoid should be prefix to item ids", strategy.getId(repo, rev, new CmsItemPath("/a/b.txt"))
				.startsWith(strategy.getIdRepository(repo)));
		assertTrue("repoid should be prefix to commit ids", strategy.getIdCommit(repo, rev)
				.startsWith(strategy.getIdRepository(repo)));
		assertTrue("repoid should be prefix to info ids", strategy.getIdEntry(repo, "someRepositoryField")
				.startsWith(strategy.getIdRepository(repo)));
	}
	
	// Too many assmuptions made on the internal calls in IdStrategy. Independent impl would be better. //@Test
	public void testSubclass() {
		IdStrategy strategy = new IdStrategyDefault() {

			@Override
			public String getIdRepository(CmsRepository repository) {
				return "x-svn://" + repository.getHost() + repository.getParentPath() + "/" + repository.getName() + "^";
			}

			@Override
			protected String getRootPath() {
				return "/"; // logical ID definition
			}

			@Override
			protected String getPegSeparator() {
				return "?p=";
			}
			
		};
		
		CmsItemId doc1 = new CmsItemIdArg("x-svn://my.host:1234/svn/demo1^/vvab/xml/documents/900108.xml").withPegRev(136L);
		assertEquals("x-svn://my.host:1234/svn/demo1^", strategy.getIdRepository(doc1.getRepository()));
		assertEquals("x-svn://my.host:1234/svn/demo1^/vvab/xml/documents/900108.xml", strategy.getIdHead(doc1));
		assertEquals("x-svn://my.host:1234/svn/demo1^/vvab/xml/documents/900108.xml?p=136", strategy.getId(doc1, new RepoRevision(136, new Date())));
		assertNotEquals("repository id must be distinguished from root item id",
				strategy.getIdRepository(doc1.getRepository()), strategy.getIdHead(doc1.getRepository(), null));		
	}
	
	@Test
	public void testNonasciiPath() {
		IdStrategy strategy = new IdStrategyDefault();
		CmsRepository repo = new CmsRepository("http://some.host:123/svn/repo1");
		RepoRevision rev = new RepoRevision(1, new Date());
		
		assertEquals("some.host:123/svn/repo1/a%20b/c.txt@0000000001", strategy.getId(repo, rev, new CmsItemPath("/a b/c.txt")));
		
		assertEquals("some.host:123/svn/repo1/a/%3F.txt@0000000001", strategy.getId(repo, rev, new CmsItemPath("/a/?.txt"))); // quite possibly not a valid path
		
		assertEquals("some.host:123/svn/repo1/a@b/c.txt@0000000001", strategy.getId(repo, rev, new CmsItemPath("/a@b/c.txt")));
		
		assertEquals("some.host:123/svn/repo1/a+b/c.txt@0000000001", strategy.getId(repo, rev, new CmsItemPath("/a+b/c.txt"))); // plus is problematic when decoding
		
	}
	
	@Test
	public void testNonasciiRepository() {
		IdStrategy strategy = new IdStrategyDefault();
		CmsRepository repo = new CmsRepository("http://some.host:123/svn/repo%20space");
		RepoRevision rev = new RepoRevision(1, new Date());
		assertEquals("some.host:123/svn/repo%20space/a/b.txt@0000000001", strategy.getId(repo, rev, new CmsItemPath("/a/b.txt")));
		@SuppressWarnings("unused")
		CmsRepository repoFromPath = new CmsRepository("http://some.host:123", "/svn", "repo space");
		// behavior still undefined in CmsRepository //assertEquals("some.host:123/svn/repo%20space/a/b.txt@1", strategy.getId(repoFromPath, rev, new CmsItemPath("/a/b.txt")));
	}
	
	/**
	 * Tests zero revision as used in ranged queries in cms-reporting.
	 */
	@Test
	public void testZeroRevisionId() {
		
		IdStrategy strategy = new IdStrategyDefault();
		CmsRepository repo = new CmsRepository("http://some.host:123/svn/repo1");
		RepoRevision rev = RepoRevision.parse("0");
		assertEquals("some.host:123/svn/repo1/a/b.txt@0000000000", strategy.getId(repo, rev, new CmsItemPath("/a/b.txt")));
		
	}
	
	@Test
	public void testRevisionMax() {
		
		IdStrategy strategy = new IdStrategyDefault();
		assertEquals("9999999999", Long.toString(strategy.getRevisionMax()));
		
	}

}
