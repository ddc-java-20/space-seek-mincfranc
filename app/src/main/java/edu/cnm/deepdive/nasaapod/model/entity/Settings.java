package edu.cnm.deepdive.nasaapod.model.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "settings")
public class Settings {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "settings_id")
  private long settingsId;

  @ColumnInfo(name = "dark_mode")
  private boolean darkMode;

  @ColumnInfo(name = "notifications_enabled")
  private boolean notificationsEnabled;

  @ColumnInfo(name = "default_start_screen")
  private String defaultStartScreen;

  public long getSettingsId() {
    return settingsId;
  }


}
