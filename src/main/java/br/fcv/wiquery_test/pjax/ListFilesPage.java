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

public class ListFilesPage extends WebPage {
    
    public ListFilesPage(PageParameters parameters) {
        add(new Header("header"));
    }
    
    public boolean isPjax() {
        WebRequest request = (WebRequest) RequestCycle.get().getRequest();
        return Boolean.parseBoolean(request.getHeader("X-PJAX")); 
    }
    
    private static class Header extends WebMarkupContainer {

        public Header(String id) {
            super(id);
            add(new Label("now", new NowModel()));
        }
        
    }
    
    private static class NowModel extends LoadableDetachableModel<String> {
        
        public static final String DEFAULT_FORMAT = "dd-MM-yyyy HH:mm:ss.SSSS";
        
        private final String format;
        
        public NowModel() {
            this(DEFAULT_FORMAT);
        }
        
        public NowModel(String format) {
            this.format = format;
        }

        @Override
        protected String load() {
            DateFormat formatter = new SimpleDateFormat(format);
            return formatter.format(new Date());
        }
        
    }

}
