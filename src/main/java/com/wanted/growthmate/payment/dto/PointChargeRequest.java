package com.wanted.growthmate.payment.dto;

import com.wanted.growthmate.payment.domain.enums.PaymentMethod;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter  // <- NOTE: setter를 안 넣으면 spring이 값을 못 넣어줘서 null이 됨
public class PointChargeRequest {
    @NotNull(message = "결제 금액은 필수 입력값입니다.")
    @Min(value = 1000, message = "결제 금액은 최소 1000원 이상이어야 합니다.")
    private Integer amount; // 실제 결제 금액 (원)

    @NotNull(message = "결제 수단을 선택해야 합니다.")
    private PaymentMethod paymentMethod;  // 결제 수단
}
