package org.dieschnittstelle.ess.mip.components.erp.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.dieschnittstelle.ess.entities.erp.IndividualisedProductItem;
import org.dieschnittstelle.ess.entities.erp.PointOfSale;
import org.dieschnittstelle.ess.entities.erp.StockItem;
import org.dieschnittstelle.ess.mip.components.erp.api.StockSystem;
import org.dieschnittstelle.ess.mip.components.erp.crud.api.PointOfSaleCRUD;
import org.dieschnittstelle.ess.mip.components.erp.crud.impl.StockItemCRUD;
import org.dieschnittstelle.ess.utils.interceptors.Logged;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@ApplicationScoped
@Transactional
@Logged
public class StockSystemImpl implements StockSystem {

    @Inject private PointOfSaleCRUD posCRUD;

    @Inject private StockItemCRUD stockItemCRUD;

    @Override
    public void addToStock(IndividualisedProductItem product, long pointOfSaleId, int units) {
        PointOfSale pos = posCRUD.readPointOfSale(pointOfSaleId);
        StockItem stockItem = stockItemCRUD.readStockItem(product, pos);
        if (stockItem == null) {
            stockItem = new StockItem(product, pos, units);
            stockItemCRUD.createStockItem(stockItem);
            return;
        }
        stockItem.setUnits(stockItem.getUnits() + units);
        stockItemCRUD.updateStockItem(stockItem);
    }

    @Override
    public void removeFromStock(IndividualisedProductItem product, long pointOfSaleId, int units) {
        this.addToStock(product, pointOfSaleId, -units);
    }

    @Override
    public List<IndividualisedProductItem> getProductsOnStock(long pointOfSaleId) {
        return stockItemCRUD.readStockItemsForPointOfSale(posCRUD.readPointOfSale(pointOfSaleId))
                .stream()
                .map(StockItem::getProduct)
                .toList();
    }

    @Override
    public List<IndividualisedProductItem> getAllProductsOnStock() {
        var pos = posCRUD.readAllPointsOfSale();
        List<IndividualisedProductItem> individualisedProductItems = new ArrayList<>();
        for(PointOfSale p : pos){
            List<StockItem> stockItems = stockItemCRUD.readStockItemsForPointOfSale(p);
            for(StockItem stockItem : stockItems){
                individualisedProductItems.add(stockItem.getProduct());
            }
        }
        return new ArrayList<>(new HashSet<>(individualisedProductItems));
    }

    @Override
    public int getUnitsOnStock(IndividualisedProductItem product, long pointOfSaleId) {
        StockItem si = stockItemCRUD.readStockItem(product, posCRUD.readPointOfSale(pointOfSaleId));
        if (si == null) {
            return 0;
        }
        return si.getUnits();
    }

    @Override
    public int getTotalUnitsOnStock(IndividualisedProductItem product) {
        return stockItemCRUD.readStockItemsForProduct(product)
                .stream()
                .mapToInt(StockItem::getUnits)
                .sum();
    }

    @Override
    public List<Long> getPointsOfSale(IndividualisedProductItem product) {
        List<StockItem> stockItems = stockItemCRUD.readStockItemsForProduct(product);
        return stockItems.stream().map(si -> si.getPos().getId()).toList();
    }
}