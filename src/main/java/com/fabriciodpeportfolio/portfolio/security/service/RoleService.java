/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fabriciodpeportfolio.portfolio.security.service;

import com.fabriciodpeportfolio.portfolio.security.enums.RoleName;
import com.fabriciodpeportfolio.portfolio.security.model.Role;
import com.fabriciodpeportfolio.portfolio.security.repository.IRoleRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Fabricio
 */
@Service
@Transactional
public class RoleService {

    @Autowired
    IRoleRepository iRoleRepository;

    public Optional<Role> getByRoleName(RoleName roleName) {
        return iRoleRepository.findByRoleName(roleName);
    }

    public void save(Role role) {
        iRoleRepository.save(role);
    }

}
