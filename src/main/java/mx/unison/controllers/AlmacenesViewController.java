package mx.unison.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mx.unison.controllers.MainController;
import mx.unison.database.DatabaseManager;
import mx.unison.models.Almacen;
import mx.unison.util.UIUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador para la vista de gestión de almacenes.
 *
 * Maneja la visualización, búsqueda, creación, edición y eliminación
 * de almacenes en el sistema.
 */
public class AlmacenesViewController {

    @FXML
    private TextField searchField;
    @FXML
    private TableView<Almacen> almacenesTable;
    @FXML
    private TableColumn<Almacen, Integer> idColumn;
    @FXML
    private TableColumn<Almacen, String> nombreColumn;
    @FXML
    private TableColumn<Almacen, String> ubicacionColumn;
    @FXML
    private TableColumn<Almacen, String> fechaCreacionColumn;
    @FXML
    private TableColumn<Almacen, String> ultimoUsuarioColumn;
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
    private ObservableList<Almacen> almacenesObservable;

    public AlmacenesViewController(MainController mainController, DatabaseManager dbManager, String usuarioActual) {
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
        ubicacionColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getUbicacion()));
        fechaCreacionColumn.setCellValueFactory(cellData -> {
            var fecha = cellData.getValue().getFechaHoraCreacion();
            String fechaStr = fecha != null ? fecha.format(String.valueOf(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))) : "";
            return new javafx.beans.property.SimpleStringProperty(fechaStr);
        });
        ultimoUsuarioColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getUltimoUsuario()));

        // Ancho de columnas
        idColumn.setPrefWidth(50);
        nombreColumn.setPrefWidth(150);
        ubicacionColumn.setPrefWidth(200);
        fechaCreacionColumn.setPrefWidth(150);
        ultimoUsuarioColumn.setPrefWidth(120);

        // Cargar almacenes
        cargarAlmacenes();

        // Configurar búsqueda en tiempo real
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filtrarAlmacenes(newVal));

        // Permitir doble click para editar
        almacenesTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !almacenesTable.getSelectionModel().isEmpty()) {
                handleEditar();
            }
        });
    }

    /**
     * Carga todos los almacenes de la base de datos.
     */
    private void cargarAlmacenes() {
        try {
            List<Almacen> almacenes = dbManager.getAlmacenDao().getAll();
            almacenesObservable = FXCollections.observableArrayList(almacenes);
            almacenesTable.setItems(almacenesObservable);
        } catch (SQLException e) {
            UIUtils.showErrorAlert("Error", "Error al cargar almacenes: " + e.getMessage());
            System.err.println("Error al cargar almacenes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Filtra los almacenes según el texto de búsqueda.
     *
     * @param searchText Texto de búsqueda
     */
    private void filtrarAlmacenes(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            almacenesTable.setItems(almacenesObservable);
        } else {
            String textoLower = searchText.toLowerCase();
            ObservableList<Almacen> almacenesFiltrados = almacenesObservable.stream()
                    .filter(a -> a.getNombre().toLowerCase().contains(textoLower) ||
                            a.getUbicacion().toLowerCase().contains(textoLower))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
            almacenesTable.setItems(almacenesFiltrados);
        }
    }

    /**
     * Abre el diálogo para agregar un nuevo almacén.
     */
    @FXML
    private void handleAgregar() {
        try {
            abrirFormularioAlmacen(null);
        } catch (Exception e) {
            UIUtils.showErrorAlert("Error", "Error al abrir formulario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Abre el diálogo para editar el almacén seleccionado.
     */
    @FXML
    private void handleEditar() {
        Almacen almacenSeleccionado = almacenesTable.getSelectionModel().getSelectedItem();

        if (almacenSeleccionado == null) {
            UIUtils.showErrorAlert("Error", "Selecciona un almacén para editar");
            return;
        }

        try {
            abrirFormularioAlmacen(almacenSeleccionado);
        } catch (Exception e) {
            UIUtils.showErrorAlert("Error", "Error al abrir formulario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Elimina el almacén seleccionado.
     */
    @FXML
    private void handleEliminar() {
        Almacen almacenSeleccionado = almacenesTable.getSelectionModel().getSelectedItem();

        if (almacenSeleccionado == null) {
            UIUtils.showErrorAlert("Error", "Selecciona un almacén para eliminar");
            return;
        }

        if (UIUtils.showConfirmAlert(
                "Confirmar Eliminación",
                "¿Estás seguro de que deseas eliminar el almacén '" + almacenSeleccionado.getNombre() + "'?"
        )) {
            try {
                dbManager.getAlmacenDao().delete(almacenSeleccionado);
                cargarAlmacenes();
                UIUtils.showSuccessAlert("Éxito", "Almacén eliminado correctamente");
            } catch (SQLException e) {
                UIUtils.showErrorAlert("Error", "Error al eliminar almacén: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Actualiza la lista de almacenes.
     */
    @FXML
    private void handleActualizar() {
        cargarAlmacenes();
        UIUtils.showSuccessAlert("Éxito", "Lista actualizada");
    }

    /**
     * Abre el formulario de almacén en una ventana modal.
     *
     * @param almacen Almacén a editar, o null para crear uno nuevo
     * @throws Exception Si hay error al cargar el FXML
     */
    private void abrirFormularioAlmacen(Almacen almacen) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/views/formAlmacen.fxml")
        );

        AlmacenFormController controller = new AlmacenFormController(
                dbManager, usuarioActual, almacen, this
        );
        loader.setController(controller);

        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();
        stage.setTitle(almacen == null ? "Nuevo Almacén" : "Editar Almacén");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.setWidth(500);
        stage.setHeight(400);
        stage.showAndWait();
    }

    /**
     * Recarga la lista de almacenes después de crear/editar uno.
     */
    public void recargarAlmacenes() {
        cargarAlmacenes();
    }
}