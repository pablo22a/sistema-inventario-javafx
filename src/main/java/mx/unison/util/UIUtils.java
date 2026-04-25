package mx.unison.util;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Clase con utilidades para la interfaz de usuario JavaFX.
 * Contiene métodos para crear componentes estilizados y animaciones.
 */
public class UIUtils {

    // ============================================================================
    // CONSTANTES DE ESTILOS
    // ============================================================================

    public static final String BUTTON_PRIMARY = "button-primary";
    public static final String BUTTON_SECONDARY = "button-secondary";
    public static final String BUTTON_SUCCESS = "button-success";
    public static final String BUTTON_DANGER = "button-danger";

    public static final String LABEL_TITLE = "label-title";
    public static final String LABEL_SUBTITLE = "label-subtitle";
    public static final String LABEL_INFO = "label-info";

    public static final String PANEL = "panel";
    public static final String PANEL_HEADER = "panel-header";
    public static final String PANEL_BODY = "panel-body";
    public static final String CARD = "card";

    public static final String ALERT_INFO = "alert-info";
    public static final String ALERT_SUCCESS = "alert-success";
    public static final String ALERT_WARNING = "alert-warning";
    public static final String ALERT_DANGER = "alert-danger";

    // ============================================================================
    // MÉTODOS PARA CREAR BOTONES
    // ============================================================================

