package org.fusesource.ide.camel.model.generated;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.camel.ExchangePattern;
import org.apache.camel.LoggingLevel;
import org.apache.camel.ShutdownRoute;
import org.apache.camel.ShutdownRunningTask;
import org.apache.camel.ThreadPoolRejectedPolicy;
import org.apache.camel.model.CatchDefinition;
import org.apache.camel.model.DataFormatDefinition;
import org.apache.camel.model.LoadBalancerDefinition;
import org.apache.camel.model.OnCompletionMode;
import org.apache.camel.model.OptimisticLockRetryPolicyDefinition;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.model.RedeliveryPolicyDefinition;
import org.apache.camel.model.config.ResequencerConfig;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.fusesource.ide.camel.model.AbstractNode;
import org.fusesource.ide.camel.model.Activator;
import org.fusesource.ide.camel.model.ExpressionPropertyDescriptor;
import org.fusesource.ide.camel.model.RouteContainer;
import org.fusesource.ide.camel.model.catalog.Parameter;
import org.fusesource.ide.camel.model.catalog.eips.Eip;
import org.fusesource.ide.camel.model.util.Objects;
import org.fusesource.ide.commons.properties.BooleanPropertyDescriptor;
import org.fusesource.ide.commons.properties.ComplexUnionPropertyDescriptor;
import org.fusesource.ide.commons.properties.EnumPropertyDescriptor;
import org.fusesource.ide.commons.properties.ListPropertyDescriptor;
import org.fusesource.ide.commons.properties.UnionTypeValue;

public class UniversalEIPNode extends AbstractNode {
	
	private static final HashMap<String, String> iconNameMap;
	private static final HashMap<String, String> documentationMap;
	private static final HashMap<String, String> categoryMap;
	
