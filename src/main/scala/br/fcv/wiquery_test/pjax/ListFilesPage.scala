package br.fcv.wiquery_test.pjax;

import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import scala.collection.JavaConversions.seqAsJavaList
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.list.ListItem
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.model.LoadableDetachableModel
import org.apache.wicket.request.cycle.RequestCycle
import org.apache.wicket.request.http.WebRequest
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.markup.html.list.ListView
import org.apache.wicket.markup.html.link.Link
import org.apache.wicket.markup.html.link.BookmarkablePageLink



class ListFilesPage(parameters: PageParameters) extends WebPage {

    add(new Header("header"));
    add(new ListFile("folder", parameters))   

    protected def isPjax = {
        val request = RequestCycle.get().getRequest().asInstanceOf[WebRequest];
        request.getHeader("X-PJAX").toBoolean;
    }

    class Header(id: String) extends WebMarkupContainer(id) {
        add(new Label("now", new NowModel()));
    }
    
    class ListFile(id: String, parameters: PageParameters) extends WebMarkupContainer(id) {
        
        private implicit val order: Ordering[File] = new Ordering[File] {
            override def compare(f1: File, f2: File) = {
                var result = (if (f1.isDirectory) 0 else 1) - (if (f2.isDirectory) 0 else 1)
                if (result == 0)
                    result = f1.getName.compareTo(f2.getName)
                result
            }
        }
        
        private val files = {
            var folder = new File(System.getProperty("user.home"))
            for (i <- 0 until parameters.getIndexedCount)
                folder = new File(folder, parameters.get(i).toString)
            
            // sort directories first and then by name
            folder.listFiles
                    .filter( !_.isHidden )
                    .sorted
        }
        
        val newParams = new PageParameters(parameters);
        if (newParams.getIndexedCount > 0)
            newParams.remove(newParams.getIndexedCount - 1);
        private val up = new BookmarkablePageLink("up", classOf[ListFilesPage], newParams)
        up.setEnabled( parameters.getIndexedCount > 0 )
        add(up)
        
        add(new ListView("files", files.toList) {
            override def populateItem(item: ListItem[File]) {
                val file = item.getModelObject;
                item
                    .add(new Label("type", if (file.isDirectory) "D" else "F" ))
                
                val newParams = new PageParameters(parameters);
                newParams.set(newParams.getIndexedCount, file.getName);
                val link = new BookmarkablePageLink("link", classOf[ListFilesPage], newParams)
                link.setEnabled(file.isDirectory)
                
                link.add(new Label("filename", file.getName))
                
                item.add(link)
            }
        })
    }

    class NowModel(format: String) extends LoadableDetachableModel[String] {
        def this() = this("dd-MM-yyyy HH:mm:ss.SSSS");

        override protected def load() = {
            val formatter = new SimpleDateFormat(format);
            formatter.format(new Date());
        }
    }

}
