package ar.edu.unnoba.poo2025.torneos.service;

import java.util.List;

import org.springframework.stereotype.Service;
import ar.edu.unnoba.poo2025.torneos.Repository.AdminRepository;
import ar.edu.unnoba.poo2025.torneos.Util.PasswordEncoder;
import ar.edu.unnoba.poo2025.torneos.models.Admin;
import ar.edu.unnoba.poo2025.torneos.exceptions.BadRequestException;
import ar.edu.unnoba.poo2025.torneos.exceptions.ResourceAlreadyExistsException;
import ar.edu.unnoba.poo2025.torneos.exceptions.ResourceNotFoundException;

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
    public Admin authenticate(String email, String password) {
        Admin admin = adminRepository.findByEmail(email);
        if (admin == null) {
            // En lugar de una Exception generica, lanzamos la que definimos
            throw new ResourceNotFoundException("Admin no encontrado");
        }
        
        if (!passwordEncoder.verify(password, admin.getPassword())) {
            // lo mismo aca.
            throw new BadRequestException("Contraseña incorrecta");
        }
        return admin;
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
    public void deleteById(Integer id) throws Exception {   //TODO: exeption
        adminRepository.deleteById(id);
    }



    @Override
    public void deleteAdmin(Integer idToDelete, Integer requesterId) {
        if (idToDelete.equals(requesterId)) {
            throw new BadRequestException("No puedes eliminar tu propia cuenta");
        }
        if (!adminRepository.existsById(idToDelete)) {
            throw new ResourceNotFoundException("El administrador no existe");
        }
        adminRepository.deleteById(idToDelete);
    }
}
