package radio;

public class ProgramInfo {
    private String name;
    private String description;
    private String startDate;
    private String endDate;
    private String imageUrl;
    private String title;

    public ProgramInfo(String name, String description, String startDate, String endDate, String imageUrl, String title ){
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.imageUrl = imageUrl;
        this.title = title;
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

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("----Programinformation ---\n");
        builder.append(getName() + '\n');
        builder.append(getTitle() + '\n');
        builder.append(getDescription() + '\n');
        builder.append(getImageUrl()+ '\n');
        builder.append(getStartDate()+ '\n');
        builder.append(getEndDate()+ '\n');
        return builder.toString();
    }
}
