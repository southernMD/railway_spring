package org.railway.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.railway.entity.TrainModel;

@EqualsAndHashCode(callSuper = true)
@Data
public class TrainModelRequest extends TrainModel {
    @NotBlank(message = "车型名称不能为空")
    @Size(max = 50, message = "车型名称不能超过50个字符")
    private String modelName;

    @NotBlank(message = "车型代码不能为空")
    @Size(max = 20, message = "车型代码不能超过20个字符")
    private String modelCode;

    @NotNull(message = "状态不能为空")
    @Min(value = 0, message = "状态值最小为0")
    @Max(value = 1, message = "状态值最大为1")
    private Integer status;  // 默认值1

    @NotNull(message = "最大载客量不能为空")
    @Positive(message = "最大载客量必须是正数")
    private Integer maxCapacity;

    private String description; // 描述信息不需要校验
}