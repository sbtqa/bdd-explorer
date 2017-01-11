**Columbo**
================

**Description**

Columbo is application which designed for automation test projects designed on top of:
 Selenium WebDriver, Test Automation Gears or Yandex HTML-Elements;

All page objects should extends of [ru.sbtqa.tag.pagefactory.Page.class](https://github.com/sbtqa/page-factory/blob/master/src/main/java/ru/sbtqa/tag/pagefactory/Page.java)

The Columbo works with compiled project classes. For example `target/classes` in case you use maven.
Columbo is automatically saves its settings and condition.

**Future Improvements**
 - Change label from tree item to disabled text field.
 - Add context menu on tree item elements for "fast-copy @ElementTitle" and "expand subTree" 
 - Add toggle button "View with inheritance"
 - Add hotkeys
 - Add dependency management at runtime, to choose desired versions from drop-down
 - Use project source files instead compiled classes
 - Add feature info (used pages, elements, sql)
 - Add usages for each element (used in features)
 - Add common project statistics