package junki.fishkeepingback.domain.userterm;

import junki.fishkeepingback.domain.term.TermService;
import junki.fishkeepingback.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserTermService {

    private final UserTermRepository userTermRepository;
    private final TermService termService;

    public void save(User user) {
        termService.getTerms()
                .forEach(term -> {
                    UserTerm userTerm = new UserTerm(user, term, true);
                    userTermRepository.save(userTerm);
                });
    }
}
