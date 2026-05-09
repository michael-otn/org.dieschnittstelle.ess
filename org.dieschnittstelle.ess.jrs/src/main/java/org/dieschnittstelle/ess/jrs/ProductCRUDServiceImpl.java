package org.dieschnittstelle.ess.jrs;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Context;
import org.apache.logging.log4j.Logger;
import org.dieschnittstelle.ess.entities.GenericCRUDExecutor;
import org.dieschnittstelle.ess.entities.erp.AbstractProduct;
import org.dieschnittstelle.ess.entities.erp.IndividualisedProductItem;

import java.util.List;

/*
 * TODO JRS2: implementieren Sie hier die im Interface deklarierten Methoden
 */

public class ProductCRUDServiceImpl implements IProductCRUDService {
	protected static Logger logger = org.apache.logging.log4j.LogManager.getLogger(ProductCRUDServiceImpl.class);
	private GenericCRUDExecutor<AbstractProduct> productCRUD;

	public ProductCRUDServiceImpl(@Context ServletContext servletContext, @Context HttpServletRequest request) {
		logger.info("<constructor>: " + servletContext + "/" + request);
		this.productCRUD = (GenericCRUDExecutor<AbstractProduct>) servletContext.getAttribute("productCRUD");
		logger.debug("read out the productCRUD from the servlet context: " + this.productCRUD);
	}

	@Override
	public AbstractProduct createProduct(
			AbstractProduct prod) {
		return (AbstractProduct) this.productCRUD.createObject(prod);
	}

	@Override
	public List<AbstractProduct> readAllProducts() {
		return (List) this.productCRUD.readAllObjects();
	}

	@Override
	public AbstractProduct updateProduct(long id,
	                                     AbstractProduct update) {
		update.setId(id);
		return (AbstractProduct) this.productCRUD.updateObject(update);
	}

	@Override
	public boolean deleteProduct(long id) {
		AbstractProduct productToDelete = this.productCRUD.readObject(id);
		if (productToDelete == null){
			throw new NotFoundException("Item not found");
		}
		return this.productCRUD.deleteObject(id);
	}

	@Override
	public AbstractProduct readProduct(long id) {
		AbstractProduct product =  this.productCRUD.readObject(id);
		if (product == null) {
			throw new NotFoundException("Item with the id: " + id + " was not found");
		}
		return product;
	}

}
