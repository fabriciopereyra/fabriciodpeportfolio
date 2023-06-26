/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.fabriciodpeportfolio.portfolio.security.repository;

import com.fabriciodpeportfolio.portfolio.security.enums.RoleName;
import com.fabriciodpeportfolio.portfolio.security.model.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Fabricio
 */
@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {

    public Optional<Role> findByRoleName(RoleName roleName);
}
