package br.fcv.wiquery_test.support.wicket;

import org.apache.wicket.request.resource.SharedResourceReference;

import br.fcv.wiquery_test.WicketApplication;

public class JQueryPjaxJavaScriptReference extends SharedResourceReference {
    
    private static final JQueryPjaxJavaScriptReference INSTANCE = new JQueryPjaxJavaScriptReference();
    
    public static final JQueryPjaxJavaScriptReference getInstance() {
        return INSTANCE;
    }

    public JQueryPjaxJavaScriptReference() {
        super(WicketApplication.class, "jquery.pjax.js");
    }

}
