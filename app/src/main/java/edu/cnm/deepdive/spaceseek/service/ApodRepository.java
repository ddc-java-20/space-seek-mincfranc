package edu.cnm.deepdive.spaceseek.service;

import android.content.Context;
import android.util.Log;
import androidx.lifecycle.LiveData;
import dagger.hilt.android.qualifiers.ApplicationContext;
import edu.cnm.deepdive.spaceseek.R;
import edu.cnm.deepdive.spaceseek.model.dao.ApodDao;
import edu.cnm.deepdive.spaceseek.model.entity.Apod;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Executors;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ApodRepository {

  private static final LocalDate SERVICE_START_DATE = LocalDate.of(1995, 6, 16);
  private static final String TAG = ApodRepository.class.getSimpleName();

  private final ApodProxyService proxyService;
  private final ApodDao apodDao;
  private final LocalDate firstApodDate;
  private final String apiKey;

  private final io.reactivex.rxjava3.core.Scheduler scheduler = Schedulers.from(
      Executors.newFixedThreadPool(4));

  @Inject
  public ApodRepository(
      @ApplicationContext Context context,
      ApodProxyService proxyService,
      ApodDao apodDao) {
    this.proxyService = proxyService;
    this.apodDao = apodDao;
    this.firstApodDate = LocalDate.parse(context.getString(R.string.first_apod_date));
    this.apiKey = context.getString(R.string.api_key);
  }

  public Completable fetch(LocalDate startDate, LocalDate endDate) {
    LocalDate validatedStartDate = startDate.isBefore(firstApodDate) ? firstApodDate : startDate;
    validateDates(validatedStartDate, endDate);

    return (endDate.isBefore(LocalDate.now())
        ? proxyService.getDateRange(validatedStartDate, endDate, apiKey)
        : proxyService.getOpenDateRange(validatedStartDate, apiKey))
        .flatMapCompletable(apods -> apodDao.insert(apods))
        .subscribeOn(scheduler)
        .doOnError(throwable -> Log.e(TAG, "Error during fetch operation", throwable));
  }

  public Completable fetchRandomApods(int count) {
    return proxyService.getRandomApods(count, apiKey)
        .flatMapCompletable(apods -> apodDao.insert(apods))
        .subscribeOn(scheduler)
        .doOnError(throwable -> Log.e(TAG, "Error fetching random APODs", throwable));
  }

  public Completable fetchApodsForDateAcrossYears(LocalDate birthDate) {
    LocalDate startDate = birthDate.withYear(SERVICE_START_DATE.getYear());
    LocalDate endDate = birthDate.withYear(LocalDate.now().getYear());

    return proxyService.getSpecificDateAcrossYears(startDate, endDate, apiKey)
        .flatMapCompletable(apods -> apodDao.insert(apods))
        .subscribeOn(scheduler)
        .doOnError(
            throwable -> Log.e(TAG, "Error fetching APODs for date across years", throwable));
  }

  public LiveData<Apod> get(long id) {
    return apodDao.select(id);
  }

  public LiveData<List<Apod>> getFavorites() {
    return apodDao.getFavorites();
  }

  public LiveData<List<Apod>> get(LocalDate startDate, @Nullable LocalDate endDate) {
    if (endDate == null) {
      return apodDao.selectOpenRange(startDate);
    }
    return apodDao.selectRange(startDate, endDate);
  }

  private void validateDates(LocalDate startDate, LocalDate endDate) {
    if (startDate.isAfter(endDate)) {
      throw new IllegalArgumentException("Start date cannot be after end date.");
    }
  }
}