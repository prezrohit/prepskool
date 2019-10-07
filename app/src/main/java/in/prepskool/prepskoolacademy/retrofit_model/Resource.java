package in.prepskool.prepskoolacademy.retrofit_model;

public class Resource {
    private int id;
    private String name;
    private String slug;
    private String url;
    private String description;
    private String boardId;
    private String subjectId;
    private String resourceId;
    private String streamId;
    private String classId;
    private String year;
    private String urlType;
    private String userId;
    private String linkType;
    private String isPaid;
    private String price;
    private Board board;
    private Stream stream;
    private Subject subject;
    private Standard standard;
    private ResourceType resourceType;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getBoardId() {
        return boardId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public String getStreamId() {
        return streamId;
    }

    public String getClassId() {
        return classId;
    }

    public String getYear() {
        return year;
    }

    public String getUrlType() {
        return urlType;
    }

    public String getUserId() {
        return userId;
    }

    public String getLinkType() {
        return linkType;
    }

    public String getIsPaid() {
        return isPaid;
    }

    public Board getBoard() {
        return board;
    }

    public Stream getStream() {
        return stream;
    }

    public Subject getSubject() {
        return subject;
    }

    public Standard getStandard() {
        return standard;
    }

    public String getPrice() {
        return price;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }
}
