package com.manage_store_api.main.Request;

import com.manage_store_api.main.Utils.Enums.SalespPaymentMethod;
import com.manage_store_api.main.Utils.SaleItem;
import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.Type;

import java.util.List;

public class CreateSaleRequest {
    @Size(min = 1, max = 100, message = "La direccion debe tener entre 1 y 100 caracteres")
    private String address;

    @PositiveOrZero(message = "Total no puede ser un numero negativo")
    private Float total;

    @PositiveOrZero(message = "Rate aplicado no puede ser un numero negativo")
    private Float appliedRate;

    @PositiveOrZero(message = "Rate oficial no puede ser un numero negativo")
    private Float oficialRate;

    @NotNull(message = "El metodo de pago es mandatorio")
    private SalespPaymentMethod paymentMethod;

    @NotNull(message = "El cliente es mandatorio")
    private Long client_id;

    @NotBlank(message = "La referencia de pago es obligatoria para PAGO_MOVIL o TRANSFERENCIA",
            groups = {PaymentReferenceRequired.class})
    private String paymentReference;

    @NotEmpty(message = "La lista de items no puede estar vacía")
    @NotNull(message = "La lista de items es obligatoria")
    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private List<SaleItem> items;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public Float getAppliedRate() {
        return appliedRate;
    }

    public void setAppliedRate(Float appliedRate) {
        this.appliedRate = appliedRate;
    }

    public Float getOficialRate() {
        return oficialRate;
    }

    public void setOficialRate(Float oficialRate) {
        this.oficialRate = oficialRate;
    }

    public SalespPaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(SalespPaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Long getClient_id() {
        return client_id;
    }

    public void setClient_id(Long client_id) {
        this.client_id = client_id;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public List<SaleItem> getItems() {
        return items;
    }

    public void setItems(List<SaleItem> items) {
        this.items = items;
    }

    public interface PaymentReferenceRequired {}

    // Método de validación personalizada
    @AssertTrue(message = "La referencia de pago es obligatoria para PAGO_MOVIL o TRANSFERENCIA")
    private boolean isPaymentReferenceValid() {
        if (paymentMethod == SalespPaymentMethod.PAGO_MOVIL ||
                paymentMethod == SalespPaymentMethod.TRANSFERENCIA) {
            return paymentReference != null && !paymentReference.trim().isEmpty();
        }
        return true;
    }
}
