package ar.edu.unnoba.poo2025.torneos.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.unnoba.poo2025.torneos.Util.JwtTokenUtil;
import ar.edu.unnoba.poo2025.torneos.dto.AuthenticationRequestDTO;
import ar.edu.unnoba.poo2025.torneos.models.Admin;
import ar.edu.unnoba.poo2025.torneos.service.AdminService;

@RestController
@RequestMapping ("/admin")
public class AdminAuthResource {
    private final AdminService adminService;
    private final JwtTokenUtil jwtTokenUtil;

    public AdminAuthResource(AdminService adminService, JwtTokenUtil jwtTokenUtil) {
        this.adminService = adminService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

@PostMapping(path = "/auth", produces = "application/json")
    public ResponseEntity<?> authentication(@RequestBody AuthenticationRequestDTO dto) {
        Admin admin = adminService.authenticate(dto.getEmail(), dto.getPassword());
        String token = jwtTokenUtil.generateToken(admin.getEmail());
        
        java.util.Map<String, Object> userMap = new java.util.HashMap<>();
        userMap.put("id", admin.getIdAdmin());
        userMap.put("email", admin.getEmail());
        userMap.put("name", admin.getName());
        userMap.put("role", "admin");

        return ResponseEntity.ok(Map.of(
            "token", token,
            "user", userMap
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<?> create(@RequestBody Admin admin) {
        Admin created = adminService.create(admin); 
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
        }
}
