/**
 * Copyright (C) 2009-2013 Simonsoft Nordic AB
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package se.simonsoft.cms.item.naming;

/**
 * Created by jonand on 17/02/16.
 */
public class CmsItemNamePattern {

    private String name;
    private String fileCounter;
    private String pattern = "^[a-zA-Z0-9_]{1,}$";
    private static String FILE_COUNTER_PATTERN= "^[#]{3,3}$";
    private static String FOLDER_COUNTER_PATTERN = "^[#]{1,}$";
    private String folderCounter;

    CmsItemNamePattern(String name) {

        if (name == null || name == "") {
            throw new IllegalArgumentException("The name pattern can't be null or empty");
        }

        setNameAndCounters(name);
    }

    public void setNameAndCounters(String namePattern) {

        int firstHash = namePattern.indexOf("#");
        if (firstHash == -1) {
            throw new IllegalArgumentException("Counter pattern must have be at least 4 # at the end of the pattern");
        }

        setName(namePattern.substring(0, firstHash));
        setFolderCounter(namePattern.substring(firstHash, namePattern.length() - 3));
        setFileCounter(namePattern.substring(namePattern.length() - 3));

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {

        if (!name.matches(pattern)) {
            throw new IllegalArgumentException("The name must be alphanumeric and at least one char long");
        }

        this.name = name;
    }

    private void setFileCounter(String fileCounter) {

        if (!fileCounter.matches(FILE_COUNTER_PATTERN)) {
            throw new IllegalArgumentException("File counter must match: " + FILE_COUNTER_PATTERN);
        }

        this.fileCounter = fileCounter;
    }

    private void setFolderCounter(String folderCounter) {

        if (!folderCounter.matches(FOLDER_COUNTER_PATTERN)) {
            throw new IllegalArgumentException("Folder counter must match: " + FOLDER_COUNTER_PATTERN);
        }
        this.folderCounter = folderCounter;
    }


    public String getFileCounter() {
        return this.fileCounter;
    }

    public String getFolderCounter() {
        return this.folderCounter;
    }

    public String getFolderCounterAsZeros () {

        int length = folderCounter.length();
        String asZeros = "";
        for (int i = 0; i < length; i++) {
            asZeros = asZeros + "0";
        }
        return asZeros;
    }

    public String getFullFolderName() {
        return getName() + getFolderCounterAsZeros();
    }

}
