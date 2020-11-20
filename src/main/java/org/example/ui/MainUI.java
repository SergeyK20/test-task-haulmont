package org.example.ui;


import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.components.grid.HeaderRow;
import org.example.dao.DoctorDAO;
import org.example.dao.PatientDAO;
import org.example.dao.RecipeDAO;
import org.example.entity.Doctor;
import org.example.entity.Patient;
import org.example.entity.Recipe;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static org.example.ui.NameTable.*;

@SpringUI
public class MainUI extends UI {

    private VerticalLayout root;

    private HorizontalLayout horizontalLayoutForTableOption;

    private VerticalLayout verticalLayoutForGridAndActionButton;

    private HorizontalLayout horizontalLayoutButtonAction;

    private NameTable nameTable;

    private DoctorDAO doctorDAO;

    private PatientDAO patientDAO;

    private RecipeDAO recipeDAO;

    @Autowired
    public MainUI(DoctorDAO doctorDAO, PatientDAO patientDAO, RecipeDAO recipeDAO){
        this.doctorDAO = doctorDAO;
        this.patientDAO = patientDAO;
        this.recipeDAO = recipeDAO;
    }

    @Override
    protected void init(VaadinRequest request) {
        getPage().setTitle("Project");
        nameTable = NameTable.RECIPE;
        initLayout();
        initButtonVariantTable();
        root.addComponent(horizontalLayoutForTableOption);
        initButtonAction();
        updateValueGrid();
        root.setComponentAlignment(horizontalLayoutForTableOption, Alignment.MIDDLE_CENTER);
        setContent(root);
    }

    /**
     * Инициализируем главные макет на которых будет располагаться вся информация
     */
    private void initLayout() {
        root = new VerticalLayout();
        verticalLayoutForGridAndActionButton = new VerticalLayout();
        horizontalLayoutForTableOption = new HorizontalLayout();
        horizontalLayoutButtonAction = new HorizontalLayout();

        verticalLayoutForGridAndActionButton.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        root.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
    }

    /**
     * Инициализируем кнопки вариантов таблиц
     */
    protected void initButtonVariantTable() {
        Button doctorButton = new Button("Доктора");
        Button patientButton = new Button("Пациенты");
        Button recipeButton = new Button("Рецепты");

        horizontalLayoutForTableOption.addComponents(doctorButton, patientButton, recipeButton);


        Button statisticButton = new Button("Показать статистику");
        statisticButton.addClickListener(clickEvent -> {
            addWindow(new StatisticWindow(doctorDAO));
        });


        doctorButton.addClickListener(clickEvent -> {
            nameTable = DOCTOR;
            if (horizontalLayoutButtonAction.getComponentCount() < 4) {
                horizontalLayoutButtonAction.addComponent(statisticButton);
            }
            updateValueGrid();
        });

        patientButton.addClickListener(clickEvent -> {
            if (horizontalLayoutButtonAction.getComponentCount() == 4) {
                horizontalLayoutButtonAction.removeComponent(statisticButton);
            }
            nameTable = PATIENT;
            updateValueGrid();
        });

        recipeButton.addClickListener(clickEvent -> {
            if (horizontalLayoutButtonAction.getComponentCount() == 4) {
                horizontalLayoutButtonAction.removeComponent(statisticButton);
            }
            nameTable = RECIPE;
            updateValueGrid();
        });
    }

