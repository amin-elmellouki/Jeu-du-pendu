import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ControleurTheme implements EventHandler<ActionEvent> {
    // Instance de la vue du jeu du Pendu
    private Pendu vue;
    // Thème à appliquer
    private String theme;

    /**
     * Constructeur de la classe ControleurTheme.
     * @param vue La vue du jeu du Pendu à laquelle le thème sera appliqué.
     * @param theme Le thème à appliquer à l'interface graphique.
     */
    public ControleurTheme(Pendu vue, String theme) {
        this.vue = vue;
        this.theme = theme;
    }

    /**
     * Change le thème de l'application en utilisant la méthode setThemeJeu de la vue.
     * @param event L'événement ActionEvent déclenché.
     */
    @Override
    public void handle(ActionEvent event) {
        vue.setThemeJeu(this.theme);
    }
}
