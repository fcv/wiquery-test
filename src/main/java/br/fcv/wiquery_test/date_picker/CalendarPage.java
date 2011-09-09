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
import org.joda.time.LocalDate;
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
        
        final DatePicker<LocalDate> datePicker = new DatePicker<LocalDate>("date", LocalDate.class) {
            
            @SuppressWarnings("unchecked")
            @Override
            public <C> IConverter<C> getConverter(Class<C> type) {
                
                return (IConverter<C>) new IConverter<LocalDate>() {
                    @Override
                    public LocalDate convertToObject(String value, Locale locale) {
                        LocalDate dt = FORMATTER.parseLocalDate(value); 
                        return dt;
                    }
                    
                    public String convertToString(LocalDate value, Locale locale) {
                        return FORMATTER.print(value);
                    };
                };
            };
        };
        datePicker
            .setShowOn(ShowOnEnum.FOCUS)
            .setDateFormat("dd/mm/yy");
        
        final IModel<String> labelModel = new LoadableDetachableModel<String>() {
            @Override
            protected String load() {
                
                LocalDate date = form.getModelObject().getDate();
                return String.valueOf(date);
            }
        };
        final Label label = new Label("label", labelModel);
        
        final SubmitLink button = new SubmitLink("submit");
        
        
        add(form.add(datePicker).add(button))
            .add(label);
    }

    public static class DateHolder {
        private LocalDate date;
        
        public void setDate(LocalDate date) {
            this.date = date;
        }
        
        public LocalDate getDate() {
            return date;
        }
    }
    
}
