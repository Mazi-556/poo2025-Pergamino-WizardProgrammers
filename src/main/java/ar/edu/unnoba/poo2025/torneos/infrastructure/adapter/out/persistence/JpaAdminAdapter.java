package ar.edu.unnoba.poo2025.torneos.infrastructure.adapter.out.persistence;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.edu.unnoba.poo2025.torneos.domain.ports.out.AdminRepositoryPort;
import ar.edu.unnoba.poo2025.torneos.infrastructure.adapter.out.persistence.jpa.AdminRepository;
import ar.edu.unnoba.poo2025.torneos.domain.model.Admin;

@Component
public class JpaAdminAdapter implements AdminRepositoryPort {

    @Autowired
    private AdminRepository adminRepository;

    public JpaAdminAdapter(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public boolean existsByEmail(String email) {
        return adminRepository.existsByEmail(email);
    }

    @Override
    public Admin findByEmail(String email) {
        return adminRepository.findByEmail(email);
    }
//Estos 2 metodos son los que estan en AdminRepository
//--------------------------------------------------------
//Los demas metodos son los metodos que estan "escondidos" por jpa (metodos genericos como save, findAll, deleteById, etc)


    @Override
    public Admin save(Admin admin) {
        return adminRepository.save(admin);
    }

    @Override
    public List<Admin> findAll() {
        return adminRepository.findAll();
    }


    @Override
    public void deleteById(Integer id) {
        adminRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        return adminRepository.existsById(id);
    }


}
