/**
 * Copyright (C) 2009-2012 Simonsoft Nordic AB
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
package se.simonsoft.cms.item.structure;

/**
 * Used to mark intent when calling this low level API,
 * for future refactoring and addition of rules.
 * Identical to {@link CmsAreaPattern}.
 */
public class CmsTranslationsPattern extends CmsAreaPattern {

	/**
	 * Translation pattern to use if not configured in the current repository.
	 */
	public static final String DEFAULT_TRANSLATIONS_PATTERN = "/*/lang";
	
	public CmsTranslationsPattern(String configValue)
			throws IllegalArgumentException {
		super(configValue);
	}

	/**
	 * @return The graphics translation path that corresponds to this config.
	 */
	public CmsTranslationsPattern getGraphicsPattern() {
		if (this.isAreaRelative()) {
			throw new UnsupportedOperationException("The pattern '" + this
					+ "' is already relative and can't be converted to a graphics translation pattern");
		}
		return new CmsTranslationsPattern(getAreaName());
	}

}
