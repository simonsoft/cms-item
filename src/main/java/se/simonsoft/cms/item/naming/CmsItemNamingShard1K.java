/**
 * Copyright (C) 2009-2016 Simonsoft Nordic AB
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.simonsoft.cms.item.CmsItemId;
import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.CmsRepository;
import se.simonsoft.cms.item.info.CmsItemLookup;
import se.simonsoft.cms.item.properties.CmsItemProperties;

import javax.inject.Inject;
import java.util.*;

/**
 * Shard1k will suggest new folder and file names with an count based on previous folders and files.
 */
public class CmsItemNamingShard1K implements CmsItemNaming {

    private CmsRepository repository;
    private CmsItemLookup lookup;
    private CmsItemNamePattern namePattern;
    private String extension;
    private static final String ITEM_ZERO = "000";
    private static int MAX_NUMBER_OF_FILES = 1000;
    private static int FILE_COUNTER_LENGTH = 3;
    private static String CMS_CLASS_PROPERTY = "cms:class";
    private static String CMS_SHARD_PARENT = "shardparent";
    private static final Logger logger = LoggerFactory.getLogger(CmsItemNamingShard1K.class);


    @Inject
    public CmsItemNamingShard1K(CmsRepository repository, CmsItemLookup lookup) {

        this.repository = repository;
        this.lookup = lookup;
    }

    /**
     * Will increment the file name with one.
     * Max files in a folder is 1000 (zero indexed) if file counter reaches 999 it will suggest a CmsItemPath with the folder incremented by 1
     * and a file with count zero.
     * Folder name and file name will follow the given namePattern.
     * Folder counter will never be higher then the given hashes in the name pattern e.g name###### the max folder count will be 999.
     * See CmsNamePattern doc for more info.
     * @param parentFolder which is parent to the folders of the folders that contains the files /folder/someFolder001/some_file000.txt
     * @param namePattern the name pattern you want to use, checks that there are no illegal chars.
     * @param extension extension of the file.
     * @return
     */
    @Override
    public CmsItemPath getItemPath(CmsItemPath parentFolder, CmsItemNamePattern namePattern, String extension) {

        if (parentFolder == null || extension == null) {
            throw new IllegalArgumentException("Folder and extension must not be null");
        }

        if (namePattern.getCounter().length() < 3) {
            throw new IllegalArgumentException("The configured naming requires a minimum 3 '#' in the naming pattern.");
        }


        this.extension = extension;

        logger.info("Trying to create new name based on path: {}, with pattern: {} and extension: {}", parentFolder.getPath(), namePattern.getPrefix(), extension);
        this.namePattern = namePattern;

        String newName;
        CmsItemId itemId = repository.getItemId(parentFolder, null);

        if (!isShardParent(itemId)) {
            throw new IllegalArgumentException("The parent folder is not intended for the configured automated naming.");
        }

        Set<CmsItemId> immediateFolders = lookup.getImmediateFolders(itemId);


        CmsItemPath folderPath;
        CmsItemId folder = getItemIdWithHighestNumber(immediateFolders);
        if (folder != null) {
            folderPath = folder.getRelPath();
            Set<CmsItemId> immediateFiles = lookup.getImmediateFiles(folder);

            if (!isFolderFullOrEmpty(immediateFiles)) {
                logger.info("Folder is not full and there is previous files, returning file based on previous file name with counter incremented by 1");

                CmsItemId itemIdWithHighestNumber = getItemIdWithHighestNumber(immediateFiles);
                if (itemIdWithHighestNumber == null) {
                    newName = folderPath.getName();
                } else {
                    String prevFileName =itemIdWithHighestNumber.getRelPath().getName();
                    newName = createNewFileName(prevFileName);
                }
            } else {
                folderPath = immediateFiles.isEmpty() ? folderPath : createNewFolderPath(folder);
                newName = folderPath.getName();
                logger.info("Folder: {}, new file name: {}", folderPath, newName);
            }
        } else {
            logger.info("No folders in path: {}, creating folder with count 0", parentFolder.getPath());
            folderPath = parentFolder.append(namePattern.getFullNameWithCountZero());
            newName = namePattern.getFullNameWithCountZero();
        }

        return getNewCmsItemPath(newName, extension, folderPath);
    }

