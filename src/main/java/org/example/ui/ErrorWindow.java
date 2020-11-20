package org.example.ui;

import com.vaadin.ui.*;

public class ErrorWindow extends Window {

    public ErrorWindow(String messageError) {
        center();
        setWidth("675px");
        setHeight("150px");

        Label label = new Label(messageError);
        VerticalLayout verticalLayout = new VerticalLayout();
        Button close = new Button("Ок");

        close.addClickListener(clickEvent -> close());

        verticalLayout.addComponents(label, close);
        verticalLayout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
        verticalLayout.setComponentAlignment(close, Alignment.MIDDLE_CENTER);
        verticalLayout.setSpacing(true);
        verticalLayout.setMargin(true);

        setContent(verticalLayout);
    }
}
