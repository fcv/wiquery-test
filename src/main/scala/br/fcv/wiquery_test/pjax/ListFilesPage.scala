package br.fcv.wiquery_test.pjax;

import org.apache.wicket.markup.html.IHeaderResponse
import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.request.cycle.RequestCycle
import org.apache.wicket.request.http.WebRequest
import org.apache.wicket.request.mapper.parameter.PageParameters
import br.fcv.wiquery_test.support.wicket.JQueryPjaxJavaScriptReference
import org.odlabs.wiquery.core.resources.CoreJavaScriptResourceReference


class ListFilesPage(parameters: PageParameters) extends WebPage {

    add( contentPanel("content", new PageParameters(parameters).remove("_pjax")) )

    protected def isPjax = {
        val request = RequestCycle.get().getRequest().asInstanceOf[WebRequest];
        val pjax = request.getHeader("X-PJAX")
        pjax != null && pjax.toBoolean
    }
    
    override def renderHead(response: IHeaderResponse) {
        response.renderJavaScriptReference(CoreJavaScriptResourceReference.get());
        response.renderJavaScriptReference(JQueryPjaxJavaScriptReference.getInstance());
    }
    
    def contentPanel(id: String, params: PageParameters) = { 
        if (isPjax) { 
            new ListFilesPanel(id, params) 
        } else {
            new FullContentPanel(id, params)
        }
    }
}
