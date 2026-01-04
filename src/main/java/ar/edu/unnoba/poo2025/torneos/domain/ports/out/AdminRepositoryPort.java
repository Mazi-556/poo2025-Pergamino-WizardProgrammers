package ar.edu.unnoba.poo2025.torneos.domain.ports.out;

import ar.edu.unnoba.poo2025.torneos.domain.model.Admin;
import java.util.List;

public interface AdminRepositoryPort {
    
    Admin save(Admin admin);

    Admin findByEmail(String email);

    boolean existsByEmail(String email);

    List<Admin> findAll();

    void deleteById(Integer id);

    boolean existsById(Integer id);
}