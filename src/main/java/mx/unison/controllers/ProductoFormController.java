package mx.unison.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import mx.unison.database.DatabaseManager;
import mx.unison.models.Almacen;
import mx.unison.models.Producto;
import mx.unison.util.UIUtils;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador del formulario de producto.
 *
 * Maneja la creación y edición de productos.
 */
public class ProductoFormController {

    @FXML
    private TextField nombreField;
    @FXML
    private TextArea descripcionArea;
    @FXML
    private Spinner<Integer> cantidadSpinner;
    @FXML
    private TextField precioField;
    @FXML
    private ComboBox<Almacen> almacenCombo;
    @FXML
    private Button guardarBtn;
    @FXML
    private Button cancelarBtn;

    private DatabaseManager dbManager;
    private String usuarioActual;
    private Producto productoActual;
    private ProductosViewController parentController;

    public ProductoFormController(DatabaseManager dbManager, String usuarioActual,
                                  Producto productoActual, ProductosViewController parentController) {
        this.dbManager = dbManager;
        this.usuarioActual = usuarioActual;
        this.productoActual = productoActual;
        this.parentController = parentController;
    }

    @FXML
    public void initialize() {
        // Configurar Spinner de cantidad
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, 0);
        cantidadSpinner.setValueFactory(valueFactory);

        // Cargar almacenes en ComboBox
        cargarAlmacenes();

        // Si es edición, cargar datos del producto
        if (productoActual != null) {
            cargarDatosProducto();
        }

        // Configurar validación de precio
        precioField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*(\\.\\d*)?")) {
                return change;
            }
            return null;
        }));
    }

    /**
     * Carga la lista de almacenes en el ComboBox.
     */
    private void cargarAlmacenes() {
        try {
            List<Almacen> almacenes = dbManager.getAlmacenDao().getAll();
            almacenCombo.setItems(FXCollections.observableArrayList(almacenes));

            // Mostrar el nombre del almacén
            almacenCombo.setCellFactory(param -> new ListCell<Almacen>() {
                @Override
                protected void updateItem(Almacen item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? "" : item.getNombre());
                }
            });

            almacenCombo.setButtonCell(new ListCell<Almacen>() {
                @Override
                protected void updateItem(Almacen item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? "" : item.getNombre());
                }
            });
        } catch (SQLException e) {
            UIUtils.showErrorAlert("Error", "Error al cargar almacenes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Carga los datos del producto para edición.
     */
    private void cargarDatosProducto() {
        nombreField.setText(productoActual.getNombre());
        descripcionArea.setText(productoActual.getDescripcion());
        cantidadSpinner.getValueFactory().setValue(productoActual.getCantidad());
        precioField.setText(String.valueOf(productoActual.getPrecio()));

        if (productoActual.getAlmacen() != null) {
            almacenCombo.setValue(productoActual.getAlmacen());
        }
    }

    /**
     * Guarda el producto (creación o actualización).
     */
    @FXML
    private void handleGuardar() {
        // Validaciones
        if (nombreField.getText().isEmpty()) {
            UIUtils.showErrorAlert("Error", "El nombre del producto es obligatorio");
            return;
        }

        if (precioField.getText().isEmpty()) {
            UIUtils.showErrorAlert("Error", "El precio es obligatorio");
            return;
        }

        try {
            double precio = Double.parseDouble(precioField.getText());

            if (productoActual == null) {
                // Crear nuevo producto
                Producto nuevoProducto = new Producto();
                nuevoProducto.setNombre(nombreField.getText());
                nuevoProducto.setDescripcion(descripcionArea.getText());
                nuevoProducto.setCantidad(cantidadSpinner.getValue());
                nuevoProducto.setPrecio(precio);
                nuevoProducto.setAlmacen(almacenCombo.getValue());
                nuevoProducto.setFechaCreacion(String.valueOf(LocalDateTime.now()));
                nuevoProducto.setUltimoUsuario(usuarioActual);

                dbManager.getProductoDao().create(nuevoProducto);
                UIUtils.showSuccessAlert("Éxito", "Producto creado correctamente");
            } else {
                // Actualizar producto existente
                productoActual.setNombre(nombreField.getText());
                productoActual.setDescripcion(descripcionArea.getText());
                productoActual.setCantidad(cantidadSpinner.getValue());
                productoActual.setPrecio(precio);
                productoActual.setAlmacen(almacenCombo.getValue());
                productoActual.setFechaModificacion(String.valueOf(LocalDateTime.now()));
                productoActual.setUltimoUsuario(usuarioActual);

                dbManager.getProductoDao().update(productoActual);
                UIUtils.showSuccessAlert("Éxito", "Producto actualizado correctamente");
            }

            parentController.recargarProductos();
            cerrarVentana();

        } catch (NumberFormatException e) {
            UIUtils.showErrorAlert("Error", "El precio debe ser un número válido");
        } catch (SQLException e) {
            UIUtils.showErrorAlert("Error", "Error al guardar producto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Cierra la ventana del formulario.
     */
    @FXML
    private void handleCancelar() {
        cerrarVentana();
    }

    /**
     * Cierra la ventana actual.
     */
    private void cerrarVentana() {
        Stage stage = (Stage) cancelarBtn.getScene().getWindow();
        stage.close();
    }
}
