package ar.edu.unnoba.poo2025.torneos.service;

import java.util.List;

import ar.edu.unnoba.poo2025.torneos.domain.model.Admin;

public interface AdminService {
    Admin create(Admin admin);
    Admin authenticate(String email, String password) throws Exception;
    Admin findByEmail(String email);
    boolean existsByEmail(String email);
    List<Admin> findAll();
    void deleteById(Integer id) throws Exception;
    void deleteAdmin(Integer idToDelete, Integer requesterId) throws Exception;
}
