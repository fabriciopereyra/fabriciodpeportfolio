/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fabriciodpeportfolio.portfolio.security.controller;

import com.fabriciodpeportfolio.portfolio.security.dto.JwtDTO;
import com.fabriciodpeportfolio.portfolio.security.dto.NewUser;
import com.fabriciodpeportfolio.portfolio.security.dto.UserLogin;
import com.fabriciodpeportfolio.portfolio.security.enums.RoleName;
import com.fabriciodpeportfolio.portfolio.security.jwt.JwtProvider;
import com.fabriciodpeportfolio.portfolio.security.model.Role;
import com.fabriciodpeportfolio.portfolio.security.model.User;
import com.fabriciodpeportfolio.portfolio.security.service.RoleService;
import com.fabriciodpeportfolio.portfolio.security.service.UserService;
import jakarta.validation.Valid;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Fabricio
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    JwtProvider jwtProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody NewUser newUser, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(new Message("Campos mal puestos o email invalido"), HttpStatus.BAD_REQUEST);
        }
        if (userService.existsByUserName(newUser.getUserName())) {
            return new ResponseEntity(new Message("Nombre de usuario ya existe"), HttpStatus.BAD_REQUEST);
        }
        if (userService.existsByEmail(newUser.getEmail())) {
            return new ResponseEntity(new Message("Email de usuario ya existe"), HttpStatus.BAD_REQUEST);
        }
        User user = new User(newUser.getName(), newUser.getUserName(), newUser.getEmail(), passwordEncoder.encode(newUser.getPassword()));
        Set<Role> roles = new HashSet<>();
        roles.add(roleService.getByRoleName(RoleName.ROLE_USER).get());
        if (newUser.getRoles().contains("admin")) {
            roles.add(roleService.getByRoleName(RoleName.ROLE_ADMIN).get());
        }
        user.setRoles(roles);
        userService.save(user);
        return new ResponseEntity(new Message("Usuario guardado"), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDTO> login(@Valid @RequestBody UserLogin userLogin, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(new Message("Usuario y/o clave incorrectos"), HttpStatus.BAD_REQUEST);
        }
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLogin.getUserName(), userLogin.getPassword()));
        } catch (AuthenticationException e) {
            return new ResponseEntity(new Message("Usuario y/o clave incorrectos"), HttpStatus.BAD_REQUEST);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        JwtDTO jwtDto = new JwtDTO(jwt, userDetails.getUsername(), userDetails.getAuthorities());
        return new ResponseEntity(jwtDto, HttpStatus.OK);
    }

}
