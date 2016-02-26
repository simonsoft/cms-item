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

/**
 * Checks if the suggested name is valid with name, folder counter and file counter
 */
public class CmsItemNamePattern {

    private String prefix;
    private String fileCounter;
    private String pattern = "^[a-zA-Z0-9_-]{1,}$";
    private static String FILE_COUNTER_PATTERN= "^[#]{3,3}$";
    private static String FOLDER_COUNTER_PATTERN = "^[#]{1,}$";
    private String folderCounter;

    /**
     * @param name must contain at least one char(^[a-zA-Z0-9_]{1,}$) for the name,
     *             one # for the folder counter and
     *             three # for the file counter.
     *             e.g name|folderCounter|fileCounter
     *             funny_name#####: name will be funny_name max folder count is 99 max file count is 999 funny_name07666.jpeg
     */
    CmsItemNamePattern(String name) {

        if (name == null || name == "") {
            throw new IllegalArgumentException("The name pattern can't be null or empty");
        }

        setPrefixAndCounters(name);
    }

    private void setPrefixAndCounters(String namePattern) {

        int firstHash = namePattern.indexOf("#");
        if (firstHash == -1) {
            throw new IllegalArgumentException("Counter pattern must have be at least 4 # at the end of the pattern");
        }

        setPrefix(namePattern.substring(0, firstHash));
        setFolderCounter(namePattern.substring(firstHash, namePattern.length() - 3));
        setFileCounter(namePattern.substring(namePattern.length() - 3));

    }

    /**
     *
     * @return returns the name without counters
     */
    public String getPrefix() {
        return prefix;
    }

    private void setPrefix(String prefix) {

        if (!prefix.matches(pattern)) {
            throw new IllegalArgumentException("The name must be alphanumeric and at least one char long");
        }

        this.prefix = prefix;
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

    /**
     * @return File counter which always is the last 3 hashes
     */
    public String getFileCounter() {
        return this.fileCounter;
    }
    /**
     * @return folder counter which always is the hashes between name and file counter
     */
    public String getFolderCounter() {
        return this.folderCounter;
    }

    /**
     * Converts hashes in folder counter.
     * @return the folder counter hashes converted to zeros as an String
     */
    public String getFolderCounterAsZeros () {

        int length = folderCounter.length();
        String asZeros = "";
        for (int i = 0; i < length; i++) {
            asZeros = asZeros + "0";
        }
        return asZeros;
    }

    public String getFileCounterAsZeros () {

        int length = fileCounter.length();
        String asZeros = "";
        for (int i = 0; i < length; i++) {
            asZeros = asZeros + "0";
        }
        return asZeros;
    }


    /**
     * Concat's name, folder counter and file counter as zeros
     * @return String name with counters as zeros
     */
    public String getFullFolderName() {
        return getPrefix().concat(getFolderCounterAsZeros()).concat(getFileCounterAsZeros());
    }

}
