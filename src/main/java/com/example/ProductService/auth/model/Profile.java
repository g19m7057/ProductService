package com.example.ProductService.auth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
@Builder
@Table(name = "profiles")
@AllArgsConstructor
@NoArgsConstructor
public class Profile implements UserDetails {
    @Id
    private Long id;

    private String email;

    private String name;

    private String password;

    private String role = "02";

    @Column("is_south_african")
    private Boolean isSouthAfrican;

    private String address;

    @Column("contact_number")
    private String contactNumber;

    @Column("identification_number")
    private String identificationNumber;

    @Column("customer_type")
    private String customerType;

    private Date dob;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
