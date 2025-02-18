package com.project.storemanager_api.domain.pay.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.project.storemanager_api.domain.pay.entity.PaymentDetail.PaymentType;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDetailResponseDto {

    // 조회용 리스폰스
    private Long paymentId;
    private String placeName;
    private Integer paymentAmount;
    private PaymentType paymentType;
    @Setter
    private List<Map<String, Object>> menuInfo;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonIgnore
    private String menuList;



}