	static {
		iconNameMap = new HashMap<String, String>();
		documentationMap = new HashMap<String, String>();
		categoryMap = new HashMap<String, String>();
		
		iconNameMap.put("delay", "generic.png");
		documentationMap.put("delay", "delayEIP");
		categoryMap.put("delay", "Control Flow");
		
		iconNameMap.put("aggregate", "aggregate.png");
		documentationMap.put("aggregate", "aggregateEIP");
		categoryMap.put("aggregate", "Routing");
		
		iconNameMap.put("aop", "generic.png");
		documentationMap.put("aop", "AOPEIP");
		categoryMap.put("aop", "Miscellaneous");
		
		iconNameMap.put("bean", "bean.png");
		documentationMap.put("bean", "beanComp");
		categoryMap.put("bean", "Components");
		
		iconNameMap.put("doCatch", "generic.png");
		documentationMap.put("catch", "catchEIP");
		categoryMap.put("doCatch", "Control Flow");
		
		iconNameMap.put("choice", "choice.png");
		documentationMap.put("choice", "choiceEIP");
		categoryMap.put("choice", "Routing");
		
		iconNameMap.put("choice", "choice.png");
		documentationMap.put("choice", "choiceEIP");
		categoryMap.put("choice", "Routing");
		
		iconNameMap.put("convertBodyTo", "convertBody.png");
		documentationMap.put("convertBodyTo", "convertEIP");
		categoryMap.put("convertBodyTo", "Transformation");
		
		iconNameMap.put("enrich", "enrich.png");
		documentationMap.put("enrich", "enrichEIP");
		categoryMap.put("enrich", "Transformation");
		
		iconNameMap.put("filter", "filter.png");
		documentationMap.put("filter", "filterEIP");
		categoryMap.put("filter", "Routing");
		
		iconNameMap.put("doFinally", "generic.png");
		documentationMap.put("doFinally", "finallyEIP");
		categoryMap.put("doFinally", "Control Flow");
		
		iconNameMap.put("idempotentConsumer", "idempotentConsumer.png");
		documentationMap.put("idempotentConsumer", "idempotentConsumer");
		categoryMap.put("idempotentConsumer", "Routing");
		
		iconNameMap.put("inOnly", "transform.png");
		documentationMap.put("inOnly", "inOnlyEIP");
		categoryMap.put("inOnly", "Transformation");
		
		iconNameMap.put("inOut", "transform.png");
		documentationMap.put("inOut", "inOutEIP");
		categoryMap.put("inOut", "Transformation");
		
		iconNameMap.put("intercept", "generic.png");
		documentationMap.put("intercept", "interceptEIP");
		categoryMap.put("intercept", "Control Flow");
		
		iconNameMap.put("interceptFrom", "generic.png");
		documentationMap.put("interceptFrom", "interceptFromEIP");
		categoryMap.put("interceptFrom", "Control Flow");
		
		iconNameMap.put("interceptSendToEndpoint", "generic.png");
		documentationMap.put("interceptSendToEndpoint", "interceptSendToEndpointEIP");
		categoryMap.put("interceptSendToEndpoint", "Control Flow");
		
		iconNameMap.put("loadBalance", "loadBalance.png");
		documentationMap.put("loadBalance", "loadBalanceEIP");
		categoryMap.put("loadBalance", "Routing");
		
		iconNameMap.put("loop", "generic.png");
		documentationMap.put("loop", "loopEIP");
		categoryMap.put("loop", "Control Flow");
		
		iconNameMap.put("log", "log.png");
		documentationMap.put("log", "logEIP");
		categoryMap.put("log", "Components");
		
		iconNameMap.put("marshal", "marshal.png");
		documentationMap.put("marshal", "marshalEIP");
		categoryMap.put("marshal", "Transformation");
		
		iconNameMap.put("multicast", "multicast.png");
		documentationMap.put("multicast", "multicastEIP");
		categoryMap.put("multicast", "Routing");
		
		iconNameMap.put("onCompletion", "generic.png");
		documentationMap.put("onCompletion", "onCompleteEIP");
		categoryMap.put("onCompletion", "Control Flow");

		iconNameMap.put("onException", "generic.png");
		documentationMap.put("onException", "onExceptionEIP");
		categoryMap.put("onException", "Control Flow");

		iconNameMap.put("otherwise", "generic.png");
		documentationMap.put("otherwise", "otherwiseEIP");
		categoryMap.put("otherwise", "Routing");
		
		iconNameMap.put("pipeline", "pipeline.png");
		documentationMap.put("pipeline", "pipelineEIP");
		categoryMap.put("pipeline", "Routing");
		
		iconNameMap.put("policy", "generic.png");
		documentationMap.put("policy", "policyNode");
		categoryMap.put("policy", "Miscellaneous");
		
		iconNameMap.put("pollEnrich", "pollEnrich.png");
		documentationMap.put("pollEnrich", "pollEnrichEIP");
		categoryMap.put("pollEnrich", "Transformation");
		
		iconNameMap.put("process", "process.png");
		documentationMap.put("process", "processor");
		categoryMap.put("process", "Components");
		
		iconNameMap.put("recipientList", "recipientList.png");
		documentationMap.put("recipientList", "recipientListEIP");
		categoryMap.put("recipientList", "Routing");
		
		iconNameMap.put("removeHeader", "transform.png");
		documentationMap.put("removeHeader", "removeHeaderNode");
		categoryMap.put("removeHeader", "Transformation");
		
		iconNameMap.put("removeHeaders", "transform.png");
		documentationMap.put("removeHeaders", "removeHeadersNode");
		categoryMap.put("removeHeaders", "Transformation");
		
		iconNameMap.put("removeProperty", "transform.png");
		documentationMap.put("removeProperty", "removePropertyNode");
		categoryMap.put("removeProperty", "Transformation");
		
		iconNameMap.put("removeProperties", "transform.png");
		documentationMap.put("removeProperties", "allEIPs");
		categoryMap.put("removeProperties", "Transformation");
		
		iconNameMap.put("resequence", "resequence.png");
		documentationMap.put("resequence", "resequenceEIPs");
		categoryMap.put("resequence", "Routing");
		
		iconNameMap.put("rollback", "generic.png");
		documentationMap.put("rollback", "rolbackNode");
		categoryMap.put("rollback", "Control Flow");
		
		iconNameMap.put("routingSlip", "routingSlip.png");
		documentationMap.put("routingSlip", "routingSlipEIP");
		categoryMap.put("routingSlip", "Routing");
		
		iconNameMap.put("sample", "generic.png");  // Warning, this id has changed from Sampling to Sample
		documentationMap.put("sample", "samplingNode");
		categoryMap.put("sample", "Miscellaneous");
		
		iconNameMap.put("setBody", "setBody.png");  
		documentationMap.put("setBody", "setBodyNode");
		categoryMap.put("setBody", "Transformation");
		
		iconNameMap.put("setExchangePattern", "transform.png");  
		documentationMap.put("setExchangePattern", "setExchangePatternNode");
		categoryMap.put("setExchangePattern", "Transformation");
		
		iconNameMap.put("setFaultBody", "transform.png");  
		documentationMap.put("setFaultBody", "setFaultBodyNode");
		categoryMap.put("setFaultBody", "Transformation");
		
		iconNameMap.put("setHeader", "transform.png");  
		documentationMap.put("setHeader", "setHeaderNode");
		categoryMap.put("setHeader", "Transformation");
		
		iconNameMap.put("setOutHeader", "transform.png");  
		documentationMap.put("setOutHeader", "setOutHeaderNode");
		categoryMap.put("setOutHeader", "Transformation");
		
		iconNameMap.put("setProperty", "transform.png");  
		documentationMap.put("setProperty", "setPropertyNode");
		categoryMap.put("setProperty", "Transformation");
		
		iconNameMap.put("sort", "generic.png");  
		documentationMap.put("sort", "sortEIP");
		categoryMap.put("sort", "Routing");
		
		iconNameMap.put("split", "split.png");  
		documentationMap.put("split", "splitEIP");
		categoryMap.put("split", "Routing");
		
		iconNameMap.put("stop", "generic.png");  
		documentationMap.put("stop", "stopNode");
		categoryMap.put("stop", "Miscellaneous");
		
		iconNameMap.put("threads", "generic.png");  
		documentationMap.put("threads", "threadNode");
		categoryMap.put("threads", "Miscellaneous");
		
		iconNameMap.put("throttle", "generic.png");  
		documentationMap.put("throttle", "throttleNode");
		categoryMap.put("throttle", "Control Flow");
		
		iconNameMap.put("throwException", "generic.png");  
		documentationMap.put("throwException", "throwExceptionNode");
		categoryMap.put("throwException", "Control Flow");
		
		iconNameMap.put("transacted", "generic.png");  
		documentationMap.put("transacted", "transactedNode");
		categoryMap.put("transacted", "Control Flow");
		
		iconNameMap.put("transform", "transform.png");  
		documentationMap.put("transform", "transformEIP");
		categoryMap.put("transform", "Transformation");
		
		iconNameMap.put("doTry", "generic.png");  
		documentationMap.put("doTry", "tryNode");
		categoryMap.put("doTry", "Control Flow");
		
		iconNameMap.put("unmarshal", "unmarshal.png");  
		documentationMap.put("unmarshal", "unmarshalNode");
		categoryMap.put("unmarshal", "Transformation");

		iconNameMap.put("validate", "generic.png");  
		documentationMap.put("validate", "validateNode");
		categoryMap.put("validate", "Miscellaneous");

		iconNameMap.put("when", "generic.png");  
		documentationMap.put("when", "whenNode");
		categoryMap.put("when", "Routing");
		
		iconNameMap.put("wireTap", "wireTap.png");  
		documentationMap.put("wireTap", "wireTapEIP");
		categoryMap.put("wireTap", "Routing");
		
		
		
		
	}
	
	
	private Eip eip;
	private HashMap<String, Object> propertyValues;
	private ProcessorDefinition definition;
    public UniversalEIPNode(ProcessorDefinition definition, RouteContainer parent, Eip eip) {
        super(parent, true);
        this.definition = definition;
        this.eip = eip;
        addCustomProperties();
        propertyValues = new HashMap<String, Object>();
        loadPropertiesFromCamelDefinition(definition);
        loadChildrenFromCamelDefinition(definition);
    }


