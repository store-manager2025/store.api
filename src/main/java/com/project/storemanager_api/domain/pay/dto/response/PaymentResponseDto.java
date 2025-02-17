package com.project.storemanager_api.domain.pay.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

import static com.project.storemanager_api.domain.pay.entity.PaymentDetail.PaymentType;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDto {

    // 조회용 리스폰스
    private Long paymentId;
    private String placeName;
    private Integer paymentAmount;
    private Integer discountAmount;
    private String menuNames;
    private PaymentType paymentType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;


}
