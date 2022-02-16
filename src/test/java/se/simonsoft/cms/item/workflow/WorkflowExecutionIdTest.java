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
