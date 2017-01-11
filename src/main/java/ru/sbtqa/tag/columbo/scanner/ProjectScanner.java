package ru.sbtqa.tag.columbo.scanner;

import javafx.scene.control.TreeItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import ru.sbtqa.tag.columbo.managers.ConfigurationManager;
import ru.sbtqa.tag.columbo.panes.PageTreePane;
import ru.sbtqa.tag.columbo.panes.treecomponents.PageTreeItemView;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for working with source files
 *
 * Created by SBT-Razuvaev-SV on 28.12.2016.
 */
@Component
@DependsOn("rootPane")
public class ProjectScanner {
    private static final Logger log = LoggerFactory.getLogger(ProjectScanner.class);

    @Autowired private ConfigurationManager configurationManager;
    @Autowired private GraphBuilder graphBuilder;
    @Autowired private PageTreePane pageTreePane;

    private File srcFolder;
    private String srcFolderPath;
    private URLClassLoader classLoader;
    private List<Class<?>> loadedClasses;

    /**
     * Run project scanning using project files path from property src_dir_path
     */
    public void loadSourceFiles() {
        log.info("Source loading...");
        String src_dir_path = configurationManager.getProperty("src_dir_path");
        if (src_dir_path != null) {
            log.info("Source path exists");
            try {
                scanSources(new File(src_dir_path));
                log.info("Restore page tree condition");
                String active_page_class = configurationManager.getProperty("active_page_class");
                if (active_page_class != null) {
                    log.info("Last active page found " + active_page_class);
                    pageTreePane.openToLevel(active_page_class);
                }
            } catch (IOException e) {
                log.error("Source loading... error", e);
            }
        } else {
            log.warn("Sources not found");
        }
    }

    /**
     * Scan sources, load project classes and build page tree
     * @param srcFolder - source path folder
     * @throws IOException - something wrong...
     */
    public void scanSources(File srcFolder) throws IOException {
        this.srcFolder = srcFolder;
        log.info("Scanning sources from " + srcFolder.getAbsolutePath());
        if ( srcFolder.isDirectory() && srcFolder.exists() ) {
            this.srcFolderPath = srcFolder.getAbsolutePath().replaceAll("\\\\", ".").replaceAll("/",".").concat(".");
        }
        loadedClasses = new ArrayList<>();
        initClassLoader();
        scanDirectory(srcFolder);
        TreeItem<PageTreeItemView> mainRoot = graphBuilder.buildPageTree(loadedClasses);
        graphBuilder.sortPageTree(mainRoot);
        pageTreePane.setMainRoot(mainRoot);
        loadedClasses.clear();
        closeClassLoader();
    }

    /**
     * Recursive method for scanning project folder
     * @param source - folder for scanning
     * @throws IOException - something wrong
     */
    private void scanDirectory(File source) throws IOException {
        log.info("Scan directory: " + source.getAbsolutePath());
        if ( source.isDirectory() ) {
            File[] files = source.listFiles();
            for ( File file : files ) {
                if ( file.isDirectory() ) {
                    scanDirectory(file);
                } else {
                    String className = file.getAbsolutePath().replaceAll("\\\\", ".").replaceAll("/",".").replaceAll(srcFolderPath, "").replaceAll(".class", "");
                    try {
                        Class<?> clazz = classLoader.loadClass(className);
                        if (clazz != null) {
                            loadedClasses.add(clazz);
                        }
                    } catch (ClassNotFoundException | NoClassDefFoundError expected) {
                        // Expected error. Some classes from source files is not include in the project, because they are unimportant.
                        log.info( "Expected error when loading class: " + className + " [" + expected.getCause() + "]" );
                    } catch (Exception ue) {
                        log.error( "Unexpected error", ue );
                    }
                }
            }
        }
    }

    /**
     * Create classloader
     * @throws MalformedURLException - source path incorrect
     */
    private void initClassLoader() throws MalformedURLException {
        log.info("Create classLoader");
        URL url = srcFolder.toURL();
        URL[] urls = new URL[]{url};
        classLoader = URLClassLoader.newInstance(urls);
    }

    /**
     * Close classloader and release resources
     * @throws IOException - something wrong
     */
    private void closeClassLoader() throws IOException {
        log.info("Close classLoader");
        classLoader.clearAssertionStatus();
        classLoader.close();
        classLoader = null;
    }

}
