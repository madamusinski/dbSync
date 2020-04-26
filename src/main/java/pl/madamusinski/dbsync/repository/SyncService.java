package pl.madamusinski.dbsync.repository;

import java.util.Date;
import java.util.List;

public interface SyncService<R> {

    Date lastSyncTime();
    List<R> findOutOfSyncObjects(Date from);
    void synchronize(List<R> toSynchronize);

}
