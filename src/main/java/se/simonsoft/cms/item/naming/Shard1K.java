package se.simonsoft.cms.item.naming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.simonsoft.cms.item.CmsItemId;
import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.CmsRepository;
import se.simonsoft.cms.item.info.CmsItemLookup;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

/**
 * Created by jonand on 17/02/16.
 */
public class Shard1K implements CmsItemNaming {

    private CmsRepository repository;
    private CmsItemLookup lookup;
    private static final String ITEM_ZERO = "000";
    private static int MAX_NUMBER_OF_FILES = 1000;
    private static int FILE_COUNTER_LENGTH = 3;
    private static final Logger logger = LoggerFactory.getLogger(Shard1K.class);


    @Inject
    public Shard1K(CmsRepository repository, @Named("CmsItemLookup") CmsItemLookup lookup) {

        this.repository = repository;
        this.lookup = lookup;
    }

    @Override
    public CmsItemPath getItemPath(CmsItemPath folder, CmsItemNamePattern namePattern, String extension) {

        if (folder == null || extension == null) {
            throw new IllegalArgumentException("Folder and extension must not be null");
        }

        logger.debug("Trying to create new name based on path: {}, with pattern: {} and extension: {}", folder.getPath(), namePattern.getName(), extension);
        String newName;

        CmsItemId itemId = repository.getItemId(folder.getPath());
        Set<CmsItemId> immediateFolders = lookup.getImmediateFolders(itemId);

        CmsItemPath newPath;
        if (immediateFolders != null && !immediateFolders.isEmpty()) {

            CmsItemId fileFolder = getItemIdWithHighestNumber(immediateFolders);
            newPath = fileFolder.getRelPath();
            Set<CmsItemId> immediateFiles = lookup.getImmediateFiles(fileFolder);
            if (immediateFiles.size() != MAX_NUMBER_OF_FILES && !immediateFiles.isEmpty()) {
                String name = getItemIdWithHighestNumber(immediateFiles).getRelPath().getName();
                newName = createNewFileName(name, extension);
            } else if (immediateFiles.isEmpty()) {
                //No Files in the folder, return item zero
                newName = newPath.getName().concat(ITEM_ZERO);
            } else {
                //Folder is full creating new one.
                newPath = createNewFolderPath(fileFolder, namePattern);
                newName = newPath.getName().concat(ITEM_ZERO);
            }
        } else {
            //Folder is empty or missing, creating new one with 0
            newPath = folder.append(namePattern.getName().concat(ITEM_ZERO));
            newName = newPath.getName().concat(ITEM_ZERO);
        }

        return getNewCmsItemPath(newName, extension, newPath);
    }

    private String createNewFileName(String name, String extension) {

        String nameWithoutExtension = name.replace("." + extension, "");
        String number = incrementNumberWithOne(getFileNameCounter(name, extension));

        return nameWithoutExtension.substring(0, nameWithoutExtension.length() - FILE_COUNTER_LENGTH).concat(number);
    }

    private String getFileNameCounter(String name, String extension) {
        String nameWithoutExtension = name.replace("." + extension, "");
        return nameWithoutExtension.substring(nameWithoutExtension.length() - FILE_COUNTER_LENGTH);
    }

    private CmsItemPath createNewFolderPath(CmsItemId fileFolder, CmsItemNamePattern pattern) throws IllegalStateException {
        String number = fileFolder.getRelPath().getName().replace(pattern.getName(), "");
        String name = pattern.getName().concat(incrementNumberWithOne(number));
        return fileFolder.getRelPath().getParent().append(name);
    }

    private CmsItemId getItemIdWithHighestNumber(Set<CmsItemId> immediateFolders) {

        Iterator<CmsItemId> iterator = immediateFolders.iterator();
        ArrayList<CmsItemId> cmsItemIds = new ArrayList<CmsItemId>();
        while (iterator.hasNext()) {
            cmsItemIds.add(iterator.next());
        }

        Collections.sort(cmsItemIds, new CmsItemIdNameComparator());
        //Sorted collection with highest name last returning the last index.
        return cmsItemIds.get(cmsItemIds.size() - 1);
    }

    private String incrementNumberWithOne(String number) throws IllegalStateException {

        int maxLength = number.length();
        int parseInt = Integer.parseInt(number);
        parseInt++;

        String incrementedNumber = Integer.toString(parseInt);
        if (incrementedNumber.length() < maxLength) {
            incrementedNumber = reGenerateZeros(incrementedNumber, maxLength);
        } else if (incrementedNumber.length() > maxLength) {
            throw new IllegalStateException("Return String can't be longer then initial strings length");
        }

        return incrementedNumber;
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
