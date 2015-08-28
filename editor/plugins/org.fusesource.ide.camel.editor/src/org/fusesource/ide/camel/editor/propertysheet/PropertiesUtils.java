/*******************************************************************************
 * Copyright (c) 2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.fusesource.ide.camel.editor.propertysheet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.corext.util.JavaConventionsUtil;
import org.fusesource.ide.camel.model.Endpoint;
import org.fusesource.ide.camel.model.service.core.catalog.components.Component;
import org.fusesource.ide.camel.model.service.core.catalog.components.UriParameter;
import org.fusesource.ide.camel.model.service.core.catalog.components.UriParameterKind;

/**
 * @author lhein
 */
public class PropertiesUtils {
	
	public static UriParameter getUriParam(String name, Component c) {
		return getUriParam(name, c.getUriParameters());
	}
	
	public static UriParameter getUriParam(String name, List<UriParameter> uriParams) {
		for (UriParameter p : uriParams) {
			if (p.getName().equals(name)) {
				return p;
			}
		}
		return null;
	}
	
	public static Component getComponentFor(Endpoint selectedEP) {
		if (selectedEP != null && selectedEP.getUri() != null) {
            int protocolSeparatorIdx = selectedEP.getUri().indexOf(":");
            if (protocolSeparatorIdx != -1) {
                return CamelComponentUtils.getComponentModel(selectedEP.getUri().substring(0, protocolSeparatorIdx));
            }
		}
		return null;
	}
	
	public static List<UriParameter> getPathProperties(Endpoint selectedEP) {
		ArrayList<UriParameter> result = new ArrayList<UriParameter>();

        if (selectedEP != null && selectedEP.getUri() != null) {
            int protocolSeparatorIdx = selectedEP.getUri().indexOf(":");
            if (protocolSeparatorIdx != -1) {
                Component componentModel = CamelComponentUtils.getComponentModel(selectedEP.getUri().substring(0, protocolSeparatorIdx));
                if (componentModel != null) {
                    for (UriParameter p : componentModel.getUriParameters()) {
                        if (p.getKind() != null && p.getKind().equalsIgnoreCase("path")) {
                        	result.add(p);
                        }
                    }
                }
            }
        }

        return result;
	}
	
    /**
     * 
     * @param kind
     * @return
     */
    public static List<UriParameter> getPropertiesFor(Endpoint selectedEP, UriParameterKind kind) {
        ArrayList<UriParameter> result = new ArrayList<UriParameter>();

        if (selectedEP != null && selectedEP.getUri() != null) {
            int protocolSeparatorIdx = selectedEP.getUri().indexOf(":");
            if (protocolSeparatorIdx != -1) {
                Component componentModel = CamelComponentUtils.getComponentModel(selectedEP.getUri().substring(0, protocolSeparatorIdx));
                if (componentModel != null) {
                    for (UriParameter p : componentModel.getUriParameters()) {
                        if (kind == UriParameterKind.CONSUMER) {
                        	if (p.getLabel() != null && p.getLabel().equalsIgnoreCase("consumer")) {
                        		result.add(p);
                        	}
                        } else if (kind == UriParameterKind.PRODUCER) {
                        	if (p.getLabel() != null && p.getLabel().equalsIgnoreCase("producer")) {
                        		result.add(p);
                        	}
                        } else if (kind == UriParameterKind.BOTH) {
                        	if (p.getLabel() == null) {
                        		result.add(p);
                        	}
                        }
                    }
                }
            }
        }

        return result;
    }

