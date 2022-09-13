package com.example.myCinema.appUser;

import static com.example.myCinema.appUser.AppUserPermission.ADMIN_READ;
import static com.example.myCinema.appUser.AppUserPermission.ADMIN_WRITE;
import static com.example.myCinema.appUser.AppUserPermission.USER_READ;
import static com.example.myCinema.appUser.AppUserPermission.USER_WRITE;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.google.common.collect.Sets;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum AppUserRole {

    USER(Sets.newHashSet(USER_READ, USER_WRITE)),
    ADMIN(Sets.newHashSet(USER_READ, USER_WRITE, ADMIN_READ, ADMIN_WRITE));

    private Set<AppUserPermission> permissions;


    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        
        // adding permissions and making them simpleGrantedAuthorities
        Set<SimpleGrantedAuthority> permissions = getPermissions()
            .stream()
            .map(permission -> new SimpleGrantedAuthority(permission.name()))
            .collect(Collectors.toSet());

        // adding role as simpleGrantedAuthority
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        return permissions;
    }


    public void setGrantedAuthorities(Set<AppUserPermission> permissions) {

        this.permissions = permissions;
    }
}