package org.railway.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.View;

@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "stations_view")
@Immutable
@Data
@View(
    query = "SELECT * FROM stations s WHERE s.status = 1"
)
public class StationView extends Base{
    @Id
    private Integer id;

    @Column(nullable = false, length = 50)
    private String stationName;

    @Column(nullable = false, length = 50)
    private String city;

    @Column(nullable = false, length = 50)
    private String province;

    @Column(length = 200)
    private String address;

    @Column(nullable = false)
    private Integer status;
}
