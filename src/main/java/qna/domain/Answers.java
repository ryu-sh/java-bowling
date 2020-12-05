package qna.domain;

import org.hibernate.annotations.Where;
import qna.CannotDeleteException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    @Where(clause = "deleted = false")
    @OrderBy("id ASC")
    private final List<Answer> value;

    public Answers() {
        value = new ArrayList<>();
    }

    public void addAnswer(Question question, Answer answer) {
        answer.toQuestion(question);
        value.add(answer);
    }

    public List<DeleteHistory> deleteAll(User loginUser) throws CannotDeleteException {
        validateDelete(loginUser);
        return value.stream()
                .peek(Answer::deleted)
                .map(answer -> new DeleteHistory(answer, loginUser, LocalDateTime.now()))
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
    }

    private void validateDelete(User loginUser) throws CannotDeleteException {
        if (hasOtherOwnerDifferentFrom(loginUser)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
    }

    private boolean hasOtherOwnerDifferentFrom(User user) {
        return value.stream()
                .anyMatch(ans -> !ans.isOwner(user));
    }
}
