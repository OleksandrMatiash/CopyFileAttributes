package com.vortex.fileattributes;

import com.vortex.fileattributes.domain.HostConfig;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

//    private static final String OK = "OK";
//    private static ConfigService configService = new ConfigService();
//    public ChoiceBox<String> srcAliasChoiceBox;
//    public ChoiceBox<HostConfig> dstAliasChoiceBox;
//    public TextField srcHostTextField;
//    public TextField srcDbTextField;
//    public TextField dstHostTextField;
//    public TextField dstDbTextField;
//    public Text srcConnectionStatus;
//    public Text dstConnectionStatus;
//    public ChoiceBox<String> srcSlotsChoiceBox;
//    public ListView<String> srcIconsListView;
//    public Button copyBtn;

    public ChoiceBox<String> dstCB1;
    public ChoiceBox<String> dstCB2;
    public ChoiceBox<String> dstCB3;
    public ChoiceBox<String> dstCB4;
    public TextField srcTF1;
    public TextField srcTF2;
    public TextField srcTF3;
    public TextField srcTF4;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        EventHandler<DragEvent> handlerOnDragOver = new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if (event.getDragboard().hasFiles()) {
                    event.acceptTransferModes(TransferMode.COPY);
                }
                event.consume();
            }
        };

        EventHandler<DragEvent> handlerOnDragDropped = new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard dragboard = event.getDragboard();
                if (dragboard.hasFiles()) {
                    List<File> files = dragboard.getFiles();

                }
                event.consume();
            }
        };
        srcTF1.setOnDragOver(handlerOnDragOver);
        srcTF1.setOnDragDropped(handlerOnDragDropped);
        srcTF2.setOnDragOver(handlerOnDragOver);
        srcTF2.setOnDragDropped(handlerOnDragDropped);
        srcTF3.setOnDragOver(handlerOnDragOver);
        srcTF3.setOnDragDropped(handlerOnDragDropped);
        srcTF4.setOnDragOver(handlerOnDragOver);
        srcTF4.setOnDragDropped(handlerOnDragDropped);

//        srcAliasChoiceBox.getItems().addAll(configService.getHosts());
//        StringConverter<HostConfig> converter = new StringConverter<HostConfig>() {
//            @Override
//            public String toString(HostConfig object) {
//                return object.getAlias();
//            }
//
//            @Override
//            public HostConfig fromString(String string) {
//                return null;
//            }
//        };
//        srcAliasChoiceBox.setConverter(converter);
//        srcAliasChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//            srcHostTextField.setText(newValue.getHost());
//            srcDbTextField.setText(newValue.getIconsDbName());
//        });
//
//        dstAliasChoiceBox.getItems().addAll(configService.getHosts());
//        dstAliasChoiceBox.setConverter(converter);
//        dstAliasChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//            dstHostTextField.setText(newValue.getHost());
//            dstDbTextField.setText(newValue.getIconsDbName());
//        });
//
//        srcHostTextField.textProperty().addListener((observable, oldValue, newValue) -> resetSrc());
//        srcDbTextField.textProperty().addListener((observable, oldValue, newValue) -> resetSrc());
//
//        dstHostTextField.textProperty().addListener((observable, oldValue, newValue) -> dstConnectionStatus.setText(""));
//        dstDbTextField.textProperty().addListener((observable, oldValue, newValue) -> dstConnectionStatus.setText(""));
//
//        srcSlotsChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//            srcIconsListView.getItems().clear();
//            if (newValue != null && !newValue.isEmpty()) {
//                srcIconsListView.getItems().addAll(dao.getSlotIcons(fieldValue(srcHostTextField), fieldValue(srcDbTextField), newValue));
//            }
//        });
    }

    public void upButtonClicked() {
        // TODO
    }

    public void downButtonClicked() {
        // TODO
    }

    //    private void resetSrc() {
//        srcConnectionStatus.setText("");
//        srcSlotsChoiceBox.getItems().clear();
//        srcIconsListView.getItems().clear();
//    }
//
//    private String fieldValue(TextField field) {
//        return field.getText().trim();
//    }
//
//    public void checkConnectionSrcClicked() {
//        String host = fieldValue(srcHostTextField);
//        String db = fieldValue(srcDbTextField);
//        if (!host.isEmpty() && !db.isEmpty() && dao.testConnection(host, db)) {
//            srcConnectionStatus.setText(OK);
//            srcSlotsChoiceBox.getItems().addAll(dao.getSlots(host, db));
//            srcSlotsChoiceBox.getSelectionModel().selectFirst();
//        } else {
//            srcConnectionStatus.setText("FAIL");
//            srcSlotsChoiceBox.getItems().clear();
//            srcIconsListView.getItems().clear();
//        }
//    }
//
//    public void checkConnectionDstClicked() {
//        String host = fieldValue(dstHostTextField);
//        String db = fieldValue(dstDbTextField);
//        if (!host.isEmpty() && !db.isEmpty() && dao.testConnection(host, db)) {
//            dstConnectionStatus.setText(OK);
//        } else {
//            dstConnectionStatus.setText("FAIL");
//        }
//    }
//
//    public void copyBtnClicked() {
//        if (!OK.equals(srcConnectionStatus.getText())) {
//            new AlertBox().createAndShow("Connect to FROM first", AlertBox.Type.CLOSE);
//        } else if (!OK.equals(dstConnectionStatus.getText())) {
//            new AlertBox().createAndShow("Connect to TO first", AlertBox.Type.CLOSE);
//        } else if (OK.equals(srcConnectionStatus.getText()) && OK.equals(dstConnectionStatus.getText())) {
//            if (fieldValue(srcHostTextField).equals(fieldValue(dstHostTextField)) && fieldValue(srcDbTextField).equals(fieldValue(dstDbTextField))) {
//                new AlertBox().createAndShow("FROM and TO are equals", AlertBox.Type.CLOSE);
//            } else {
//                boolean alreadyExists = dao.validateIconsNotExistOnDst(fieldValue(srcHostTextField), fieldValue(srcDbTextField), srcSlotsChoiceBox.getValue(),
//                        fieldValue(dstHostTextField), fieldValue(dstDbTextField));
//
//                if (alreadyExists) {
//                    AlertBox alertBox = new AlertBox();
//                    alertBox.createAndShow("Icons already exist on destination. Replace old icons?", AlertBox.Type.YES_CANCEL);
//                    if (!alertBox.isYesPressed()) {
//                        return;
//                    }
//                    dao.removeIconsFromDst(fieldValue(dstHostTextField), fieldValue(dstDbTextField), srcSlotsChoiceBox.getValue());
//                }
//
//                dao.copyIconsFromSrcToDst(fieldValue(srcHostTextField), fieldValue(srcDbTextField), srcSlotsChoiceBox.getValue(),
//                        fieldValue(dstHostTextField), fieldValue(dstDbTextField));
//
////                Dialog dialog = new Dialog(DialogType.INFORMATION,
////                        "Success",
////                        "Icons are copied!!!");
////                dialog.showAndWait();
//                new AlertBox().createAndShow("Icons are copied!!!", AlertBox.Type.CLOSE);
//            }
//        }
//    }
//
    public void stop() {
//        dao.closeConnections();
    }
}