    /**
     * 
     * @param p
     * @return
     */
    public static String getPropertyFromUri(Endpoint selectedEP, UriParameter p, Component c) {
    	// we need to distinguish between parameters in the uri part after the ? char
    	if (p.getKind() != null && p.getKind().equalsIgnoreCase("parameter")) {
	    	int idx = selectedEP.getUri().indexOf(p.getName() + "=");
	        if (idx != -1) {
	            return selectedEP.getUri().substring(idx + (p.getName() + "=").length(),
	                    selectedEP.getUri().indexOf('&', idx + 1) != -1 ? selectedEP.getUri().indexOf('&', idx + 1) : selectedEP.getUri().length());
	        } else {
	        	// no value defined....return the default
	        	if (p.getDefaultValue() != null && p.getDefaultValue().trim().length()>0) return p.getDefaultValue();
	        }
	    // and those which are part of the part between the scheme: and the ? char
        } else if (p.getKind() != null && p.getKind().equalsIgnoreCase("path")) {
        	// first get the delimiters
        	String delimiters = getDelimitersAsString(c.getSyntax(), c.getUriParameters());
        	// now get the uri without scheme and options
        	String uri = selectedEP.getUri().substring(selectedEP.getUri().indexOf(":") + 1, selectedEP.getUri().indexOf("?") != -1 ? selectedEP.getUri().indexOf("?") : selectedEP.getUri().length());
        	
        	// sometimes there is only one field, so there are no delimiters
        	if (delimiters.length()<1) {
        		// we just return the full uri
        		return uri;
        	} else {
        		return getPathMap(selectedEP, c).get(p.getName());       
        	}
        }
    	// all other cases are unsupported atm
        return null;
    }
    
    private static int getFieldIndex(String delimiters, String syntax, String fieldName) {
    	int idx = -1;
    	StringTokenizer syntaxTok = new StringTokenizer(syntax, delimiters);
		while (syntaxTok.hasMoreTokens()) {
			idx++;
			String fName = syntaxTok.nextToken();
    		if (fName.equals(fieldName)) break;
		}
    	return idx;
    }
    
    /**
     * strips off the scheme and field names from the syntax and then returns all remaining delimiters
     *  
     * @param syntax
     * @param params
     * @return
     */
    private static String getDelimitersAsString(String syntax, List<UriParameter> params) {
    	String delimiterString = syntax;
    	final String syntaxWithoutScheme = delimiterString.substring(delimiterString.indexOf(":")+1);
    	
    	// first strip off the <scheme>:
    	delimiterString = syntaxWithoutScheme;
    	
    	Collections.sort(params, new Comparator<UriParameter>() {
    		/* (non-Javadoc)
    		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
    		 */
    		@Override
    		public int compare(UriParameter o1, UriParameter o2) {
    			return o2.getName().length() - o1.getName().length();
    		}
    	});
    	
    	// then strip off the remaining variable names
    	for (UriParameter p : params) {
    		if (CamelComponentUtils.isUriPathParameter(p)) {
    			delimiterString = delimiterString.replace(p.getName(), "");
    		}
    	}
    	return delimiterString;
    }

    /**
     * EXPERIMENTAL
     * 
     * @param selectedEP
     * @param c
     * @return
     */
    private static Map<String, String> getPathMap(Endpoint selectedEP, Component c) {
    	HashMap<String, String> retVal = new HashMap<String, String>();

    	// get all path params
    	List<UriParameter> pathParams = getPathProperties(selectedEP);
    	// get all delimiters
    	String delimiters = getDelimitersAsString(c.getSyntax(), pathParams);
    	// now get the uri without scheme and options
    	String uri = selectedEP.getUri().substring(selectedEP.getUri().indexOf(":") + 1, selectedEP.getUri().indexOf("?") != -1 ? selectedEP.getUri().indexOf("?") : selectedEP.getUri().length());
    	
    	Map<Integer, UriParameter> fieldMapping = new HashMap<Integer, UriParameter>();
    	for (UriParameter param : pathParams) {
    		int idx = getFieldIndex(delimiters, c.getSyntax().substring(c.getSyntax().indexOf(":")+1), param.getName());
    		fieldMapping.put(idx, param);
    	}

    	int lastPos = 0;
		int skippedDelimiters = 0;
    	for (int field=0; field < delimiters.length()+1; field++) {
    		UriParameter uriParam = fieldMapping.get(field);
    		boolean required = uriParam.getRequired() != null && uriParam.getRequired().equalsIgnoreCase("true");
    		if (skippedDelimiters>0) {
    			if (!required) {
    				retVal.put(uriParam.getName(), null);
    				skippedDelimiters--;
    				continue;
    			}
    		}
    		int foundPos = -1;
    		for (int delIdx = field+skippedDelimiters; delIdx < delimiters.length(); delIdx++) {
    			char delim = delimiters.charAt(delIdx);
    			int pos = uri.indexOf(delim, lastPos);
    			if (pos != -1) {
    				foundPos = pos;
    				break;
    			} else {
    				skippedDelimiters++;
    			}
    		}
    		if (foundPos != -1) {
    			retVal.put(uriParam.getName(), uri.substring(lastPos, foundPos));
    			lastPos = foundPos+1;
    		} else {
    			// no delimiters found, so we have only one value
    			String v = uri.substring(lastPos);
    			if (delimiters.endsWith("/") && field == delimiters.length()) v = "/" + v;
    			String fieldName = null;
    			while (fieldName == null && uriParam != null) {
    				// this check is required if we start with an optional field and its not in the uri
    				if (field == 0 && (uriParam.getRequired() == null || uriParam.getRequired().equalsIgnoreCase("false"))) {
        				uriParam = fieldMapping.get(++field);
        			} else {
        				fieldName = uriParam.getName();
        			}
    			}
    			retVal.put(fieldName, v);
    			break;
    		}
    	}
    	
    	return retVal; 
    }
    
