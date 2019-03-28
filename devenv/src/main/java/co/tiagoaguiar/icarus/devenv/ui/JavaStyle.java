package co.tiagoaguiar.icarus.devenv.ui;

import java.util.regex.Pattern;

/**
 * Março, 28 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
class JavaStyle {

  private static final String[] KEYWORDS = new String[]{
          "abstract",
          "assert",
          "boolean",
          "break",
          "byte",
          "case",
          "catch",
          "char",
          "class",
          "const",
          "continue",
          "default",
          "do",
          "double",
          "else",
          "enum",
          "extends",
          "final",
          "finally",
          "float",
          "for",
          "goto",
          "if",
          "implements",
          "import",
          "instanceof",
          "int",
          "interface",
          "long",
          "native",
          "new",
          "package",
          "private",
          "protected",
          "public",
          "return",
          "short",
          "static",
          "strictfp",
          "super",
          "switch",
          "synchronized",
          "this",
          "throw",
          "throws",
          "transient",
          "try",
          "void",
          "volatile",
          "while"
  };


  private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
  private static final String PAREN_PATTERN = "\\(|\\)";
  private static final String BRACE_PATTERN = "\\{|\\}";
  private static final String BRACKET_PATTERN = "\\[|\\]";
  private static final String SEMICOLON_PATTERN = "\\;";
  private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
  private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";


  public static final Pattern PATTERN = Pattern.compile(
          "(?<KEYWORD>" + KEYWORD_PATTERN + ")" //
                  + "|(?<PAREN>" + PAREN_PATTERN + ")" //
                  + "|(?<BRACE>" + BRACE_PATTERN + ")" //
                  + "|(?<BRACKET>" + BRACKET_PATTERN + ")" //
                  + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")" //
                  + "|(?<STRING>" + STRING_PATTERN + ")" //
                  + "|(?<COMMENT>" + COMMENT_PATTERN + ")" //
  );

}
