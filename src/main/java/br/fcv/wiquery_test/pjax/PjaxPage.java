package br.fcv.wiquery_test.pjax;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.behavior.IBehaviorListener;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.ComponentRenderingRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.odlabs.wiquery.core.commons.CoreJavaScriptResourceReference;

import br.fcv.wiquery_test.support.wicket.JQueryPjaxJavaScriptReference;

public class PjaxPage extends WebPage {
    
    private final ListView<File> listView;
    private final WebMarkupContainer container;
    
    public PjaxPage(final PageParameters parameters) {
        
        String strHome = System.getProperty("user.home");
        File home = new File(strHome);
        
        List<File> files = listFiles(home);
        
        container = new WebMarkupContainer("container");
        listView = new ListView<File>("listView", files) {

            @Override
            protected void populateItem(ListItem<File> item) {
                final File file = item.getModelObject();
                
                item.add(new Label("type", file.isDirectory() ? "D" : "F"));
                item.add(new Label("name", file.getName()));
                  
                if (file.isDirectory()) {                    
                    item.add(new AttributeModifier("class", new Model<String>("link")));
                    item.add(new MyBehavior() {

                        @Override
                        public void onRequest() {
                            listView.setModelObject(listFiles(file));
                            RequestCycle.get().replaceAllRequestHandlers(new ComponentRenderingRequestHandler(container));
                        }
                    });
                }
            }
        };
        container.setOutputMarkupId(true);
        container.add(listView);
        add(container);
        
        Link<Void> link = new Link<Void>("link") {

            @Override
            public void onClick() {
                AjaxRequestTarget target = AjaxRequestTarget.get();
                System.out.println(target);
                getRequestCycle().replaceAllRequestHandlers(new ComponentRenderingRequestHandler(this));                
            }
            
            @Override
            protected CharSequence getURL() {   
                parameters.set(parameters.getIndexedCount(), "other");
                return urlFor(PjaxPage.this.getClass(), parameters);
            }
        };
        link.add(new MyBehavior() {

            @Override
            public void onRequest() {
                   
            }
        });
        
        add(link);
    }

    private List<File> listFiles(File dir) {
        List<File> files = Arrays.asList(dir.listFiles( new FileFilter() {
            
            @Override
            public boolean accept(File pathname) {                
                return !pathname.isHidden();
            }
        }));
        
        Collections.sort(files, new Comparator<File>() {
            
            @Override
            public int compare(File f1, File f2) {
                int result = (f1.isDirectory() ? 0 : 1) - (f2.isDirectory() ? 0 : 1);
                if (result == 0) {
                    result = f1.getName().compareTo(f2.getName());
                }
                return result;
            }
        });
        return files;
    }
    
    private abstract class MyBehavior extends Behavior implements IBehaviorListener {
        
        @Override
        public void bind(Component component) {
            component.setOutputMarkupId(true);
        }
        
        @Override
        public void onComponentTag(Component component, ComponentTag tag) {
            super.onComponentTag(component, tag);
            String event = new StringBuilder()
                .append("javascript:$.pjax({container: '#")
                .append("target")
                .append("', url: '")
                .append(component.urlFor(this, IBehaviorListener.INTERFACE))
                .append("'})")
                .toString();
            tag.put("onclick", event);
        }
        
        @Override
        public void renderHead(Component component, IHeaderResponse response) {
            response.renderJavaScriptReference(CoreJavaScriptResourceReference.get());
            response.renderJavaScriptReference(JQueryPjaxJavaScriptReference.getInstance());
        }       
    }

}
