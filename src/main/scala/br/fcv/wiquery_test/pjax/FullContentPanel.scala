package br.fcv.wiquery_test.pjax

import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.request.mapper.parameter.PageParameters

class FullContentPanel(id: String, params: PageParameters) extends Panel(id) {

    add(new HeaderPanel("header"))
    add(new ListFilesPanel("folder", params))
}