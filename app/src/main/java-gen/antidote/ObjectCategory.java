package antidote;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table OBJECT_CATEGORY.
 */
public class ObjectCategory {

    private Long id;
    private String name;
    private String slug;
    private String parentID;
    private String shortDescription;
    private String fullDescription;
    private String thumbnail;
    private String image;
    private String isActive;

    public ObjectCategory() {
    }

    public ObjectCategory(Long id) {
        this.id = id;
    }

    public ObjectCategory(Long id, String name, String slug, String parentID, String shortDescription, String fullDescription, String thumbnail, String image, String isActive) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.parentID = parentID;
        this.shortDescription = shortDescription;
        this.fullDescription = fullDescription;
        this.thumbnail = thumbnail;
        this.image = image;
        this.isActive = isActive;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

}