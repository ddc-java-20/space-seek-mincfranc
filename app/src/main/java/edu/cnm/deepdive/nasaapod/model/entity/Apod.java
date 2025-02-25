package edu.cnm.deepdive.nasaapod.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.net.URL;
import java.time.LocalDate;

@Entity(tableName = "apod")
public class Apod {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "apod_id")
  private long Id;

  @ColumnInfo(index = true, collate = ColumnInfo.NOCASE)
  @Expose
  @NonNull
  private String title;

  @ColumnInfo(collate = ColumnInfo.NOCASE)
  @Expose
  @SerializedName("explanation")
  private String description;

  @Expose
  @NonNull
  private LocalDate date;

  @Expose
  private String copyright;

  @ColumnInfo(name = "media_type", index = true)
  @Expose
  @SerializedName("media_type")
  @NonNull
  private MediaType mediaType;

  @ColumnInfo(name = "low_def_url")
  @Expose
  @SerializedName("url")
  @NonNull
  private URL lowDefUrl;

  @ColumnInfo(name = "high_def_url")
  @Expose
  @SerializedName("hdurl")
  private URL highDefdUrl;

  public long getId() {return Id;}

  public void setId(long id) {Id = id;}

  @NonNull
  public String getTitle() {return title;}

  public void setTitle(@NonNull String title) {this.title = title;}

  public String getDescription() {return description;}

  public void setDescription(String description) {this.description = description;}

  @NonNull
  public LocalDate getDate() {return date;}

  public void setDate(@NonNull LocalDate date) {this.date = date;}

  public String getCopyright() {return copyright;}

  public void setCopyright(String copyright) {this.copyright = copyright;}

  @NonNull
  public MediaType getMediaType() {return mediaType;}

  public void setMediaType(@NonNull MediaType mediaType) {this.mediaType = mediaType;}

  @NonNull
  public URL getLowDefUrl() {return lowDefUrl;}

  public void setLowDefUrl(@NonNull URL lowDefUrl) {this.lowDefUrl = lowDefUrl;}

  public URL getHighDefdUrl() {return highDefdUrl;}

  public void setHighDefUrl(URL highDefdUrl) {this.highDefdUrl = highDefdUrl;}

  public enum MediaType {
    @SerializedName("Image")
    IMAGE(true),
    @SerializedName("Video")
    VIDEO(false);

    private final boolean downloadable;

    MediaType(boolean downloadable) {
      this.downloadable = downloadable;
    }

    //this is a generated getter for downloadable
    public boolean isDownloadable() {
      return downloadable;
    }

  }
}
