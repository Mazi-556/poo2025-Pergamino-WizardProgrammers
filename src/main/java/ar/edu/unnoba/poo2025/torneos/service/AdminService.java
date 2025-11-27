package ar.edu.unnoba.poo2025.torneos.service;

import java.util.List;

import ar.edu.unnoba.poo2025.torneos.models.Admin;

public interface AdminService {
    Admin create(Admin admin) throws Exception;
    Admin authenticate(String email, String password) throws Exception;
    Admin findByEmail(String email);
    boolean existsByEmail(String email);
    List<Admin> findAll();
    void deleteById(Integer id) throws Exception;
}
