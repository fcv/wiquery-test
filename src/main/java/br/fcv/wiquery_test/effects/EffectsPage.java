package br.fcv.wiquery_test.effects;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.devutils.inspector.InspectorPage;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.odlabs.wiquery.core.events.Event;
import org.odlabs.wiquery.core.events.MouseEvent;
import org.odlabs.wiquery.core.events.WiQueryAjaxEventBehavior;
import org.odlabs.wiquery.core.events.WiQueryEventBehavior;
import org.odlabs.wiquery.core.javascript.JsQuery;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.core.javascript.JsStatement;
import org.odlabs.wiquery.ui.effects.ShakeEffect;
import org.odlabs.wiquery.ui.effects.ShakeEffectJavaScriptResourceReference;

public class EffectsPage extends WebPage {

    public EffectsPage() {

        WebMarkupContainer button = new WebMarkupContainer("button");
        button.add(new WiQueryEventBehavior(new Event(MouseEvent.CLICK) {

            @Override
            public JsScope callback() {
                return JsScope.quickScope(new JsStatement().$(null, "#target")
                        .chain(new ShakeEffect()).render());
            }
        }));
        add(button);

        Button buttonAjax = new Button("buttonAjax");
        buttonAjax.add(new WiQueryAjaxEventBehavior(MouseEvent.CLICK) {

            @Override
            protected void onEvent(AjaxRequestTarget target) {
                target.appendJavaScript(new JsQuery().$("#target")
                        .chain(new ShakeEffect()).render());
            }

        });
        add(buttonAjax);
        
        add(new Link<Void>("inspector") {

            @Override
            public void onClick() {
                
                PageParameters params = new PageParameters();
                params.add("pageId", getPage().getId());
                setResponsePage(new InspectorPage(params)) ;               
            }            
        });
    }
    
    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderJavaScriptReference(ShakeEffectJavaScriptResourceReference.get());
    }

}
