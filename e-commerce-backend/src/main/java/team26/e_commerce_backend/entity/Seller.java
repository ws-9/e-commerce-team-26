package team26.e_commerce_backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "sellers")
public class Seller {
  @Id
  @Column(name = "user_id")
  Long userId;

  @OneToOne
  @MapsId
  @JoinColumn(name = "user_id")
  User user;

  @Column(name = "business_name")
  String businessName;

  @Column(name = "description")
  String description;

  @Column(name = "verified")
  Boolean verified;

  @Column(name = "taxId")
  String taxId;

  @Column(name = "contact_email")
  String contactEmail;

  public Seller() {}

  public Seller(
      User user,
      String businessName,
      String description,
      Boolean verified,
      String taxId,
      String contactEmail) {
    this.user = user;
    this.businessName = businessName;
    this.description = description;
    this.verified = verified;
    this.taxId = taxId;
    this.contactEmail = contactEmail;
  }

  public Seller(
      User user, String businessName, String description, String taxId, String contactEmail) {
    this.user = user;
    this.businessName = businessName;
    this.description = description;
    this.verified = true;
    this.taxId = taxId;
    this.contactEmail = contactEmail;
  }

  public Long getUserId() {
    return userId;
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
