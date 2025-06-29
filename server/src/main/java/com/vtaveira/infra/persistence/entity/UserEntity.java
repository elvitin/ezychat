package com.vtaveira.infra.persistence.entity;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.vtaveira.domain.model.UserStatus;
import lombok.Data;

@Data
@DatabaseTable(tableName = "users")
public class UserEntity {
  @DatabaseField(generatedId = true)
  private long id;

  @DatabaseField(canBeNull = false, unique = true)
  private String username;

  @DatabaseField(canBeNull = false)
  private String fullName;

  @DatabaseField(canBeNull = false, unique = true)
  private String email;

  @DatabaseField(canBeNull = false)
  private String password;

  @DatabaseField(canBeNull = false)
  private UserStatus status;

  @ForeignCollectionField(eager = true)
  private ForeignCollection<UserGroupEntity> userGroups;

  public UserEntity() {
  }

  public UserEntity(String username, String fullName, String email, String password) {
    this.username = username;
    this.fullName = fullName;
    this.email = email;
    this.password = password;
    this.status = UserStatus.OFFLINE; // Default status
  }
}
