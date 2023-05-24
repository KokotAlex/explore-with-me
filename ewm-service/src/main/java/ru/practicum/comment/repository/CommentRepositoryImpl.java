package ru.practicum.comment.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.comment.model.*;
import ru.practicum.util.Utils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static com.querydsl.core.types.Order.ASC;
import static com.querydsl.core.types.Order.DESC;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    public final EntityManager entityManager;

    @Override
    public List<Comment> findCommentsByParameters(CommentRequestParameters parameters) {
        QComment qComment = QComment.comment;

        // Сформируем отборы
        BooleanBuilder filter = getFilter(parameters);

        // Сформируем сортировку
        OrderSpecifier<LocalDateTime> orderSpecifier;
        CommentSort sort = parameters.getSort();
        if (sort != null && parameters.getSort().equals(CommentSort.ASC)) {
            orderSpecifier = new OrderSpecifier<>(ASC, qComment.created);
        } else {
            orderSpecifier = new OrderSpecifier<>(DESC, qComment.created);
        }

        // Сформируем запрос.
        JPAQuery<Comment> query = new JPAQuery<>(entityManager);
        query.select(qComment)
             .from(qComment)
             .where(filter)
             .orderBy(orderSpecifier);

        // Добавим пагинацию если требуется.
        Integer from = parameters.getFrom();
        if (from != null) {
            query.offset(from);
        }
        Integer size = parameters.getSize();
        if (size != null) {
            query.limit(size);
        }

        // Выполним запрос.
        return query.fetch();
    }

    private BooleanBuilder getFilter(CommentRequestParameters parameters) {
        QComment qComment = QComment.comment;

        BooleanBuilder filter = new BooleanBuilder();

        String text = parameters.getText();
        if (text != null && !text.isBlank()) {
            filter.and(qComment.description.likeIgnoreCase(text));
        }

        Long authorId = parameters.getAuthorId();
        if (authorId != null) {
            filter.and(qComment.author.id.eq(authorId));
        }

        Long eventId = parameters.getEventId();
        if (eventId != null) {
            filter.and(qComment.event.id.eq(eventId));
        }

        CommentState state = parameters.getState();
        if (state != null) {
            filter.and(qComment.status.eq(state));
        }

        String startString = parameters.getStart();
        LocalDateTime start;
        if (startString != null && !startString.isBlank()) {
            start = Utils.stringToLocalDateTime(startString);
        } else {
            start = LocalDateTime.of(1,1,1,0,0);
        }
        String endString = parameters.getEnd();
        LocalDateTime end;
        if (endString != null && !endString.isBlank()) {
            end = Utils.stringToLocalDateTime(endString);
        } else {
            end = LocalDateTime.now();
        }
        filter.and(qComment.created.between(start, end));

        return filter;
    }

}
