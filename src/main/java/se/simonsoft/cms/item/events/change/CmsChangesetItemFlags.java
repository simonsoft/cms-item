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
package se.simonsoft.cms.item.events.change;

public interface CmsChangesetItemFlags {

	boolean isFile();

	boolean isFolder();

	/**
	 * This is true for {@link #isMove()}s too.
	 * Only true for the new item, not the source, see {@link #isCopySource()}.
	 * @return true if the entry is a copy to a new location
	 */
	boolean isCopy();

	/**
	 * For some copy operations, notably all moves, the source is also modified in the changeset.
	 * For copies with untouched source there might be no copy source in the same changeset.
	 * There might be multiple copy targets of the same source so this method can not return a path,
	 * and even if we returned all paths the information is a bit useless when some sources are never reported.
	 * @return true if there is, in the same commit, an copy with this item as {@link #getCopyFromPath()}
	 */
	boolean isCopySource();	
	
	/**
	 * @return true if the item was added, excluding replace
	 */
	boolean isAdd();

	/**
	 * Replacements should be a single change entry.
	 * Moves ({@link #isMove()} can also be replacements.
	 * 
	 * A replace is never an {@link #isDelete()} or {@link #isAdd()}
	 * because that could be surprising for an item that exists both before and after.
	 * 
	 * For derived items the replace status is uncertain, unless we implement comparison of trees,
	 * so the method should always return false for derived.
	 * 
	 * @return true if the item was replaced, i.e. not a diff but completely changed
	 */
	boolean isReplace();

	/**
	 * Flags true for deletions if there is one or more additions in the same commit
	 * with the path AND revision as copy-from. Odd case with historical copy-from revision
	 * should return false but may not be handled.
	 * 
	 * (relaxation: it is ok for backend to not match copy-from revision because
	 *              it requires complex lookup and is unlikely to mismatch for moves)
	 * 
	 * Obviously flags true for move targets. Those have {@link #getCopyFromPath()}.
	 * 
	 * A reason for not making move atomic is that a deleted file may have multiple copy destinations,
	 * which are indistinguishable unless server knows about atomic moves.
	 * 
	 * In an event model for repository changes it is interesting to note that the order
	 * in which changes are reported is undefined, or at least not respecting copy-from info
	 * so a move might need to be modelled using two changeset items.
	 * 
	 * @return true if the path is a copy from anywhere any revision, true also if it is a deletion that looks like a move
	 */
	boolean isMove();

	/**
	 * @return true if the item was deleted, excluding replace
	 */
	boolean isDelete();

	/**
	 * @return true if content was actually modified,
	 *  true for copies if destination is not identical to source,
	 *  not true for add and delete,
	 *  never true for replace because detection would be tricky to implement
	 */
	boolean isContentModified();

	/**
	 * @return true if content was added, copied, replaced, modified or deleted
	 */
	boolean isContent();

	/**
	 * @return true if the property set was actually modified,
	 *  true for copies only if destination props differ from source,
	 *  not true for add and delete,
	 *  never true for replace because detection would be tricky to implement
	 */
	boolean isPropertiesModified();

	/**
	 * @return true on anything that might mean a different set of properties at the path
	 */
	boolean isProperties();

}