package co.tiagoaguiar.icarus.devenv.ui;

import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.fxmisc.wellbehaved.event.EventPattern;
import org.fxmisc.wellbehaved.event.InputMap;
import org.fxmisc.wellbehaved.event.Nodes;
import org.reactfx.Subscription;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;

import co.tiagoaguiar.icarus.devenv.Settings;
import co.tiagoaguiar.icarus.devenv.model.FileExtension;
import co.tiagoaguiar.icarus.devenv.util.FileHelper;
import co.tiagoaguiar.icarus.devenv.util.logging.LoggerManager;
import javafx.concurrent.Task;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import sun.rmi.runtime.Log;

/**
 * Março, 28 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public class CodeEditor {

  private final TabPane tabPane;
  private final HashMap<Tab, Boolean> tabFileLoaded;
  private final HashMap<Tab, String> tabFileName;
  private final HashMap<String, Tab> tabFilePath;
  private final HashMap<Tab, String> tabHash;

  private Tab currentTab;

  public CodeEditor(TabPane tabPane) {
    this.tabPane = tabPane;
    this.tabFileLoaded = new HashMap<>();
    this.tabFileName = new HashMap<>();
    this.tabFilePath = new HashMap<>();
    this.tabHash = new HashMap<>();

    setup();
  }

  private void setup() {
    tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
      if (oldValue != newValue)
        this.currentTab = newValue;
    });
  }

  public void open(File file, FileExtension fileExtension) {
    try {
      // select file already opened
      Tab currentTab = tabFilePath.get(file.getAbsolutePath());
      if (currentTab != null) {
        tabPane.getSelectionModel().select(currentTab);
        return;
      }

      Optional<String> _fileName = Optional.ofNullable(file.getName())
              .filter(s -> s.contains("."))
              .map(s -> s.substring(0, s.lastIndexOf(".")));

      Tab tab = new Tab(_fileName.orElse(file.getName()));

      tabPane.getTabs().add(tab);

      // code area stuff
      tab.setContent(new StackPane(new VirtualizedScrollPane<>(buildCodeArea(FileHelper.getText(file), fileExtension))));
      applyStyle(fileExtension);

      // local state
      tabFileLoaded.put(tab, false);
      tabFileName.put(tab, tab.getText());
      tabHash.put(tab, file.getAbsolutePath());
      tabFilePath.put(file.getAbsolutePath(), tab);

      // select last file opened
      tabPane.getSelectionModel().select(tabPane.getTabs().size() - 1);

      // reset when close tab
      tab.setOnCloseRequest(event -> {
        tabFileLoaded.remove(tab);
        tabFileName.remove(tab);
        tabFilePath.remove(tabHash.get(tab));
        tabHash.remove(tab);
      });

    } catch (IOException e) {
      LoggerManager.error(e);
    }
  }

  public void save() {
    String absoluteFilePath = this.tabHash.get(currentTab);
    StackPane stackPane = (StackPane) currentTab.getContent();
    VirtualizedScrollPane virtualScrollPane = (VirtualizedScrollPane) stackPane.getChildren().get(0);
    CodeArea codeArea = (CodeArea) virtualScrollPane.getContent();
    String text = codeArea.getText();

    try {
      // FIXME: 09/04/19 New File > Edit > Save > Crash 
      Files.write(new File(absoluteFilePath).toPath(), text.getBytes());
      applyFileChanged(false);

    } catch (IOException e) {
      LoggerManager.error(e);
    }
  }

  private void applyStyle(FileExtension fileExtension) {
    switch (fileExtension) {
      case JAVA:
        tabPane.getStylesheets().add(Settings.JAVA_CSS);
        break;
      default:
        break;
    }
  }

  private CodeArea buildCodeArea(String text, FileExtension fileExtension) {
    Executor executor = Executors.newSingleThreadExecutor();
    CodeArea codeArea = new CodeArea();

    codeArea.getStyleClass().add("code-area-icarus");
    codeArea.getStylesheets().add(Settings.CODE_AREA_CSS);
    codeArea.setUseInitialStyleForInsertion(true);

    // width TAB for 2 spaces
    InputMap<KeyEvent> im = InputMap.consume(
            EventPattern.keyPressed(KeyCode.TAB),
            e -> codeArea.replaceSelection("  ")
    );
    Nodes.addInputMap(codeArea, im);

    // configure codeArea
    codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
    Subscription cleanupWhenNoLongerNeedIt = codeArea.multiPlainChanges()
            .successionEnds(Duration.ofMillis(100))
            .supplyTask(() -> computeHighlightingAsync(executor, codeArea, fileExtension))
            .awaitLatest(codeArea.multiPlainChanges())
            .filterMap(t -> {
              if (t.isSuccess()) {
                return Optional.of(t.get());
              } else {
                t.getFailure().printStackTrace();
                return Optional.empty();
              }
            })
            .subscribe((highlighting) -> {
              this.applyHighlighting(codeArea, highlighting);
              applyFileChanged(true);
            });

    // when no longer need syntax highlighting and wish to clean up memory leaks
    // run: `cleanupWhenNoLongerNeedIt.unsubscribe();`
    codeArea.replaceText(0, 0, text);

    return codeArea;
  }

  private void applyHighlighting(CodeArea codeArea, StyleSpans<Collection<String>> highlighting) {
    codeArea.setStyleSpans(0, highlighting);
  }

  private void applyFileChanged(boolean fileChanged) {
    Tab selectedItem = tabPane.getSelectionModel().getSelectedItem();

    Boolean fileLoaded = tabFileLoaded.get(selectedItem);
    if (!fileLoaded) {
      tabFileLoaded.put(selectedItem, true);
      return;
    }

    if (fileChanged) {
      selectedItem.setText(tabFileName.get(selectedItem) + "*");
    } else {
      selectedItem.setText(selectedItem.getText().substring(0, selectedItem.getText().length() - 1));
    }
  }

  private Task<StyleSpans<Collection<String>>> computeHighlightingAsync(Executor executor, CodeArea codeArea, FileExtension fileExtension) {
    String text = codeArea.getText();
    Task<StyleSpans<Collection<String>>> task = new Task<StyleSpans<Collection<String>>>() {
      @Override
      protected StyleSpans<Collection<String>> call() throws Exception {
        return computeHighlighting(text, fileExtension);
      }
    };
    executor.execute(task);
    return task;
  }

  private StyleSpans<Collection<String>> computeHighlighting(String text, FileExtension fileExtension) {
    Matcher matcher = null;

    switch (fileExtension) {
      case JAVA:
        matcher = JavaStyle.PATTERN.matcher(text);
    }

    int lastKwEnd = 0;
    StyleSpansBuilder<Collection<String>> spansBuilder
            = new StyleSpansBuilder<>();
    while (matcher.find()) {
      String styleClass =
              matcher.group("KEYWORD") != null ? "keyword" :
                      matcher.group("PAREN") != null ? "paren" :
                              matcher.group("BRACE") != null ? "brace" :
                                      matcher.group("BRACKET") != null ? "bracket" :
                                              matcher.group("SEMICOLON") != null ? "semicolon" :
                                                      matcher.group("STRING") != null ? "string" :
                                                              matcher.group("COMMENT") != null ? "comment" :
                                                                      null; /* never happens */
      assert styleClass != null;
      spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
      spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
      lastKwEnd = matcher.end();
    }
    spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
    return spansBuilder.create();
  }

}
