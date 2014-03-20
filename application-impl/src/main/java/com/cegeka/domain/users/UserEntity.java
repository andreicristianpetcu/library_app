package com.cegeka.domain.users;

import com.cegeka.application.Role;
import com.google.common.collect.Sets;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Locale;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;

@Entity
@Table(name = "USERS")
public class UserEntity {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    private String password;

    @ElementCollection(targetClass = Role.class,fetch = FetchType.EAGER)
    @CollectionTable(name = "USERS_ROLES")
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = Sets.newHashSet();

    @Column(unique = true)
    private String email;

    @Transient
    private Locale locale = Locale.ENGLISH; //TODO: who should provide this?

    private boolean confirmed = false; //TODO: there should be enabled and confirmed, security on enabled (user admin too), confirmed for email confirmation

    @OneToOne(cascade = ALL)
    @JoinColumn(referencedColumnName = "ID", nullable = false)
    private UserProfileEntity profile = new UserProfileEntity();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public UserProfileEntity getProfile() {
        return profile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return !(id != null ? !id.equals(that.id) : that.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}

