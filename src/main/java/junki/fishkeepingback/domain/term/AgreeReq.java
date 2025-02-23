package junki.fishkeepingback.domain.term;

public record AgreeReq(
        Boolean ageAgree,
        Boolean privacyAgree,
        Boolean termsAgree
) { }
