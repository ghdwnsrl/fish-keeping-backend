package junki.fishkeepingback.global.response;

import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class PageableCustom {
    private boolean first;
    private boolean last;
    private boolean hasNext;
    private int totalPages;
    private long totalElements;
    private int page;
    private int size;

    public PageableCustom() {
    }

    public PageableCustom(Page page) {
        this.first = page.isFirst();
        this.last = page.isLast();
        this.hasNext = page.hasNext();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.page = page.getNumber() + 1;
        this.size = page.getSize();
    }
}
