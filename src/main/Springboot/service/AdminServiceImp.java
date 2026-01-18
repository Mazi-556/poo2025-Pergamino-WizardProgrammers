package ar.edu.unnoba.poo2025.torneos.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ar.edu.unnoba.poo2025.torneos.Util.PasswordEncoder;
import ar.edu.unnoba.poo2025.torneos.domain.model.Admin;
import ar.edu.unnoba.poo2025.torneos.domain.ports.out.AdminRepositoryPort;
import ar.edu.unnoba.poo2025.torneos.infrastructure.adapter.out.persistence.jpa.AdminRepositoryPort;
import ar.edu.unnoba.poo2025.torneos.xexceptions.ResourceAlreadyExistsException;

@Service
public class AdminServiceImp implements AdminService {
    private final AdminRepositoryPort adminRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    public AdminServiceImp(AdminRepositoryPort adminRepositoryPort, PasswordEncoder passwordEncoder) {
        this.adminRepositoryPort = adminRepositoryPort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Admin create(Admin admin){
        if (existsByEmail(admin.getEmail())) {
            throw new ResourceAlreadyExistsException("Email ya utilizado por otro admin");
        }
        String hashed = passwordEncoder.encode(admin.getPassword());
        admin.setPassword(hashed);

        return adminRepositoryPort.save(admin);
    }
    @Override
    public Admin authenticate(String email, String password) throws Exception {
        Admin db = adminRepositoryPort.findByEmail(email);
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
        return adminRepositoryPort.findByEmail(email);
    }
    @Override
    public boolean existsByEmail(String email){
        return adminRepositoryPort.existsByEmail(email);
    }
    @Override
    public List<Admin> findAll(){
        return adminRepositoryPort.findAll();
    }
    @Override
    public void deleteById(Integer id) throws Exception {   //TODO: exeption
        adminRepositoryPort.deleteById(id);
    }



    @Override
    public void deleteAdmin(Integer idToDelete, Integer requesterId) throws Exception { //TODO: exception
        // 1. Regla de Negocio: Autoprotección
        if (idToDelete.equals(requesterId)) {
            throw new Exception("No puedes eliminar tu propia cuenta de administrador.");
        }

        // 2. Validación de existencia (opcional pero recomendada)
        if (!adminRepositoryPort.existsById(idToDelete)) {
            throw new Exception("El administrador a eliminar no existe.");
        }

        // 3. Acción
        adminRepositoryPort.deleteById(idToDelete);
    }
}
