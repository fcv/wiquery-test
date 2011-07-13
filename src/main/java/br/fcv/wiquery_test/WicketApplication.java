package br.fcv.wiquery_test;

import org.apache.wicket.Application;
import org.apache.wicket.protocol.http.WebApplication;
import org.odlabs.wiquery.core.commons.IWiQueryInitializer;
import org.odlabs.wiquery.core.commons.WiQuerySettings;

import br.fcv.wiquery_test.dialog.DialogPage;
import br.fcv.wiquery_test.effects.EffectsPage;
import br.fcv.wiquery_test.lazy.LazyComponentsPage;
import br.fcv.wiquery_test.pjax.ListFilesPage;
import br.fcv.wiquery_test.pjax.PjaxPage;
import br.fcv.wiquery_test.pjax.SimplePjaxExamplePage;

/**
 * Application object for your web application. If you want to run this
 * application without deploying, run the Start class.
 * 
 * @see br.fcv.wiquery_test.Start#main(String[])
 */
public class WicketApplication extends WebApplication implements IWiQueryInitializer {
    /**
     * @see org.apache.wicket.Application#getHomePage()
     */
    @Override
    public Class<HomePage> getHomePage() {
        return HomePage.class;
    }

    /**
     * @see org.apache.wicket.Application#init()
     */
    @Override
    public void init() {
        super.init();

        mountPage("effects", EffectsPage.class);
        mountPage("lazy", LazyComponentsPage.class);
        mountPage("dialog", DialogPage.class);
        mountPage("pjax", PjaxPage.class);
        mountPage("simple-pjax", SimplePjaxExamplePage.class);
        mountPage("list-files", ListFilesPage.class);
        
    }
    
    public void init(Application application, WiQuerySettings settings) {
        settings.setMinifiedJavaScriptResources(true);        
    }
    
    public static WicketApplication get() {
        return (WicketApplication) Application.get();
    }

}
