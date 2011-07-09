package br.fcv.wiquery_test.pjax;

import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;

import br.fcv.wiquery_test.support.wicket.JQueryPjaxJavaScriptReference;

public class PjaxPage extends WebPage {
    
    public PjaxPage() {
    }
    
    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderJavaScriptReference(JQueryPjaxJavaScriptReference.getInstance());
    }

}
