package br.fcv.wiquery_test.pjax

import java.text.SimpleDateFormat
import java.util.Date

import org.apache.wicket.model.LoadableDetachableModel

class NowModel(format: String) extends LoadableDetachableModel[String] {
 
    def this() = this("dd-MM-yyyy HH:mm:ss.SSSS");

    override protected def load() = {
        val formatter = new SimpleDateFormat(format);
        formatter.format(new Date());
    }
}