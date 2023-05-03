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
package se.simonsoft.cms.item.info;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class CmsCurrentUserBaseTest {

	class CmsCurrentUserBaseMock extends CmsCurrentUserBase {
		public CmsCurrentUserBaseMock(Set<String> roles) {
			super(roles);
		}

		@Override
		public String getUsername() {
			return null;
		}
	}

	@Test
	public void testSingleRole() {
		Set<String> assignedRoles = new HashSet<>();

		assignedRoles.add("CmsAdmin");
		CmsCurrentUserBaseMock cmsCurrentUserBaseMock = new CmsCurrentUserBaseMock(assignedRoles);

		Set<String> expectedRoles = new HashSet<>();
		assertFalse(cmsCurrentUserBaseMock.hasRole(expectedRoles));

		expectedRoles.add("CmsUserSuper");
		assertFalse(cmsCurrentUserBaseMock.hasRole(expectedRoles));

		expectedRoles.add("CmsUser");
		assertFalse(cmsCurrentUserBaseMock.hasRole(expectedRoles));

		expectedRoles.add("CmsAdmin");
		assertTrue(cmsCurrentUserBaseMock.hasRole(expectedRoles));
	}

	@Test
	public void testMultipleRoles() {
		Set<String> assignedRoles = new HashSet<>();

		assignedRoles.add("CmsAdmin");
		assignedRoles.add("CmsUserSuper");
		assignedRoles.add("CmsUser");
		CmsCurrentUserBaseMock cmsCurrentUserBaseMock = new CmsCurrentUserBaseMock(assignedRoles);

		Set<String> expectedRoles = new HashSet<>();
		assertFalse(cmsCurrentUserBaseMock.hasRole(expectedRoles));

		expectedRoles.add("CmsUserFoo");
		assertFalse(cmsCurrentUserBaseMock.hasRole(expectedRoles));

		expectedRoles.add("CmsUser");
		assertTrue(cmsCurrentUserBaseMock.hasRole(expectedRoles));

		expectedRoles.clear();
		expectedRoles.add("CmsAdmin");
		assertTrue(cmsCurrentUserBaseMock.hasRole(expectedRoles));

		expectedRoles.add("CmsUserSuper");
		assertTrue(cmsCurrentUserBaseMock.hasRole(expectedRoles));

		expectedRoles.add("CmsUserBar");
		assertTrue(cmsCurrentUserBaseMock.hasRole(expectedRoles));
	}

	@Test
	public void testWildcard() {
		Set<String> assignedRoles = new HashSet<>();
		CmsCurrentUserBaseMock cmsCurrentUserBaseMock = new CmsCurrentUserBaseMock(assignedRoles);

		Set<String> expectedRoles = new HashSet<>();
		expectedRoles.add("*");
		assertTrue(cmsCurrentUserBaseMock.hasRole(expectedRoles));

		assignedRoles.add("CmsAdmin");
		assignedRoles.add("CmsUserSuper");
		assignedRoles.add("CmsUser");
		cmsCurrentUserBaseMock = new CmsCurrentUserBaseMock(assignedRoles);
		assertTrue(cmsCurrentUserBaseMock.hasRole(expectedRoles));

		expectedRoles.add("CmsUserFoo");
		assertFalse(cmsCurrentUserBaseMock.hasRole(expectedRoles));
	}
}
