package br.fcv.wiquery_test.date_picker;

import java.util.Locale;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.util.convert.IConverter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.odlabs.wiquery.ui.datepicker.DatePicker;
import org.odlabs.wiquery.ui.datepicker.DatePicker.ShowOnEnum;

public class CalendarPage extends WebPage {
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern("dd/MM/yyyy");
    
    public CalendarPage() {
        final IModel<DateHolder> model = new LoadableDetachableModel<DateHolder>() {
            @Override
            protected DateHolder load() {
                return new DateHolder();
            }
        };
        
        final Form<DateHolder> form = new Form<DateHolder>("form", new CompoundPropertyModel<DateHolder>(model));
        
        final DatePicker<DateTime> datePicker = new DatePicker<DateTime>("date", DateTime.class) {
            
            @SuppressWarnings("unchecked")
            @Override
            public <C> IConverter<C> getConverter(Class<C> type) {
                
                return (IConverter<C>) new IConverter<DateTime>() {
                    @Override
                    public DateTime convertToObject(String value, Locale locale) {
                        DateTime dt = FORMATTER.parseDateTime(value); 
                        return dt;
                    }
                    
                    public String convertToString(DateTime value, Locale locale) {
                        return FORMATTER.print(value);
                    };
                };
            };
        }.setShowOn(ShowOnEnum.FOCUS);
        
        final IModel<String> labelModel = new LoadableDetachableModel<String>() {
            @Override
            protected String load() {
                
                DateTime date = form.getModelObject().getDate();
                return String.valueOf(date);
            }
        };
        final Label label = new Label("label", labelModel);
        
        final SubmitLink button = new SubmitLink("submit");
        
        
        add(form.add(datePicker).add(button))
            .add(label);
    }

    public static class DateHolder {
        private DateTime date;
        
        public void setDate(DateTime date) {
            this.date = date;
        }
        
        public DateTime getDate() {
            return date;
        }
    }
    
}