    /**
     * 
     * @param p
     * @return
     */
    public static Object getTypedPropertyFromUri(Endpoint selectedEP, UriParameter p, Component c) {
        String val = getPropertyFromUri(selectedEP, p, c);

        if (CamelComponentUtils.isBooleanProperty(p)) {
            return Boolean.parseBoolean(val);
        }

        if (CamelComponentUtils.isTextProperty(p)) {
            return val;
        }

        if (CamelComponentUtils.isNumberProperty(p)) {
            return val;
        }

        return null;
    }
    
    /**
     * updates the uri for the changed value
     * 
     * @param p
     * @param value
     */
    public static void updateURIParams(Endpoint selectedEP, UriParameter p, Object value, Component c, IObservableMap modelMap) {
    	if (p.getKind().equalsIgnoreCase("path")) {
    		// simply rebuild the uri
    		String newUri = "";
    		
    		// first build the path part
    		String syntax = c.getSyntax();
    		List<UriParameter> pathParams = getPathProperties(selectedEP);
    		for (UriParameter pparam : pathParams) {
    			String val = "";
    			if (p.getName().equals(pparam.getName())) {
    				val = value.toString();
    			} else {
    				val = modelMap.get(pparam.getName()).toString();
    			}
    			if (val.trim().length()<1) val = pparam.getDefaultValue();
    			if (val != null && val.startsWith("/") && !CamelComponentUtils.isFileProperty(pparam)) val = val.substring(1);
    			if (val != null) syntax = syntax.replace(pparam.getName(), val);
    		}
    		newUri += syntax + "?";
    		
    		// now build the options
    		for (UriParameter uriParam : c.getUriParameters()) {
    			if (uriParam.getKind().equalsIgnoreCase("path")) continue;
    			String pName = uriParam.getName();
    			String pValue = getPropertyFromUri(selectedEP, uriParam, c);
    			if (pValue == null || pValue.trim().length()<1) continue;
    			
    			// remove values wich equal the default
    			if (uriParam.getDefaultValue() != null && uriParam.getDefaultValue().trim().length()>0 && pValue.equals(uriParam.getDefaultValue())) continue;
    			
    			if (newUri.endsWith("?") == false) newUri += "&";
    			newUri += String.format("%s=%s", pName, pValue);
    		}
    		
    		if (newUri.endsWith("?")) newUri = newUri.substring(0, newUri.length()-1);
    		
    		selectedEP.setUri(newUri);
    	} else {
	    	// normal uri options
    		boolean valueDeleted = value == null || value.toString().trim().length()<1 || (p.getDefaultValue() != null && value.toString().equals(p.getDefaultValue()));
	        String val = getPropertyFromUri(selectedEP, p, c);
	        String key = String.format("%s=", p.getName());
	        int idx = selectedEP.getUri().indexOf(key);
	        if (val != null && idx != -1) {
	            // special replace logic needed as special expression chars can break the replacement
	            String newUri = "";
	            boolean firstParam = idx == -1 || selectedEP.getUri().charAt(idx-1) == '?';
	            newUri = valueDeleted ? selectedEP.getUri().substring(0, firstParam ? idx : idx-1) : selectedEP.getUri().substring(0, idx + key.length());
	            if (!valueDeleted) newUri += value.toString();
	            if (valueDeleted && firstParam) {
	                newUri += selectedEP.getUri().substring(idx + key.length() + val.length());
	            } else {
	                newUri += selectedEP.getUri().substring(idx + key.length() + val.length());
	            }
	            if (newUri.indexOf("?&") != -1) newUri = newUri.replace("?&", "?");
	            if (newUri.endsWith("?")) newUri = newUri.substring(0, newUri.indexOf("?"));
	            selectedEP.setUri(newUri);
	        } else {
	            String newUri = selectedEP.getUri();
	            if (valueDeleted == false) {
		            if (selectedEP.getUri().indexOf('?') == -1) {
		                newUri += '?';
		            }
		            if (selectedEP.getUri().indexOf('=') != -1) {
		                newUri += '&';
		            }
	            	newUri += String.format("%s=%s", p.getName(), value.toString());
	            }
	            selectedEP.setUri(newUri);
	        }
    	}
    }
    
