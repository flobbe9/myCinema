package com.example.myDiningReview.models;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import com.example.myDiningReview.any.AppUserRole;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class AppUser implements UserDetails {
    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "appUserSequence"
    )
    @SequenceGenerator(
        name = "appUserSequence",
        allocationSize = 1
    )
    private Long id;
    private String userName;
    private String password;
    private String email;
    private String city;
    private String state;
    private Integer zipCode;
    private Boolean peanutAllergies;
    private Boolean eggAllergies;
    private Boolean dairyAllergies;
    @Enumerated(EnumType.STRING)
    private AppUserRole role;
    private Boolean enabled = false;
    private Boolean locked = false;

    public AppUser(String userName, 
                   String password,
                   String email,
                   String city, 
                   String state, 
                   Integer zipCode, 
                   Boolean peanutAllergies,
                   Boolean eggAllergies, 
                   Boolean dairyAllergies,
                   AppUserRole role) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.peanutAllergies = peanutAllergies;
        this.eggAllergies = eggAllergies;
        this.dairyAllergies = dairyAllergies;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList("ROLE_" + this.role.name());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled; 
    }
}
