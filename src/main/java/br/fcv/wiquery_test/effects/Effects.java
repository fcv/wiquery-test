package br.fcv.wiquery_test.effects;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.odlabs.wiquery.core.commons.IWiQueryPlugin;
import org.odlabs.wiquery.core.commons.WiQueryResourceManager;
import org.odlabs.wiquery.core.events.Event;
import org.odlabs.wiquery.core.events.MouseEvent;
import org.odlabs.wiquery.core.events.WiQueryAjaxEventBehavior;
import org.odlabs.wiquery.core.events.WiQueryEventBehavior;
import org.odlabs.wiquery.core.javascript.JsQuery;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.core.javascript.JsStatement;
import org.odlabs.wiquery.ui.effects.EffectsHelper;
import org.odlabs.wiquery.ui.effects.ShakeEffect;

public class Effects extends WebPage implements IWiQueryPlugin {

    public Effects() {

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
                target.appendJavascript(new JsQuery().$("#target")
                        .chain(new ShakeEffect()).render().toString());
            }

        });
        add(buttonAjax);
    }

    public void contribute(WiQueryResourceManager manager) {
        EffectsHelper.shake(manager);
    }

    public JsStatement statement() {
        return null;
    }

}
