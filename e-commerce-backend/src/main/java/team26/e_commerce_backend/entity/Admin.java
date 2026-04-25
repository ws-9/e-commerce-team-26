package team26.e_commerce_backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "admins")
public class Admin {
  @Id
  @Column(name = "user_id")
  Long userId;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  @MapsId
  @JoinColumn(name = "user_id")
  User user;

  @Column(name = "enabled")
  Boolean enabled;

  public Admin() {}

  public Admin(User user, boolean enabled) {
    this.user = user;
    this.enabled = enabled;
  }

  public Admin(User user) {
    this(user, true);
  }

  public Long getUserId() {
    return userId;
  }

  public User getUser() {
    return user;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }
}
