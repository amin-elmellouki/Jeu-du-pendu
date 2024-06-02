import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import java.util.Optional;

/**
 * Contrôleur à activer lorsque l'on clique sur le bouton rejouer ou Lancer une partie
 */
public class ControleurLancerPartie implements EventHandler<ActionEvent> {
    /**
     * modèle du jeu
     */
    private MotMystere modelePendu;
    /**
     * vue du jeu
     **/
    private Pendu vuePendu;

    /**
     * @param modelePendu modèle du jeu
     * @param p vue du jeu
     */
    public ControleurLancerPartie(MotMystere modelePendu, Pendu vuePendu) {
        this.modelePendu = modelePendu;
        this.vuePendu = vuePendu;
    }

    /**
     * L'action consiste à recommencer une partie. Il faut vérifier qu'il n'y a pas une partie en cours
     * @param actionEvent l'événement action
     */
   @Override
    public void handle(ActionEvent actionEvent) {
        if (vuePendu.partieEstEnCours()) {
            Alert alert = vuePendu.popUpPartieEnCours();
            alert.getButtonTypes().setAll(new ButtonType("Oui", ButtonBar.ButtonData.YES), new ButtonType("Non", ButtonBar.ButtonData.NO));
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.YES) {
                vuePendu.lancePartie();
                System.out.println("*Voix de forain* C'est reparti pour un tour !");
            } else {
                System.out.println("Ok on fait rien chef !");
            }
        } else {
            vuePendu.lancePartie();
        }
    }


}