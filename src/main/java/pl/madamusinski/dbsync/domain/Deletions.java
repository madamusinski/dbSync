package pl.madamusinski.dbsync.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;


/**
 * @author Mateusz Adamusiński
 * Deletions entity
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "deletions", schema = "public")
public class Deletions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "id_deleted", nullable = false)
    private Integer idDeleted;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "time_stamp")
    private Date timeStamp;

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(Objects.isNull(o) && !(o instanceof Deletions)) return false;
        Deletions that = (Deletions) o;
        return Objects.equals(that.getId(), this.getId());
    }

    @Override
    public int hashCode(){
        return Objects.hashCode(getId());
    }

    @Override
    public String toString(){
        return "Deletions{id: " + getId()
                + ", id_deleted: " + getIdDeleted()
                + ", timestamp: " + getTimeStamp() + "}";
    }
}
