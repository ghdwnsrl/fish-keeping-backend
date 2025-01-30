package junki.fishkeepingback.global.response;

import lombok.Getter;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;

@Getter
public class PageCustom<T> implements Serializable {

    private final List<T> content;

    private final PageableCustom pageable;

    public PageCustom(List<T> content, Pageable pageable, long total) {
        this.content = content;
        this.pageable = new PageableCustom(new PageImpl(content, pageable, total));
    }

}