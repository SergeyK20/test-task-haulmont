package org.example.ui;

import com.vaadin.ui.*;
import org.example.dao.DoctorDAO;
import org.example.dao.PatientDAO;
import org.example.dao.RecipeDAO;
import org.example.entity.Doctor;
import org.example.entity.Patient;
import org.example.entity.Priority;
import org.example.entity.Recipe;

import java.sql.Date;
import java.util.List;


public class RecipeWindow extends Window {

    private VerticalLayout root;
    private Label nameWindowLabel;
    private HorizontalLayout doctorLayout;
    private HorizontalLayout patientLayout;
    private HorizontalLayout dateCreateLayout;
    private HorizontalLayout validityLayout;
    private HorizontalLayout priorityLayout;
    private HorizontalLayout descriptionLayout;
    private HorizontalLayout buttonLayout;
    private Label doctorLabel;
    private ComboBox<String> doctorComboBox;
    private Label patientLabel;
    private ComboBox<String> patientComboBox;
    private Label dateCreateLabel;
    private DateField dateCreateField;
    private Label validityLabel;
    private DateField validityField;
    private Label priorityLabel;
    private ComboBox<String> priorityComboBox;
    private Label descriptionLabel;
    private TextField descriptionField;
    private Button okButton;
    private Button closeButton;
    private List<Doctor> doctors;
    private List<Patient> patients;
    private DoctorDAO doctorDAO;
    private PatientDAO patientDAO;
    private RecipeDAO recipeDAO;
    private MainUI mainUI;
    private Recipe recipe;

    public RecipeWindow(DoctorDAO doctorDAO, PatientDAO patientDAO, RecipeDAO recipeDAO, MainUI mainUI) throws Exception {
        this.doctorDAO = doctorDAO;
        this.patientDAO = patientDAO;
        this.recipeDAO = recipeDAO;
        this.mainUI = mainUI;
        init();
    }

    public RecipeWindow(DoctorDAO doctorDAO, PatientDAO patientDAO, RecipeDAO recipeDAO, MainUI mainUI, Recipe recipe) throws Exception {
        this.doctorDAO = doctorDAO;
        this.patientDAO = patientDAO;
        this.recipeDAO = recipeDAO;
        this.mainUI = mainUI;
        this.recipe = recipe;
        init();
    }

    public void init() throws Exception {
        try {
            initForm();
            initComboBox();
            if (recipe != null) {
                initField();
            }
            buttonEvent();
            setContent(root);
        } catch (Exception e) {
            throw new Exception("Нет ключевых полей");
        }
    }

    public void initForm() {
        center();
        setWidth("600px");
        setHeight("600px");
        root = new VerticalLayout();
        nameWindowLabel = new Label("Рецепты");
        doctorLayout = new HorizontalLayout();
        patientLayout = new HorizontalLayout();
        dateCreateLayout = new HorizontalLayout();
        validityLayout = new HorizontalLayout();
        priorityLayout = new HorizontalLayout();
        descriptionLayout = new HorizontalLayout();
        buttonLayout = new HorizontalLayout();
        doctorLabel = new Label("Имя Фималия доктора");
        doctorComboBox = new ComboBox<>();
        patientLabel = new Label("Имя Фамилия пациента");
        patientComboBox = new ComboBox<>();
        dateCreateLabel = new Label("Дата создания рецепта");
        dateCreateField = new DateField();
        validityLabel = new Label("Срок годности рецепта");
        validityField = new DateField();
        priorityLabel = new Label("Приоритет");
        priorityComboBox = new ComboBox<>();
        descriptionLabel = new Label("Описание рецепта");
        descriptionField = new TextField();
        okButton = new Button("Ок");
        closeButton = new Button("Отмена");

        doctorLabel.setWidth("150px");
        patientLabel.setWidth("150px");
        dateCreateLabel.setWidth("150px");
        validityLabel.setWidth("150px");
        priorityLabel.setWidth("150px");
        descriptionLabel.setWidth("150px");
        okButton.setWidth("100px");
        closeButton.setWidth("100px");
        doctorComboBox.setWidth("250px");
        patientComboBox.setWidth("250px");

        doctorLayout.addComponents(doctorLabel, doctorComboBox);
        patientLayout.addComponents(patientLabel, patientComboBox);
        dateCreateLayout.addComponents(dateCreateLabel, dateCreateField);
        validityLayout.addComponents(validityLabel, validityField);
        priorityLayout.addComponents(priorityLabel, priorityComboBox);
        descriptionLayout.addComponents(descriptionLabel, descriptionField);
        buttonLayout.addComponents(okButton, closeButton);
        root.addComponents(nameWindowLabel, doctorLayout, patientLayout, dateCreateLayout, validityLayout, priorityLayout, descriptionLayout, buttonLayout);
    }

