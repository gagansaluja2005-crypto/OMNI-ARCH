package com.omniarch.talentvela.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "EMPLOYER_PROFILE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // user_id is a foreign key in DB, mapping to User entity.
    @Column(name = "user_id", nullable = false, unique = true)
    private Integer userId; 

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "company_logo")
    private String companyLogo;

    @Column(name = "company_description", columnDefinition = "TEXT")
    private String companyDescription;

    private String industry;

    @Column(name = "company_size")
    private String companySize;

    @Column(name = "founded_year")
    private Integer foundedYear;

    @Column(name = "contact_person")
    private String contactPerson;

    @Column(name = "company_email")
    private String companyEmail;

    @Column(name = "company_phone")
    private String companyPhone;

    @Column(name = "address_line")
    private String addressLine;

    private String city;
    private String state;
    private String country;

    @Column(name = "pin_zip")
    private String pinZip;

    private String website;

    @Column(name = "linkedin_url")
    private String linkedinUrl;

    @Column(name = "about_company", columnDefinition = "TEXT")
    private String aboutCompany;

    @Column(name = "benefits", columnDefinition = "TEXT")
    private String benefits;

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public Integer getUserId() { return userId; }

    public void setUserId(Integer userId) { this.userId = userId; }

    public String getCompanyName() { return companyName; }

    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getCompanyLogo() { return companyLogo; }

    public void setCompanyLogo(String companyLogo) { this.companyLogo = companyLogo; }

    public String getCompanyDescription() { return companyDescription; }

    public void setCompanyDescription(String companyDescription) { this.companyDescription = companyDescription; }

    public String getIndustry() { return industry; }

    public void setIndustry(String industry) { this.industry = industry; }

    public String getCompanySize() { return companySize; }

    public void setCompanySize(String companySize) { this.companySize = companySize; }

    public Integer getFoundedYear() { return foundedYear; }

    public void setFoundedYear(Integer foundedYear) { this.foundedYear = foundedYear; }

    public String getContactPerson() { return contactPerson; }

    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }

    public String getCompanyEmail() { return companyEmail; }

    public void setCompanyEmail(String companyEmail) { this.companyEmail = companyEmail; }

    public String getCompanyPhone() { return companyPhone; }

    public void setCompanyPhone(String companyPhone) { this.companyPhone = companyPhone; }

    public String getAddressLine() { return addressLine; }

    public void setAddressLine(String addressLine) { this.addressLine = addressLine; }

    public String getCity() { return city; }

    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }

    public void setState(String state) { this.state = state; }

    public String getCountry() { return country; }

    public void setCountry(String country) { this.country = country; }

    public String getPinZip() { return pinZip; }

    public void setPinZip(String pinZip) { this.pinZip = pinZip; }

    public String getWebsite() { return website; }

    public void setWebsite(String website) { this.website = website; }

    public String getLinkedinUrl() { return linkedinUrl; }

    public void setLinkedinUrl(String linkedinUrl) { this.linkedinUrl = linkedinUrl; }

    public String getAboutCompany() { return aboutCompany; }

    public void setAboutCompany(String aboutCompany) { this.aboutCompany = aboutCompany; }

    public String getBenefits() { return benefits; }

    public void setBenefits(String benefits) { this.benefits = benefits; }
}
