package br.fcv.wiquery_test;

import org.apache.wicket.protocol.http.WebApplication;

import br.fcv.wiquery_test.effects.Effects;

/**
 * Application object for your web application. If you want to run this
 * application without deploying, run the Start class.
 * 
 * @see br.fcv.wiquery_test.Start#main(String[])
 */
public class WicketApplication extends WebApplication {
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

        mountBookmarkablePage("effects", Effects.class);
    }
}
