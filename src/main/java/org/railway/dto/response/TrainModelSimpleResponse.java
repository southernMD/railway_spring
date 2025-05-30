package org.railway.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.railway.entity.TrainModel;

@Data
@EqualsAndHashCode(callSuper = true)
public class TrainModelSimpleResponse extends TrainModel {
    private Integer totalSeats;
}
