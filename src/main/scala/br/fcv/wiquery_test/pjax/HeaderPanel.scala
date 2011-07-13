package br.fcv.wiquery_test.pjax

import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.markup.html.basic.Label

class HeaderPanel(id: String) extends Panel(id) {
    
    add(new Label("now", new NowModel()));

}