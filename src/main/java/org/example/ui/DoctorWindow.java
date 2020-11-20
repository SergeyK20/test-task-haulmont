package org.example.ui;

import com.vaadin.ui.*;
import org.example.dao.DoctorDAO;
import org.example.entity.Doctor;


public class DoctorWindow extends Window {

    private VerticalLayout root;
    private Label nameWindowLabel;
    private HorizontalLayout nameLayout;
    private HorizontalLayout surnameLayout;
    private HorizontalLayout patronymicLayout;
    private HorizontalLayout specializationLayout;
    private HorizontalLayout buttonLayout;
    private Label nameLabel;
    private TextField nameField;
    private Label surnameLabel;
    private TextField surnameField;
    private Label patronymicLabel;
    private TextField patronymicField;
    private Label specializationLabel;
    private TextField specializationField;
    private Button okButton;
    private Button closeButton;
    private Doctor doctor;
    private DoctorDAO doctorDAO;
    private MainUI mainUI;

    public DoctorWindow(DoctorDAO doctorDAO, MainUI mainUI) {
        this.doctorDAO = doctorDAO;
        this.mainUI = mainUI;
        init();
    }

    public DoctorWindow(Doctor doctor, DoctorDAO doctorDAO, MainUI mainUI) {
        this.mainUI = mainUI;
        this.doctorDAO = doctorDAO;
        this.doctor = doctor;
        init();
    }

    public void init() {
        initForm();
        if (doctor != null) {
            initField();
        }
        buttonEvent();
        setContent(root);
    }

    private void initField() {
        nameField.setValue(doctor.getName());
        surnameField.setValue(doctor.getSurname());
        patronymicField.setValue(doctor.getPatronymic());
        specializationField.setValue(doctor.getSpecialization());
    }

    public void initForm() {
        center();
        setWidth("400px");
        setHeight("400px");
        root = new VerticalLayout();
        nameWindowLabel = new Label("Доктора");
        nameLayout = new HorizontalLayout();
        surnameLayout = new HorizontalLayout();
        patronymicLayout = new HorizontalLayout();
        specializationLayout = new HorizontalLayout();
        buttonLayout = new HorizontalLayout();
        nameLabel = new Label("Имя");
        nameField = new TextField();
        surnameLabel = new Label("Фамилия");
        surnameField = new TextField();
        patronymicLabel = new Label("Отчество");
        patronymicField = new TextField();
        specializationLabel = new Label("Специализация");
        okButton = new Button("Ок");
        closeButton = new Button("Отмена");
        specializationField = new TextField();

        nameLabel.setWidth("150px");
        surnameLabel.setWidth("150px");
        patronymicLabel.setWidth("150px");
        specializationLabel.setWidth("150px");
        okButton.setWidth("100px");
        closeButton.setWidth("100px");

        nameLayout.addComponents(nameLabel, nameField);
        surnameLayout.addComponents(surnameLabel, surnameField);
        patronymicLayout.addComponents(patronymicLabel, patronymicField);
        specializationLayout.addComponents(specializationLabel, specializationField);
        buttonLayout.addComponents(okButton, closeButton);
        root.addComponents(nameWindowLabel, nameLayout, surnameLayout, patronymicLayout, specializationLayout, buttonLayout);
    }

    private void buttonEvent() {
        okButton.addClickListener(clickEvent -> {
            Doctor newDoctor = new Doctor();
            try {
                newDoctor.setName(nameField.getValue().isEmpty() ? null : nameField.getValue());
                newDoctor.setSurname(surnameField.getValue().isEmpty() ? null : surnameField.getValue());
                newDoctor.setPatronymic(patronymicField.getValue().isEmpty() ? null : patronymicField.getValue());
                newDoctor.setSpecialization(specializationField.getValue().isEmpty() ? null : specializationField.getValue());

                if (newDoctor.getName() == null || newDoctor.getSurname() == null || newDoctor.getPatronymic() == null || newDoctor.getSpecialization() == null) {
                    throw new NullPointerException();
                }

                //редактирование
                if (doctor != null) {
                    doctorDAO.update(doctor.getId(), newDoctor);
                } else { // создание
                    doctorDAO.save(newDoctor);
                }

            } catch (NullPointerException e) {
                mainUI.addWindow(new ErrorWindow("Основные поля остались не заполенны..."));
            } catch (Exception e) {
                mainUI.addWindow(new ErrorWindow("Не удалось загрузить в бд.."));
            }
            mainUI.updateValueGrid();
            close();
        });

        closeButton.addClickListener(clickEvent ->

                close());
    }
}
