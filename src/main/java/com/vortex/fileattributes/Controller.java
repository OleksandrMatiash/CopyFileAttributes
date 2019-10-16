package com.vortex.fileattributes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

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

    private FilesHelper filesHelper = new FilesHelper();

    private List<TextField> srcTFs;
    private List<ChoiceBox<String>> dstCBs;

    private Set<File> srcFiles = new LinkedHashSet<>();
    private Set<File> dstFiles = new LinkedHashSet<>();
    private Map<String, String> matchedFileNames = new HashMap<>();
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
                filesChanged();
            }
            event.consume();
        };
    }

    private EventHandler<DragEvent> getOnDragDroppedDst() {
        return event -> {
            Dragboard dragboard = event.getDragboard();
            if (dragboard.hasFiles()) {
                dstFiles.addAll(dragboard.getFiles());
                filesChanged();
            }
            event.consume();
        };
    }

    private void filesChanged() {
        saveSelection();
        Map<String, String> autoMatched = filesHelper.matchFiles(srcFiles, dstFiles);
        for (Map.Entry<String, String> entry : autoMatched.entrySet()) {
            matchedFileNames.put(entry.getKey(), entry.getValue());
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
                ObservableList<String> observableList = FXCollections.observableArrayList(dstList);
                observableList.add(0, null);
                choiceBox.setItems(observableList);
                if (matchedFileNames.containsKey(srcFilename)) {
                    choiceBox.getSelectionModel().select(matchedFileNames.get(srcFilename));
                }
            } else {
                srcTFs.get(i).textProperty().setValue("");
                ChoiceBox<String> choiceBox = dstCBs.get(i);
                choiceBox.disableProperty().set(true);
            }
        }
    }

    @FXML
    private void upButtonClicked() {
        if (displayFirstItemIndex > 0) {
            saveSelection();
            displayFirstItemIndex--;
            redraw();
        }
    }

    @FXML
    private void downButtonClicked() {
        if (displayFirstItemIndex < Math.max(srcFiles.size(), dstFiles.size()) - srcTFs.size()) {
            saveSelection();
            displayFirstItemIndex++;
            redraw();
        }
    }

    @FXML
    private void copyButtonClicked() {
        saveSelection();
        if (checkDuplicatesInDst()) {
            Map<File, File> matchedFiles = convertToMatchedFiles(matchedFileNames);
            if (!matchedFiles.isEmpty()) {
                String warningMsg = matchedFiles.entrySet().stream()
                        .map(e -> e.getKey().getAbsolutePath() + " -> " + e.getValue().getAbsolutePath())
                        .collect(Collectors.joining("\r\n"));
                AlertBox box = new AlertBox();
                box.createAndShow("You are going to copy attributes for such files:\r\n" + warningMsg, AlertBox.Type.YES_CANCEL);
                if (box.isYesPressed()) {
                    filesHelper.copyAttributes(matchedFiles);

                    for (Map.Entry<File, File> entry : matchedFiles.entrySet()) {
                        srcFiles.removeIf(srcFile -> srcFile.equals(entry.getKey().getAbsoluteFile()));
                        dstFiles.removeIf(dstFile -> dstFile.equals(entry.getValue().getAbsoluteFile()));
                    }
                    displayFirstItemIndex = 0;
                    matchedFileNames.clear();
                    redraw();
                }
            }
        }
    }

    private Map<File, File> convertToMatchedFiles(Map<String, String> matchedFileNames) {
        Map<File, File> result = new HashMap<>();
        for (Map.Entry<String, String> entry : matchedFileNames.entrySet()) {
            if (entry.getKey() != null && entry.getValue() != null) {
                result.put(getFileByName(entry.getKey(), srcFiles), getFileByName(entry.getValue(), dstFiles));
            }
        }
        return result;
    }

    private File getFileByName(String name, Collection<File> files) {
        for (File file : files) {
            if (file.getAbsolutePath().equals(name)) {
                return file;
            }
        }
        throw new IllegalArgumentException("can't get file by it's name: " + name);
    }

    private boolean checkDuplicatesInDst() {
        Set<String> alreadyMatched = new HashSet<>();
        Set<String> duplicates = new HashSet<>();
        for (String candidate : matchedFileNames.values()) {
            if (candidate != null) {
                if (alreadyMatched.contains(candidate)) {
                    duplicates.add(candidate);
                } else {
                    alreadyMatched.add(candidate);
                }
            }
        }

        if (!duplicates.isEmpty()) {

            new AlertBox().createAndShow("there are duplicates in dst files:\r\n" +
                    String.join("\r\n", duplicates), AlertBox.Type.CLOSE);
            return false;
        }
        return true;
    }

    private void saveSelection() {
        for (int i = 0; i < srcTFs.size(); i++) {
            String key = srcTFs.get(i).textProperty().getValue();
            if (key != null && !key.isEmpty()) {
                String value = dstCBs.get(i).getSelectionModel().getSelectedItem();
                matchedFileNames.put(key, value);
            }
        }
    }

}
