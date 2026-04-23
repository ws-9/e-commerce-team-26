package team26.e_commerce_backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "sellers")
public class Seller {
  @Id
  private Long id;

  @OneToOne
  @MapsId
  @JoinColumn(name = "id")
  private User user;

  @Column(name = "businessName")
  private String businessName;

  @Column(name = "description")
  private String description;

  @Column(name = "verified")
  private Boolean verified = true;

  @Column(name = "taxId")
  private String taxId;

  @Column(name = "contactEmail")
  private String contactEmail;

  public Seller() {}

  public Seller(
      User user,
      String businessName,
      String description,
      String taxId,
      String contactEmail) {
    this.user = user;
    this.businessName = businessName;
    this.description = description;
    this.taxId = taxId;
    this.contactEmail = contactEmail;
    this.verified = true;
  }

  public Long getId() {
    return id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getBusinessName() {
    return businessName;
  }

  public void setBusinessName(String businessName) {
    this.businessName = businessName;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Boolean getVerified() {
    return verified;
  }

  public void setVerified(Boolean verified) {
    this.verified = verified;
  }

  public String getTaxId() {
    return taxId;
  }

  public void setTaxId(String taxId) {
    this.taxId = taxId;
  }

  public String getContactEmail() {
    return contactEmail;
  }

  public void setContactEmail(String contactEmail) {
    this.contactEmail = contactEmail;
  }
}
