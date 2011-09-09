package br.fcv.wiquery_test.pjax;

import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import org.odlabs.wiquery.core.resources.CoreJavaScriptResourceReference;

import br.fcv.wiquery_test.support.wicket.GoogleClosureJavaScriptCompressor;
import br.fcv.wiquery_test.support.wicket.JQueryPjaxJavaScriptReference;

public class SimplePjaxExamplePage extends WebPage {
    
    public SimplePjaxExamplePage(PageParameters params) {
        StringValue paramValue = params.get("value");        
        final int value = paramValue.toInt(0) + 1;
        
        
        
        PageParameters newParams = new PageParameters(params);
        newParams.set("value", value);
        newParams.remove("_pjax");
        
        add(new Label("isPjax", String.valueOf(isPjax())));
        add(new Label("value", String.valueOf(value)));
        add(new BookmarkablePageLink<Void>("link", SimplePjaxExamplePage.class, newParams));    
    }
    
    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderJavaScriptReference(CoreJavaScriptResourceReference.get());
        response.renderJavaScriptReference(JQueryPjaxJavaScriptReference.getInstance());
        String js = "       //-- testing stripping \n" +
        "       $(function() {\n" +
        "              var value = 'teste';\n" +
        "              alert('v: ' + value);\n" +
        "       })\n" + 
        "       //-- end of testing\n";
        
        js = GoogleClosureJavaScriptCompressor.getInstance().compress(js);
        response.renderJavaScript(js, "bleh");
    }  
    
    public boolean isPjax() {
        WebRequest request = (WebRequest) RequestCycle.get().getRequest();
        return Boolean.parseBoolean(request.getHeader("X-PJAX")); 
    }

}
