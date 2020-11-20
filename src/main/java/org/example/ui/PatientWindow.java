package org.example.ui;

import com.vaadin.ui.*;
import org.example.dao.PatientDAO;
import org.example.entity.Patient;

public class PatientWindow extends Window {

    private VerticalLayout root;
    private Label nameWindowLabel;
    private HorizontalLayout nameLayout;
    private HorizontalLayout surnameLayout;
    private HorizontalLayout patronymicLayout;
    private HorizontalLayout numberPhoneLayout;
    private HorizontalLayout buttonLayout;
    private Label nameLabel;
    private TextField nameField;
    private Label surnameLabel;
    private TextField surnameField;
    private Label patronymicLabel;
    private TextField patronymicField;
    private Label numberPhoneLabel;
    private TextField numberPhoneField;
    private Button okButton;
    private Button closeButton;
    private Patient patient;
    private PatientDAO patientDAO;
    private MainUI mainUI;

    public PatientWindow(PatientDAO patientDAO, MainUI mainUI) {
        this.patientDAO = patientDAO;
        this.mainUI = mainUI;
        init();
    }

    public PatientWindow(PatientDAO patientDAO, MainUI mainUI, Patient patient) {
        this.patientDAO = patientDAO;
        this.mainUI = mainUI;
        this.patient = patient;
        init();
    }

    public void init() {
        initForm();
        if (patient != null) {
            initField();
        }
        buttonEvent();
        setContent(root);
    }

    public void initForm() {
        center();
        setWidth("400px");
        setHeight("400px");
        root = new VerticalLayout();
        nameWindowLabel = new Label("Пациенты");
        nameLayout = new HorizontalLayout();
        surnameLayout = new HorizontalLayout();
        patronymicLayout = new HorizontalLayout();
        numberPhoneLayout = new HorizontalLayout();
        buttonLayout = new HorizontalLayout();
        nameLabel = new Label("Имя");
        nameField = new TextField();
        surnameLabel = new Label("Фамилия");
        surnameField = new TextField();
        patronymicLabel = new Label("Отчество");
        patronymicField = new TextField();
        numberPhoneLabel = new Label("Номер телефона");
        okButton = new Button("Ок");
        closeButton = new Button("Отмена");
        numberPhoneField = new TextField();

        nameLabel.setWidth("150px");
        surnameLabel.setWidth("150px");
        patronymicLabel.setWidth("150px");
        numberPhoneLabel.setWidth("150px");
        okButton.setWidth("100px");
        closeButton.setWidth("100px");

        nameLayout.addComponents(nameLabel, nameField);
        surnameLayout.addComponents(surnameLabel, surnameField);
        patronymicLayout.addComponents(patronymicLabel, patronymicField);
        numberPhoneLayout.addComponents(numberPhoneLabel, numberPhoneField);
        buttonLayout.addComponents(okButton, closeButton);
        root.addComponents(nameWindowLabel, nameLayout, surnameLayout, patronymicLayout, numberPhoneLayout, buttonLayout);
    }

    private void initField() {
        nameField.setValue(patient.getName());
        surnameField.setValue(patient.getSurname());
        patronymicField.setValue(patient.getPatronymic());
        numberPhoneField.setValue(String.valueOf(patient.getPhoneNumber()));
    }

    private void buttonEvent() {

        okButton.addClickListener(clickEvent -> {
            Patient newPatient = new Patient();
            try {
                newPatient.setName(nameField.getValue().isEmpty() ? null : nameField.getValue());
                newPatient.setSurname(surnameField.getValue().isEmpty() ? null : surnameField.getValue());
                newPatient.setPatronymic(patronymicField.getValue().isEmpty() ? null : patronymicField.getValue());
                newPatient.setPhoneNumber(Long.parseLong(numberPhoneField.getValue()));

                if (newPatient.getName() == null || newPatient.getSurname() == null || newPatient.getPatronymic() == null) {
                    throw new NullPointerException();
                }

                //редактирование
                if (patient != null) {
                    patientDAO.update(patient.getId(), newPatient);
                } else { // создание
                    patientDAO.save(newPatient);
                }
            } catch (NullPointerException e) {
                mainUI.addWindow(new ErrorWindow("Основные поля остались не заполены..."));
            } catch (NumberFormatException e) {
                mainUI.addWindow(new ErrorWindow("Неверный формат ввода пароля.."));
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
