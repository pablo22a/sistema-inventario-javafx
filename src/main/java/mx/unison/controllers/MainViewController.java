package mx.unison.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import mx.unison.database.DatabaseManager;
import mx.unison.util.UIUtils;

import java.sql.SQLException;

/**
 * Controlador de la vista principal.
 *
 * Gestiona la interfaz de usuario principal después del login
 * y la navegación entre los diferentes módulos del sistema.
 */
public class MainViewController {

    @FXML
    private BorderPane rootPane;

    // Sidebar
    @FXML
    private VBox sidebar;
    @FXML
    private Button dashboardBtn;
    @FXML
    private Button productosBtn;
    @FXML
    private Button almacenesBtn;
    @FXML
    private Button configBtn;
    @FXML
    private Label userNameLabel;
    @FXML
    private Label userRoleLabel;

    // Topbar
    @FXML
    private Label pageTitle;
    @FXML
    private Label pageSubtitle;

    // Content Area
    @FXML
    private StackPane contentStackPane;
    @FXML
    private VBox dashboardView;
    @FXML
    private VBox productosView;
    @FXML
    private VBox almacenesView;
    @FXML
    private VBox configView;

    // Stats Labels
    @FXML
    private Label productosCountLabel;
    @FXML
    private Label almacenesCountLabel;
    @FXML
    private Label stockTotalLabel;

    private MainController mainController;
    private DatabaseManager dbManager;
    private String userName;
    private String userRole;

    /**
     * Constructor del controlador de la vista principal.
     *
     * @param mainController Controlador principal de la aplicación
     * @param dbManager Gestor de la base de datos
     * @param userName Nombre del usuario autenticado
     * @param userRole Rol del usuario autenticado
     */
    public MainViewController(MainController mainController, DatabaseManager dbManager,
                              String userName, String userRole) {
        this.mainController = mainController;
        this.dbManager = dbManager;
        this.userName = userName;
        this.userRole = userRole;
    }

    /**
     * Inicializa el controlador después de cargar el FXML.
     */
    @FXML
    public void initialize() {
        // Configurar información del usuario
        userNameLabel.setText("👤 " + userName);
        userRoleLabel.setText("Rol: " + userRole);

        // Mostrar botones según el rol
        configureNavigationByRole();

        // Cargar estadísticas
        loadStatistics();

        // Establecer Dashboard como vista inicial
        showDashboard();
    }

    /**
     * Configura la visibilidad de los botones según el rol del usuario.
     */
    private void configureNavigationByRole() {
        switch (userRole) {
            case "ADMIN" -> {
                // Admin ve todo
                productosBtn.setVisible(true);
                productosBtn.setManaged(true);
                almacenesBtn.setVisible(true);
                almacenesBtn.setManaged(true);
                configBtn.setVisible(true);
                configBtn.setManaged(true);
            }
            case "PRODUCTOS" -> {
                // Solo ve productos
                productosBtn.setVisible(true);
                productosBtn.setManaged(true);
            }
            case "ALMACENES" -> {
                // Solo ve almacenes
                almacenesBtn.setVisible(true);
                almacenesBtn.setManaged(true);
            }
            default -> {
                // Vista limitada
            }
        }
    }

