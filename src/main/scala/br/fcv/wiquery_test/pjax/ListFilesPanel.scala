package br.fcv.wiquery_test.pjax

import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import org.apache.wicket.markup.html.list.ListView
import java.io.File
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.list.ListItem

import scala.collection.JavaConversions._

class ListFilesPanel(id: String, parameters: PageParameters) extends Panel(id) {

    protected def home = new File(System.getProperty("user.home"))

    private implicit val order: Ordering[File] = new Ordering[File] {
        override def compare(f1: File, f2: File) = {
            var result = (if (f1.isDirectory) 0 else 1) - (if (f2.isDirectory) 0 else 1)
            if (result == 0)
                result = f1.getName.compareTo(f2.getName)
            result
        }
    }

    private val files = {
        val folder = (0 until parameters.getIndexedCount) // -- iterator over index
            .map(i => parameters.get(i)) //-- map to List[StringValue]
            .foldLeft(home) { (folder, param) => new File(folder, param.toString) } // fold over params values creating folder hierarchy

        if (!folder.exists)
            throw new AbortWithHttpErrorCodeException(404, "folder does not exist")

        // sort directories first and then by name
        folder.listFiles
            .filter(!_.isHidden)
            .sorted
    }

    add(new BookmarkablePageLink("up", classOf[ListFilesPage], new PageParameters(parameters).remove(parameters.getIndexedCount - 1))
        .setEnabled(parameters.getIndexedCount > 0))

    add(new ListView("files", files.toList) {
        override def populateItem(item: ListItem[File]) {
            val file = item.getModelObject;
            item
                .add(new Label("type", if (file.isDirectory) "D" else "F"))

            val newParams = new PageParameters(parameters);
            newParams.set(newParams.getIndexedCount, file.getName);
            val link = new BookmarkablePageLink("link", classOf[ListFilesPage], newParams)
            link.setEnabled(file.isDirectory)

            link.add(new Label("filename", file.getName))

            item.add(link)
        }
    })

    add(new Label("now", new NowModel))

}