    public static String getUsedProtocol(Endpoint selectedEP) {
        return selectedEP.getUri().substring(0, selectedEP.getUri().indexOf(':'));
    }
    
    /**
     * Checks if the package field has to be pre-filled in this page and returns the package
     * fragment to be used for that. The package fragment has the name of the project if the source
     * folder does not contain any package and if the project name is a valid package name. If the
     * source folder contains exactly one package then the name of that package is used as the
     * package fragment's name. <code>null</code> is returned if none of the above is applicable.
     * 
     * @param javaProject the containing Java project of the selection used to initialize this page
     * 
     * @return the package fragment to be pre-filled in this page or <code>null</code> if no
     *         suitable package can be suggested for the given project
     * 
     * @since 3.9
     */
    public static IPackageFragment getPackage(IJavaProject javaProject, final IPackageFragmentRoot pkgFragmentRoot) {
        String packName= null;
        IJavaElement[] packages= null;
        try {
            if (pkgFragmentRoot != null && pkgFragmentRoot.exists()) {
                packages= pkgFragmentRoot.getChildren();
                if (packages.length == 1) { // only default package -> use Project name
                    packName= javaProject.getElementName();
                    // validate package name
                    IStatus status= validatePackageName(packName, javaProject);
                    if (status.getSeverity() == IStatus.OK) {
                        return pkgFragmentRoot.getPackageFragment(packName);
                    }
                } else {
                    int noOfPackages= 0;
                    IPackageFragment thePackage= null;
                    for (final IJavaElement pack : packages) {
                        IPackageFragment pkg= (IPackageFragment) pack;
                        // ignoring empty parent packages and default package
                        if ((!pkg.hasSubpackages() || pkg.hasChildren()) && !pkg.isDefaultPackage()) {
                            noOfPackages++;
                            thePackage= pkg;
                            if (noOfPackages > 1) {
                                return null;
                            }
                        }
                    }
                    if (noOfPackages == 1) { // use package name
                        packName= thePackage.getElementName();
                        return pkgFragmentRoot.getPackageFragment(packName);
                    }
                }
            }
        } catch (JavaModelException e) {
            // fall through
        }
        return null;
    }

    public static IStatus validatePackageName(String text, IJavaProject project) {
        if (project == null || !project.exists()) {
            return JavaConventions.validatePackageName(text, JavaCore.VERSION_1_3, JavaCore.VERSION_1_3);
        }
        return JavaConventionsUtil.validatePackageName(text, project);
    }
}
