package com.dentistarchive.repository;

import com.dentistarchive.entity.User;
import com.dentistarchive.repository.jpa.UserJpaRepository;
import com.dentistarchive.repository.search.UserSearchMapper;
import com.dentistarchive.search.filter.UserFilter;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilderFactory;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.dentistarchive.entity.QUser.user;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserRepository extends BaseReadOnlyRepository<User, UserFilter> {

    UserJpaRepository jpaRepository;
    EntityManager entityManager;

    public UserRepository(
            UserSearchMapper searchMapper,
            UserJpaRepository jpaRepository,
            EntityManager entityManager
    ) {
        super(searchMapper, jpaRepository);
        this.jpaRepository = jpaRepository;
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    public Optional<User> getById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Transactional
    public User save(User actor) {
        return jpaRepository.save(actor);
    }

    /**
     * Custom query is used to add join fetches in sql query and avoid n+1 queries
     */
    @Override
    @SuppressWarnings("unchecked")
    protected JPAQuery<User> createCustomQuery(Predicate predicate, Pageable pageable) {
        var queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, entityManager);
        var querydsl = new Querydsl(entityManager, new PathBuilderFactory().create(User.class));

        var query = (JPAQuery<User>) queryFactory.from(user)
                .leftJoin(user.roles).fetchJoin();

        if (pageable.isPaged()) {
            // select needed ids to avoid query with join fetch + pagination
            JPAQuery<UUID> queryIds = queryFactory.select(user.id).from(user).where(predicate);
            List<UUID> actorIds = querydsl.applyPagination(pageable, queryIds).fetch();

            query.where(user.id.in(actorIds));
        } else {
            query.where(predicate);
        }
        querydsl.applySorting(pageable.getSort(), query);
        return query;
    }
}
