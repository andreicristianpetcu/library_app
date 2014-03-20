package com.cegeka.domain.confirmation;

import com.cegeka.domain.users.UserEntity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "CONFIRMATION_TOKEN")
public class ConfirmationToken {
    public static enum ConfirmationState {
        GENERATED, VALIDATED, EXPIRED
    }

    @JoinColumn(unique = true)
    @OneToOne(optional = false)
    private UserEntity user;

    @Id
    private String token;

    @Enumerated(EnumType.STRING)
    private ConfirmationState state = ConfirmationState.GENERATED;

    @Column(name="creation_time", nullable = false)
    private Date creationTime = new Date();

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ConfirmationState getState() {
        return state;
    }

    public void setState(ConfirmationState state) {
        this.state = state;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }
}


