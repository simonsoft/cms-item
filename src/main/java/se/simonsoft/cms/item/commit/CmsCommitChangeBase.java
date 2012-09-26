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
package se.simonsoft.cms.item.commit;

/**
 * 
 * @deprecated not really needed anymore, toString can use class name and path.
 */
public abstract class CmsCommitChangeBase implements CmsCommitChange {

	protected enum StatContent { _, M, A, R, D }
	protected enum StatProps { _, M }

	protected abstract StatContent getStatContents();
	
	protected abstract StatProps getStatProps();
	
	protected abstract boolean isCopy();

	@Override
	public String toString() {
		return "" + getStatContents() + getStatProps() + (isCopy() ? "+" : "_") + ":" + getPath();
	}
	
}
