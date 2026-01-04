package ar.edu.unnoba.poo2025.torneos.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.unnoba.poo2025.torneos.domain.model.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Admin findByEmail(String email);
    boolean existsByEmail(String email);
}
