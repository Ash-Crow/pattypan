/*
 * The MIT License
 *
 * Copyright 2016 Pawel Marynowski.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package pattypan.panes;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import pattypan.Session;
import pattypan.Util;
import pattypan.elements.WikiButton;
import pattypan.elements.WikiLabel;
import pattypan.elements.WikiPane;
import pattypan.elements.WikiTextField;

public class ChooseDirectoryPane extends WikiPane {

  Stage stage;

  WikiLabel descLabel;
  WikiTextField browsePath;
  WikiButton browseButton;
  ScrollPane scrollText = new ScrollPane();

  public ChooseDirectoryPane(Stage stage) {
    super(stage, 0.0);
    this.stage = stage;

    setContent();
    
    if (Session.DIRECTORY != null) {
      browsePath.setText(Session.DIRECTORY.getAbsolutePath());
      getFileListByDirectory(Session.DIRECTORY);
    }
  }

  private void chooseAndSetDirectory() {
    DirectoryChooser fileChooser = new DirectoryChooser();
    fileChooser.setTitle("Choose directory");
    if (Session.DIRECTORY != null) {
      fileChooser.setInitialDirectory(Session.DIRECTORY);
    }

    File file = fileChooser.showDialog(stage);
    if (file != null) {
      Session.DIRECTORY = file;
      browsePath.setText(file.getAbsolutePath());
      getFileListByDirectory(file);
    }
  }

  public BorderPane getContent() {
    return this;
  }

  private void getFileListByDirectory(File directory) {
    File[] files = directory.listFiles();
    int counter = 0;
    VBox content = new VBox();

    for (File f : files) {
      if (f.isFile() && Util.isFileAllowedToUpload(f)) {
        content.getChildren().add(new WikiLabel(f.getName()));
        ++counter;
        Session.FILES.add(f);
      }
    }
    scrollText.setContent(content);
    nextButton.setDisable(counter == 0);
  }

  private BorderPane setContent() {
    descLabel = new WikiLabel("In cursus nunc enim, ac ullamcorper lectus consequat accumsan. Mauris erat sapien, iaculis a quam in, molestie dapibus libero. Morbi mollis mattis porta. Pellentesque at suscipit est, id vestibulum risus.").setWrapped(true);
    descLabel.setTextAlignment(TextAlignment.LEFT);
    addElement(descLabel);

    browsePath = new WikiTextField("");
    browseButton = new WikiButton("Browse", "small").setWidth(100);
    browseButton.setOnAction((ActionEvent e) -> {
      chooseAndSetDirectory();
    });
    addElementRow(
            new Node[]{browsePath, browseButton},
            new Priority[]{Priority.ALWAYS, Priority.NEVER}
    );

    scrollText.getStyleClass().add("mw-ui-scrollpane");
    addElement(scrollText);
    
    prevButton.linkTo("StartPane", stage);
    nextButton.linkTo("ChooseColumnsPane", stage);
    nextButton.setDisable(true);
    
    return this;
  }
}
