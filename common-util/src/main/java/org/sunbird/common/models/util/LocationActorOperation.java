package org.sunbird.common.models.util;

/** Created by arvind on 18/4/18. */
public enum LocationActorOperation {
  CREATE_LOCATION("createLocation"),
  UPDATE_LOCATION("updateLocation"),
  SEARCH_LOCATION("searchLocation"),
  DELETE_LOCATION("deleteclLocation"),
  READ_LOCATION_TYPE("readLocationType"),
  UPSERT_LOCATION_TO_ES("upsertLocationDataToES");

  private String value;

  /**
   * constructor
   *
   * @param value String
   */
  LocationActorOperation(String value) {
    this.value = value;
  }

  /**
   * returns enum value
   *
   * @return
   */
  public String getValue() {
    return this.value;
  }
}