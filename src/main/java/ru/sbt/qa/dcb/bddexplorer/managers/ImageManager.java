package ru.sbt.qa.dcb.bddexplorer.managers;

import javafx.scene.image.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import ru.sbt.qa.dcb.bddexplorer.Ctx;
import ru.sbt.qa.dcb.bddexplorer.panes.treecomponents.TreeItemType;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by SBT-Razuvaev-SV on 27.12.2016.
 */
@Component
public class ImageManager {
    private static final Logger log = LoggerFactory.getLogger(ImageManager.class);

    private final HashMap<String, Image> images = new HashMap<>();

    /** Read images from inner folder */
    @PostConstruct
    private void loadImages() {
        try {
            for ( Resource r : Ctx.get().getResources("classpath:images/*") ) {
                images.put( r.getFilename(), new Image(r.getInputStream()) );
            }
            log.debug("ImageManager constructed successfully");
        } catch (IOException e) {
            log.error( "Error when loading images", e );
        }
    }

    /** Return Image from /images by name
     * @param fileName - Image filename
     * @return         - Image
     * @throws NullPointerException - If image not exists
     */
    public Image getImage(String fileName) throws NullPointerException {
        if ( this.images.containsKey(fileName) ) {
            return this.images.get(fileName);
        }
        throw new NullPointerException("No image with name " + fileName + " found");
    }

    /**
     * Return Image from /images by TreeItemType
     * @param treeItemType - TreeItemType
     */
    public Image getImageForTreeItem(TreeItemType treeItemType) {
        Image imageForTreeItem = null;
        switch (treeItemType) {
            case ABSTRACT_PAGE: imageForTreeItem = getImage("abstract-page.png"); break;
            case PAGE: imageForTreeItem = getImage("page.png"); break;
            case ACTION_METHOD: imageForTreeItem = getImage("action-method.png"); break;
            case HTML_ELEMENT: imageForTreeItem = getImage("html-element.png"); break;
            case LIST: imageForTreeItem = getImage("list-elements.png"); break;
            case TYPIFIED_ELEMENT: imageForTreeItem = getImage("typified-element.png"); break;
            case WEB_ELEMENT: imageForTreeItem = getImage("web-element.png"); break;
            case FOLDER: imageForTreeItem = getImage("folder-horizontal.png"); break;
        }
        return imageForTreeItem;
    }

}
