package com.vtaveira.infra.persistence.entity;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "groups")
public class GroupEntity {
  @DatabaseField(generatedId = true)
  private long id;

  @DatabaseField(canBeNull = false, unique = true)
  private String name;

  @ForeignCollectionField(eager = true)
  private ForeignCollection<UserGroupEntity> userGroups;

  public GroupEntity() {
  }

  public GroupEntity(String name) {
    this.name = name;
  }
}
