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
package se.simonsoft.cms.item.naming;

import se.simonsoft.cms.item.CmsItemPath;

public interface CmsItemNaming {

    /**
     * increments by 1 based on previous folder and files in sent in folder
     * @param folder which is parent to the folders of the folders that contains the files /folder/someFolder001/some_file000.txt
     * @param namePattern the name pattern you want to use, checks that there are no illegal chars.
     * @param extension extension of the file.
     * @return a complete CmsItemPath pointing at the file with the name incremented by 1.
     */
    CmsItemPath getItemPath(CmsItemPath folder, CmsItemNamePattern namePattern, String extension);
}
