package microservice.user.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "phone")
public class PhoneEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private Long number;
  private int cityCode;
  private String countryCode;

  @JsonBackReference
  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserEntity user;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PhoneEntity that = (PhoneEntity) o;
    return cityCode == that.cityCode && Objects.equals(id, that.id) && Objects.equals(number, that.number) && Objects.equals(countryCode, that.countryCode) && Objects.equals(user, that.user);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, number, cityCode, countryCode, user);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getNumber() {
    return number;
  }

  public void setNumber(Long number) {
    this.number = number;
  }

  public int getCityCode() {
    return cityCode;
  }

  public void setCityCode(int cityCode) {
    this.cityCode = cityCode;
  }

  public String getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }

  public UserEntity getUser() {
    return user;
  }

  public void setUser(UserEntity user) {
    this.user = user;
  }
}
