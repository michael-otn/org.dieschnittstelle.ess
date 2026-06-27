package org.dieschnittstelle.ess.mip.components.erp.api.dto;

public class StockItemDTO
{
    private long prodId;
    private long posId;
    private int units;

    public StockItemDTO() {}

    public StockItemDTO(long prodId, long posId, int units){
        this.prodId = prodId;
        this.posId = posId;
        this.units = units;
    }

    public long getPosId() {
        return posId;
    }

    public void setPosId(long posId) {
        this.posId = posId;
    }

    public long getProdId() {
        return prodId;
    }

    public void setProdId(long prodId) {
        this.prodId = prodId;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    @Override
    public String toString() {
        return String.format("StockItemDTO (prodId: %d, posId: %d, units: %d)", prodId, posId, units);
    }
}
