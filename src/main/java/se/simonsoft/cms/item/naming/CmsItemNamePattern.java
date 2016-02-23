package se.simonsoft.cms.item.naming;

/**
 * Created by jonand on 17/02/16.
 */
public class CmsItemNamePattern {

    private String name = null;
    private String pattern = "^[a-zA-Z0-9_]*$";

    CmsItemNamePattern (String name) {
        this.name = validateName(name);
    }

    private String validateName(String name) {

        if(name == null || name == "") {
            throw new IllegalArgumentException("The name pattern can't be null or empty");
        }

        if(!name.matches(pattern)) {
            throw new IllegalArgumentException("The name must be alphanumeric");
        }

        return name;
    }

    public String getName() {
        return name;
    }
}
