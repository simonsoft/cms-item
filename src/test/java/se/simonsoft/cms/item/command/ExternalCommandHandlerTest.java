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
package se.simonsoft.cms.item.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import se.simonsoft.cms.item.CmsItemId;

public class ExternalCommandHandlerTest {

	private ExternalCommandHandler<String> handler = new ExternalCommandHandler<String>() {
		@Override
		public String handleExternalCommand(CmsItemId item, String arguments) {
			return "handled " + arguments;
		}
		@Override
		public Class<String> getArgumentsClass() {
			return String.class;
		}
	};
	
	@Test
	public void testContextDefaultDelegates() {
		assertEquals("handled a", handler.handleExternalCommand(null, "a", new ExternalCommandContext("token")));
		assertEquals("handled b", handler.handleExternalCommand(null, "b", null));
	}
	
	@Test
	public void testCommandContext() throws Exception {
		ExternalCommand command = new ExternalCommand(null, "action", "a", new ExternalCommandContext("token"));
		assertEquals("token", command.getContext().getTaskToken());
		assertEquals("a", command.getArgs(String.class));
		
		assertNull(new ExternalCommand(null, "action", "a").getContext());
	}
}
