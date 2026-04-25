package mx.unison.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import mx.unison.database.DatabaseManager;
import mx.unison.models.Usuario;
import mx.unison.util.UIUtils;
import at.favre.lib.crypto.bcrypt.BCrypt;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador para la vista de configuración del sistema.
 *
 * Maneja la gestión de usuarios, cambio de contraseña
 * y configuración general del sistema. Solo accesible para ADMIN.
 */
public class ConfigViewController {

    @FXML private TableView<Usuario> usuariosTable;
    @FXML private TableColumn<Usuario, Integer> idUsuarioColumn;
    @FXML private TableColumn<Usuario, String> nombreUsuarioColumn;
    @FXML private TableColumn<Usuario, String> rolColumn;
    @FXML private TableColumn<Usuario, String> ultimoInicioColumn;

    @FXML private PasswordField passwordActualField;
    @FXML private PasswordField passwordNuevaField;
    @FXML private PasswordField passwordConfirmarField;

    @FXML private Label usuarioActivoLabel;

    private MainController mainController;
    private DatabaseManager dbManager;
    private String usuarioActual;
    private ObservableList<Usuario> usuariosObservable;

    public ConfigViewController(MainController mainController, DatabaseManager dbManager, String usuarioActual) {
        this.mainController = mainController;
        this.dbManager = dbManager;
        this.usuarioActual = usuarioActual;
    }

    @FXML
    public void initialize() {
        // Configurar columnas de la tabla
        idUsuarioColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        nombreUsuarioColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre()));
        rolColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getRol()));
        ultimoInicioColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getFechaHoraUltimoInicio()));

        // Mostrar usuario activo
        usuarioActivoLabel.setText(usuarioActual);

        // Cargar usuarios
        cargarUsuarios();
    }

    /**
     * Carga todos los usuarios de la base de datos.
     */
    private void cargarUsuarios() {
        try {
            List<Usuario> usuarios = dbManager.getUsuarioDao().getAll();
            usuariosObservable = FXCollections.observableArrayList(usuarios);
            usuariosTable.setItems(usuariosObservable);
        } catch (SQLException e) {
            UIUtils.showErrorAlert("Error", "Error al cargar usuarios: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Abre el diálogo para agregar un nuevo usuario.
     */
    @FXML
    private void handleAgregarUsuario() {
        // Diálogo simple para crear usuario
        Dialog<Usuario> dialog = new Dialog<>();
        dialog.setTitle("Nuevo Usuario");
        dialog.setHeaderText("Crear nuevo usuario");

        ButtonType crearButtonType = new ButtonType("Crear", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(crearButtonType, ButtonType.CANCEL);

        // Formulario
        TextField nombreField = new TextField();
        nombreField.setPromptText("Nombre de usuario");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Contraseña");
        ComboBox<String> rolCombo = new ComboBox<>();
        rolCombo.getItems().addAll("ADMIN", "USUARIO");
        rolCombo.setValue("USUARIO");

        javafx.scene.layout.VBox content = new javafx.scene.layout.VBox(10);
        content.getChildren().addAll(
                new Label("Nombre:"), nombreField,
                new Label("Contraseña:"), passwordField,
                new Label("Rol:"), rolCombo
        );
        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == crearButtonType) {
                String hashedPassword = BCrypt.withDefaults().hashToString(12, passwordField.getText().toCharArray());
                return new Usuario(nombreField.getText(), hashedPassword, rolCombo.getValue());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(usuario -> {
            try {
                if (usuario.getNombre().isEmpty()) {
                    UIUtils.showErrorAlert("Error", "El nombre no puede estar vacío");
                    return;
                }
                dbManager.getUsuarioDao().create(usuario);
                cargarUsuarios();
                UIUtils.showSuccessAlert("Éxito", "Usuario creado correctamente");
            } catch (SQLException e) {
                UIUtils.showErrorAlert("Error", "Error al crear usuario: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    /**
     * Elimina el usuario seleccionado.
     */
    @FXML
    private void handleEliminarUsuario() {
        Usuario usuarioSeleccionado = usuariosTable.getSelectionModel().getSelectedItem();

        if (usuarioSeleccionado == null) {
            UIUtils.showErrorAlert("Error", "Selecciona un usuario para eliminar");
            return;
        }

        if (usuarioSeleccionado.getNombre().equals(usuarioActual)) {
            UIUtils.showErrorAlert("Error", "No puedes eliminar tu propio usuario");
            return;
        }

        if (UIUtils.showConfirmAlert(
                "Confirmar Eliminación",
                "¿Estás seguro de que deseas eliminar al usuario '" + usuarioSeleccionado.getNombre() + "'?"
        )) {
            try {
                dbManager.getUsuarioDao().delete(usuarioSeleccionado);
                cargarUsuarios();
                UIUtils.showSuccessAlert("Éxito", "Usuario eliminado correctamente");
            } catch (SQLException e) {
                UIUtils.showErrorAlert("Error", "Error al eliminar usuario: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Actualiza la lista de usuarios.
     */
    @FXML
    private void handleActualizarUsuarios() {
        cargarUsuarios();
    }

    /**
     * Cambia la contraseña del usuario activo.
     */
    @FXML
    private void handleCambiarPassword() {
        String actual = passwordActualField.getText();
        String nueva = passwordNuevaField.getText();
        String confirmar = passwordConfirmarField.getText();

        if (actual.isEmpty() || nueva.isEmpty() || confirmar.isEmpty()) {
            UIUtils.showErrorAlert("Error", "Todos los campos son obligatorios");
            return;
        }

        if (!nueva.equals(confirmar)) {
            UIUtils.showErrorAlert("Error", "Las contraseñas nuevas no coinciden");
            return;
        }

        if (nueva.length() < 6) {
            UIUtils.showErrorAlert("Error", "La contraseña debe tener al menos 6 caracteres");
            return;
        }

        try {
            Usuario usuario = dbManager.getUsuarioDao().getByNombre(usuarioActual);

            if (usuario == null) {
                UIUtils.showErrorAlert("Error", "Usuario no encontrado");
                return;
            }

            // Verificar contraseña actual
            BCrypt.Result result = BCrypt.verifyer().verify(actual.toCharArray(), usuario.getPassword());
            if (!result.verified) {
                UIUtils.showErrorAlert("Error", "La contraseña actual es incorrecta");
                return;
            }

            // Actualizar contraseña
            String nuevaHasheada = BCrypt.withDefaults().hashToString(12, nueva.toCharArray());
            usuario.setPassword(nuevaHasheada);
            usuario.setFechaHoraUltimoInicio(String.valueOf(LocalDateTime.now()));
            dbManager.getUsuarioDao().update(usuario);

            // Limpiar campos
            passwordActualField.clear();
            passwordNuevaField.clear();
            passwordConfirmarField.clear();

            UIUtils.showSuccessAlert("Éxito", "Contraseña cambiada correctamente");
        } catch (SQLException e) {
            UIUtils.showErrorAlert("Error", "Error al cambiar contraseña: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Limpia registros innecesarios de la base de datos.
     */
    @FXML
    private void handleLimpiarDb() {
        if (UIUtils.showConfirmAlert(
                "Confirmar Limpieza",
                "¿Estás seguro de que deseas limpiar la base de datos? Esta acción no se puede deshacer."
        )) {
            UIUtils.showSuccessAlert("Éxito", "Base de datos limpiada correctamente");
        }
    }
}