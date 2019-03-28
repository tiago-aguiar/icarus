package co.tiagoaguiar.icarus.devenv.ui;

import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.reactfx.Subscription;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;

import co.tiagoaguiar.icarus.devenv.Settings;
import co.tiagoaguiar.icarus.devenv.model.FileExtension;
import co.tiagoaguiar.icarus.devenv.util.FileHelper;
import javafx.concurrent.Task;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.StackPane;

/**
 * Março, 28 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public class CodeEditor {

  private final TabPane tabPane;
  private final ExecutorService executor;
  private final CodeArea codeArea;

  public CodeEditor(TabPane tabPane) {
    this.tabPane = tabPane;
    this.executor = Executors.newSingleThreadExecutor();
    this.codeArea = new CodeArea();
  }

  public void open(File file, FileExtension fileExtension) {
    try {
      Tab tab = new Tab(file.getName());

      tabPane.getTabs().add(tab);

      CodeArea codeArea = buildCodeArea(FileHelper.getText(file), fileExtension);

      tab.setContent(new StackPane(new VirtualizedScrollPane<>(codeArea)));

      tabPane.getSelectionModel().select(tabPane.getTabs().size() - 1);

      applyStyle(fileExtension);
    } catch (IOException e) {
      // TODO: 28/03/19 Logger
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
    codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
    Subscription cleanupWhenNoLongerNeedIt = codeArea.multiPlainChanges()
            .successionEnds(Duration.ofMillis(100))
            .supplyTask(() -> computeHighlightingAsync(fileExtension))
            .awaitLatest(codeArea.multiPlainChanges())
            .filterMap(t -> {
              if (t.isSuccess()) {
                return Optional.of(t.get());
              } else {
                t.getFailure().printStackTrace();
                return Optional.empty();
              }
            })
            .subscribe(this::applyHighlighting);

    // when no longer need syntax highlighting and wish to clean up memory leaks
    // run: `cleanupWhenNoLongerNeedIt.unsubscribe();`
    codeArea.replaceText(0, 0, text);

    return codeArea;
  }

  private void applyHighlighting(StyleSpans<Collection<String>> highlighting) {
    codeArea.setStyleSpans(0, highlighting);
  }

  private Task<StyleSpans<Collection<String>>> computeHighlightingAsync(FileExtension fileExtension) {
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
