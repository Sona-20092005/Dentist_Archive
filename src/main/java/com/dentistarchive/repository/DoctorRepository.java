package com.dentistarchive.repository;

import com.dentistarchive.dto.auth.enums.UserScope;
import com.dentistarchive.entity.Doctor;
import com.dentistarchive.repository.jpa.DoctorJpaRepository;
import com.dentistarchive.repository.search.DoctorSearchMapper;
import com.dentistarchive.search.filter.DoctorFilter;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilderFactory;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import static com.dentistarchive.entity.QDoctor.doctor;

import jakarta.persistence.EntityManager;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static java.util.stream.Collectors.toMap;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DoctorRepository extends BaseReadOnlyRepository<Doctor, DoctorFilter> {

    DoctorJpaRepository jpaRepository;
    EntityManager entityManager;

    public DoctorRepository(
            DoctorSearchMapper searchMapper,
            DoctorJpaRepository jpaRepository,
            EntityManager entityManager
    ) {
        super(searchMapper, jpaRepository);
        this.jpaRepository = jpaRepository;
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    public Optional<Doctor> getUserByNickname(String nickName) {
        return jpaRepository.findByNickName(nickName);
    }

    @Transactional(readOnly = true)
    public Optional<Doctor> getUserByEmailAndScope(String email, UserScope scope) {
        return jpaRepository.findUserByEmailAndScope(email, scope);
    }

    @Transactional(readOnly = true)
    public Optional<Doctor> getUserByPhoneAndScope(String phone, UserScope scope) {
        return jpaRepository.findUserByPhoneAndScope(phone, scope);
    }

    @Transactional
    public void save(Doctor user) {
        jpaRepository.save(user);
    }

    @Transactional
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Transactional
    public void deleteAll() {
        jpaRepository.deleteAll();
    }

    @Transactional(readOnly = true)
    public Map<UUID, String> getFullNamesByIds(Collection<UUID> ids) {
        return jpaRepository.findAllByIdIn(ids).stream()
                .collect(toMap(DoctorJpaRepository.IdAndFullName::id, DoctorJpaRepository.IdAndFullName::fullName));
    }

    /**
     * Custom query is used to add join fetches in sql query and avoid n+1 queries
     */
    @Override
    @SuppressWarnings("unchecked")
    protected JPAQuery<Doctor> createCustomQuery(Predicate predicate, Pageable pageable) {
        var queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, entityManager);
        var querydsl = new Querydsl(entityManager, new PathBuilderFactory().create(Doctor.class));

        var query = (JPAQuery<Doctor>) queryFactory.from(QDoctor.user)
                .leftJoin(QDoctor.user.roles).fetchJoin();

        if (pageable.isPaged()) {
            // select needed ids to avoid query with join fetch + pagination
            JPAQuery<UUID> queryIds = queryFactory.select(QDoctor.user.id).from(QDoctor.user).where(predicate);
            List<UUID> userIds = querydsl.applyPagination(pageable, queryIds).fetch();

            query.where(QDoctor.user.id.in(userIds));
        } else {
            query.where(predicate);
        }
        querydsl.applySorting(pageable.getSort(), query);
        return query;
    }
}
