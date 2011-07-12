package br.fcv.wiquery_test.pjax;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.mapper.parameter.PageParameters;

class ListFilesPage(parameter: PageParameters) extends WebPage {
    
    add(new Header("header"));
    
    protected def isPjax = {
        val request = RequestCycle.get().getRequest().asInstanceOf[WebRequest];
        request.getHeader("X-PJAX").toBoolean; 
    }
    
    class Header(id: String) extends WebMarkupContainer(id) {        
        add(new Label("now", new NowModel())); 
    }
    
   class NowModel(format: String) extends LoadableDetachableModel[String] {        
        def this() = this("dd-MM-yyyy HH:mm:ss.SSSS");
        
        override protected def load() = {
            val formatter = new SimpleDateFormat(format);
            formatter.format(new Date());
        }        
    }

}
