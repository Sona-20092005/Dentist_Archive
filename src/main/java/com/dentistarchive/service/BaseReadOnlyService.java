package com.dentistarchive.service;

import com.dentistarchive.exception.EntityNotFoundByIdException;
import com.dentistarchive.search.filter.BaseFilter;
import com.dentistarchive.entity.BaseEntity;
import com.dentistarchive.repository.BaseReadOnlyRepository;
import com.dentistarchive.search.filter.SearchRequest;
import com.dentistarchive.service.access.BaseReadOnlyAccessValidator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Validated
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public abstract class BaseReadOnlyService<E extends BaseEntity, F extends BaseFilter<F>> {

    Class<E> entityClass;
    Class<F> filterClass;
    BaseReadOnlyRepository<E, F> readOnlyRepository;
    BaseReadOnlyAccessValidator<E, F> readOnlyAccessValidator;

    @SneakyThrows
    public Optional<E> getById(@NotNull UUID id) {
        F filter = filterClass.getDeclaredConstructor().newInstance();
        filter.setIds(Set.of(id));
        return searchOne(filter);
    }

    public E getByIdOrElseThrow(@NotNull UUID id) {
        return getById(id).orElseThrow(() -> new EntityNotFoundByIdException(entityClass, id));
    }

    @SneakyThrows
    public List<E> getByIds(@NotEmpty Set<UUID> ids) {
        F filter = filterClass.getDeclaredConstructor().newInstance();
        filter.setIds(ids);
        return search(filter);
    }

    public List<E> getByIdsOrElseThrow(@NotEmpty Set<UUID> ids) {
        List<E> entities = getByIds(ids);
        if (entities.size() < ids.size()) {
            List<UUID> foundIds = entities.stream().map(BaseEntity::getId).toList();
            Set<UUID> notFoundIds = ids.stream().filter(id -> !foundIds.contains(id)).collect(toSet());
            throw new EntityNotFoundByIdException(entityClass, notFoundIds);
        }
        return entities;
    }

    public List<E> search(@NotNull @Valid F filter) {
        return readOnlyRepository.search(withAccessControl(filter));
    }

    public Optional<E> searchOne(@NotNull @Valid F filter) {
        List<E> entities = search(filter);
        if (entities.size() > 1) {
            throw new ResponseStatusException(BAD_REQUEST,
                    "Found " + entities.size() + " entities by filter " + filter + ", but expected only 1");
        }
        return entities.stream().findFirst();
    }

    public Page<E> search(@NotNull @Valid SearchRequest<F, ?> searchRequest) {
        return readOnlyRepository.search(withAccessControl(searchRequest));
    }

    public long count(@NotNull @Valid F filter) {
        return readOnlyRepository.count(withAccessControl(filter));
    }

    public Map<String, Long> count(@Valid @NotEmpty @Size(max = 10) Map<String, F> filterMap) {
        return filterMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        it -> count(it.getValue())
                ));
    }

    public boolean exists(@NotNull @Valid F filter) {
        return readOnlyRepository.exists(withAccessControl(filter));
    }

    @SneakyThrows
    public void existsByIdOrElseThrow(@NotNull UUID id) {
        F filter = filterClass.getDeclaredConstructor().newInstance();
        filter.setIds(Set.of(id));
        if (!exists(filter)) {
            throw new EntityNotFoundByIdException(entityClass, id);
        }
    }

    protected SearchRequest<F, ?> withAccessControl(SearchRequest<F, ?> searchRequest) {
        searchRequest.setFilter(readOnlyAccessValidator.getAccessControlFilter().and(searchRequest.getFilter()));
        return searchRequest;
    }

    protected F withAccessControl(F filter) {
        return readOnlyAccessValidator.getAccessControlFilter().and(filter);
    }
}
