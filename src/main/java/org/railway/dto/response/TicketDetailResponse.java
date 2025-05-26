package org.railway.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.railway.entity.Ticket;

@EqualsAndHashCode(callSuper = true)
@Data
public class TicketDetailResponse extends Ticket {
}
