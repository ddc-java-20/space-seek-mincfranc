package edu.cnm.deepdive.spaceseek.service;

import android.content.Context;
import androidx.lifecycle.LiveData;
import dagger.hilt.android.qualifiers.ApplicationContext;
import edu.cnm.deepdive.spaceseek.R;
import edu.cnm.deepdive.spaceseek.model.dao.ApodDao;
import edu.cnm.deepdive.spaceseek.model.entity.Apod;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.time.LocalDate;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ApodRepository {

  public static final LocalDate SERVICE_START_DATE = LocalDate.of(1995, 6,
      16); // First APOD available

  private final ApodProxyService proxyService;
  private final ApodDao apodDao;
  private final LocalDate firstApodDate;
  private final Scheduler scheduler;
  private final String apiKey;

  @Inject
  ApodRepository(
      @ApplicationContext Context context, ApodProxyService proxyService, ApodDao apodDao) {
    this.proxyService = proxyService;
    this.apodDao = apodDao;
    firstApodDate = LocalDate.parse(
        context.getString(R.string.first_apod_date)); // Retrieve first APOD date from strings.xml
    scheduler = Schedulers.io(); // TODO: 2025-02-25 Investigate a fixed-size pool.
    apiKey = context.getString(R.string.api_key); // API key for NASA APOD service
  }

  public Completable fetch(LocalDate startDate, LocalDate endDate) {
    if (startDate.isBefore(firstApodDate)) {
      startDate = firstApodDate; // Ensure the start date isn't before the first available APOD
    }
    return (!endDate.isBefore(LocalDate.now())  // If end date is NOT before current date
        ? proxyService.getOpenDateRange(startDate, apiKey)
        : proxyService.getDateRange(startDate, endDate, apiKey)
    )
        .flatMapCompletable(apodDao::insert)
        .subscribeOn(scheduler);
  }

  public LiveData<Apod> get(long id) {
    return apodDao.select(id);
  }

  public LiveData<Apod> get() {
    return apodDao.select(LocalDate.now());
  }

  public LiveData<List<Apod>> get(LocalDate startDate, LocalDate endDate) {
    return apodDao.selectRange(startDate, endDate);
  }

  public LiveData<List<Apod>> get(LocalDate startDate) {
    return apodDao.selectOpenRange(startDate);
  }

  // Fetches the list of favorite APODs.
  public LiveData<List<Apod>> getFavorites() {
    return apodDao.getFavorites(); // Delegates the call to ApodDao
  }

  public Completable fetchRandomApods(int count) {
    return proxyService
        .getRandomApods(count, apiKey) // Fetch random APODs
        .flatMapCompletable(apodDao::insert) // Save the results into the database
        .subscribeOn(scheduler); // Perform the operation on a background thread
  }

  // Fetches APODs for a specific birthdate across all years.
  public Completable fetchApodsForDateAcrossYears(LocalDate birthDate) {
    LocalDate startDate = birthDate.withYear(
        SERVICE_START_DATE.getYear()); // Start at June 16, 1995
    LocalDate endDate = birthDate.withYear(LocalDate.now().getYear()); // End at the current year

    return proxyService.getSpecificDateAcrossYears(startDate, endDate,
            apiKey) // Use newly added proxy service method
        .flatMapCompletable(apodDao::insert) // Save the data to the database
        .subscribeOn(scheduler);
  }
}