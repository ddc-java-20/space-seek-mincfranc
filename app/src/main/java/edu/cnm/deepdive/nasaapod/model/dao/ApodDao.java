package edu.cnm.deepdive.nasaapod.model.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import edu.cnm.deepdive.nasaapod.model.entity.Apod;
import io.reactivex.rxjava3.core.Single;
import java.util.Iterator;
import java.util.List;

@Dao
public interface ApodDao {

  //abstract method Room will implement, this is how we tell Room what to do about duplicates
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  Single<List<Long>> insert(List<Apod> apods);

  //iterate in lockstep over 2 lists, 1 list primary key, 1 list
  //use iterator object without having to use 1+ which starts all over again from the beginning for each iteration
  //iterator takes an enhanced for loop and turns into conventional loop, nested for loop
  //a while loop is best
  default Single<List<Apod>> insertAndUpdate(List<Apod> apods) {
    return insert(apods)
        .map((ids) -> {
          Iterator<Apod> apodIter = apods.iterator();
          Iterator<Long> idIter = ids.iterator();
          while (apodIter.hasNext() && idIter.hasNext()) {
            apodIter
                .next()
                .setId(idIter.next());
          }
          apods.removeIf((apod) -> apod.getId() < 0);
          return apods;
    });

  }


}
