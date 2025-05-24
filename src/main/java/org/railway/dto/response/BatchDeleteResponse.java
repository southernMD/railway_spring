package org.railway.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchDeleteResponse {
    private List<Long> success = new ArrayList<>();
    private List<Long> failed = new ArrayList<>();
}