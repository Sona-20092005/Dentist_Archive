package com.dentistarchive.repository.search;

import com.dentistarchive.entity.QUser;
import com.dentistarchive.search.PredicateBuilder;
import com.dentistarchive.search.SearchMapper;
import com.dentistarchive.search.SortBuilder;
import com.dentistarchive.search.filter.UserFilter;
import com.dentistarchive.search.sort.SortDirection;
import com.dentistarchive.search.sort.UserSort;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import static com.dentistarchive.entity.QUser.user;

@Component
public class UserSearchMapper extends SearchMapper<UserFilter, UserSort> {

    @Override
    protected Predicate toPredicateExceptSubFilters(UserFilter filter) {
        var builder = PredicateBuilder.builder(PredicateBuilder.Aggregation.AND)
                .in(user.id, filter.getIds())
                .in(user.scope, filter.getScopes());

        if (filter.getLogin() != null) {
            builder.and(PredicateBuilder.builder(PredicateBuilder.Aggregation.OR)
                    .eq(user.nickName, filter.getLogin())
//                    .eq(user.as(QUser.class).email.toLowerCase(), filter.getLogin().toLowerCase())
//                    .eq(user.as(QUser.class).phone, filter.getLogin())
                    .build()
            );
        }

        return builder.build();
    }

    @Override
    protected Sort.Order toOrder(UserSort sortName, SortDirection direction) {
        return switch (sortName) {
            case FULL_NAME -> SortBuilder.buildOrder(user.fullName, direction);
        };
    }

    @Override
    protected Class<UserFilter> getFilterClass() {
        return UserFilter.class;
    }
}