    private void initComboBox() {
        doctors = doctorDAO.findAll();
        doctorComboBox.setItems(doctors.stream().map(element -> element.getName() + " " + element.getSurname()));
        doctorComboBox.setValue(doctors.get(0).getName() + " " + doctors.get(0).getSurname());

        patients = patientDAO.findAll();
        patientComboBox.setItems(patients.stream().map(element -> element.getName() + " " + element.getSurname()));
        patientComboBox.setValue(patients.get(0).getName() + " " + patients.get(0).getSurname());

        priorityComboBox.setItems(Priority.NORMALLY.getListPriority());
        priorityComboBox.setValue(Priority.NORMALLY.getRussianNameOfPriority());
    }

    private void initField() throws Exception {
        doctorComboBox.setValue(recipe.getDoctor().getName() + " " + recipe.getDoctor().getSurname());
        patientComboBox.setValue(recipe.getPatient().getName() + " " + recipe.getPatient().getSurname());
        dateCreateField.setValue(recipe.getDateCreate().toLocalDate());
        validityField.setValue(recipe.getValidity().toLocalDate());
        priorityComboBox.setValue(recipe.getPriority().getRussianNameOfPriority());
        descriptionField.setValue(recipe.getDescription());
    }

    private void buttonEvent() {
        okButton.addClickListener(clickEvent -> {
            Recipe newRecipe = new Recipe();
            try {
                newRecipe.setDoctor(findDoctorByNameAndSurName(doctorComboBox.getValue()));
                newRecipe.setPatient(findPatientByNameAndSurName(patientComboBox.getValue()));
                newRecipe.setDateCreate(Date.valueOf(dateCreateField.getValue()));
                newRecipe.setValidity(Date.valueOf(validityField.getValue()));
                newRecipe.setPriority(Priority.NORMALLY.getPriorityByStringValue(priorityComboBox.getValue()));
                newRecipe.setDescription(descriptionField.getValue().isEmpty() ? null : descriptionField.getValue());

                if (newRecipe.getDateCreate().getTime() > newRecipe.getValidity().getTime()) {
                    mainUI.addWindow(new ErrorWindow("Дата создания не может быть позже\n срока годности рецепта.."));
                } else {
                    //редактирование
                    if (recipe != null) {
                        recipeDAO.update(recipe.getId(), newRecipe);
                    } else { // создание
                        recipeDAO.save(newRecipe);
                    }
                }
            } catch (NullPointerException e) {
                mainUI.addWindow(new ErrorWindow("Основные поля остались не заполенны..."));
            } catch (Exception e) {
                mainUI.addWindow(new ErrorWindow("Не удалось загрузить в бд.."));
            }

            mainUI.updateValueGrid();
            close();
        });
    }

    private Doctor findDoctorByNameAndSurName(String nameAndSurname) {
        for (Doctor doctor : doctors) {
            if ((doctor.getName() + " " + doctor.getSurname()).equals(nameAndSurname)) {
                return doctor;
            }
        }
        return null;
    }

    private Patient findPatientByNameAndSurName(String nameAndSurname) {
        for (Patient patient : patients) {
            if ((patient.getName() + " " + patient.getSurname()).equals(nameAndSurname)) {
                return patient;
            }
        }
        return null;
    }

}
