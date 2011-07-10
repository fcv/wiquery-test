package br.fcv.wiquery_test.pjax;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.ILinkListener;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.ComponentRenderingRequestHandler;
import org.apache.wicket.request.handler.ListenerInterfaceRequestHandler;
import org.apache.wicket.request.handler.PageAndComponentProvider;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.odlabs.wiquery.core.commons.CoreJavaScriptResourceReference;
import org.odlabs.wiquery.core.commons.IWiQueryPlugin;
import org.odlabs.wiquery.core.commons.WiQueryResourceManager;
import org.odlabs.wiquery.core.javascript.JsStatement;

import br.fcv.wiquery_test.support.wicket.JQueryPjaxJavaScriptReference;

public class PjaxPage extends WebPage {
    
    public PjaxPage(PageParameters parameters) {
        this();
    }
    
    public PjaxPage() {
        
        Link<Void> link = new Link<Void>("link") {

            @Override
            public void onClick() {
                AjaxRequestTarget target = AjaxRequestTarget.get();
                System.out.println(target);
                getRequestCycle().replaceAllRequestHandlers(new ComponentRenderingRequestHandler(this));                
            }
        };
        link.add(new MyBehavior());
        
        add(link);        
        //--
    }
    
    //-- using IWiQueryPlugin is one possible solution to have jquery inserted at page... but i still need to know how
    // to correctly order it.. MyBehavior's javascript refereces are still rendered at first
    private static abstract class MyLink<T> extends Link<T> implements IWiQueryPlugin {

        public MyLink(String id) {
            super(id);
        }
        
        public MyLink(String id, IModel<T> model) {
            super(id, model);
        }
        
        @Override
        public JsStatement statement() {
            return null;
        }
        
        @Override
        public void contribute(WiQueryResourceManager manager) {
        }
        
    }
    
    private static class MyBehavior extends Behavior {
        
        @Override
        public void bind(Component component) {
            component.setOutputMarkupId(true);
        }
        
        @Override
        public void onComponentTag(Component component, ComponentTag tag) {
            super.onComponentTag(component, tag);
            tag.put("href", "/nice-value");
        }
        
        @Override
        public void renderHead(Component component, IHeaderResponse response) {
            response.renderJavaScriptReference(CoreJavaScriptResourceReference.get());
            response.renderJavaScriptReference(JQueryPjaxJavaScriptReference.getInstance());
            
            
            IRequestHandler handler = new ListenerInterfaceRequestHandler(
                    new PageAndComponentProvider(component.getPage(), component), ILinkListener.INTERFACE);
            Url url = RequestCycle.get().mapUrlFor(handler);
            
            //-- i do have a problem since jquery.pjax user url to replace browser's url... but i would like to 
            // use a nice url at browser but still use wick
            response.renderJavaScript("$('#" + component.getMarkupId() + "').pjax('#src', {url: '" + url + "'})", 
                    "MyBehavior-" + component.getMarkupId());            
            
        }
        
    }

}
