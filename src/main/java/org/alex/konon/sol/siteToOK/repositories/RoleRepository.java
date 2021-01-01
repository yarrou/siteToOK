package org.alex.konon.sol.siteToOK.repositories;

import org.alex.konon.sol.siteToOK.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    public Role save(Role role);
    public Role findByName(String name);
}

