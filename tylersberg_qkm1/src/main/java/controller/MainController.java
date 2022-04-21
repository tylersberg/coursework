package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import model.Part;
import model.Product;

/** 
 * Controller for the MainView
 */

public class MainController {
    @FXML private HBox mainBox;
/**
* Controls for the Part Pane. 
*/
    @FXML private Pane partPane;
    @FXML private Label partTitle;
    @FXML private TableView<Part> partTable;
    @FXML private TableColumn<Part, Integer> partIdCol;
    @FXML private TableColumn<Part, String> partNameCol;
    @FXML private TableColumn<Part, Integer> partStockCol;
    @FXML private TableColumn<Part, Double> partCostCol;
    @FXML private HBox partControls;
    @FXML private TextField partSearch;
    @FXML private Button addPartButton;
    @FXML private Button modifyPartButton;
    @FXML private Button deletePartButton;

    public void partSearchHandler() {
    }

    public void addPartHandler() {
    }

    public void modifyPartHandler() {
    }

    public void deletePartHandler() {
    }

/**
* Controls for the Product Pane. 
*/
    @FXML private Pane productPane;
    @FXML private Label productTitle;
    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, Integer> productIdCol;
    @FXML private TableColumn<Product, String> productNameCol;
    @FXML private TableColumn<Product, Integer> productStockCol;
    @FXML private TableColumn<Product, Double> productCostCol;
    @FXML private HBox productControls;
    @FXML private TextField productSearch;
    @FXML private Button addProductButton;
    @FXML private Button modifyProductButton;
    @FXML private Button deleteProductButton;

    public void productSearchHandler() {
    }

    public void addProductHandler() {
    }

    public void modifyProductHandler() {
    }

    public void deleteProductHandler() {
    }
}
