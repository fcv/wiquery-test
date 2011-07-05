package br.fcv.wiquery_test.lazy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.fcv.wiquery_test.support.wicket.AjaxLazyLoadPanel;

/**
 * Used to test lazy loading components.. not really wiquery stuff.. but whatever
 * @author veronez
 *
 */
public class LazyComponentsPage extends WebPage {
    
    private static final Logger logger = LoggerFactory.getLogger(LazyComponentsPage.class);
    
    private final AtomicInteger counter = new AtomicInteger(0);
    
    public LazyComponentsPage() {
        
        for (int i = 1; i <= 4; i ++) {
            final int index = i;
            add(new AjaxLazyLoadPanel("lazy-" + i) {
                
                @Override
                public Component getLazyLoadComponent(String id) {
                    logger.debug("entered into 'getLazyLoadComponent' method for index {}", index);                    
                    long time = (long) (Math.random() * 8000);
                    logger.debug("time to wait: {} (in ms)", time);
                    long start = System.currentTimeMillis();
                    try {
                        Thread.sleep(time);                        
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    long end = System.currentTimeMillis();
                    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSSS");
                    
                    Label label = new Label(id, String.format("%s - [%s] Start: %s, End: %s, after wait %s miliseconds (real waiting: %s)",
                            counter.incrementAndGet(),
                            Thread.currentThread().getName(),
                            formatter.format(new Date(start)),
                            formatter.format(new Date(end)),
                            time,
                            end - start
                        )); 
                    logger.debug("returning from 'getLazyLoadComponent' method for {} component", id);
                    return label;
                }
            });
        }
        
    }

}
