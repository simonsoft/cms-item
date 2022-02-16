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
package se.simonsoft.cms.item.workflow;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class WorkflowExecutionIdTest {

	private String arn = "arn:aws:states:eu-west-1:123123123123:execution:cms-demo-dev-publish-v1:5eff28e6-4796-4678-8e6e-9d6e8a977a6b";
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testNull() {
		@SuppressWarnings("unused")
		WorkflowExecutionId id = new WorkflowExecutionId("");
		fail();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testEmpty() {
		@SuppressWarnings("unused")
		WorkflowExecutionId id = new WorkflowExecutionId("");
		fail();
	}

	@Test
	public void testFullArn() {
		WorkflowExecutionId id = new WorkflowExecutionId(arn);
		assertEquals(arn, id.toString());
		assertTrue(id.hasUuid());
		assertEquals("5eff28e6-4796-4678-8e6e-9d6e8a977a6b", id.getUuid());
	}

	@Test
	public void testUuid() {
		WorkflowExecutionId id = new WorkflowExecutionId("5eff28e6-4796-4678-8e6e-9d6e8a977a6b");
		assertEquals("5eff28e6-4796-4678-8e6e-9d6e8a977a6b", id.toString());
		assertTrue(id.hasUuid());
		assertEquals("5eff28e6-4796-4678-8e6e-9d6e8a977a6b", id.getUuid());
	}	
	
	@Test
	public void testUuidInvalid() {
		WorkflowExecutionId id = new WorkflowExecutionId("5eff28e6-4796-4678-8e6e0-9d6e8a977a6b");
		assertEquals("return invalid uuid transparently in toString()", "5eff28e6-4796-4678-8e6e0-9d6e8a977a6b", id.toString());
		assertFalse("no valid uuid" ,id.hasUuid());
		assertNull(id.getUuid());
	}	
	
}
