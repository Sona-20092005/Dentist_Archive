package com.dentistarchive.repository;

import com.dentistarchive.search.SearchMapper;
import com.dentistarchive.search.filter.SearchRequest;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.dentistarchive.search.filter.BaseFilter;
import com.dentistarchive.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.apache.commons.collections4.IterableUtils.toList;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public abstract class BaseReadOnlyRepository<E extends BaseEntity, F extends BaseFilter<F>> {

    SearchMapper<F, ?> searchMapper;
    QuerydslPredicateExecutor<E> jpaRepository;

    @Transactional(readOnly = true)
    public Page<E> search(SearchRequest<F, ?> searchRequest) {
        Predicate predicate = searchMapper.toPredicate(searchRequest.getFilter());
        Sort sort = searchMapper.toSort(searchRequest.getSorts());
        Pageable pageable = searchMapper.toPageable(searchRequest.getPagination(), sort);
        if (pageable != null) {
            return fetchPage(predicate, pageable);
        }
        return sort != null
                ? new PageImpl<>(fetchList(predicate, sort))
                : fetchPage(predicate, Pageable.unpaged());
    }

    @Transactional(readOnly = true)
    public List<E> search(F filter) {
        return fetchList(searchMapper.toPredicate(filter), Sort.unsorted());
    }

    @Transactional(readOnly = true)
    public long count(F filter) {
        Predicate predicate = searchMapper.toPredicate(filter);
        return jpaRepository.count(predicate);
    }

    @Transactional(readOnly = true)
    public boolean exists(F filter) {
        Predicate predicate = searchMapper.toPredicate(filter);
        return jpaRepository.exists(predicate);
    }

    protected List<E> fetchList(Predicate predicate, Sort sort) {
        JPAQuery<E> query = createCustomQuery(predicate, Pageable.unpaged(sort));
        if (query != null) {
            return query.fetch();
        }
        return toList(jpaRepository.findAll(predicate, sort));
    }

    protected Page<E> fetchPage(Predicate predicate, Pageable pageable) {
        JPAQuery<E> query = createCustomQuery(predicate, pageable);
        if (query != null) {
            List<E> entities = query.fetch();
            return new PageImpl<>(entities, pageable, jpaRepository.count(predicate));
        }
        return jpaRepository.findAll(predicate, pageable);
    }

    protected JPAQuery<E> createCustomQuery(Predicate predicate, Pageable pageable) {
        return null; // implement this method if you need custom query (e.g. with custom joins)
    }
}
