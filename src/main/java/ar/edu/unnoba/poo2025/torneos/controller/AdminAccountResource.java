package ar.edu.unnoba.poo2025.torneos.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.unnoba.poo2025.torneos.Util.JwtTokenUtil;
import ar.edu.unnoba.poo2025.torneos.dto.AdminAccountResponseDTO;
import ar.edu.unnoba.poo2025.torneos.dto.AuthenticationRequestDTO;
import ar.edu.unnoba.poo2025.torneos.exceptions.ResourceNotFoundException;
import ar.edu.unnoba.poo2025.torneos.service.AdminService;
import ar.edu.unnoba.poo2025.torneos.models.Admin;

@RestController
@RequestMapping ("/admin/accounts")
public class AdminAccountResource {
    private final AdminService adminService;
    private final JwtTokenUtil jwtTokenUtil;

    public AdminAccountResource(AdminService adminService, JwtTokenUtil jwtTokenUtil) {
        this.adminService = adminService;
        this.jwtTokenUtil = jwtTokenUtil;
    }
    private Admin getCurrentAdmin(String authenticationHeader){
        jwtTokenUtil.validateToken(authenticationHeader);
        String email = jwtTokenUtil.getSubject(authenticationHeader);
        Admin current = adminService.findByEmail(email);
        if (current == null) {
            throw new ResourceNotFoundException("Admin del token no encontrado");
        }
        return current;
    }


    @GetMapping(produces = "application/json")
    public ResponseEntity<?> getAll(@RequestHeader("Authorization") String authenticationHeader){
        getCurrentAdmin(authenticationHeader);
        List<Admin> admins = adminService.findAll();
        List<AdminAccountResponseDTO> response = admins.stream()
                .map(a -> new AdminAccountResponseDTO(a.getIdAdmin(), a.getEmail()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }



    @PostMapping(produces = "application/json")
    public ResponseEntity<?> create(@RequestHeader("Authorization") String authenticationHeader, @RequestBody AuthenticationRequestDTO dto) {
        getCurrentAdmin(authenticationHeader);

        Admin admin = new Admin();
        admin.setEmail(dto.getEmail());
        admin.setPassword(dto.getPassword());

        Admin created = adminService.create(admin);
        AdminAccountResponseDTO response = new AdminAccountResponseDTO(created.getIdAdmin(), created.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }



    @DeleteMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<?> delete(@RequestHeader("Authorization") String authenticationHeader, 
                                    @PathVariable("id") Integer id) {
        Admin current = getCurrentAdmin(authenticationHeader);
        adminService.deleteAdmin(id, current.getIdAdmin());
        return ResponseEntity.noContent().build();
    }
}
