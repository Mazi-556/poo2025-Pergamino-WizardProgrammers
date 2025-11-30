package ar.edu.unnoba.poo2025.torneos.service;

import java.util.List;

import org.springframework.stereotype.Service;
import ar.edu.unnoba.poo2025.torneos.Repository.AdminRepository;
import ar.edu.unnoba.poo2025.torneos.Util.PasswordEncoder;
import ar.edu.unnoba.poo2025.torneos.models.Admin;
import ar.edu.unnoba.poo2025.torneos.exceptions.ResourceAlreadyExistsException;

@Service
public class AdminServiceImp implements AdminService {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminServiceImp(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Admin create(Admin admin){
        if (existsByEmail(admin.getEmail())) {
            throw new ResourceAlreadyExistsException("Email ya utilizado por otro admin");
        }
        String hashed = passwordEncoder.encode(admin.getPassword());
        admin.setPassword(hashed);

        return adminRepository.save(admin);
    }
    @Override
    public Admin authenticate(String email, String password) throws Exception {
        Admin db = adminRepository.findByEmail(email);
        if (db == null) {
            throw new Exception("Admin no encontrado");
        }
        boolean ok = passwordEncoder.verify(password, db.getPassword());
        if (!ok) {
            throw new Exception("Password incorrecto");
        }

        return db;
    }
    @Override
    public Admin findByEmail(String email) {
        return adminRepository.findByEmail(email);
    }
    @Override
    public boolean existsByEmail(String email){
        return adminRepository.existsByEmail(email);
    }
    @Override
    public List<Admin> findAll(){
        return adminRepository.findAll();
    }
    @Override
    public void deleteById(Integer id) throws Exception {
        adminRepository.deleteById(id);
    }
}
