import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
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
        if (vuePendu.partieEnCours()) {
            // Demandez la confirmation de l'utilisateur pour interrompre la partie en cours
            Optional<ButtonType> reponse = vuePendu.popUpPartieEnCours().showAndWait();
            if (reponse.isPresent() && reponse.get().equals(ButtonType.YES)) {
                vuePendu.lancePartie();
            }
        } else {
            // Si aucune partie n'est en cours, lancez directement une nouvelle partie
            vuePendu.lancePartie();
        }
    }
}