    @Override
    public String getIconName() {
    	String ret = iconNameMap.get(eip.getName());
    	if( ret == null ) {
    		System.out.println("Blank");
    	}
    	return ret;
    }

    @Override
    public String getDocumentationFileName() {
    	return documentationMap.get(eip.getName());
    }

    @Override
    public String getCategoryName() {
    	return categoryMap.get(eip.getName());
    }

    private String capitalizeFirstLetter(String input) {
    	return input.substring(0,1).toUpperCase() + input.substring(1);
    }
    
    
    
    /*
     * This method necessarily actually resolves classes in camel. 
     * If this is ever abstracted out, so that model.core or commons doesn't depend on one version
     * of camel, this will need to be isolated
     */
    protected PropertyDescriptor createPropertyDescriptor(String javaType, String propertyKey, String display) {
    	int parametizedStart = javaType.indexOf("<");
    	String cleanType = (parametizedStart == -1 ? javaType : javaType.substring(0, parametizedStart));

    	
        if( cleanType.equals("java.lang.Boolean")) {
        	return new BooleanPropertyDescriptor(propertyKey, display);
        }
        if( cleanType.equals("java.util.List")) {
        	return new ListPropertyDescriptor(propertyKey, display);
        }
        if( cleanType.equals("org.apache.camel.model.language.ExpressionDefinition")) {
            return new ExpressionPropertyDescriptor(propertyKey, display);
        }
        if( cleanType.equals("org.apache.camel.model.OptimisticLockRetryPolicyDefinition")) {
        	return new ComplexUnionPropertyDescriptor(propertyKey, display, OptimisticLockRetryPolicyDefinition.class, 
        			new UnionTypeValue[] {});
        }
        if( cleanType.equals("org.apache.camel.model.RedeliveryPolicyDefinition")) {
        	return new ComplexUnionPropertyDescriptor(propertyKey, display,
        			RedeliveryPolicyDefinition.class, new UnionTypeValue[] {});
        }
        if( cleanType.equals("org.apache.camel.ExchangePattern")) {
        	return new EnumPropertyDescriptor(propertyKey, display,ExchangePattern.class);
        }
        
        if( cleanType.equals("java.util.concurrent.TimeUnit")) {
        	return new EnumPropertyDescriptor(propertyKey, display,TimeUnit.class);
        }
        if( cleanType.equals("org.apache.camel.ThreadPoolRejectedPolicy")) {
        	return new EnumPropertyDescriptor(propertyKey, display, ThreadPoolRejectedPolicy.class);
        }
        if( cleanType.equals("org.apache.camel.LoggingLevel")) {
        	return new EnumPropertyDescriptor(propertyKey,display, LoggingLevel.class);
        }
        if( cleanType.equals("org.apache.camel.model.OnCompletionMode")) {
        	return new EnumPropertyDescriptor(propertyKey, display,  OnCompletionMode.class);
        }
        if( cleanType.equals("org.apache.camel.model.WhenDefinition")) {
        	return null;  // TODO?  Not in current file, seems left out
        }
        if( cleanType.equals("org.apache.camel.ShutdownRoute")) {
        	return new EnumPropertyDescriptor(propertyKey, display, ShutdownRoute.class);
        }
        if( cleanType.equals("org.apache.camel.ShutdownRunningTask")) {
        	return new EnumPropertyDescriptor(propertyKey, display, ShutdownRunningTask.class);
        }
        if( cleanType.equals("org.apache.camel.model.DataFormatDefinition")) {
            PropertyDescriptor descDataFormatType = new ComplexUnionPropertyDescriptor(propertyKey, display, DataFormatDefinition.class, new UnionTypeValue[] {
                    new UnionTypeValue("avro", org.apache.camel.model.dataformat.AvroDataFormat.class),
                    new UnionTypeValue("base64", org.apache.camel.model.dataformat.Base64DataFormat.class),
                    new UnionTypeValue("beanio", org.apache.camel.model.dataformat.BeanioDataFormat.class),
                    new UnionTypeValue("bindy", org.apache.camel.model.dataformat.BindyDataFormat.class),
                    new UnionTypeValue("castor", org.apache.camel.model.dataformat.CastorDataFormat.class),
                    new UnionTypeValue("crypto", org.apache.camel.model.dataformat.CryptoDataFormat.class),
                    new UnionTypeValue("csv", org.apache.camel.model.dataformat.CsvDataFormat.class),
                    new UnionTypeValue("custom", org.apache.camel.model.dataformat.CustomDataFormat.class),
                    new UnionTypeValue("flatpack", org.apache.camel.model.dataformat.FlatpackDataFormat.class),
                    new UnionTypeValue("gzip", org.apache.camel.model.dataformat.GzipDataFormat.class),
                    new UnionTypeValue("hl7", org.apache.camel.model.dataformat.HL7DataFormat.class),
                    new UnionTypeValue("ical", org.apache.camel.model.dataformat.IcalDataFormat.class),
                    new UnionTypeValue("jaxb", org.apache.camel.model.dataformat.JaxbDataFormat.class),
                    new UnionTypeValue("jibx", org.apache.camel.model.dataformat.JibxDataFormat.class),
                    new UnionTypeValue("json", org.apache.camel.model.dataformat.JsonDataFormat.class),
                    new UnionTypeValue("protobuf", org.apache.camel.model.dataformat.ProtobufDataFormat.class),
                    new UnionTypeValue("rss", org.apache.camel.model.dataformat.RssDataFormat.class),
                    new UnionTypeValue("secureXML", org.apache.camel.model.dataformat.XMLSecurityDataFormat.class),
                    new UnionTypeValue("serialization", org.apache.camel.model.dataformat.SerializationDataFormat.class),
                    new UnionTypeValue("soapjaxb", org.apache.camel.model.dataformat.SoapJaxbDataFormat.class),
                    new UnionTypeValue("string", org.apache.camel.model.dataformat.StringDataFormat.class),
                    new UnionTypeValue("syslog", org.apache.camel.model.dataformat.SyslogDataFormat.class),
                    new UnionTypeValue("tidyMarkup", org.apache.camel.model.dataformat.TidyMarkupDataFormat.class),
                    new UnionTypeValue("univocity-csv", org.apache.camel.model.dataformat.UniVocityCsvDataFormat.class),
                    new UnionTypeValue("univocity-fixed", org.apache.camel.model.dataformat.UniVocityFixedWidthDataFormat.class),
                    new UnionTypeValue("univocity-tsv", org.apache.camel.model.dataformat.UniVocityTsvDataFormat.class),
                    new UnionTypeValue("xmlBeans", org.apache.camel.model.dataformat.XMLBeansDataFormat.class),
                    new UnionTypeValue("xmljson", org.apache.camel.model.dataformat.XmlJsonDataFormat.class),
                    new UnionTypeValue("xmlrpc", org.apache.camel.model.dataformat.XmlRpcDataFormat.class),
                    new UnionTypeValue("xstream", org.apache.camel.model.dataformat.XStreamDataFormat.class),
                    new UnionTypeValue("pgp", org.apache.camel.model.dataformat.PGPDataFormat.class),
                    new UnionTypeValue("zip", org.apache.camel.model.dataformat.ZipDataFormat.class),
                    new UnionTypeValue("zipFile", org.apache.camel.model.dataformat.ZipFileDataFormat.class),
            });
        }
        if( cleanType.equals("org.apache.camel.model.LoadBalancerDefinition")) {
        	return new ComplexUnionPropertyDescriptor(propertyKey, display, LoadBalancerDefinition.class, new UnionTypeValue[] {
                    new UnionTypeValue("failover", org.apache.camel.model.loadbalancer.FailoverLoadBalancerDefinition.class),
                    new UnionTypeValue("random", org.apache.camel.model.loadbalancer.RandomLoadBalancerDefinition.class),
                    new UnionTypeValue("custom", org.apache.camel.model.loadbalancer.CustomLoadBalancerDefinition.class),
                    new UnionTypeValue("roundRobin", org.apache.camel.model.loadbalancer.RoundRobinLoadBalancerDefinition.class),
                    new UnionTypeValue("sticky", org.apache.camel.model.loadbalancer.StickyLoadBalancerDefinition.class),
                    new UnionTypeValue("topic", org.apache.camel.model.loadbalancer.TopicLoadBalancerDefinition.class),
                    new UnionTypeValue("weighted", org.apache.camel.model.loadbalancer.WeightedLoadBalancerDefinition.class),
                    new UnionTypeValue("circuitBreaker", org.apache.camel.model.loadbalancer.CircuitBreakerLoadBalancerDefinition.class),
            });
        }
        if( cleanType.equals("org.apache.camel.model.config.ResequencerConfig")) {
            return new ComplexUnionPropertyDescriptor(propertyKey, display, ResequencerConfig.class, new UnionTypeValue[] {
                    new UnionTypeValue("batch-config", org.apache.camel.model.config.BatchResequencerConfig.class),
                    new UnionTypeValue("stream-config", org.apache.camel.model.config.StreamResequencerConfig.class),
            });
        }
        // TODO add more here as I discover them
        
        // by default just return a text descriptor
        return new TextPropertyDescriptor(propertyKey, display);
    }
    
