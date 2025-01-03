package junki.fishkeepingback.domain.post.dto;

public record PostSearchParam(String type, String target) {
    public PostSearchParam(String type, String target) {
        this.type = type;
        this.target = target;
    }
}
