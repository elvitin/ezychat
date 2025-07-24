package com.vtaveira.infra.persistence.entity;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.vtaveira.domain.model.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@DatabaseTable(tableName = "users")
public class UserEntity {

  @DatabaseField(id = true)
  private String username;

  @DatabaseField(canBeNull = false)
  private String fullName;

  @DatabaseField(canBeNull = false, unique = true)
  private String email;

  @DatabaseField(canBeNull = false)
  private String password;

  @DatabaseField(canBeNull = false)
  private UserStatus status = UserStatus.OFFLINE;

  @ForeignCollectionField(eager = true)
  private ForeignCollection<UserGroupEntity> userGroups;
}