    private boolean isShardParent(CmsItemId itemId) {

        boolean shardParent = false;
        CmsItemProperties properties = this.lookup.getItem(itemId).getProperties();
        String classProperty = properties.getString(CMS_CLASS_PROPERTY);

        if (classProperty != null && classProperty.contains(CMS_SHARD_PARENT)) {
            shardParent = true;
        }

        return shardParent;
    }

    private boolean isFolderFullOrEmpty(Set<CmsItemId> immediateFiles) {

        boolean fullOrEmpty = (immediateFiles.size() == MAX_NUMBER_OF_FILES && immediateFiles.isEmpty()) ? true : false;

        boolean not999 = false;
        if (!fullOrEmpty) {
            CmsItemId itemIdWithHighestNumber = getItemIdWithHighestNumber(immediateFiles);
            not999 = (itemIdWithHighestNumber != null) ? itemIdWithHighestNumber.getRelPath().getName().endsWith("999.".concat(extension)) : false;
        }

        return fullOrEmpty ? fullOrEmpty : not999;
    }

    private String createNewFileName(String name) {

        String nameWithoutExtension = name.substring(0, name.lastIndexOf("."));
        String number = incrementNumberWithOne(getFileNameCounter(nameWithoutExtension));

        return nameWithoutExtension.substring(0, nameWithoutExtension.length() - FILE_COUNTER_LENGTH).concat(number);
    }

    private String getFileNameCounter(String nameWithoutExtension) {
        return nameWithoutExtension.substring(nameWithoutExtension.length() - FILE_COUNTER_LENGTH);
    }

    private CmsItemPath createNewFolderPath(CmsItemId fileFolder) {

        String number = getFolderCounter(fileFolder);

        String name = namePattern.getPrefix().concat(incrementNumberWithOne(number));
        return fileFolder.getRelPath().getParent().append(name.concat(ITEM_ZERO));
    }

    private String getFolderCounter(CmsItemId folder) {

        String folderAndFileCounter = folder.getRelPath().getName().replace(namePattern.getPrefix(), "");
        return folderAndFileCounter.substring(0, folderAndFileCounter.length() - FILE_COUNTER_LENGTH);
    }

    private CmsItemId getItemIdWithHighestNumber(Set<CmsItemId> immediateFolders) {

        ArrayList<CmsItemId> cmsItemIds = new ArrayList<CmsItemId>();
        cmsItemIds.addAll(immediateFolders);

        Iterator<CmsItemId> iterator = cmsItemIds.iterator();
        removeNoneShards(iterator);

        Collections.sort(cmsItemIds, new CmsItemIdNameComparator());
        Collections.reverse(cmsItemIds);

        CmsItemId highestNumber = null;

        if(cmsItemIds.size() != 0) {
            highestNumber = cmsItemIds.get(0);
        }

        return highestNumber;
    }

    private void removeNoneShards(Iterator<CmsItemId> iterator) {

        while (iterator.hasNext()) {
            String name = iterator.next().getRelPath().getName();
            int i = name.lastIndexOf(".");
            if (i != -1) {
                name = name.substring(0, i);
            }
            if (!this.namePattern.isNameMatchingPattern(name)) {
                iterator.remove();
            }
        }
    }

    private String incrementNumberWithOne(String number) throws IllegalStateException {

        int maxLength = number.length();
        int parseInt;
        try {
            parseInt = Integer.parseInt(number);
        }catch (NumberFormatException e) {
            throw new IllegalStateException("Counter is missing, namePattern: " + namePattern.getFullNameWithCountZero() + " needs hashes that represents counters");
        }

        parseInt++;

        String incrementedString = Integer.toString(parseInt);
        if (incrementedString.length() < maxLength) {
            incrementedString = reGenerateZeros(incrementedString, maxLength);
        } else if (incrementedString.length() > maxLength) {
            throw new IllegalStateException("Incremented string can't be longer then initial strings length");
        }

        return incrementedString;
    }

    private String reGenerateZeros(String number, int length) {

        for (int i = 0; i <= length; i++) {
            if (number.length() < length)
            number = "0" + number;
        }
        return number;
    }

    private CmsItemPath getNewCmsItemPath(String name, String extension, CmsItemPath path) {
        return new CmsItemPath(path.getPath()).append(name.concat(".").concat(extension));
    }

    private class CmsItemIdNameComparator implements Comparator<CmsItemId> {

        @Override
        public int compare(CmsItemId itemId1, CmsItemId itemId2) {
            return itemId1.getRelPath().getName().compareTo(itemId2.getRelPath().getName());
        }
    }

}
