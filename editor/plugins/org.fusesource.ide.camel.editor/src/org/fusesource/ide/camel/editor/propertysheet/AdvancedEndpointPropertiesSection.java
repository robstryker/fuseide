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

import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.Observables;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.map.WritableMap;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.internal.core.SourceType;
import org.eclipse.jdt.internal.ui.dialogs.FilteredTypesSelectionDialog;
import org.eclipse.jdt.internal.ui.wizards.NewClassCreationWizard;
import org.eclipse.jdt.ui.wizards.NewClassWizardPage;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.project.IMavenProjectFacade;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.internal.forms.widgets.FormsResources;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.fusesource.ide.camel.editor.AbstractNodes;
import org.fusesource.ide.camel.editor.Activator;
import org.fusesource.ide.camel.editor.utils.DiagramUtils;
import org.fusesource.ide.camel.model.AbstractNode;
import org.fusesource.ide.camel.model.Endpoint;
import org.fusesource.ide.camel.model.catalog.components.Component;
import org.fusesource.ide.camel.model.catalog.components.UriParameter;
import org.fusesource.ide.camel.model.catalog.components.UriParameterKind;
import org.fusesource.ide.commons.ui.Selections;
import org.fusesource.ide.foundation.core.util.Strings;

/**
 * @author lhein
 */
public class AdvancedEndpointPropertiesSection extends AbstractPropertySection {