    /**
     * Crea un botón primario estilizado.
     *
     * @param text Texto del botón
     * @return Botón configurado
     */
    public static Button createPrimaryButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add(BUTTON_PRIMARY);
        button.setPrefWidth(150);
        button.setPrefHeight(40);
        return button;
    }

    /**
     * Crea un botón secundario estilizado.
     *
     * @param text Texto del botón
     * @return Botón configurado
     */
    public static Button createSecondaryButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add(BUTTON_SECONDARY);
        button.setPrefWidth(150);
        button.setPrefHeight(40);
        return button;
    }

    /**
     * Crea un botón de éxito estilizado.
     *
     * @param text Texto del botón
     * @return Botón configurado
     */
    public static Button createSuccessButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add(BUTTON_SUCCESS);
        button.setPrefWidth(150);
        button.setPrefHeight(40);
        return button;
    }

    /**
     * Crea un botón de peligro estilizado.
     *
     * @param text Texto del botón
     * @return Botón configurado
     */
    public static Button createDangerButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add(BUTTON_DANGER);
        button.setPrefWidth(150);
        button.setPrefHeight(40);
        return button;
    }

    // ============================================================================
    // MÉTODOS PARA CREAR ETIQUETAS
    // ============================================================================

    /**
     * Crea una etiqueta de título estilizada.
     *
     * @param text Texto del título
     * @return Etiqueta configurada
     */
    public static Label createTitle(String text) {
        Label label = new Label(text);
        label.getStyleClass().add(LABEL_TITLE);
        return label;
    }

    /**
     * Crea una etiqueta de subtítulo estilizada.
     *
     * @param text Texto del subtítulo
     * @return Etiqueta configurada
     */
    public static Label createSubtitle(String text) {
        Label label = new Label(text);
        label.getStyleClass().add(LABEL_SUBTITLE);
        return label;
    }

    /**
     * Crea una etiqueta de información estilizada.
     *
     * @param text Texto de la información
     * @return Etiqueta configurada
     */
    public static Label createInfoLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add(LABEL_INFO);
        return label;
    }

    // ============================================================================
    // MÉTODOS PARA CREAR PANELES
    // ============================================================================

    /**
     * Crea un panel VBox estilizado.
     *
     * @param spacing Espaciado entre elementos
     * @param children Elementos hijos
     * @return Panel configurado
     */
    public static VBox createPanel(double spacing, Node... children) {
        VBox panel = new VBox(spacing);
        panel.getStyleClass().add(PANEL);
        panel.getChildren().addAll(children);
        return panel;
    }

    /**
     * Crea un panel header estilizado.
     *
     * @param title Título del header
     * @return Panel header configurado
     */
    public static VBox createPanelHeader(String title) {
        VBox header = new VBox();
        header.getStyleClass().add(PANEL_HEADER);
        header.setPadding(new Insets(15));

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        header.getChildren().add(titleLabel);

        return header;
    }

    /**
     * Crea un card estilizado.
     *
     * @param children Elementos hijos
     * @return Card configurado
     */
    public static VBox createCard(Node... children) {
        VBox card = new VBox(10);
        card.getStyleClass().add(CARD);
        card.setPadding(new Insets(15));
        card.getChildren().addAll(children);
        return card;
    }

    // ============================================================================
    // MÉTODOS PARA CREAR CAMPOS DE ENTRADA
    // ============================================================================

    /**
     * Crea un campo de texto con etiqueta.
     *
     * @param labelText Texto de la etiqueta
     * @param promptText Texto de sugerencia
     * @return VBox con etiqueta y campo de texto
     */
    public static VBox createLabeledTextField(String labelText, String promptText) {
        VBox container = new VBox(5);
        Label label = new Label(labelText);
        label.getStyleClass().add("label-info");

        TextField textField = new TextField();
        textField.setPromptText(promptText);
        textField.setPrefHeight(35);

        container.getChildren().addAll(label, textField);
        return container;
    }

    /**
     * Obtiene el TextField de un VBox creado por createLabeledTextField.
     *
     * @param container Contenedor VBox
     * @return TextField
     */
    public static TextField getTextFieldFromContainer(VBox container) {
        return (TextField) container.getChildren().get(1);
    }

    /**
     * Crea un campo de contraseña con etiqueta.
     *
     * @param labelText Texto de la etiqueta
     * @param promptText Texto de sugerencia
     * @return VBox con etiqueta y campo de contraseña
     */
    public static VBox createLabeledPasswordField(String labelText, String promptText) {
        VBox container = new VBox(5);
        Label label = new Label(labelText);
        label.getStyleClass().add("label-info");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText(promptText);
        passwordField.setPrefHeight(35);

        container.getChildren().addAll(label, passwordField);
        return container;
    }

    /**
     * Obtiene el PasswordField de un VBox creado por createLabeledPasswordField.
     *
     * @param container Contenedor VBox
     * @return PasswordField
     */
    public static PasswordField getPasswordFieldFromContainer(VBox container) {
        return (PasswordField) container.getChildren().get(1);
    }

    // ============================================================================
    // MÉTODOS PARA ALERTAS
    // ============================================================================

    /**
     * Crea una alerta de información.
     *
     * @param title Título de la alerta
     * @param message Mensaje de la alerta
     */
    public static void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Crea una alerta de éxito.
     *
     * @param title Título de la alerta
     * @param message Mensaje de la alerta
     */
    public static void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Crea una alerta de error.
     *
     * @param title Título de la alerta
     * @param message Mensaje de la alerta
     */
    public static void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Crea una alerta de advertencia.
     *
     * @param title Título de la alerta
     * @param message Mensaje de la alerta
     * @return true si el usuario acepta, false si cancela
     */
    public static boolean showConfirmAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    // ============================================================================
    // MÉTODOS PARA ANIMACIONES
    // ============================================================================

    /**
     * Aplica una transición de opacidad (fade-in) a un nodo.
     *
     * @param node Nodo a animar
     * @param duration Duración de la animación en milisegundos
     */
    public static void fadeIn(Node node, double duration) {
        FadeTransition fadeTransition = new FadeTransition(
                Duration.millis(duration), node
        );
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();
    }

    /**
     * Aplica una transición de opacidad (fade-out) a un nodo.
     *
     * @param node Nodo a animar
     * @param duration Duración de la animación en milisegundos
     */
    public static void fadeOut(Node node, double duration) {
        FadeTransition fadeTransition = new FadeTransition(
                Duration.millis(duration), node
        );
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.play();
    }

    // ============================================================================
    // MÉTODOS AUXILIARES
    // ============================================================================

    /**
     * Crea un espaciador que ocupa todo el espacio disponible.
     *
     * @return Region configurada como espaciador
     */
    public static Region createHorizontalSpacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }

    /**
     * Crea un espaciador vertical que ocupa todo el espacio disponible.
     *
     * @return Region configurada como espaciador vertical
     */
    public static Region createVerticalSpacer() {
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        return spacer;
    }

    /**
     * Crea un separador horizontal estilizado.
     *
     * @return Separator configurado
     */
    public static Separator createHorizontalSeparator() {
        Separator separator = new Separator();
        separator.getStyleClass().add("separator");
        return separator;
    }
}