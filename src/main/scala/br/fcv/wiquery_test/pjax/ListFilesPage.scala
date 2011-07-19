package br.fcv.wiquery_test.pjax;

import org.apache.wicket.markup.html.IHeaderResponse
import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.request.cycle.RequestCycle
import org.apache.wicket.request.http.WebRequest
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.odlabs.wiquery.core.commons.CoreJavaScriptResourceReference

import br.fcv.wiquery_test.support.wicket.JQueryPjaxJavaScriptReference


class ListFilesPage(parameters: PageParameters) extends WebPage {

    parameters.remove("_pjax");
    
    add( contentPanel("content", parameters) )

    protected def isPjax = {
        val request = RequestCycle.get().getRequest().asInstanceOf[WebRequest];
        val pjax = request.getHeader("X-PJAX")
        println("pjxa: " + pjax)
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
