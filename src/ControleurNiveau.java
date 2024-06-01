import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.RadioButton;

/**
 * Controleur des radio boutons gérant le niveau
 */
public class ControleurNiveau implements EventHandler<ActionEvent> {

    /**
     * modèle du jeu
     */
    private MotMystere modelePendu;


    /**
     * @param modelePendu modèle du jeu
     */
    public ControleurNiveau(MotMystere modelePendu) {
        this.modelePendu = modelePendu;
    }

    /**
     * gère le changement de niveau
     * @param actionEvent
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        // A implémenter
        RadioButton radiobouton = (RadioButton) actionEvent.getTarget();
        String nomDuRadiobouton = radiobouton.getText();
        switch(nomDuRadiobouton){
            case "Facile":
            this.modelePendu.setNiveau(this.modelePendu.FACILE);
            break;

            case "Médium":
            this.modelePendu.setNiveau(this.modelePendu.MOYEN);
            break;

            case "Difficile":
            this.modelePendu.setNiveau(this.modelePendu.DIFFICILE);
            break;
            
            case "Expert":
            this.modelePendu.setNiveau(this.modelePendu.EXPERT);
            break;
        }
        System.out.println(nomDuRadiobouton);
    }
}
