package com.manage_store_api.main.Request;

import jakarta.validation.constraints.PositiveOrZero;

public class CreateConvertionRequest {
    @PositiveOrZero(message = "rateApplied no puede ser un numero negativo")
    private Float rateApplied;

    @PositiveOrZero(message = "oficialRate no puede ser un numero negativo")
    private Float oficialRate;

    public Float getRateApplied() {
        return rateApplied;
    }

    public void setRateApplied(Float rateApplied) {
        this.rateApplied = rateApplied;
    }

    public Float getOficialRate() {
        return oficialRate;
    }

    public void setOficialRate(Float oficialRate) {
        this.oficialRate = oficialRate;
    }
}
