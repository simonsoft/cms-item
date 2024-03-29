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
package se.simonsoft.cms.item.encoding;

import static org.junit.Assert.*;

import org.junit.Test;

public class CmsItemURLEncoderTest {

	@Test
	public void testEncodeSvnkit256() {

		// Adapter invalid chars: :*?"<>|^+@#&
		String windowsInvalidChars = ":*?\"<>\\/|";
		String newlines = "\n\r\f";
		String invalidChars = newlines.concat(windowsInvalidChars);

		StringBuilder sb = new StringBuilder(300);

		for (int i = 1; i <= 256; i++) {
			char[] ch = Character.toChars(i);
			if (!invalidChars.contains(new String(ch))) {
				sb.append(ch);
			}
		}

		String str = sb.toString();

		// Svnkit encoding as of Svnkit 1.8.4.
		String svnKitExpected = "%01%02%03%04%05%06%07%08%09%0B%0E%0F%10%11%12%13%14%15%16%17%18%19%1A%1B%1C%1D%1E%1F%20!%23$%25&'()+,-.0123456789%3B=@ABCDEFGHIJKLMNOPQRSTUVWXYZ%5B%5D%5E_%60abcdefghijklmnopqrstuvwxyz%7B%7D~%7F%C2%80%C2%81%C2%82%C2%83%C2%84%C2%85%C2%86%C2%87%C2%88%C2%89%C2%8A%C2%8B%C2%8C%C2%8D%C2%8E%C2%8F%C2%90%C2%91%C2%92%C2%93%C2%94%C2%95%C2%96%C2%97%C2%98%C2%99%C2%9A%C2%9B%C2%9C%C2%9D%C2%9E%C2%9F%C2%A0%C2%A1%C2%A2%C2%A3%C2%A4%C2%A5%C2%A6%C2%A7%C2%A8%C2%A9%C2%AA%C2%AB%C2%AC%C2%AD%C2%AE%C2%AF%C2%B0%C2%B1%C2%B2%C2%B3%C2%B4%C2%B5%C2%B6%C2%B7%C2%B8%C2%B9%C2%BA%C2%BB%C2%BC%C2%BD%C2%BE%C2%BF%C3%80%C3%81%C3%82%C3%83%C3%84%C3%85%C3%86%C3%87%C3%88%C3%89%C3%8A%C3%8B%C3%8C%C3%8D%C3%8E%C3%8F%C3%90%C3%91%C3%92%C3%93%C3%94%C3%95%C3%96%C3%97%C3%98%C3%99%C3%9A%C3%9B%C3%9C%C3%9D%C3%9E%C3%9F%C3%A0%C3%A1%C3%A2%C3%A3%C3%A4%C3%A5%C3%A6%C3%A7%C3%A8%C3%A9%C3%AA%C3%AB%C3%AC%C3%AD%C3%AE%C3%AF%C3%B0%C3%B1%C3%B2%C3%B3%C3%B4%C3%B5%C3%B6%C3%B7%C3%B8%C3%B9%C3%BA%C3%BB%C3%BC%C3%BD%C3%BE%C3%BF%C4%80";

		CmsItemURLEncoder encoder = new CmsItemURLEncoder();

		assertEquals("ensure encoding is same as Svnkit", svnKitExpected, encoder.encode(str));

	}

	@Test
	public void testEncodeSvnkitMultibyteCharacters() {

		StringBuilder sb = new StringBuilder(64);
		// 2-bytes characters
		sb.append("αβγ");
		// 3-bytes characters
		sb.append("൦൪൬൮");
		// 4-bytes characters
		sb.append("😀😲🙂😕😠");
		String str = sb.toString();

		// Svnkit encoding as of Svnkit 1.10.4.
		String svnKitExpected = "%CE%B1%CE%B2%CE%B3%E0%B5%A6%E0%B5%AA%E0%B5%AC%E0%B5%AE%F0%9F%98%80%F0%9F%98%B2%F0%9F%99%82%F0%9F%98%95%F0%9F%98%A0";

		CmsItemURLEncoder encoder = new CmsItemURLEncoder();

		assertEquals("ensure encoding is same as Svnkit", svnKitExpected, encoder.encode(str));

	}
}