    /**
     * Carga las estadísticas del dashboard.
     */
    private void loadStatistics() {
        try {
            // Contar productos
            int productosCount = dbManager.getProductoDao().getAll().size();
            productosCountLabel.setText(String.valueOf(productosCount));

            // Contar almacenes
            int almacenesCount = dbManager.getAlmacenDao().getAll().size();
            almacenesCountLabel.setText(String.valueOf(almacenesCount));

            // Calcular stock total
            int stockTotal = dbManager.getProductoDao().getAll()
                    .stream()
                    .mapToInt(p -> p.getCantidad())
                    .sum();
            stockTotalLabel.setText(String.valueOf(stockTotal));

        } catch (SQLException e) {
            System.err.println("Error al cargar estadísticas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ============================================================================
    // MANEJADORES DE NAVEGACIÓN
    // ============================================================================

    @FXML
    private void handleDashboard() {
        showView(dashboardView, dashboardBtn, "Dashboard", "Bienvenido al sistema");
    }

    @FXML
    private void handleProductos() {
        showView(productosView, productosBtn, "Gestión de Productos", "Administra tus productos");

        // Cargar vista de productos si no está cargada
        if (productosView.getChildren().isEmpty()) {
            try {
                loadProductosView();
            } catch (Exception e) {
                UIUtils.showErrorAlert("Error", "Error al cargar vista de productos: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Carga la vista de productos desde el FXML.
     */
    private void loadProductosView() throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/views/productos.fxml")
        );

        ProductosViewController controller = new ProductosViewController(
                mainController, dbManager, userName
        );
        loader.setController(controller);

        productosView.getChildren().clear();
        productosView.getChildren().add(loader.load());
    }

    @FXML
    private void handleAlmacenes() {
        showView(almacenesView, almacenesBtn, "Gestión de Almacenes", "Administra tus almacenes");

        // Cargar vista de almacenes si no está cargada
        if (almacenesView.getChildren().isEmpty()) {
            try {
                loadAlmacenesView();
            } catch (Exception e) {
                UIUtils.showErrorAlert("Error", "Error al cargar vista de almacenes: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Carga la vista de almacenes desde el FXML.
     */
    private void loadAlmacenesView() throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/views/almacenes.fxml")
        );

        AlmacenesViewController controller = new AlmacenesViewController(
                mainController, dbManager, userName
        );
        loader.setController(controller);

        almacenesView.getChildren().clear();
        almacenesView.getChildren().add(loader.load());
    }

    @FXML
    private void handleConfig() {
        showView(configView, configBtn, "Configuración", "Administración del sistema");
        if (configView.getChildren().isEmpty()) {
            try {
                loadConfigView();
            } catch (Exception e) {
                UIUtils.showErrorAlert("Error", "Error al cargar vista de configuración: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void loadConfigView() throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/views/config.fxml")
        );
        ConfigViewController controller = new ConfigViewController(
                mainController, dbManager, userName
        );
        loader.setController(controller);
        configView.getChildren().clear();
        configView.getChildren().add(loader.load());
    }

    /**
     * Muestra una vista específica en el StackPane.
     *
     * @param view Vista a mostrar
     * @param button Botón de navegación asociado
     * @param title Título de la página
     * @param subtitle Subtítulo de la página
     */
    private void showView(VBox view, Button button, String title, String subtitle) {
        // Ocultar todas las vistas
        dashboardView.setVisible(false);
        dashboardView.setManaged(false);
        productosView.setVisible(false);
        productosView.setManaged(false);
        almacenesView.setVisible(false);
        almacenesView.setManaged(false);
        configView.setVisible(false);
        configView.setManaged(false);

        // Remover estado activo de todos los botones
        dashboardBtn.getStyleClass().remove("nav-button-active");
        productosBtn.getStyleClass().remove("nav-button-active");
        almacenesBtn.getStyleClass().remove("nav-button-active");
        configBtn.getStyleClass().remove("nav-button-active");

        // Mostrar la vista seleccionada
        view.setVisible(true);
        view.setManaged(true);

        // Marcar botón como activo
        if (!button.getStyleClass().contains("nav-button-active")) {
            button.getStyleClass().add("nav-button-active");
        }

        // Actualizar topbar
        pageTitle.setText(title);
        pageSubtitle.setText(subtitle);
    }

    /**
     * Cierra la sesión y vuelve a mostrar el login.
     */
    @FXML
    private void handleLogout() {
        if (UIUtils.showConfirmAlert(
                "Confirmar Cierre de Sesión",
                "¿Deseas cerrar sesión?"
        )) {
            try {
                mainController.showLoginViewAgain();
            } catch (Exception e) {
                UIUtils.showErrorAlert("Error", "Error al cerrar sesión: " + e.getMessage());
                System.err.println("Error al cerrar sesión: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Muestra el dashboard.
     */
    private void showDashboard() {
        showView(dashboardView, dashboardBtn, "Dashboard", "Bienvenido al sistema");
    }

    /**
     * Obtiene el nombre del usuario autenticado.
     *
     * @return Nombre del usuario
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Obtiene el rol del usuario autenticado.
     *
     * @return Rol del usuario
     */
    public String getUserRole() {
        return userRole;
    }
}