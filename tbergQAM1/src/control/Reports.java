package control;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import model.Appointment;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * The Reports class controls the reports window, which displays various text reports to the user.
 * Before Reports is initialized, the report variable should be set to one of the ReportType enum values.
 */
public class Reports extends Controller {

    /**
     * The ReportType enum contains values that correspond to different report types.
     * When the Reports controller is initialized a switch statement is used to run the code of the ReportType.
     */
    protected enum ReportType {
        TYPE_AND_MONTH, CONTACT_SCHEDULES, CUSTOMER_SCHEDULES
    }
    protected static ReportType report;

    @FXML private TextArea reportArea;

    /**
     * The initialize method handles setting the text of the reportArea.
     * A switch statement is used to run the report code corresponding to the selected report.
     */
    public void initialize() {
        try {
            List<Appointment> appointments = dao.getAppointments();
            StringBuilder reportText = new StringBuilder();
            switch (report) {
                case TYPE_AND_MONTH:
                    Map<String, Long> monthTypeMap = appointments.stream()
                            .collect(Collectors.groupingBy(a -> (a.getStart().getMonth().toString() +": "+ a.getType()), Collectors.counting()));
                    reportArea.setText(monthTypeMap.entrySet().stream()
                            .map(e -> {return (e.getKey() + " = " + e.getValue());})
                            .sorted()
                            .collect(Collectors.joining(System.lineSeparator())));
                    break;
                case CONTACT_SCHEDULES:
                    Map<String, List<Appointment>> contactMap = appointments.stream().collect(Collectors.groupingBy(Appointment::getContactName));
                    for (Map.Entry<String, List<Appointment>> e : contactMap.entrySet()) {
                        reportText.append(e.getKey()).append("'s Schedule:").append(System.lineSeparator());
                        for (Appointment a : e.getValue()) {
                            reportText.append("    Appointment# ")
                                    .append(String.join(" , ", String.valueOf(a.getId()), a.getTitle(), a.getTitle(), a.getDescription()))
                                    .append(". At ").append(formatTime(a.getStart())).append(" until ").append(formatTime(a.getEnd()))
                                    .append(" with Customer# ").append(a.getCustomerId()).append(System.lineSeparator());
                        }
                    }
                    reportArea.setText(String.valueOf(reportText));
                    break;
                case CUSTOMER_SCHEDULES:
                    Map<Integer, List<Appointment>> customerMap = appointments.stream().collect(Collectors.groupingBy(Appointment::getCustomerId));
                    for (Map.Entry<Integer, List<Appointment>> e : customerMap.entrySet()) {
                        reportText.append("Schedule for Customer #").append(e.getKey()).append(System.lineSeparator());
                        for (Appointment a : e.getValue()) {
                            reportText.append("    Appointment# ")
                                    .append(String.join(" , ", String.valueOf(a.getId()), a.getTitle(), a.getTitle(), a.getDescription()))
                                    .append(". At ").append(formatTime(a.getStart())).append(" until ").append(formatTime(a.getEnd()))
                                    .append(" with Contact: ").append(a.getContactName()).append(System.lineSeparator());
                        }
                    }
                    reportArea.setText(String.valueOf(reportText));
                    break;
                default:
                    reportArea.setText("ERROR: Could not determine report type.");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            error(e.getMessage());
        } finally {
            report = null;
        }
    }

}
