package org.fusesource.ide.camel.model.service.internal;

import java.net.URL;

import org.fusesource.ide.camel.model.service.core.ICamelManagerService;

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
	
	/* (non-Javadoc)
	 * @see org.fusesource.ide.camel.model.service.core.ICamelManagerService#getComponentModelURL()
	 */
	@Override
	public URL getComponentModelURL() {
		return CamelServiceImplementationActivator.getDefault().getBundle().getEntry(COMPONENTS_CATALOG_FILE);
	}
	
	/* (non-Javadoc)
	 * @see org.fusesource.ide.camel.model.service.core.ICamelManagerService#getEipModelURL()
	 */
	@Override
	public URL getEipModelURL() {
		return CamelServiceImplementationActivator.getDefault().getBundle().getEntry(EIPS_CATALOG_FILE);
	}
	
	/* (non-Javadoc)
	 * @see org.fusesource.ide.camel.model.service.core.ICamelManagerService#getDataFormatModelURL()
	 */
	@Override
	public URL getDataFormatModelURL() {
		return CamelServiceImplementationActivator.getDefault().getBundle().getEntry(DATAFORMATS_CATALOG_FILE);
	}
	
	/* (non-Javadoc)
	 * @see org.fusesource.ide.camel.model.service.core.ICamelManagerService#getLanguageModelURL()
	 */
	@Override
	public URL getLanguageModelURL() {
		return CamelServiceImplementationActivator.getDefault().getBundle().getEntry(LANGUAGES_CATALOG_FILE);
	}
}
