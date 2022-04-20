package control;

import javafx.event.ActionEvent;

/**
 * The Editable interface is implemented by the Controllers of scenes which will be loaded underneath the menu bar,
 * and enables them to respond to ActionEvents from the 'Edit' menu of the menu bar.
 */
public interface Editable {
    /**
     * The modify method should handle the user selecting 'modify' from the edit menu.
     * @param event ActionEvent from the menu item selection.
     */
    void modify(ActionEvent event);

    /**
     * The modify method should handle the user selecting 'delete' from the edit menu.
     * @param event ActionEvent from the menu item selection.
     */
    void delete(ActionEvent event);
}
