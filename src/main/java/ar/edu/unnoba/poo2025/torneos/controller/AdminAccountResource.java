package ar.edu.unnoba.poo2025.torneos.controller;

import java.util.List;
import java.util.Map;
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
import ar.edu.unnoba.poo2025.torneos.domain.model.Admin;
import ar.edu.unnoba.poo2025.torneos.dto.AdminAccountResponseDTO;
import ar.edu.unnoba.poo2025.torneos.dto.AuthenticationRequestDTO;
import ar.edu.unnoba.poo2025.torneos.service.AdminService;

@RestController
@RequestMapping ("/admin/accounts")
public class AdminAccountResource {
    private final AdminService adminService;
    private final JwtTokenUtil jwtTokenUtil;

    public AdminAccountResource(AdminService adminService, JwtTokenUtil jwtTokenUtil) {
        this.adminService = adminService;
        this.jwtTokenUtil = jwtTokenUtil;
    }
    private Admin getCurrentAdmin(String authenticationHeader) throws Exception {
        jwtTokenUtil.validateToken(authenticationHeader);
        String email = jwtTokenUtil.getSubject(authenticationHeader);
        Admin current = adminService.findByEmail(email);
        if (current == null) {
            throw new Exception("Admin del token no encontrado");
        }
        return current;
    }


    @GetMapping(produces = "application/json")
    public ResponseEntity<?> getAll(@RequestHeader("Authorization") String authenticationHeader){
        try {
            getCurrentAdmin(authenticationHeader);
            List<Admin> admins = adminService.findAll();
            List<AdminAccountResponseDTO> response = admins.stream()
                    .map(a -> new AdminAccountResponseDTO(a.getIdAdmin(), a.getEmail()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", e.getMessage()));
        }
    }



    @PostMapping(produces = "application/json")
    public ResponseEntity<?> create(@RequestHeader("Authorization") String authenticationHeader, @RequestBody AuthenticationRequestDTO dto) {
        try {
            getCurrentAdmin(authenticationHeader);

            Admin admin = new Admin();
            admin.setEmail(dto.getEmail());
            admin.setPassword(dto.getPassword());   //TODO investigar si va en el service

            Admin created = adminService.create(admin);
            AdminAccountResponseDTO response = new AdminAccountResponseDTO(created.getIdAdmin(), created.getEmail());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e){
            HttpStatus status = e.getMessage() != null && e.getMessage().contains("ya utilizado")
                    ? HttpStatus.BAD_REQUEST
                    : HttpStatus.UNAUTHORIZED;

            return ResponseEntity.status(status)
                    .body(Map.of("error", e.getMessage()));
        }
    }



    @DeleteMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<?> delete(@RequestHeader("Authorization") String authenticationHeader, 
                                    @PathVariable("id") Integer id) {
        try {
            Admin current = getCurrentAdmin(authenticationHeader);

            //Delegar la acción al servicio
            adminService.deleteAdmin(id, current.getIdAdmin());

            return ResponseEntity.noContent().build();
            
        } catch (Exception e) {
            HttpStatus status = e.getMessage().contains("No puedes eliminar") 
                    ? HttpStatus.BAD_REQUEST 
                    : HttpStatus.UNAUTHORIZED;

            return ResponseEntity.status(status)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
