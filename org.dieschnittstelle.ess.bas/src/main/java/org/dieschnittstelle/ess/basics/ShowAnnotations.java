package org.dieschnittstelle.ess.basics;


import org.dieschnittstelle.ess.basics.annotations.AnnotatedStockItemBuilder;
import org.dieschnittstelle.ess.basics.annotations.StockItemProxyImpl;

import java.lang.reflect.Field;

import static org.dieschnittstelle.ess.utils.Utils.*;

public class ShowAnnotations {

	public static void main(String[] args) {
		// we initialise the collection
		StockItemCollection collection = new StockItemCollection(
				"stockitems_annotations.xml", new AnnotatedStockItemBuilder());
		// we load the contents into the collection
		collection.load();

		for (IStockItem consumable : collection.getStockItems()) {
			showAttributes(((StockItemProxyImpl) consumable).getProxiedObject());
		}

		// we initialise a consumer
		Consumer consumer = new Consumer();
		// ... and let them consume
		consumer.doShopping(collection.getStockItems());
	}

	/*
	 * TODO BAS2
	 */
	private static void showAttributes(Object instance) {
		show("class is: " + instance.getClass());

		try {
			Class<?> klass = instance.getClass();

			StringBuilder sb = new StringBuilder();
			sb.append("{");
			sb.append(klass.getSimpleName()).append(" ");

			java.lang.reflect.Field[] fields = klass.getDeclaredFields();

			for (int i = 0; i < fields.length; i++) {
				java.lang.reflect.Field field = fields[i];
				String fieldName = field.getName();
				// Getter dynamisch erzeugen (Konvention!)
				String getterName =
						"get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);

				Object value = null;
				try {
					// Methode zur Laufzeit suchen
					java.lang.reflect.Method getter = klass.getMethod(getterName);
					// Führt den Getter aus, ohne ihn statisch zu kennen
					value = getter.invoke(instance);

				} catch (NoSuchMethodException e) {
					// Falls kein Getter existiert → direkt über Field
					field.setAccessible(true);
					value = field.get(instance);
				}

				sb.append(fieldName).append(":").append(value);

				if (i < fields.length - 1) {
					sb.append(", ");
				}
			}

			sb.append("}");

			show(sb.toString());
			// TODO BAS3: if the new @DisplayAs annotation is present on a field,
			//  the string representation will not use the field's name, but the name
			//  specified in the the annotation. Regardless of @DisplayAs being present
			//  or not, the field's value will be included in the string representation.

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("showAttributes(): exception occurred: " + e, e);
		}
	}
}