    /**
     * Обновляет таблицу
     */
    public void updateValueGrid() {
        switch (nameTable) {
            case DOCTOR:
                Grid<Doctor> gridDoctor = new Grid<>();
                gridDoctor.setItems(doctorDAO.findAll());
                gridDoctor.addColumn(Doctor::getName).setCaption("Name");
                gridDoctor.addColumn(Doctor::getSurname).setCaption("Surname");
                gridDoctor.addColumn(Doctor::getPatronymic).setCaption("Patronymic");
                gridDoctor.addColumn(Doctor::getSpecialization).setCaption("Specialization");
                addOnComponent(gridDoctor);
                break;
            case PATIENT:
                Grid<Patient> gridPatient = new Grid<>();
                gridPatient.setItems(patientDAO.findAll());
                gridPatient.addColumn(Patient::getName).setCaption("Name");
                gridPatient.addColumn(Patient::getSurname).setCaption("Surname");
                gridPatient.addColumn(Patient::getPatronymic).setCaption("Patronymic");
                gridPatient.addColumn(Patient::getPhoneNumber).setCaption("Number phone");
                addOnComponent(gridPatient);
                break;
            case RECIPE:
                Grid<Recipe> gridRecipe = new Grid<>();
                gridRecipe.setItems(recipeDAO.findAll());
                gridRecipe.addColumn(recipe -> recipe.getDoctor().getName() + " " + recipe.getDoctor().getSurname()).setCaption("Name doctor");
                gridRecipe.addColumn(recipe -> recipe.getPatient().getName() + " " + recipe.getPatient().getSurname()).setCaption("Name patient");
                gridRecipe.addColumn(Recipe::getDateCreate).setCaption("Date create recipe");
                gridRecipe.addColumn(Recipe::getValidity).setCaption("Validity");
                gridRecipe.addColumn(recipe -> recipe.getPriority().getRussianNameOfPriority()).setCaption("Priority");
                gridRecipe.addColumn(Recipe::getDescription).setCaption("Description");
                addFilter(gridRecipe);
                addOnComponent(gridRecipe);
                break;
        }
    }

    /**
     * Инициализирует кнопки действия (удаление, добавление, редактирование)
     */
    private void initButtonAction() {
        Button addButton = new Button("Добавить");
        Button updateButton = new Button("Редактировать");
        Button deleteButton = new Button("Удалить");

        addButton.addClickListener(clickEvent -> {
            try {
                switch (nameTable) {
                    case DOCTOR:
                        addWindow(new DoctorWindow(doctorDAO, this));
                        break;
                    case PATIENT:
                        try {
                            addWindow(new PatientWindow(patientDAO, this));
                        } catch (NumberFormatException e) {
                            addWindow(new ErrorWindow("Неправильный формат номера телефона"));
                        }
                        break;
                    case RECIPE:
                        addWindow(new RecipeWindow(doctorDAO, patientDAO, recipeDAO, this));
                        break;
                }
            } catch (NullPointerException e) {
                addWindow(new ErrorWindow("Не заполненные ключевые поля"));
            } catch (Exception e) {
                addWindow(new ErrorWindow(e.getMessage()));
            }
        });

        updateButton.addClickListener(clickEvent -> {
            try {
                Grid<?> grid1 = (Grid<?>) verticalLayoutForGridAndActionButton.getComponent(0);
                Set<?> set = grid1.getSelectionModel().getSelectedItems();
                switch (nameTable) {
                    case DOCTOR:
                        Doctor doctor = (Doctor) set.toArray()[0];
                        addWindow(new DoctorWindow(doctor, doctorDAO, this));
                        break;
                    case PATIENT:
                        try {
                            Patient patient = (Patient) set.toArray()[0];
                            addWindow(new PatientWindow(patientDAO, this, patient));
                        } catch (NumberFormatException e) {
                            addWindow(new ErrorWindow("Неправильный формат номера телефона"));
                        }
                        break;
                    case RECIPE:
                        Recipe recipe = (Recipe) set.toArray()[0];
                        addWindow(new RecipeWindow(doctorDAO, patientDAO, recipeDAO, this, recipe));
                        break;
                }
            } catch (NullPointerException e) {
                addWindow(new ErrorWindow("Не заполненные ключевые поля"));
            } catch (ArrayIndexOutOfBoundsException e) {
                addWindow(new ErrorWindow("Не выбранно ни одного значения"));
            } catch (Exception e) {
                addWindow(new ErrorWindow(e.getMessage()));
            }
        });

        deleteButton.addClickListener(clickEvent -> {
            long id = -1;
            Grid<?> grid1 = (Grid<?>) verticalLayoutForGridAndActionButton.getComponent(0);
            Set<?> set = grid1.getSelectionModel().getSelectedItems();
            System.out.println();
            try {
                switch (nameTable) {
                    case DOCTOR:
                        id = ((Doctor) set.toArray()[0]).getId();
                        doctorDAO.delete(id);
                        break;
                    case PATIENT:
                        id = ((Patient) set.toArray()[0]).getId();
                        patientDAO.delete(id);
                        break;
                    case RECIPE:
                        id = ((Recipe) set.toArray()[0]).getId();
                        recipeDAO.delete(id);
                        break;
                }
                updateValueGrid();
            } catch (ArrayIndexOutOfBoundsException e) {
                addWindow(new ErrorWindow("Не выбранно ни одного значения"));
            } catch (Exception e) {
                addWindow(new ErrorWindow("Нельзя удалить данное значение, \n" +
                        "так оно свзяанно с таблице Рецепты"));
            }
        });

        horizontalLayoutButtonAction.addComponents(addButton, updateButton, deleteButton);
    }

