package com.example.vaadindemo;

import com.example.vaadindemo.domain.Person;
import com.vaadin.annotations.Title;
import com.vaadin.ui.Field;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.VerticalLayout;

@Title("Vaadin Demo App")
public class VaadinApp extends UI {
	
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("deprecation")
	@Override
	protected void init(VaadinRequest request) {
		
		HorizontalLayout hl = new HorizontalLayout();
		VerticalLayout vl = new VerticalLayout();
		
	//Item
		Person person = new Person("Kazik", 1945);
		BeanItem<Person> personBean = new BeanItem<Person>(person);
		
		final FormLayout formLayout = new FormLayout();
		
		//new: manual form fields building
		/*final TextField imieTF = new TextField("Imię", personBean.getItemProperty("firstName"));
		formLayout.addComponent(imieTF);
		imieTF.setImmediate(true);
		
		imieTF.setRequired(true);
		imieTF.addValidator(new StringLengthValidator("Zła długość", 2, 5, false));
		*/
		//--
		final FieldGroup form = new FieldGroup(); //new name: form
		//new
		form.setItemDataSource(personBean); 
		form.setBuffered(true); 
		//---
		Field imieField = form.buildAndBind("Name", "firstName");
		imieField.setRequired(true);
		imieField.addValidator(new StringLengthValidator("bad length", 2, 5, false));
		formLayout.addComponent(imieField);
		formLayout.addComponent(form.buildAndBind("YOB", "yob"));
		
	//---

		
	//Container
		final BeanItemContainer<Person> beanContainer = new BeanItemContainer<Person>(Person.class);
		beanContainer.addBean(new Person("Bolek", 1934));
		beanContainer.addBean(new Person("Lolek", 1933));
		
		final Table personTable = new Table();
		
		//container data binding
		personTable.setContainerDataSource(beanContainer);
		
		personTable.setSelectable(true);
		personTable.setImmediate(true);
		
		
		setContent(hl);
		
		personTable.addValueChangeListener(new Table.ValueChangeListener() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(personTable.getValue() != null){
					//new: bind selected item into form
					Person selectedPerson = (Person) personTable.getValue();
					Notification.show("Wybrano osobę " + selectedPerson.toString());
					BeanItem<Person> newPersonBean = new BeanItem<Person>(selectedPerson);
					form.setItemDataSource(newPersonBean);
					
					//imieTF.setValue(selectedPerson.getFirstName()); //manually set form field value
					//--
				}
				
			}
		});
	//---
		
		
		//new: add person from form into table
		final Button addBtn = new Button("Add");

		final Label errLbl = new Label();
		errLbl.setContentMode(ContentMode.HTML);
		
		addBtn.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				try {
					form.commit();
					@SuppressWarnings("unchecked")
					Person newPerson = ((BeanItem<Person>) form.getItemDataSource()).getBean();
					Notification.show("Osoba z formularza " + newPerson.toString());					
					Person p = new Person(newPerson.getFirstName(), newPerson.getYob());
					beanContainer.addBean(p);
				} catch (CommitException e) { //show error message
					for(com.vaadin.ui.Field<?> field: form.getFields()){
						ErrorMessage errmsg = ((AbstractField<?>)field).getErrorMessage();
						if(errmsg != null){
							errLbl.setValue(errmsg.getFormattedHtmlMessage());
						}
					}
				}		
			}
		});
		//---
		
		//setting layout
		hl.addComponent(personTable);
		vl.addComponent(formLayout);
		vl.addComponent(addBtn);
		vl.addComponent(errLbl);
		
		hl.addComponent(vl);

	}

}
