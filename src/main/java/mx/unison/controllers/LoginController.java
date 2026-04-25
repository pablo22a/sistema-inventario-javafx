package mx.unison.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import mx.unison.controllers.MainController;
import mx.unison.database.DatabaseManager;
import mx.unison.models.Usuario;
import mx.unison.service.AuthService;
import mx.unison.util.UIUtils;

import java.sql.SQLException;

/**
 * Controlador de la vista de login.
 *
 * Gestiona la autenticación de usuarios y la navegación
 * a la vista principal después del login exitoso.
 */
public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private CheckBox rememberCheckBox;
    @FXML private Button loginButton;
    @FXML private Button exitButton;
    @FXML private Label errorLabel;

    private MainController mainController;
    private AuthService authService;

    /**
     * Constructor del controlador de login.
     *
     * @param mainController Controlador principal de la aplicación
     * @param dbManager Gestor de la base de datos
     */
    public LoginController(MainController mainController, DatabaseManager dbManager) {
        this.mainController = mainController;
        this.authService = new AuthService(dbManager.getUsuarioDao());
    }

    /**
     * Inicializa el controlador después de cargar el FXML.
     */
    @FXML
    public void initialize() {
        // Permitir login con Enter
        usernameField.setOnKeyPressed(this::handleKeyPress);
        passwordField.setOnKeyPressed(this::handleKeyPress);

        // Cargar credenciales guardadas si existen
        loadSavedCredentials();

        // Focus en el campo de usuario
        usernameField.requestFocus();
    }

    /**
     * Maneja el evento de tecla presionada.
     * Permite login al presionar Enter.
     *
     * @param event Evento de teclado
     */
    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handleLogin();
        }
    }

    /**
     * Maneja el evento de click en el botón de login.
     */
    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        // Validar campos no vacíos
        if (username.isEmpty() || password.isEmpty()) {
            showError("Por favor ingrese el usuario y la contraseña");
            return;
        }

        try {
            // Deshabilitar botón mientras se procesa
            loginButton.setDisable(true);

            // Autenticar usuario
            Usuario usuario = authService.authenticate(username, password);

            if (usuario != null) {
                // Login exitoso
                hideError();

                // Guardar credenciales si está marcado
                if (rememberCheckBox.isSelected()) {
                    saveCredentials(username, password);
                }

                // Mostrar alerta de bienvenida
                UIUtils.showSuccessAlert(
                        "Bienvenido",
                        "¡Hola " + usuario.getNombre() + "! Has iniciado sesión correctamente."
                );

                // Navegar a la vista principal
                mainController.showMainView(usuario.getNombre(), usuario.getRol());

            } else {
                // Login fallido
                showError("Usuario o contraseña incorrectos");
                passwordField.clear();
                usernameField.requestFocus();
            }

        } catch (SQLException e) {
            showError("Error de base de datos: " + e.getMessage());
            System.err.println("Error en autenticación: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            showError("Error al iniciar sesión: " + e.getMessage());
            System.err.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
        } finally {
            loginButton.setDisable(false);
        }
    }

    /**
     * Maneja el evento de click en el botón de salir.
     */
    @FXML
    private void handleExit() {
        if (UIUtils.showConfirmAlert(
                "Confirmar Salida",
                "¿Estás seguro de que deseas salir de la aplicación?"
        )) {
            Platform.exit();
        }
    }

    /**
     * Muestra un mensaje de error.
     *
     * @param message Mensaje de error
     */
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }

    /**
     * Oculta el mensaje de error.
     */
    private void hideError() {
        errorLabel.setText("");
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
    }

    /**
     * Guarda las credenciales en las preferencias del sistema.
     * Nota: En producción, estas deberían estar encriptadas.
     *
     * @param username Nombre de usuario
     * @param password Contraseña
     */
    private void saveCredentials(String username, String password) {
        // Aquí se podrían guardar en las preferencias del sistema
        // Por ahora, solo guardamos en memoria durante la sesión
        // TODO: Implementar almacenamiento seguro de credenciales
    }

    /**
     * Carga las credenciales guardadas si existen.
     */
    private void loadSavedCredentials() {
        // TODO: Implementar carga de credenciales guardadas de forma segura
    }
}
