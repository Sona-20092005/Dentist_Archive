package com.dentistarchive.repository;


import com.dentistarchive.entity.Role;
import com.dentistarchive.repository.jpa.RoleJpaRepository;
import com.dentistarchive.repository.search.RoleSearchMapper;
import com.dentistarchive.search.filter.RoleFilter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleRepository extends BaseReadOnlyRepository<Role, RoleFilter> {

    RoleJpaRepository jpaRepository;

    public RoleRepository(RoleSearchMapper searchMapper, RoleJpaRepository jpaRepository) {
        super(searchMapper, jpaRepository);
        this.jpaRepository = jpaRepository;
    }

    @Transactional(readOnly = true)
    public List<Role> getAll() {
        return jpaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Role> getByCode(String code) {
        return jpaRepository.findByCode(code);
    }

    @Transactional
    public void saveAll(Collection<Role> roles) {
        jpaRepository.saveAll(roles);
        jpaRepository.flush();
    }

    @Transactional
    public void deleteAll(Collection<Role> roles) {
        jpaRepository.deleteAll(roles);
        jpaRepository.flush();
    }

    @Transactional
    public void deleteAll() {
        jpaRepository.deleteAll();
        jpaRepository.flush();
    }
}