    private FormToolkit toolkit;
    private Form form;
    private CTabFolder tabFolder;
    private CTabItem pathTab;
    private CTabItem commonTab;
    private CTabItem consumerTab;
    private CTabItem producerTab;
    private Endpoint selectedEP;
    private DataBindingContext dbc;
    private IObservableMap modelMap = new WritableMap();
    private Component component;
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#dispose()
     */
    @Override
    public void dispose() {
        if (toolkit != null) {
            toolkit.dispose();
            toolkit = null;
        }
        this.component = null;
        super.dispose();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#setInput
     * (org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
     */
    @Override
    public void setInput(IWorkbenchPart part, ISelection selection) {
        super.setInput(part, selection);
        
        this.dbc = new DataBindingContext();
//        this.modelMap.clear();
        
        Object o = Selections.getFirstSelection(selection);
        AbstractNode n = AbstractNodes.toAbstractNode(o);
        
        if (n instanceof Endpoint) {
            this.selectedEP = (Endpoint) n;
            this.component = PropertiesUtils.getComponentFor(selectedEP);
            form.setText("Advanced Properties - " + DiagramUtils.filterFigureLabel(selectedEP.getDisplayText()));
        } else {
            this.selectedEP = null;
            form.setText("Advanced Properties");
        }
        
        int idx = Math.max(tabFolder.getSelectionIndex(), 0);

        if (pathTab != null) 		pathTab.dispose();
        if (commonTab != null)      commonTab.dispose();
        if (consumerTab != null)    consumerTab.dispose();
        if (producerTab != null)    producerTab.dispose();

        // now generate the tab contents
        createPathTab(tabFolder);
        createCommonsTab(tabFolder);
        createConsumerTab(tabFolder);
        createProducerTab(tabFolder);
        
        tabFolder.setSingle(tabFolder.getItemCount()==1);
        
        tabFolder.setSelection(Math.min(idx, tabFolder.getItemCount()-1));
    }
    
    /**
     * 
     * @param props
     * @param page
     */
    protected void generateTabContents(List<UriParameter> props, final Composite page, boolean ignorePathProperties) {
        // display all the properties in alphabetic order - sorting needed
        Collections.sort(props, new Comparator<UriParameter>() {
            /* (non-Javadoc)
             * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
             */
            @Override
            public int compare(UriParameter o1, UriParameter o2) {
                return o1.getName().compareTo(o2.getName());
            }
        }); 
        
        for (UriParameter p : props) {
        	// atm we don't want to care about path parameters
        	if (ignorePathProperties && p.getKind() != null && p.getKind().equalsIgnoreCase("path")) continue;
            
        	final UriParameter prop = p;
            
            ISWTObservableValue uiObservable = null;
            IObservableValue modelObservable = null;
            IValidator validator = null;
            
            String s = Strings.humanize(p.getName());
            if (p.getDeprecated() != null && p.getDeprecated().equalsIgnoreCase("true")) s += " (deprecated)"; 
            
            Label l = toolkit.createLabel(page, s);            
            l.setLayoutData(new GridData());
            if (p.getDescription() != null) {
            	l.setToolTipText(p.getDescription());
            }
            if (p.getRequired() != null && p.getRequired().equalsIgnoreCase("true")) {
            	l.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
            }
            
            Control c = null;
            
            // BOOLEAN PROPERTIES
            if (CamelComponentUtils.isBooleanProperty(prop)) {
                Button checkBox = toolkit.createButton(page, "", SWT.CHECK | SWT.BORDER);
                Boolean b = (Boolean)PropertiesUtils.getTypedPropertyFromUri(selectedEP, prop, component);
                checkBox.setSelection(b);
                checkBox.addSelectionListener(new SelectionAdapter() {
                    /* (non-Javadoc)
                     * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
                     */
                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        PropertiesUtils.updateURIParams(selectedEP, prop, ((Button)e.getSource()).getSelection(), component, modelMap);
                    }
                });
                checkBox.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
                c = checkBox;
                
                //initialize the map entry
                modelMap.put(p.getName(), checkBox.getSelection());
                // create observables for the control
                uiObservable = WidgetProperties.selection().observe(checkBox);
                
            // TEXT PROPERTIES
            } else if (CamelComponentUtils.isTextProperty(prop)) {
                Text txtField = toolkit.createText(page, PropertiesUtils.getPropertyFromUri(selectedEP, prop, component), SWT.SINGLE | SWT.BORDER | SWT.LEFT);
                txtField.addModifyListener(new ModifyListener() {
                    @Override
                    public void modifyText(ModifyEvent e) {
                        Text txt = (Text)e.getSource();
                        PropertiesUtils.updateURIParams(selectedEP, prop, txt.getText(), component, modelMap);
                    }
                });
                txtField.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
                c = txtField;
                //initialize the map entry
                modelMap.put(p.getName(), txtField.getText());
                // create observables for the control
                uiObservable = WidgetProperties.text(SWT.Modify).observe(txtField);
                if (p.getRequired() != null && p.getRequired().equalsIgnoreCase("true")) {
					validator = new IValidator() {
						/*
						 * (non-Javadoc)
						 * @see org.eclipse.core.databinding.validation.IValidator#validate(java.lang.Object)
						 */
						@Override
						public IStatus validate(Object value) {
							if (selectedEP.getUri().startsWith("ref:")) {
								// check for broken refs
								String refId = selectedEP.getUri().trim().length()>"ref:".length() ? selectedEP.getUri().substring("ref:".length()) : null;
								if (refId == null || refId.trim().length()<1 || selectedEP.getParent().getParent().getCamelContextEndpointUris().get(refId) == null) {
									return ValidationStatus.error("The entered reference does not exist in your context!");
								}
							}
							
							if (value != null && value instanceof String && value.toString().trim().length()>0) {
								return ValidationStatus.ok();
							}
							return ValidationStatus.error("Parameter " + prop.getName() + " is a mandatory field and cannot be empty.");
						}
					};
                }
                
            // NUMBER PROPERTIES
            } else if (CamelComponentUtils.isNumberProperty(prop)) {
                Text txtField = toolkit.createText(page, PropertiesUtils.getPropertyFromUri(selectedEP, prop, component), SWT.SINGLE | SWT.BORDER | SWT.RIGHT);
                txtField.addModifyListener(new ModifyListener() {
                    @Override
                    public void modifyText(ModifyEvent e) {
                        Text txt = (Text)e.getSource();
                        String val = txt.getText();
                        try {
                        	Double.parseDouble(val);
                        	txt.setBackground(ColorConstants.white);
                            PropertiesUtils.updateURIParams(selectedEP, prop, txt.getText(), component, modelMap);
                        } catch (NumberFormatException ex) {
                        	// invalid character found
                            txt.setBackground(ColorConstants.red);
                            return;
                        }
                    }
                });
                txtField.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
                c = txtField;
                //initialize the map entry
                modelMap.put(p.getName(), txtField.getText());
                // create observables for the control
                uiObservable = WidgetProperties.text(SWT.Modify).observe(txtField);                
                validator = new IValidator() {
					/*
					 * (non-Javadoc)
					 * @see org.eclipse.core.databinding.validation.IValidator#validate(java.lang.Object)
					 */
					@Override
					public IStatus validate(Object value) {
						if (prop.getRequired() != null && prop.getRequired().equalsIgnoreCase("true") && (value == null || value.toString().trim().length()<1)) {
							return ValidationStatus.error("Parameter " + prop.getName() + " is a mandatory field and cannot be empty.");
						}
						// only check non-empty fields
						if (value != null && value.toString().trim().length()>0) {
							try {
								Double.parseDouble(value.toString());
							} catch (NumberFormatException ex) {
								return ValidationStatus.error("The parameter " + prop.getName() + " requires a numeric value.");
							}
						}
						return ValidationStatus.ok();
					}
				};

			// CHOICE PROPERTIES
            } else if (CamelComponentUtils.isChoiceProperty(prop)) {
                CCombo choiceCombo = new CCombo(page, SWT.BORDER | SWT.FLAT | SWT.READ_ONLY | SWT.SINGLE);
                toolkit.adapt(choiceCombo, true, true);
                choiceCombo.setEditable(false);
                choiceCombo.setItems(CamelComponentUtils.getChoices(prop));
                for (int i=0; i < choiceCombo.getItems().length; i++) {
                    if (choiceCombo.getItem(i).equalsIgnoreCase(PropertiesUtils.getPropertyFromUri(selectedEP, prop, component))) {
                        choiceCombo.select(i);
                        break;
                    }
                }
                choiceCombo.addSelectionListener(new SelectionAdapter() {
                    /* (non-Javadoc)
                     * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
                     */
                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        CCombo choice = (CCombo)e.getSource();
                        PropertiesUtils.updateURIParams(selectedEP, prop, choice.getText(), component, modelMap);
                    }
                });
                choiceCombo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
                c = choiceCombo;
                //initialize the map entry
                modelMap.put(p.getName(), choiceCombo.getText());
                // create observables for the control
                uiObservable = WidgetProperties.selection().observe(choiceCombo);                
                if (p.getRequired() != null && p.getRequired().equalsIgnoreCase("true")) {
					validator = new IValidator() {
						/*
						 * (non-Javadoc)
						 * @see org.eclipse.core.databinding.validation.IValidator#validate(java.lang.Object)
						 */
						@Override
						public IStatus validate(Object value) {
							if (value != null && value instanceof String && value.toString().trim().length()>0) {
								return ValidationStatus.ok();
							}
							return ValidationStatus.error("Parameter " + prop.getName() + " is a mandatory field and cannot be empty.");
						}
					};
                }
            // FILE PROPERTIES
            } else if (CamelComponentUtils.isFileProperty(prop)) {
                final Text txtField = toolkit.createText(page, PropertiesUtils.getPropertyFromUri(selectedEP, prop, component), SWT.SINGLE | SWT.BORDER | SWT.LEFT);
                txtField.addModifyListener(new ModifyListener() {
                    @Override
                    public void modifyText(ModifyEvent e) {
                        Text txt = (Text)e.getSource();
                        PropertiesUtils.updateURIParams(selectedEP, prop, txt.getText(), component, modelMap);
                    }
                });
                txtField.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
                
                Button btn_browse = toolkit.createButton(page, "...", SWT.BORDER | SWT.PUSH);
                btn_browse.addSelectionListener(new SelectionAdapter() {
                    /* (non-Javadoc)
                     * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
                     */
                    @Override
                    public void widgetSelected(SelectionEvent e) {
                    	DirectoryDialog dd = new DirectoryDialog(page.getShell());
                        String pathName = dd.open();
                        if (pathName != null) {
                            txtField.setText(pathName);
                        }
                    }
                });
                btn_browse.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
                c = txtField;
                if (p.getRequired() != null && p.getRequired().equalsIgnoreCase("true")) {
					validator = new IValidator() {
						/*
						 * (non-Javadoc)
						 * @see org.eclipse.core.databinding.validation.IValidator#validate(java.lang.Object)
						 */
						@Override
						public IStatus validate(Object value) {
							if (value != null && value instanceof String && value.toString().trim().length()>0) {
								return ValidationStatus.ok();
							}
							return ValidationStatus.error("Parameter " + prop.getName() + " is a mandatory field and cannot be empty.");
						}
					};
                }
                //initialize the map entry
                modelMap.put(p.getName(), txtField.getText());
                // create observables for the control
                uiObservable = WidgetProperties.text(SWT.Modify).observe(txtField);                

            // EXPRESSION PROPERTIES
            } else if (CamelComponentUtils.isExpressionProperty(prop)) {
                Text txtField = toolkit.createText(page, PropertiesUtils.getPropertyFromUri(selectedEP, prop, component), SWT.SINGLE | SWT.BORDER | SWT.LEFT);
                txtField.addModifyListener(new ModifyListener() {
                    @Override
                    public void modifyText(ModifyEvent e) {
                        Text txt = (Text)e.getSource();
                        PropertiesUtils.updateURIParams(selectedEP, prop, txt.getText(), component, modelMap);
                    }
                });
                txtField.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
                c = txtField;
                if (p.getRequired() != null && p.getRequired().equalsIgnoreCase("true")) {
					validator = new IValidator() {
						/*
						 * (non-Javadoc)
						 * @see org.eclipse.core.databinding.validation.IValidator#validate(java.lang.Object)
						 */
						@Override
						public IStatus validate(Object value) {
							if (value != null && value instanceof String && value.toString().trim().length()>0) {
								return ValidationStatus.ok();
							}
							return ValidationStatus.error("Parameter " + prop.getName() + " is a mandatory field and cannot be empty.");
						}
					};
                }
                //initialize the map entry
                modelMap.put(p.getName(), txtField.getText());
                // create observables for the control
                uiObservable = WidgetProperties.text(SWT.Modify).observe(txtField);                
                
            // UNSUPPORTED PROPERTIES / REFS
            } else if (CamelComponentUtils.isUnsupportedProperty(prop)) {
            	
            	// TODO: check how to handle lists and maps - for now we treat them as string field only
            	
            	Text txtField = toolkit.createText(page, PropertiesUtils.getPropertyFromUri(selectedEP, prop, component), SWT.SINGLE | SWT.BORDER | SWT.LEFT);
                txtField.addModifyListener(new ModifyListener() {
                    @Override
                    public void modifyText(ModifyEvent e) {
                        Text txt = (Text)e.getSource();
                        PropertiesUtils.updateURIParams(selectedEP, prop, txt.getText(), component, modelMap);
                    }
                });
                txtField.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
                c = txtField;
                if (p.getRequired() != null && p.getRequired().equalsIgnoreCase("true")) {
					validator = new IValidator() {
						/*
						 * (non-Javadoc)
						 * @see org.eclipse.core.databinding.validation.IValidator#validate(java.lang.Object)
						 */
						@Override
						public IStatus validate(Object value) {
							if (value != null && value instanceof String && value.toString().trim().length()>0) {
								return ValidationStatus.ok();
							}
							return ValidationStatus.error("Parameter " + prop.getName() + " is a mandatory field and cannot be empty.");
						}
					};
                }
                //initialize the map entry
                modelMap.put(p.getName(), txtField.getText());
                // create observables for the control
                uiObservable = WidgetProperties.text(SWT.Modify).observe(txtField);                
                
            // CLASS BASED PROPERTIES - REF OR CLASSNAMES AS STRINGS
            } else {
                // must be some class as all other options were missed
                final Text txtField = toolkit.createText(page, PropertiesUtils.getPropertyFromUri(selectedEP, prop, component), SWT.SINGLE | SWT.BORDER | SWT.LEFT);
                txtField.addModifyListener(new ModifyListener() {
                    @Override
                    public void modifyText(ModifyEvent e) {
                        Text txt = (Text)e.getSource();
                        PropertiesUtils.updateURIParams(selectedEP, prop, txt.getText(), component, modelMap);
                    }
                });
                txtField.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
                
                URLClassLoader child = CamelComponentUtils.getProjectClassLoader();
                Class classToLoad;
                try {
                    if (prop.getJavaType().indexOf("<")!=-1) {
                        classToLoad = child.loadClass(prop.getJavaType().substring(0,  prop.getJavaType().indexOf("<")));
                    } else {
                        classToLoad = child.loadClass(prop.getJavaType());   
                    }
                } catch (ClassNotFoundException ex) {
                    Activator.getLogger().warning("Cannot find class " + prop.getJavaType() + " on classpath.", ex);
                    classToLoad = null;
                }
                
                final IProject project = Activator.getDiagramEditor().getCamelContextFile().getProject();
                final Class fClass = classToLoad;
                
                Button btn_create = toolkit.createButton(page, " + ", SWT.BORDER | SWT.PUSH);
                btn_create.addSelectionListener(new SelectionAdapter() {
                    /* (non-Javadoc)
                     * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
                     */
                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        NewClassCreationWizard wiz = new NewClassCreationWizard();
                        wiz.addPages();
                        wiz.init(PlatformUI.getWorkbench(), null);
                        NewClassWizardPage wp = (NewClassWizardPage)wiz.getStartingPage();
                        WizardDialog wd = new WizardDialog(e.display.getActiveShell(), wiz);
                        if (fClass.isInterface()) {
                            wp.setSuperInterfaces(Arrays.asList(fClass.getName()), true);
                        } else {
                            wp.setSuperClass(fClass.getName(), true);
                        }
                        wp.setAddComments(true, true);
                        IPackageFragmentRoot fragroot = null;
                        try {
                            IJavaProject javaProject = (IJavaProject)project.getNature(JavaCore.NATURE_ID);
                            IMavenProjectFacade facade = MavenPlugin.getMavenProjectRegistry().create(project, new NullProgressMonitor());
                            IPath[] paths = facade.getCompileSourceLocations();
                            if (paths != null && paths.length>0) {
                                for (IPath p :paths) {
                                    if (p == null) continue; 
                                    IResource res = project.findMember(p);
                                    fragroot = javaProject.getPackageFragmentRoot(res);
                                    break;
                                }
                                if (fragroot != null) wp.setPackageFragmentRoot(fragroot, true);   
                                wp.setPackageFragment(PropertiesUtils.getPackage(javaProject, fragroot), true);
                            }
                        } catch (Exception ex) {
                            Activator.getLogger().error(ex);
                        }
                        if (Window.OK == wd.open()) {
                            String value = wp.getCreatedType().getFullyQualifiedName();
                            if (value != null) txtField.setText(value);
                        }
                    }
                });
                btn_create.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
                btn_create.setEnabled(fClass != null);
                
                Button btn_browse = toolkit.createButton(page, "...", SWT.BORDER | SWT.PUSH);
                btn_browse.addSelectionListener(new SelectionAdapter() {
                    /* (non-Javadoc)
                     * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
                     */
                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        try {
                            IJavaProject javaProject = (IJavaProject)project.getNature(JavaCore.NATURE_ID);
                            IJavaElement[] elements=new IJavaElement[]{javaProject};
                            IJavaSearchScope scope=SearchEngine.createJavaSearchScope(elements);
                            
                            FilteredTypesSelectionDialog dlg = new FilteredTypesSelectionDialog(Display.getDefault().getActiveShell(), 
                                    false, 
                                    PlatformUI.getWorkbench().getProgressService(), 
                                    scope, 
                                    IJavaSearchConstants.CLASS);
                            
                            if (Window.OK == dlg.open()) {
                                Object o = dlg.getFirstResult();
                                if (o instanceof SourceType) {
                                    txtField.setText(((SourceType)o).getFullyQualifiedName());
                                }
                            }
                        } catch (Exception ex) {
                            Activator.getLogger().error(ex);
                        }
                    }
                });
                btn_browse.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
                btn_browse.setEnabled(fClass != null);
                c = txtField;
                if (p.getRequired() != null && p.getRequired().equalsIgnoreCase("true")) {
					validator = new IValidator() {
						/*
						 * (non-Javadoc)
						 * @see org.eclipse.core.databinding.validation.IValidator#validate(java.lang.Object)
						 */
						@Override
						public IStatus validate(Object value) {
							if (value != null && value instanceof String && value.toString().trim().length()>0) {
								return ValidationStatus.ok();
							}
							return ValidationStatus.error("Parameter " + prop.getName() + " is a mandatory field and cannot be empty.");
						}
					};
                }
                //initialize the map entry
                modelMap.put(p.getName(), txtField.getText());
                // create observables for the control
                uiObservable = WidgetProperties.text(SWT.Modify).observe(txtField);                
                
            }
            
