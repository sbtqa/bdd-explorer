package ru.sbtqa.tag.columbo.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.pagefactory.Annotations;
import ru.sbtqa.tag.pagefactory.Page;
import ru.sbtqa.tag.pagefactory.annotations.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Assistive methods
 *
 * Created by SBT-Razuvaev-SV on 30.12.2016.
 */
public class ReflectionUtils {

    public static String getPageEntry(Class clazz) {
        if ((Page.class).isAssignableFrom(clazz)) {
            PageEntry pageEntry = (PageEntry) clazz.getAnnotation(PageEntry.class);
            if (pageEntry != null) {
                return pageEntry.title();
            }
        }
        return null;
    }

    public static String getElementTitle(Field field) {
        ElementTitle elementTitle = field.getAnnotation(ElementTitle.class);
        if (elementTitle != null) {
            return elementTitle.value();
        } else {
            return null;
        }
    }

    public static List<String> getActionTitles(Method method) {
        List<String> actionTitlesList = new ArrayList<>();
        ActionTitles actionTitles = method.getAnnotation(ActionTitles.class);
        if (actionTitles != null) {
            for (ActionTitle actionTitle : actionTitles.value()) {
                actionTitlesList.add(actionTitle.value());
            }
        } else {
            ActionTitle actionTitle = method.getAnnotation(ActionTitle.class);
            if (actionTitle != null) {
                actionTitlesList.add(actionTitle.value());
            }
        }
        return actionTitlesList;
    }

    public static By getFindByFromClass(Class clazz) {
        FindBy findBy = (FindBy) clazz.getAnnotation(FindBy.class);
        if (findBy != null) {
            if (!"".equals(findBy.className())) {
                return By.className(findBy.className());
            }
            if (!"".equals(findBy.css())) {
                return By.cssSelector(findBy.css());
            }
            if (!"".equals(findBy.id())) {
                return By.id(findBy.id());
            }
            if (!"".equals(findBy.linkText())) {
                return By.linkText(findBy.linkText());
            }
            if (!"".equals(findBy.name())) {
                return By.name(findBy.name());
            }
            if (!"".equals(findBy.partialLinkText())) {
                return By.partialLinkText(findBy.partialLinkText());
            }
            if (!"".equals(findBy.tagName())) {
                return By.tagName(findBy.tagName());
            }
            if (!"".equals(findBy.xpath())) {
                return By.xpath(findBy.xpath());
            }
        }
        return null;
    }

    public static Map<String, String> extractPropertiesFromField(Field field) {
        Map<String, String> propMap = new LinkedHashMap<>();
        Annotations annotations = new Annotations(field);
        try {
            By by = annotations.buildBy();
            propMap.put("@FindBy from field", by.toString());
        } catch (IllegalArgumentException iae) {
            // There is no FindBy annotation
        }
        By findByFromClass = getFindByFromClass(field.getType());
        if ( findByFromClass != null ) {
            propMap.put("@FindBy from class", findByFromClass.toString());
        }
        String elementTitle = getElementTitle(field);
        if (elementTitle != null) {
            propMap.put("@ElementTitle", field.getAnnotation(ElementTitle.class).value());
        }
        propMap.put("Name", field.getName());
        propMap.put("Class", field.getType().getName());
        propMap.put("Modifiers", Modifier.toString(field.getModifiers()));
        return propMap;
    }

    public static boolean isAbstract(Class clazz) {
        return Modifier.toString(clazz.getModifiers()).contains("abstract");
    }

}
