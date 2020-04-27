package pl.madamusinski.dbsync.repository;

import pl.madamusinski.dbsync.domain.Deletions;

import java.util.Date;
import java.util.List;

/**
 * @author Mateusz Adamusiński
 * Interface for SyncService
 * @param <R> you can specify for what kind of entity you want to use
 *           and implement synchronization service. it works only for same two table entities
 */
public interface SyncService<R> {

    Date lastSyncTime();
    List<R> findOutOfSyncObjects(Date from);
    List<Deletions> lookUpDeletedEntries(Date from);
    void synchronizeDeletions(List<Deletions> toSynchronizeDeletions);
    void synchronize(List<R> toSynchronize);

}
