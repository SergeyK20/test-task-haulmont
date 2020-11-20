package org.example.ui;

import com.vaadin.ui.*;
import org.example.dao.DoctorDAO;
import org.example.entity.Doctor;

public class StatisticWindow extends Window {

    private VerticalLayout root;
    private Grid<Doctor> doctorTable;
    private DoctorDAO doctorDAO;

    public StatisticWindow(DoctorDAO doctorDAO) {
        this.doctorDAO = doctorDAO;
        init();
    }

    private void init() {
        root = new VerticalLayout();
        center();
        setWidth("600px");
        setHeight("500px");
        initGrid();
        initButton();
        setContent(root);
    }

    private void initButton() {
        Button button = new Button("Ок");
        button.addClickListener(click -> close());
        root.addComponent(button);
        root.setComponentAlignment(button, Alignment.MIDDLE_CENTER);
    }

    private void initGrid() {
        doctorTable = new Grid<>();
        doctorTable.setItems(doctorDAO.findAll());

        doctorTable.addColumn(element -> element.getName() + " " + element.getSurname()).setCaption("Name and surname doctor");
        doctorTable.addColumn(element -> doctorDAO.countRecipe(element.getId())).setCaption("Count recipe");


        root.addComponent(doctorTable);
        root.setComponentAlignment(doctorTable, Alignment.MIDDLE_CENTER);
    }
}
