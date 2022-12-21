package radio;

public class ProgramInfo {
    private String name;
    private String description;
    private String startDate;
    private String endDate;
    private String imageUrl;

    public ProgramInfo(String name, String description, String startDate, String endDate, String imageUrl ){
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
