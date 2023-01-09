package radio;

import java.time.LocalDateTime;


/**
 * Contains information about a single program in a tableau from SR. Has a name, description,
 * start date, end date, image url, title, and an episode id.
 * @author Lee Lannerblad (ens19lld)
 * Course: Applikationsutveckling (Java)
 * Version information: 2023-01-09
 */
public class ProgramInfo {
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String imageUrl;
    private String title;
    private Long episodeId;

    /**
     * Creates a new ProgramInfo object and sets its attributes.
     * @param name the name of the program
     * @param description the description of the program
     * @param startDate when the episode starts
     * @param endDate when the episode ends
     * @param imageUrl an url to an image
     * @param title the title of the episode
     * @param episodeId the episode's id
     */
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

    /**
     * Returns @this name
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns @this description
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns @this startDate
     * @return the start date
     */
    public LocalDateTime getStartDate() {
        return startDate;
    }

    /**
     * Returns @this endDate
     * @return the end date
     */
    public LocalDateTime getEndDate() {
        return endDate;
    }

    /**
     * Returns @this imageUrl
     * @return the url for the image
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Returns @this title
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns @this episodeId
     * @return the episode's id.
     */
    public Long getEpisodeId() {
        return episodeId;
    }

    /**
     * Converts the object to a String to be able to see its attributes
     * @return the object to a String
     */
    @Override
    public String toString() {
        String builder = "----Programinformation ---\n" +
                getName() + '\n' +
                getTitle() + '\n' +
                getDescription() + '\n' +
                getImageUrl() + '\n' +
                getStartDate().toString() + '\n' +
                getEndDate().toString() + '\n';
        return builder;
    }
}
