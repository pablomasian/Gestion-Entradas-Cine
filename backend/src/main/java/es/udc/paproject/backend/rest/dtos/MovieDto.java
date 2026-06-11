package es.udc.paproject.backend.rest.dtos;

public class MovieDto {

    private Long id;
    private String title;
    private String resume;
    private int duration;

    public MovieDto() {}

    public MovieDto(Long id, String title, String resume, int duration) {

        this.id = id;
        this.title = title;
        this.resume = resume;
        this.duration = duration;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

}