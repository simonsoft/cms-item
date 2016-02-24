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
    public CmsItemPath getItemPath(CmsItemPath parentFolder, CmsItemNamePattern namePattern, String extension) {

        if (parentFolder == null || extension == null) {
            throw new IllegalArgumentException("Folder and extension must not be null");
        }
        logger.info("Trying to create new name based on path: {}, with pattern: {} and extension: {}", parentFolder.getPath(), namePattern.getName(), extension);

        String newName;
        CmsItemId itemId = repository.getItemId(parentFolder.getPath());
        Set<CmsItemId> immediateFolders = lookup.getImmediateFolders(itemId);

        CmsItemPath folderPath;
        if (immediateFolders != null) {
            CmsItemId folder = getItemIdWithHighestNumber(immediateFolders);
            folderPath = folder.getRelPath();

            Set<CmsItemId> immediateFiles = lookup.getImmediateFiles(folder);
            if (!isFolderFullOrEmpty(immediateFiles)) {
                logger.info("Folder is not full and there is previous files, returning file based on previous file name with counter incremented by 1");
                String prevFileName = getItemIdWithHighestNumber(immediateFiles).getRelPath().getName();
                newName = createNewFileName(prevFileName, extension);
            } else {
                folderPath = immediateFiles.isEmpty() ? folderPath : createNewFolderPath(folder, namePattern);
                newName = folderPath.getName().concat(ITEM_ZERO);
                logger.info("Folder: {}, new file name: {}", folderPath, newName);
            }
        } else {
            logger.info("No folders in path: {}, creating folder with count 0", parentFolder.getPath());
            folderPath = parentFolder.append(namePattern.getName().concat(ITEM_ZERO));
            newName = folderPath.getName().concat(ITEM_ZERO);
        }

        return getNewCmsItemPath(newName, extension, folderPath);
    }

    private boolean isFolderFullOrEmpty(Set<CmsItemId> immediateFiles) {
        return (immediateFiles.size() != MAX_NUMBER_OF_FILES && !immediateFiles.isEmpty()) ? false : true;
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

    private CmsItemPath createNewFolderPath(CmsItemId fileFolder, CmsItemNamePattern pattern) {

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
        Collections.reverse(cmsItemIds);
        return cmsItemIds.get(0);
    }

    private String incrementNumberWithOne(String number) throws IllegalStateException {

        int maxLength = number.length();
        int parseInt = Integer.parseInt(number);
        parseInt++;

        String incrementedNumber = Integer.toString(parseInt);
        if (incrementedNumber.length() < maxLength) {
            incrementedNumber = reGenerateZeros(incrementedNumber, maxLength);
        } else if (incrementedNumber.length() > maxLength) {
            throw new IllegalStateException("Incremented string can't be longer then initial strings length");
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