            // create UpdateValueStrategy and assign to the binding
            UpdateValueStrategy strategy = new UpdateValueStrategy();
            strategy.setBeforeSetValidator(validator);
            
            // create observables for the Map entries
            modelObservable = Observables.observeMapEntry(modelMap, p.getName());
            // bind the observables
            Binding bindValue = dbc.bindValue(uiObservable, modelObservable, strategy, null);
            ControlDecorationSupport.create(bindValue, SWT.TOP | SWT.LEFT); 
            
            if (p.getDescription() != null) c.setToolTipText(p.getDescription());
        }
    }
    
    private void createPathTab(CTabFolder folder) {
    	List<UriParameter> props = PropertiesUtils.getPathProperties(selectedEP);

        if (props.isEmpty()) return;
        
        pathTab = new CTabItem(tabFolder, SWT.NONE);
        pathTab.setText("Path");

        Composite page = toolkit.createComposite(folder);
        page.setLayout(new GridLayout(4, false));
                
        generateTabContents(props, page, false);

        pathTab.setControl(page);
    }
        
    private void createCommonsTab(CTabFolder folder) {
        List<UriParameter> props = PropertiesUtils.getPropertiesFor(selectedEP, UriParameterKind.BOTH);

        if (props.isEmpty()) return;
        
        commonTab = new CTabItem(tabFolder, SWT.NONE);
        commonTab.setText("General");

        Composite page = toolkit.createComposite(folder);
        page.setLayout(new GridLayout(4, false));
                
        generateTabContents(props, page, true);

        commonTab.setControl(page);
    }

    private void createConsumerTab(CTabFolder folder) {
        List<UriParameter> props = PropertiesUtils.getPropertiesFor(selectedEP, UriParameterKind.CONSUMER);
        
        if (props.isEmpty()) return;
        
        consumerTab = new CTabItem(tabFolder, SWT.NONE);
        consumerTab.setText("Consumer");

        Composite page = toolkit.createComposite(folder);
        page.setLayout(new GridLayout(4, false));
                
        generateTabContents(props, page, true);        
        
        consumerTab.setControl(page);
    }

    private void createProducerTab(CTabFolder folder) {
        List<UriParameter> props = PropertiesUtils.getPropertiesFor(selectedEP, UriParameterKind.PRODUCER);
        
        if (props.isEmpty()) return;
        
        producerTab = new CTabItem(tabFolder, SWT.NONE);
        producerTab.setText("Producer");
        
        Composite page = toolkit.createComposite(folder);
        page.setLayout(new GridLayout(4, false));
                
        generateTabContents(props, page, true);
        
        producerTab.setControl(page);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#createControls
     * (org.eclipse.swt.widgets.Composite,
     * org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
     */
    @Override
    public void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage) {
        this.toolkit = new FormToolkit(parent.getDisplay());
        super.createControls(parent, aTabbedPropertySheetPage);

        // now setup the file binding properties page
        parent.setLayout(new GridLayout());
        parent.setLayoutData(new GridData(GridData.FILL_BOTH));

        form = toolkit.createForm(parent);
        form.setLayoutData(new GridData(GridData.FILL_BOTH));
        form.getBody().setLayout(new GridLayout(1, false));

        Composite sbody = form.getBody();

        tabFolder = new CTabFolder(sbody, SWT.TOP | SWT.FLAT);
        toolkit.adapt(tabFolder, true, true);
        tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));

        Color selectedColor = toolkit.getColors().getColor(IFormColors.SEPARATOR);
        tabFolder.setSelectionBackground(new Color[] { selectedColor, toolkit.getColors().getBackground() }, new int[] { 20 }, true);
        tabFolder.setCursor(FormsResources.getHandCursor());
        toolkit.paintBordersFor(tabFolder);

        form.setText("Advanced Properties");
        toolkit.decorateFormHeading(form);
        
        form.layout();
        tabFolder.setSelection(0);
    }
}
