package radio;

import java.time.LocalDateTime;

public class ProgramInfo {
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String imageUrl;
    private String title;
    private Long episodeId;

    public ProgramInfo(String name, String description, LocalDateTime startDate,
                       LocalDateTime endDate, String imageUrl, String title, Long episodeId ){
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.imageUrl = imageUrl;
        this.title = title;
        this.episodeId = episodeId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public Long getEpisodeId() {
        return episodeId;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("----Programinformation ---\n");
        builder.append(getName() + '\n');
        builder.append(getTitle() + '\n');
        builder.append(getDescription() + '\n');
        builder.append(getImageUrl()+ '\n');
        builder.append(getStartDate().toString()+ '\n');
        builder.append(getEndDate().toString()+ '\n');
        return builder.toString();
    }
}
