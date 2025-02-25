package edu.cnm.deepdive.nasaapod.service;

import edu.cnm.deepdive.nasaapod.model.dao.ApodDao;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.time.LocalDate;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ApodRepository {

  public static final LocalDate SERVICE_START_DATE = LocalDate.of(1995, 6, 16);

  private final ApodProxyService proxyService;
  private final ApodDao apodDao;
  private final Scheduler scheduler;
  
  @Inject
  ApodRepository(ApodProxyService proxyService, ApodDao apodDao) {
    this.proxyService = proxyService;
    this.apodDao = apodDao;
    scheduler = Schedulers.io(); // TODO: 2/25/25  Investigate a fixed size
  }
  
}
