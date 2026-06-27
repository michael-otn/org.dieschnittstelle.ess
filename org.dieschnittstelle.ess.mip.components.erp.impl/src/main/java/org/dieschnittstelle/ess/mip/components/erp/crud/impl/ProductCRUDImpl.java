package org.dieschnittstelle.ess.mip.components.erp.crud.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.dieschnittstelle.ess.entities.erp.AbstractProduct;
import org.dieschnittstelle.ess.entities.erp.Campaign;
import org.dieschnittstelle.ess.mip.components.erp.crud.api.ProductCRUD;

import java.util.List;

@ApplicationScoped
@Transactional
public class ProductCRUDImpl implements ProductCRUD {

    @Inject
    @EntityManagerProvider.ERPDataAccessor
    private EntityManager em;

    @Override
    public AbstractProduct createProduct(AbstractProduct prod) {
        return em.merge(prod);
    }

    @Override
    public List<AbstractProduct> readAllProducts() {
        Query query = em.createQuery("SElECT p FROM AbstractProduct p");
        return query.getResultList();
    }

    @Override
    public AbstractProduct updateProduct(AbstractProduct update) {
        return em.merge(update);
    }

    @Override
    public AbstractProduct readProduct(long productID) {
        return em.find(AbstractProduct.class, productID);
    }

    @Override
    public boolean deleteProduct(long productID) {
        try {
            AbstractProduct p = this.readProduct(productID);
            if (p == null) {
                return false;
            }
            em.remove(p);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            //throw new InternalServerErrorException(e.getMessage());
            return false;
        }
    }

    @Override
    public List<Campaign> getCampaignsForProduct(long productID) {
        Query query = em.createQuery(
                "SELECT DISTINCT c FROM Campaign c " +
                        "JOIN c.bundles b " +
                        "WHERE b.product.id = :productId",
                Campaign.class
        );
        query.setParameter("productId", productID);
        return query.getResultList();
    }
}

