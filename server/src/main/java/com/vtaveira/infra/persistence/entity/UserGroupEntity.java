package com.vtaveira.infra.persistence.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "user_groups")
public class UserGroupEntity {
  @DatabaseField(canBeNull = false, foreign = true)
  private UserEntity user;

  @DatabaseField(canBeNull = false, foreign = true)
  private GroupEntity group;

  public UserGroupEntity() {
  }

  public UserGroupEntity(UserEntity user, GroupEntity group) {
    this.user = user;
    this.group = group;
  }
}
