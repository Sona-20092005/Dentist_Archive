package com.dentistarchive.service;

import com.dentistarchive.entity.BaseEntity;
import com.dentistarchive.exception.EntityNotFoundByIdException;
import com.dentistarchive.repository.BaseReadOnlyRepository;
import com.dentistarchive.search.filter.BaseFilter;
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

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
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

    public Optional<E> getById(@NotNull UUID id) {
        F filter = newEmptyFilter();
        filter.setIds(Set.of(id));

        Optional<E> entity = searchOne(filter);
        entity.ifPresent(readOnlyAccessValidator::validateAccess);
        return entity;
    }

    public E getByIdOrElseThrow(UUID id) {
        return getById(id)
                .orElseThrow(() -> new EntityNotFoundByIdException(entityClass, id));
    }

    public List<E> getByIds(@NotEmpty Set<UUID> ids) {
        F filter = newEmptyFilter();
        filter.setIds(ids);

        List<E> entities = search(filter);
        entities.forEach(readOnlyAccessValidator::validateAccess);
        return entities;
    }

    public List<E> getByIdsOrElseThrow(@NotEmpty Set<UUID> ids) {
        List<E> entities = getByIds(ids);

        if (entities.size() < ids.size()) {
            List<UUID> foundIds = entities.stream()
                    .map(BaseEntity::getId)
                    .toList();

            Set<UUID> notFoundIds = ids.stream()
                    .filter(id -> !foundIds.contains(id))
                    .collect(toSet());

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
            throw new ResponseStatusException(
                    BAD_REQUEST,
                    "Found " + entities.size() + " entities by filter " + filter + ", but expected only 1"
            );
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

    public void existsByIdOrElseThrow(@NotNull UUID id) {
        F filter = newEmptyFilter();
        filter.setIds(Set.of(id));

        if (!exists(filter)) {
            throw new EntityNotFoundByIdException(entityClass, id);
        }
    }

    protected SearchRequest<F, ?> withAccessControl(SearchRequest<F, ?> searchRequest) {
        searchRequest.setFilter(withAccessControl(searchRequest.getFilter()));
        return searchRequest;
    }

    protected F withAccessControl(F filter) {
        F accessFilter = readOnlyAccessValidator.getAccessControlFilter();
        return accessFilter == null ? filter : accessFilter.and(filter);
    }

    @SneakyThrows
    protected F newEmptyFilter() {
        return filterClass.getDeclaredConstructor().newInstance();
    }
}