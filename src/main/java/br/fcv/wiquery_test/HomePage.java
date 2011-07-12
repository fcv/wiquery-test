package br.fcv.wiquery_test;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import br.fcv.wiquery_test.dialog.DialogPage;
import br.fcv.wiquery_test.effects.EffectsPage;
import br.fcv.wiquery_test.lazy.LazyComponentsPage;
import br.fcv.wiquery_test.pjax.PjaxPage;
import br.fcv.wiquery_test.pjax.SimplePjaxExamplePage;

public class HomePage extends WebPage {

    public HomePage(final PageParameters parameters) {
        
        @SuppressWarnings("unchecked")
        List<Class<? extends WebPage>> pages = Arrays.asList(DialogPage.class, 
                EffectsPage.class, 
                LazyComponentsPage.class,
                PjaxPage.class,
                SimplePjaxExamplePage.class);
        
        
        ListView<Class<? extends WebPage>> listView = new ListView<Class<? extends WebPage>>("repeater", pages) {

            @Override
            protected void populateItem(ListItem<Class<? extends WebPage>> item) {
                final Class<? extends WebPage> classPage = item.getModelObject();
                
                Link<Void> button = new Link<Void>("link") {
                    @Override
                    public void onClick() {
                        setResponsePage(classPage);
                    }                    
                };
                button.add(new Label("label", classPage.getSimpleName()));
                
                item.add(button);
            }
        };
        add(listView);
        
    }

    

}
