package org.fusesource.ide.camel.model.service.internal;

import java.io.IOException;
import java.net.URL;

import org.apache.camel.catalog.CamelCatalog;
import org.apache.camel.catalog.DefaultCamelCatalog;
import org.fusesource.ide.camel.model.service.core.CamelSchemaProvider;
import org.fusesource.ide.camel.model.service.core.ICamelManagerService;
import org.fusesource.ide.camel.model.service.core.adopters.CamelModelLoader;
import org.fusesource.ide.camel.model.service.core.adopters.XmlCamelModelLoader;
import org.fusesource.ide.camel.model.service.core.catalog.CamelModel;

public class CamelService implements ICamelManagerService {

	/**
	 * TODO :	IMPROVE CODE
	 * 
	 * At the moment we just reuse the xml files we already have. In future we will generate the model objects on
	 * the fly using the api functions of camelcatalog class. once thats done these constants are obsolete and
	 * should be removed together with the methods for obtaining the files.
	 */
	private static final String CATALOG_FOLDER = "catalogs";
	
	private static final String COMPONENTS_FILENAME = "components.xml";
	private static final String EIPS_FILENAME = "eips.xml";
	private static final String LANGUAGES_FILENAME = "languages.xml";
	private static final String DATAFORMATS_FILENAME = "dataformats.xml";
	
	private static final String COMPONENTS_CATALOG_FILE = String.format("%s/%s", CATALOG_FOLDER, COMPONENTS_FILENAME);
	private static final String EIPS_CATALOG_FILE = String.format("%s/%s", CATALOG_FOLDER, EIPS_FILENAME);
	private static final String LANGUAGES_CATALOG_FILE = String.format("%s/%s", CATALOG_FOLDER, LANGUAGES_FILENAME);
	private static final String DATAFORMATS_CATALOG_FILE = String.format("%s/%s", CATALOG_FOLDER, DATAFORMATS_FILENAME);

	private CamelModelLoader loader;
	private CamelCatalog catalog;
	
	/* (non-Javadoc)
	 * @see org.fusesource.ide.camel.model.service.core.ICamelManagerService#getCamelModel()
	 */
	@Override
	public CamelModel getCamelModel() {
		if (this.loader == null) this.loader = new XmlCamelModelLoader();
		try {
			return loader.getCamelModel(	getComponentModelURL(), 
											getEipModelURL(), 
											getLanguageModelURL(), 
											getDataFormatModelURL());
		} catch (IOException ex) {
			CamelServiceImplementationActivator.pluginLog().logError(ex);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.fusesource.ide.camel.model.service.core.ICamelManagerService#getCamelSchemaProvider()
	 */
	@Override
	public CamelSchemaProvider getCamelSchemaProvider() {
		if (catalog == null) catalog = new DefaultCamelCatalog();
		return new CamelSchemaProvider(catalog.blueprintSchemaAsXml(), catalog.springSchemaAsXml());
	}	
	
	private URL getComponentModelURL() {
		return CamelServiceImplementationActivator.getDefault().getBundle().getEntry(COMPONENTS_CATALOG_FILE);
	}
	
	private URL getEipModelURL() {
		return CamelServiceImplementationActivator.getDefault().getBundle().getEntry(EIPS_CATALOG_FILE);
	}
	
	private URL getDataFormatModelURL() {
		return CamelServiceImplementationActivator.getDefault().getBundle().getEntry(DATAFORMATS_CATALOG_FILE);
	}
	
	private URL getLanguageModelURL() {
		return CamelServiceImplementationActivator.getDefault().getBundle().getEntry(LANGUAGES_CATALOG_FILE);
	}
}
