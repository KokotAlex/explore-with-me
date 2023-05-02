package ru.practicum.event.repository;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventSort;
import ru.practicum.event.model.QEvent;
import ru.practicum.event.model.RequestParameters;
import ru.practicum.event.model.EventState;
import ru.practicum.requests.model.MembershipRequestStatus;
import ru.practicum.requests.model.QRequest;
import ru.practicum.util.Utils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class EventRepositoryImpl implements EventRepositoryCustom {

    public final EntityManager entityManager;

    @Override
    public List<Event> findEventsByParameters(RequestParameters parameters) {

        QEvent qEvent = QEvent.event;
        QRequest qRequest = QRequest.request;

        // Сформируем подзапрос на получение количества одобренных заявок.
        Expression<Long> confirmedRequests = JPAExpressions
                .select(qRequest.count())
                .from(qRequest)
                .where(qRequest.in(qEvent.requests)
                               .and(qRequest.status.eq(MembershipRequestStatus.CONFIRMED)));

        // Сформируем конструктор.
        ConstructorExpression<Event> constructor = getEventConstructor(confirmedRequests);

        // Сформируем запрос.
        JPAQuery<Event> query = new JPAQuery<>(entityManager);
        query.select(constructor)
             .from(qEvent)
             .where(getFilters(parameters, confirmedRequests));

        // Добавим сортировку.
        EventSort sort = parameters.getSort();
        if (sort != null && sort.equals(EventSort.VIEWS)) {
            return query.fetch();
        }

        return query.orderBy(qEvent.eventDate.desc())
                    .offset(parameters.getFrom())
                    .limit(parameters.getSize())
                    .fetch();

        //TODO Доработать для возвращения requests в составе event
    }

    private BooleanExpression getFilters(RequestParameters parameters, Expression<Long> confirmedRequests) {
        // Сформируем отборы.
        QEvent qEvent = QEvent.event;

        List<BooleanExpression> conditions = new ArrayList<>();

        List<Long> users = parameters.getUsers();
        if (users != null && !users.isEmpty()) {
            conditions.add(qEvent.user.id.in(users));
        }

        Boolean paid = parameters.getPaid();
        if (paid != null) {
            conditions.add(qEvent.paid.eq(paid));
        }

        List<EventState> states = parameters.getStates();
        if (states != null && !states.isEmpty()) {
            conditions.add(qEvent.state.in(states));
        }

        List<Long> categories = parameters.getCategories();
        if (categories != null && !categories.isEmpty()) {
            conditions.add(qEvent.category.id.in(categories));
        }

        String rangeStart = parameters.getRangeStart();
        if (rangeStart != null && !rangeStart.isEmpty()) {
            LocalDateTime startDate = Utils.stringToLocalDateTime(rangeStart);
            conditions.add(qEvent.eventDate.goe(startDate));
        }

        String rangeEnd = parameters.getRangeEnd();
        if (rangeEnd != null && !rangeEnd.isEmpty()) {
            LocalDateTime endDate = Utils.stringToLocalDateTime(rangeEnd);
            conditions.add(qEvent.eventDate.loe(endDate));
        }

        Set<Long> events = parameters.getEvents();
        if (events != null && !events.isEmpty()) {
            conditions.add(qEvent.id.in(events));
        }

        String text = parameters.getText();
        if (text != null) {
            conditions.add(qEvent.annotation.likeIgnoreCase(text)
                                            .or(qEvent.description.likeIgnoreCase(text)));
        }

        Boolean onlyAvailable = parameters.getOnlyAvailable();
        if (onlyAvailable != null && onlyAvailable.equals(Boolean.TRUE)) {
            conditions.add(qEvent.participantLimit.gt(confirmedRequests));
        }

        BooleanExpression identity = Expressions.ONE.eq(1);
        return conditions.stream().reduce(identity, BooleanExpression::and);
    }

    private ConstructorExpression<Event> getEventConstructor(Expression<Long> confirmedRequests) {
        QEvent qEvent = QEvent.event;

        return Projections.constructor(
                Event.class,
                qEvent.id,
                qEvent.annotation,
                qEvent.category,
                qEvent.createdOn,
                qEvent.description,
                qEvent.eventDate,
                qEvent.user,
                qEvent.location,
                qEvent.paid,
                qEvent.participantLimit,
                qEvent.publishedOn,
                qEvent.requestModeration,
                qEvent.state,
                qEvent.title,
                confirmedRequests
//                qEvent.requests
        );
    }

}
