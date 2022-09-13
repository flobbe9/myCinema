package com.example.myCinema.appUser;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Contains user data and some account status fields. Implements UserDetails for neccessary account methods.
 * 
 * <p>Implements UserDetails.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AppUser implements UserDetails {

    @Id
    @GeneratedValue(generator = "_appUser_id_sequence", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "_appUser_id_sequence", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    /** Serves as username. */
    @Column(nullable = false, unique = true)
    @EqualsAndHashCode.Include
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String adress;

    @Column(nullable = false)
    private Integer zipCode;

    @Column(nullable = false)
    private String city;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(nullable = false)
    private LocalDate birthday;

    @Column(nullable = false)
    private Long age;
        
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppUserRole role;

    /** Subscription or similar could expire. */
    private boolean isExpired = false;

    /** Account can be locked e.g. for safety reasons. */
    private boolean isLocked = false;
    
    /** Account has to be enabled after registering. Login impossible otherwise. */
    private boolean isEnabled = false;


    public AppUser(String firstName, 
                   String lastName, 
                   String email, 
                   String password, 
                   String adress, 
                   Integer zipCode,
                   String city, 
                   LocalDate birthday,
                   AppUserRole role) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.adress = adress;
        this.zipCode = zipCode;
        this.city = city;
        this.birthday = birthday;
        this.role = role;
    }
    
    
    public Long calculateAge(LocalDate birthDay) {

        return birthDay.until(LocalDate.now(), ChronoUnit.YEARS);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return role.getGrantedAuthorities();
    }


    @Override
    public String getUsername() {

        return this.email;
    }


    @Override
    public boolean isAccountNonExpired() {

        return !this.isExpired;
    }


    @Override
    public boolean isAccountNonLocked() {

        return !this.isLocked;
    }


    @Override
    public boolean isCredentialsNonExpired() {

        return true;
    }


    @Override
    public boolean isEnabled() {

        return this.isEnabled;
    }


    @Override
    public String toString() {

        return this.firstName + " " + this.lastName + ", " + this.email;
    }
}


/**
 * Wrapper class for some enums helping thymeleaf to pass data to api.
 */
@Getter
class AppUserWrapper {
    
    private AppUserPermission[] permissions = AppUserPermission.values();

    /** True value means the permission with the same index in permissions array will be granted. */
    private boolean[] granted = new boolean[permissions.length];

    private AppUserRole[] roles = AppUserRole.values();
}