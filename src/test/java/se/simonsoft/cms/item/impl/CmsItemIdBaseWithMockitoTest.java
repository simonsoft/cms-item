package se.simonsoft.cms.item.impl;

import org.junit.Ignore;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class CmsItemIdBaseWithMockitoTest {

	@Test
	@Ignore // there is an issue with CmsItemIdBase and mockito but mocking concrete classes is a bad idea anyway
	public void testGetRepository() {
		CmsItemIdArg id = mock(CmsItemIdArg.class);
		id.getRepository();
		when(id.withRelPath(null)).thenReturn(null);
		id.getRepository();
	}
	
}
