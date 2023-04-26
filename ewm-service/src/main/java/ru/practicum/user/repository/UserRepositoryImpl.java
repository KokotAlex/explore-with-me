package ru.practicum.user.repository;

import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import ru.practicum.user.model.QUser;
import ru.practicum.user.model.User;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    public final EntityManager entityManager;

    @Override
    public List<User> findFilteringUsers(Integer from, Integer size, @Nullable List<Long> ids) {

        QUser qUser = QUser.user;

        JPAQuery<User> query = new JPAQuery<>(entityManager)
                .select(qUser)
                .from(qUser)
                .orderBy(qUser.id.asc())
                .offset(from)
                .limit(size);

        // Добавим условие.
        if (ids != null && !ids.isEmpty()) {
            query.where(qUser.id.in(ids));
        }

        // Преобразуем полученный результат.
        return query.stream().collect(Collectors.toList());
    }

}