    /**
     * добавляет компоненыты grid и макета кнопок действия в вертикальный макет, его уже добавляем в макет root
     * @param grid таблицу которую добавляем на макет
     */
    public void addOnComponent(Grid grid) {
        grid.setWidth("1200px");
        verticalLayoutForGridAndActionButton.removeAllComponents();
        verticalLayoutForGridAndActionButton.addComponents(grid, horizontalLayoutButtonAction);
        root.addComponent(verticalLayoutForGridAndActionButton, 1);
    }

    /**
     * Добавление фильтров в таблицу
     * @param grid в какую таблицу требуется фильтр
     */
    private void addFilter(Grid<Recipe> grid) {
        HeaderRow headerRow = grid.appendHeaderRow();
        singleFilterPatientTextField(headerRow, grid);
        singleFilterPriorityTextField(headerRow, grid);
        singleFilterDescriptionTextField(headerRow, grid);
    }

    /**
     * Добавляет фильтр по пациентам в верхний колонтикул
     */
    private void singleFilterPatientTextField(HeaderRow headerRow, Grid<Recipe> grid) {
        TextField filterField = new TextField();
        filterField.setValueChangeMode(ValueChangeMode.EAGER);
        filterField.setWidth("100%");
        filterField.setPlaceholder("filter...");
        filterField.addValueChangeListener(valueChangeEvent -> {
            ListDataProvider<Recipe> dataProvider = (ListDataProvider<Recipe>) grid.getDataProvider();
            dataProvider.setFilter((item) -> (item.getPatient().getName() + " " + item.getPatient().getSurname()).contains(filterField.getValue()));
        });
        headerRow.getCell(grid.getColumns().get(1)).setComponent(filterField);
    }

    /**
     * Добавляет фильтр по приоритету в верхний колонтикул
     */
    private void singleFilterPriorityTextField(HeaderRow headerRow, Grid<Recipe> grid) {
        TextField filterPriority = new TextField();
        filterPriority.setValueChangeMode(ValueChangeMode.EAGER);
        filterPriority.setWidth("100%");
        filterPriority.setPlaceholder("filter...");
        filterPriority.addValueChangeListener(valueChangeEvent -> {
            ListDataProvider<Recipe> dataProvider = (ListDataProvider<Recipe>) grid.getDataProvider();
            dataProvider.setFilter((item) -> (item.getPriority().getRussianNameOfPriority().contains(filterPriority.getValue())));
        });
        headerRow.getCell(grid.getColumns().get(4)).setComponent(filterPriority);
    }

    /**
     * Добавляет фильтр по описанию в верхний колонтикул
     */
    private void singleFilterDescriptionTextField(HeaderRow headerRow, Grid<Recipe> grid) {
        TextField filterDescription = new TextField();
        filterDescription.setValueChangeMode(ValueChangeMode.EAGER);
        filterDescription.setWidth("100%");
        filterDescription.setPlaceholder("filter...");
        filterDescription.addValueChangeListener(valueChangeEvent -> {
            ListDataProvider<Recipe> dataProvider = (ListDataProvider<Recipe>) grid.getDataProvider();
            dataProvider.setFilter((item) -> (item.getDescription().contains(filterDescription.getValue())));
        });
        headerRow.getCell(grid.getColumns().get(5)).setComponent(filterDescription);
    }
}
