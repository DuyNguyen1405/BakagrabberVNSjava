package gui;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import core.BakaGrabber;
import core.ImgUpload;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;


public class GUIController {

  @FXML
  private ResourceBundle resources;

  @FXML
  private URL location;

  @FXML
  private ToggleGroup BBcode;

  @FXML
  private Button btnCopy;

  @FXML
  private Button btnGenerate;

  @FXML
  private ComboBox<String> cbbHost;

  @FXML
  private Menu mnuBBcode;

  @FXML
  private RadioMenuItem mnuCustom;

  @FXML
  private RadioMenuItem mnuDefaultEng;

  @FXML
  private RadioMenuItem mnuDefaultRaw;

  @FXML
  private CheckMenuItem mnuFetchCover;

  @FXML
  private TextArea txrDownloadLinks;

  @FXML
  private TextArea txrResult;

  @FXML
  private TextField txtMangaID;


  @FXML
  void handleBtnCopyAction(ActionEvent event) {
    StringSelection stringSelection = new StringSelection(txrResult.getText());
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    clipboard.setContents(stringSelection, null);
  }

  @FXML
  void handleBtnGenerateAction(ActionEvent event) {
    try {
      if (!txtMangaID.getText().matches("[0-9]+")) {
        txrResult.setText("Please enter a valid ID");
        return;
      }

      String
          mangaupdatesLink =
          "http://www.mangaupdates.com/series.html?id=" + txtMangaID.getText();
      BakaGrabber bakaManga = new BakaGrabber(mangaupdatesLink);

      if (Config.getCover()) {
        String cover = ImgUpload.upload(bakaManga.getCover());
        bakaManga.setCover(cover);
      }

      String downloadLinks = txrDownloadLinks.getText();
      bakaManga.setdownloadLinks(downloadLinks);

      String result = bakaManga.toString();
      txrResult.setText(result);


    } catch (Exception e) {
      // TODO Auto-generated catch block
      txrResult.setText(e.getMessage());
    }
  }

  @FXML
  void handleTxtMangaIdAction(ActionEvent event) {

  }

  @FXML
  void handleMnuDefaultRawAction(ActionEvent event) throws FileNotFoundException, IOException {
    if (mnuDefaultRaw.isSelected()) {
      Config.setValue(Config.currentTemplate, Config.defaultRawTemplate);
    }
  }

  @FXML
  void handleMnuDefaultEngAction(ActionEvent event) throws FileNotFoundException, IOException {
    if (mnuDefaultEng.isSelected()) {
      Config.setValue(Config.currentTemplate, Config.defaultEngTemplate);
    }
  }

  @FXML
  void handleMnuCustomAction(ActionEvent event) throws FileNotFoundException, IOException {
    if (mnuCustom.isSelected()) {
      Config.setValue(Config.currentTemplate, Config.customTemplate);
    }
  }

  @FXML
  void handleMnuFetchCoverAction(ActionEvent event) throws FileNotFoundException, IOException {
    String getCover = String.valueOf(mnuFetchCover.isSelected());
    Config.setValue(Config.getCover, getCover);
  }

  @FXML
  void handleCbbHostAction(ActionEvent event) throws FileNotFoundException, IOException {
    String host = cbbHost.getValue();
    Config.setValue(Config.host, host);
  }


  private void getConfig() throws IOException {
    Config.loadConfig();
    String defaultTemplate = Config.getCurrentConfig().getProperty(Config.currentTemplate);

    if (defaultTemplate.equals(Config.defaultEngTemplate)) {
      mnuDefaultEng.setSelected(true);
    } else if (defaultTemplate.equals(Config.customTemplate)) {
      mnuCustom.setSelected(true);
    } else {
      mnuDefaultRaw.setSelected(true);
    }

    if (Config.getCurrentConfig().getProperty(Config.getCover).equals("true")) {
      mnuFetchCover.setSelected(true);
    } else {
      mnuFetchCover.setSelected(false);
    }

//	if (Config.getCurrentConfig().getProperty(Config.extractFolder).equals("true"))
//	    chckbxmntmExtractFolder.setSelected(true);
//	else
//	    chckbxmntmExtractFolder.setSelected(false);

  }

  @FXML
  void initialize() throws IOException {
    assert BBcode != null : "fx:id=\"BBcode\" was not injected: check your FXML file 'GUI.fxml'.";
    assert btnCopy != null : "fx:id=\"btnCopy\" was not injected: check your FXML file 'GUI.fxml'.";
    assert btnGenerate
           != null : "fx:id=\"btnGenerate\" was not injected: check your FXML file 'GUI.fxml'.";
    assert cbbHost != null : "fx:id=\"cbbHost\" was not injected: check your FXML file 'GUI.fxml'.";
    assert mnuBBcode
           != null : "fx:id=\"mnuBBcode\" was not injected: check your FXML file 'GUI.fxml'.";
    assert mnuCustom
           != null : "fx:id=\"mnuCustom\" was not injected: check your FXML file 'GUI.fxml'.";
    assert mnuDefaultEng
           != null : "fx:id=\"mnuDefaultEng\" was not injected: check your FXML file 'GUI.fxml'.";
    assert mnuDefaultRaw
           != null : "fx:id=\"mnuDefaultRaw\" was not injected: check your FXML file 'GUI.fxml'.";
    assert mnuFetchCover
           != null : "fx:id=\"mnuFetchCover\" was not injected: check your FXML file 'GUI.fxml'.";
    assert txrDownloadLinks
           != null : "fx:id=\"txrDownloadLinks\" was not injected: check your FXML file 'GUI.fxml'.";
    assert txrResult
           != null : "fx:id=\"txrResult\" was not injected: check your FXML file 'GUI.fxml'.";
    assert txtMangaID
           != null : "fx:id=\"txtMangaID\" was not injected: check your FXML file 'GUI.fxml'.";

    getConfig();
  }

}
