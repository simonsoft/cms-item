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
package se.simonsoft.cms.item.commit;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicInteger;

import jakarta.inject.Provider;

import org.junit.Test;

import se.simonsoft.cms.item.CmsItemPath;

public class FileContentProviderTest {

	@Test
	public void fileAddDefersOpeningContent() throws IOException {
		AtomicInteger calls = new AtomicInteger();
		Provider<InputStream> provider = () -> {
			calls.incrementAndGet();
			return new ByteArrayInputStream(new byte[] {42});
		};

		FileAdd item = new FileAdd(new CmsItemPath("/file.xml"), provider);
		assertEquals(0, calls.get());

		try (InputStream content = item.getWorkingFile()) {
			assertEquals(42, content.read());
		}
		assertEquals(1, calls.get());
	}

	@Test
	public void fileModificationLockedDefersOpeningContent() throws IOException {
		AtomicInteger calls = new AtomicInteger();
		Provider<InputStream> provider = () -> {
			calls.incrementAndGet();
			return new ByteArrayInputStream(new byte[] {42});
		};

		FileModificationLocked item = new FileModificationLocked(new CmsItemPath("/file.xml"), provider);
		assertEquals(0, calls.get());

		try (InputStream content = item.getWorkingFile()) {
			assertEquals(42, content.read());
		}
		assertEquals(1, calls.get());
	}
}
