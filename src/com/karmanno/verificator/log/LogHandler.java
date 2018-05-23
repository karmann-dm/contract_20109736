package com.karmanno.verificator.log;

import javafx.scene.control.TextArea;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LogHandler {
    private TextArea textArea;

    public LogHandler(TextArea textArea) {
        this.textArea = textArea;
    }

    public void log(String message) {
        StringBuilder result = new StringBuilder();
        result.append("[");

        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        result.append(dateFormat.format(Calendar.getInstance().getTime()));
        result.append("]: ");
        result.append(message);

        textArea.appendText(result.toString());
    }
}
