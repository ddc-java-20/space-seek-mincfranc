package edu.cnm.deepdive.nasaapod.service;

import edu.cnm.deepdive.nasaapod.model.entity.Apod;
import io.reactivex.rxjava3.core.Single;
import java.time.LocalDate;
import java.util.List;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApodService {

  @GET("apod")
  Single<List<Apod>> getDateRange(
      @Query("start_date") LocalDate startDate,
      @Query("end_date") LocalDate endDate,
      @Query("api_key") String apiKey);

}
