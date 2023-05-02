package ru.practicum.compilation.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.model.QCompilation;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CompilationRepositoryImpl implements CompilationRepositoryCustom {

    public final EntityManager entityManager;

    public List<Compilation> findCompilationsByParameters(Integer from, Integer size, @Nullable Boolean pinned) {
        QCompilation qCompilation = QCompilation.compilation;
        BooleanExpression expression;
        if (pinned != null) {
            expression = qCompilation.pinned.eq(pinned);
        } else {
            expression = Expressions.ONE.eq(1);
        }
        return new JPAQuery<>(entityManager)
                .select(qCompilation)
                .from(qCompilation)
                .where(expression)
                .orderBy(qCompilation.title.asc())
                .offset(from)
                .limit(size)
                .fetch();
    }

}
