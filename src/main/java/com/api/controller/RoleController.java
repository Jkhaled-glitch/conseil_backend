package com.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.dao.mysql.PermissionDao;
import com.api.dao.mysql.RoleDao;
import com.api.dao.mysql.UserDao;
import com.api.entities.mysql.Permission;
import com.api.entities.mysql.Role;
import com.api.exception.BadRequestException;
import com.api.exception.DuplicateResourceException;
import com.api.exception.ResourceNotFoundException;
import com.api.request.PermissionRequest;
import com.api.service.AuthenticationService;
import com.api.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/roles")

@Tag(name = "Role", description = "handle roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleDao roleDao;

    private final UserDao userDao;

    private final PermissionDao permissionDao;

    private final UserService userService;

    private final AuthenticationService authenticationService;

    // handle Groupes
    @GetMapping()
    public List<Role> getAll() {
        return roleDao.findAll();
    }

    @GetMapping("/{name}")
    public Role getRoleById(@PathVariable String name) {
        String roleName = name.trim().toUpperCase();

        return roleDao.findById(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role " + roleName + " n'existe pas"));
    }

    @GetMapping("permissions/{permissionId}")
    public Permission getPermissionById(@PathVariable Long permissionId) {

        return permissionDao.findById(permissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Permission  n'existe pas"));
    }

    @GetMapping("/{name}/permissions")
    public List<Permission> getPermissionsByRole(@PathVariable String name) {
        Role role = getRoleById(name);
        return role.getPermissions();
    }

    @PostMapping("/{roleName}/permissions/add")
    public Permission addPermission(@PathVariable String roleName, @RequestBody Long permissionId) {
        Role role = getRoleById(roleName);

        Permission permission = getPermissionById(permissionId);

        role.getPermissions().add(permission);

        roleDao.save(role);

        return permission;
    }

    @PostMapping("/{roleName}/permissions/remove")
    public String removePermission(@PathVariable String roleName, @RequestBody Long permissionId) {
        // Récupérer le rôle par son nom
        Role role = getRoleById(roleName);

        // Récupérer la permission par son nom
        Permission permission = getPermissionById(permissionId);

        // Vérifier si la permission existe dans le rôle avant de tenter de la supprimer
        if (role.getPermissions().contains(permission)) {
            role.getPermissions().remove(permission); // Supprimer la permission
            roleDao.save(role); // Sauvegarder les changements dans la base de données
        }

        
        // Déconnecter tous les utilisateurs ayant ce rôle pour s'assurer qu'ils n'ont plus accès
        userService.findAllByRole(role).forEach(user -> authenticationService.revokeAllUserTokens(user));

        // Retourner la permission supprimée
        return "Permission Annuler avec succées";
    }

    @GetMapping("/permissions")
    public List<Permission> getAllPermissions() {

        return permissionDao.findAllByOrderByNameAsc();
    }

    @PostMapping("/permissions/add")
    public Permission addPermission(@RequestBody PermissionRequest request) {
        String name = request.getName();
        String path = request.getPath();
        if (name == null || name.trim().isEmpty()) {
            throw new BadRequestException("Nom de permission ne peut etre vide");
        }

        if (path == null || path.trim().isEmpty()) {
            throw new BadRequestException("Route de permission ne peut etre vide");
        }

        Optional<Permission> existingByName = permissionDao.findByName(name);
        if (existingByName.isPresent()) {
            throw new DuplicateResourceException("La Permission " + name + " existe déjà !");
        }

        Optional<Permission> existingByPath = permissionDao.findByPath(path);
        if (existingByPath.isPresent()) {
            Permission existing = existingByPath.get();
            throw new DuplicateResourceException("Une permission appelée '" + existing.getName() + "' avec le path '" + path + "' existe déjà !");
        }

        Permission entity = new Permission();
        entity.setName(name);
        entity.setPath(path);

        return permissionDao.save(entity);
    }

    @PostMapping("/add")
    public Role add(@RequestBody String role) {

        if (role == null || role.trim().isEmpty()) {
            throw new BadRequestException("Role ne peut etre vide");
        }

        // Convertir le nom du rôle en majuscules
        role = role.trim().toUpperCase();

        Optional<Role> existing = roleDao.findById(role);
        if (existing.isPresent()) {
            throw new DuplicateResourceException("Le rôle " + role + " existe déjà !");
        }

        Role entity = new Role();
        entity.setName(role);

        return roleDao.save(entity);
    }

    @DeleteMapping("{name}")
    @Transactional
    public String delete(@PathVariable String name) {

        Role role = getRoleById(name);

        // verifier d'abord si il ya des utilisateur qui est de ce role existe deja 
        if (userDao.existsByRoleAndIsDeletedFalse(role)) {
            throw new BadRequestException("Role déja utiliser par des utilsateus");
        }

//mettre a jour le role de ses utilisateur a null 
        userDao.updateRoleToNullForUsers(role);

        roleDao.delete(role);

        return "Role " + role.getName() + " supprimé avec sucées!";
    }

}
