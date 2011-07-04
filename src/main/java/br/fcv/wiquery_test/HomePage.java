package br.fcv.wiquery_test;

import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.PropertyModel;
import org.odlabs.wiquery.core.events.MouseEvent;
import org.odlabs.wiquery.core.events.WiQueryAjaxEventBehavior;
import org.odlabs.wiquery.ui.dialog.Dialog;

public class HomePage extends WebPage {
	
	private int counter = 0;

	public HomePage(final PageParameters parameters) {

		final Dialog dialog = new Dialog("dialog");
		add(dialog);
		
		final Label counter = new Label("counter", new PropertyModel<String>(this, "counter"));
		counter.setOutputMarkupId(true);
		dialog.add(counter);

		Button button = new Button("open-dialog");
		button.add(new WiQueryAjaxEventBehavior(MouseEvent.CLICK) {

			@Override
			protected void onEvent(AjaxRequestTarget target) {
				increment();				
				target.addComponent(counter);
				target.appendJavascript(dialog.open().render().toString());
			}

		});
		add(button);
	}	
	
	private void increment() {
		counter++;
	}

}
