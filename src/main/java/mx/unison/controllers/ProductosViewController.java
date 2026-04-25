package mx.unison.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mx.unison.database.DatabaseManager;
import mx.unison.models.Almacen;
import mx.unison.models.Producto;
import mx.unison.util.UIUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador para la vista de gestión de productos.
 *
 * Maneja la visualización, búsqueda, creación, edición y eliminación
 * de productos en el sistema.
 */
public class ProductosViewController {

    @FXML
    private TextField searchField;
    @FXML
    private TableView<Producto> productosTable;
    @FXML
    private TableColumn<Producto, Integer> idColumn;
    @FXML
    private TableColumn<Producto, String> nombreColumn;
    @FXML
    private TableColumn<Producto, String> descripcionColumn;
    @FXML
    private TableColumn<Producto, Integer> cantidadColumn;
    @FXML
    private TableColumn<Producto, Double> precioColumn;
    @FXML
    private TableColumn<Producto, String> almacenColumn;
    @FXML
    private Button agregarBtn;
    @FXML
    private Button editarBtn;
    @FXML
    private Button eliminarBtn;
    @FXML
    private Button actualizarBtn;

    private MainController mainController;
    private DatabaseManager dbManager;
    private String usuarioActual;
    private ObservableList<Producto> productosObservable;

    public ProductosViewController(MainController mainController, DatabaseManager dbManager, String usuarioActual) {
        this.mainController = mainController;
        this.dbManager = dbManager;
        this.usuarioActual = usuarioActual;
    }

    @FXML
    public void initialize() {
        // Configurar columnas de la tabla
        idColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        nombreColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre()));
        descripcionColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDescripcion()));
        cantidadColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getCantidad()).asObject());
        precioColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getPrecio()).asObject());
        almacenColumn.setCellValueFactory(cellData -> {
            Almacen almacen = cellData.getValue().getAlmacen();
            String almacenNombre = almacen != null ? almacen.getNombre() : "Sin almacén";
            return new javafx.beans.property.SimpleStringProperty(almacenNombre);
        });

        // Ancho de columnas
        idColumn.setPrefWidth(50);
        nombreColumn.setPrefWidth(150);
        descripcionColumn.setPrefWidth(200);
        cantidadColumn.setPrefWidth(80);
        precioColumn.setPrefWidth(80);
        almacenColumn.setPrefWidth(120);

        // Cargar productos
        cargarProductos();

        // Configurar búsqueda en tiempo real
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filtrarProductos(newVal));

        // Permitir doble click para editar
        productosTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !productosTable.getSelectionModel().isEmpty()) {
                handleEditar();
            }
        });
    }

    /**
     * Carga todos los productos de la base de datos.
     */
    private void cargarProductos() {
        try {
            List<Producto> productos = dbManager.getProductoDao().getAll();
            productosObservable = FXCollections.observableArrayList(productos);
            productosTable.setItems(productosObservable);
        } catch (SQLException e) {
            UIUtils.showErrorAlert("Error", "Error al cargar productos: " + e.getMessage());
            System.err.println("Error al cargar productos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Filtra los productos según el texto de búsqueda.
     *
     * @param searchText Texto de búsqueda
     */
    private void filtrarProductos(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            productosTable.setItems(productosObservable);
        } else {
            String textoLower = searchText.toLowerCase();
            ObservableList<Producto> productosFiltrados = productosObservable.stream()
                    .filter(p -> p.getNombre().toLowerCase().contains(textoLower) ||
                            p.getDescripcion().toLowerCase().contains(textoLower))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
            productosTable.setItems(productosFiltrados);
        }
    }

    /**
     * Abre el diálogo para agregar un nuevo producto.
     */
    @FXML
    private void handleAgregar() {
        try {
            abrirFormularioProducto(null);
        } catch (Exception e) {
            UIUtils.showErrorAlert("Error", "Error al abrir formulario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Abre el diálogo para editar el producto seleccionado.
     */
    @FXML
    private void handleEditar() {
        Producto productoSeleccionado = productosTable.getSelectionModel().getSelectedItem();

        if (productoSeleccionado == null) {
            UIUtils.showErrorAlert("Error", "Selecciona un producto para editar");
            return;
        }

        try {
            abrirFormularioProducto(productoSeleccionado);
        } catch (Exception e) {
            UIUtils.showErrorAlert("Error", "Error al abrir formulario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Elimina el producto seleccionado.
     */
    @FXML
    private void handleEliminar() {
        Producto productoSeleccionado = productosTable.getSelectionModel().getSelectedItem();

        if (productoSeleccionado == null) {
            UIUtils.showErrorAlert("Error", "Selecciona un producto para eliminar");
            return;
        }

        if (UIUtils.showConfirmAlert(
                "Confirmar Eliminación",
                "¿Estás seguro de que deseas eliminar el producto '" + productoSeleccionado.getNombre() + "'?"
        )) {
            try {
                dbManager.getProductoDao().delete(productoSeleccionado);
                cargarProductos();
                UIUtils.showSuccessAlert("Éxito", "Producto eliminado correctamente");
            } catch (SQLException e) {
                UIUtils.showErrorAlert("Error", "Error al eliminar producto: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Actualiza la lista de productos.
     */
    @FXML
    private void handleActualizar() {
        cargarProductos();
        UIUtils.showSuccessAlert("Éxito", "Lista actualizada");
    }

    /**
     * Abre el formulario de producto en una ventana modal.
     *
     * @param producto Producto a editar, o null para crear uno nuevo
     * @throws Exception Si hay error al cargar el FXML
     */
    private void abrirFormularioProducto(Producto producto) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/views/formProducto.fxml")
        );

        ProductoFormController controller = new ProductoFormController(
                dbManager, usuarioActual, producto, this
        );
        loader.setController(controller);

        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();
        stage.setTitle(producto == null ? "Nuevo Producto" : "Editar Producto");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.setWidth(500);
        stage.setHeight(600);
        stage.showAndWait();
    }

    /**
     * Recarga la lista de productos después de crear/editar uno.
     */
    public void recargarProductos() {
        cargarProductos();
    }
}