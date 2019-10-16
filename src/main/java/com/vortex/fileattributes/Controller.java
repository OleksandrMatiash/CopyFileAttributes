package com.vortex.fileattributes;

import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public class Controller implements Initializable {

    @FXML
    private TextField srcTF1;
    @FXML
    private TextField srcTF2;
    @FXML
    private TextField srcTF3;
    @FXML
    private TextField srcTF4;
    @FXML
    private ChoiceBox<String> dstCB1;
    @FXML
    private ChoiceBox<String> dstCB2;
    @FXML
    private ChoiceBox<String> dstCB3;
    @FXML
    private ChoiceBox<String> dstCB4;

    private FilesMatcher filesMatcher = new FilesMatcher();

//    private Stage stage;
    private List<TextField> srcTFs;
    private List<ChoiceBox<String>> dstCBs;


//    public void setStage(Stage stage) {
//        this.stage = stage;
//    }

    private Set<File> srcFiles = new LinkedHashSet<>();
    private Set<File> dstFiles = new LinkedHashSet<>();
    private Map<String, String> matchedFiles = new HashMap<>();
    private int displayFirstItemIndex = 0;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        srcTFs = new ArrayList<>(asList(srcTF1, srcTF2, srcTF3, srcTF4));
        dstCBs = new ArrayList<>(asList(dstCB1, dstCB2, dstCB3, dstCB4));

        for (TextField srcTF : srcTFs) {
            srcTF.setOnDragOver(getOnDragOver());
            srcTF.setOnDragDropped(getOnDragDroppedSrc());
        }
        for (ChoiceBox<String> dstChoiceBox : dstCBs) {
            dstChoiceBox.setOnDragOver(getOnDragOver());
            dstChoiceBox.setOnDragDropped(getOnDragDroppedDst());
        }
        redraw();
    }

    private EventHandler<DragEvent> getOnDragOver() {
        return event -> {
            if (event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        };
    }

    private EventHandler<DragEvent> getOnDragDroppedSrc() {
        return event -> {
            Dragboard dragboard = event.getDragboard();
            if (dragboard.hasFiles()) {
                srcFiles.addAll(dragboard.getFiles());
                srcFilesChanged();
            }
            event.consume();
        };
    }

    private EventHandler<DragEvent> getOnDragDroppedDst() {
        return event -> {
            Dragboard dragboard = event.getDragboard();
            if (dragboard.hasFiles()) {
                dstFiles.addAll(dragboard.getFiles());
                dstFilesChanged();
            }
            event.consume();
        };
    }

    private void srcFilesChanged() {
        System.out.println("\r\nSRC files:" + srcFiles.stream().map(f -> f.getName()).collect(Collectors.joining(",")));
        filesChanged();
    }

    private void dstFilesChanged() {
        System.out.println("\nDST files:" + dstFiles.stream().map(f -> f.getName()).collect(Collectors.joining(",")));
        filesChanged();
    }

    private void filesChanged() {
        Map<String, String> autoMatched = filesMatcher.matchFiles(srcFiles, dstFiles);
        for (Map.Entry<String, String> entry : autoMatched.entrySet()) {
            matchedFiles.put(entry.getKey(), entry.getValue());
        }

        redraw();
    }

    private void redraw() {
        List<String> srcList = srcFiles.stream()
                .map(File::getAbsolutePath)
                .collect(Collectors.toList());
        List<String> dstList = dstFiles.stream()
                .map(File::getAbsolutePath)
                .collect(Collectors.toList());

        for (int i = 0; i < srcTFs.size(); i++) {
            if (i + displayFirstItemIndex < srcList.size()) {
                String srcFilename = srcList.get(i + displayFirstItemIndex);
                srcTFs.get(i).textProperty().setValue(srcFilename);

                ChoiceBox<String> choiceBox = dstCBs.get(i);
                choiceBox.disableProperty().set(false);
                choiceBox.setItems(FXCollections.observableList(dstList)); //
                if (matchedFiles.containsKey(srcFilename)) {
                    choiceBox.getSelectionModel().select(matchedFiles.get(srcFilename));
                }
            } else {
                srcTFs.get(i).textProperty().setValue("");
                ChoiceBox<String> choiceBox = dstCBs.get(i);
                choiceBox.disableProperty().set(true);
            }
        }
    }


    public void upButtonClicked() {
        if (displayFirstItemIndex > 0) {
            saveSelection();
            displayFirstItemIndex--;
            redraw();
        }
    }

    public void downButtonClicked() {
        if (displayFirstItemIndex < Math.max(srcFiles.size(), dstFiles.size()) - srcTFs.size()) {
            saveSelection();
            displayFirstItemIndex++;
            redraw();
        }
    }

    private void saveSelection() {
        for (int i = 0; i < srcTFs.size(); i++) {
            String key = srcTFs.get(i).textProperty().getValue();
            if (key != null && !key.isEmpty()) {
                String value = dstCBs.get(i).getSelectionModel().getSelectedItem();
                matchedFiles.put(key, value);
            }
        }
    }

}