    protected Class findClass(String str) {
    	//  TODO currently errors on generics...   java.util.List<org.apache.camel.model.WhenDefinition>  fails
    	int parametizedStart = str.indexOf("<");
    	str = (parametizedStart == -1 ? str : str.substring(0, parametizedStart));
    	
    	try {
    		return Class.forName(str);
    	} catch(ClassNotFoundException cnfe) {
    		Activator.getDefault().getLog().log(new Status(IStatus.ERROR, "org.fusesource.ide.camel.model", cnfe.getMessage(), cnfe));
    	}
    	return null;
    }
    
    @Override
    protected void addCustomProperties(Map<String, PropertyDescriptor> descriptors) {
        super.addCustomProperties(descriptors);
        String eipName = eip.getName();
        String propertyPrefix = capitalizeFirstLetter(eipName) + ".";
        ArrayList<Parameter> params = eip.getParameters();
        Iterator<Parameter> it = params.iterator();
        while(it.hasNext()) {
        	Parameter p = it.next();
        	if( !isIgnored(p)) {
	        	String javaType = p.getJavaType();
	        	String paramName = p.getName();
	        	String propertyDescriptorId = propertyPrefix + capitalizeFirstLetter(paramName);
	        	
	        	// Replace all camel case with a space so it's human readable
	        	String display = paramName.replaceAll("(\\p{Ll})(\\p{Lu})","$1 $2");
	        	PropertyDescriptor desc = createPropertyDescriptor(javaType, propertyDescriptorId, capitalizeFirstLetter(display));
	        	descriptors.put(propertyDescriptorId, desc);
        	}
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.views.properties.IPropertySource\#setPropertyValue(java.lang.Object, java.lang.Object)
     */
    @Override
    public void setPropertyValue(Object id, Object value) {
        Object oldValue = propertyValues.get(id);
        propertyValues.put((String)id,  value);
        if (!isSame(oldValue, value)) {
            firePropertyChange((String)id, oldValue, value);
        }
        super.setPropertyValue(id, value);
    }

    /* (non-Javadoc)
     * @see org.fusesource.ide.camel.model.AbstractNode\#getPropertyValue(java.lang.Object)
     */
    @Override
    public Object getPropertyValue(Object id) {
    	if( propertyValues.get(id) != null )
    		return propertyValues.get(id);
    	return super.getPropertyValue(id);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public ProcessorDefinition createCamelDefinition() {
        String eipName = eip.getName();
        String propertyPrefix = capitalizeFirstLetter(eipName) + ".";
        try {
	    	ProcessorDefinition answer = definition.getClass().newInstance();
	        ArrayList<Parameter> params = eip.getParameters();
	        Iterator<Parameter> it = params.iterator();
	        while(it.hasNext()) {
	        	Parameter p = it.next();
	        	if(!isIgnored(p)) {
	        		runSetter(p, answer, propertyPrefix);
	        	}
	        }
	        super.savePropertiesToCamelDefinition(answer);
	        return answer;
        } catch(Exception e) {
        	Activator.getDefault().getLog().log(new Status(
        			IStatus.ERROR, "org.fusesource.ide.camel.model", e.getMessage(), e));
        }
        return null;
    }
    
    
    private void runSetter(Parameter p, ProcessorDefinition answer, String propertyPrefix) throws Exception {
		String paramName = p.getName();
    	String propertyDescriptorId = propertyPrefix + capitalizeFirstLetter(paramName);
    	Object val = propertyValues.get(propertyDescriptorId);
		String setterMethod = "set" + capitalizeFirstLetter(p.getName());
		Class paramClass = findClass(p.getJavaType());
		Method m = null;
		if( paramClass != null ) {
			try {
				m = answer.getClass().getMethod(setterMethod, paramClass);
			} catch(NoSuchMethodException nsme) {
				nsme.printStackTrace();
			}
			if( m == null ) {
				// It's possible the model is wrong and the setter doesn't match the param name
				// We may try adding an 's' at the end in case the param is 'exception' but the setter is setExceptions
				try {
					m = answer.getClass().getMethod(setterMethod + "s", paramClass);
				} catch(NoSuchMethodException nsme2) {
					nsme2.printStackTrace();
				}
			}
			
			if( m != null ) {
				try {
		    		m.invoke(answer, val);
		    		return;
				} catch(IllegalAccessException iae) {
					iae.printStackTrace();
				} catch(InvocationTargetException ite) {
					ite.printStackTrace();
				}
			} 

			// Try to find a field with the parameter name?
			try {
				Objects.setField(answer, paramName, toXmlPropertyValue(propertyDescriptorId, val));
				return;
			} catch(Exception e) {
				e.printStackTrace();
			}

			try {
				Objects.setField(answer, paramName + "s", toXmlPropertyValue(propertyDescriptorId, val));
				return;
			} catch(Exception e2) {
				e2.printStackTrace();
			}
			
			throw new Exception("All attempts at setting value have failed for parameter " + p.getName());
		}
    }
    
    protected boolean isIgnored(Parameter p) {
    	String name = p.getName();
    	if( "id".equals(name) || "outputs".equals(name) || "description".equals(name))
    		return true;
    	return false;
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected void loadPropertiesFromCamelDefinition(ProcessorDefinition processor) {
        super.loadPropertiesFromCamelDefinition(processor);
        ArrayList<Parameter> params = eip.getParameters();
        Iterator<Parameter> it = params.iterator();
        String eipName = eip.getName();
        String propertyPrefix = capitalizeFirstLetter(eipName) + ".";
        while(it.hasNext()) {
        	Parameter p = it.next();
	        String propertyDescriptorId = propertyPrefix + capitalizeFirstLetter(p.getName());
        	if( !isIgnored(p)) {
        		Object result = runGetter(p, processor, propertyPrefix);
		        setPropertyValue(propertyDescriptorId, result);
        	}
        }
    }

    private Object runGetter(Parameter p, ProcessorDefinition processor, String propertyPrefix) {
    	// Check a properly-formed getter
		String getter = "get" + capitalizeFirstLetter(p.getName());
		Method m = null;
        try {
	        m = processor.getClass().getMethod(getter, null);
        } catch(NoSuchMethodException nsme) {
    		// TODO
    		nsme.printStackTrace();
        }
        if( m == null ) {
        	// Check a getter that's plural
        	try {
        		m = processor.getClass().getMethod(getter + "s", null);
        	} catch(NoSuchMethodException nsme) {
        		// TODO
        		nsme.printStackTrace();
        	}
        }
        if( m != null ) {
        	try {
		        String propertyDescriptorId = propertyPrefix + capitalizeFirstLetter(p.getName());
		        Object result = m.invoke(processor, null);
		        return result;
        	} catch(IllegalAccessException iae) {
        		iae.printStackTrace();
        	} catch(InvocationTargetException iae) {
        		iae.printStackTrace();
        	}
        } else {
        	System.out.println("Expected getter not found: " + getter);
        	// Try via field
        	try {
	        	return Objects.getField(processor, p.getName(), findClass(p.getJavaType()));
        	}  catch(NoSuchFieldException nsfe) {
        		nsfe.printStackTrace();
        	}  catch(IllegalAccessException nsfe) {
        		nsfe.printStackTrace();
        	}

        	try {
        		// Try field with an 's'
	        	return Objects.getField(processor, p.getName() + "s", findClass(p.getJavaType()));
        	}  catch(NoSuchFieldException nsfe) {
        		nsfe.printStackTrace();
        	}  catch(IllegalAccessException nsfe) {
        		nsfe.printStackTrace();
        	}

        	// Error that the expected getter wasn't found
        }
        return null;
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public Class<?> getCamelDefinitionClass() {
        return definition.getClass();
    }

}
