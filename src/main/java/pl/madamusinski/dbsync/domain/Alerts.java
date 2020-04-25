package pl.madamusinski.dbsync.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "alerts", schema = "public")
public class Alerts {

    @Id
    @NotNull
    @Column(name = "id")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "message", length = -1, nullable = false)
    private String message;
    @Column(name = "code", nullable = false)
    private Integer code;
//    @Generated(GenerationTime.ALWAYS)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "time_stamp")
    private Date timeStamp;

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(Objects.isNull(o) && !(o instanceof Alerts)) return false;
        Alerts that = (Alerts)o;
        return Objects.equals(that.getId(), this.getId());
    }

    @Override
    public int hashCode(){
        return Objects.hashCode(getId());
    }

    @Override
    public String toString(){
        return "AlertsSyncOne{id: " + getId()
                + ", message: '" + getMessage() + "'"
                + ", code: " + getCode()
                + ", timestamp: " + getTimeStamp() + "}";
    }
}
