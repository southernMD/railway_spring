package org.railway.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.railway.entity.Train;

@EqualsAndHashCode(callSuper = true)
@Data
public class TrainResponse extends Train {
    private Integer totalSeats;
}
