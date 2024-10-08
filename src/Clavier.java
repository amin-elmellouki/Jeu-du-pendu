import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;
import javafx.scene.shape.Circle ;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Génère la vue d'un clavier et associe le contrôleur aux touches
 * le choix ici est d'un faire un héritié d'un TilePane
 */
public class Clavier extends TilePane{
    /**
     * il est conseillé de stocker les touches dans un ArrayList
     */
    private List<Button> clavier;

    /**
     * constructeur du clavier
     * @param touches une chaine de caractères qui contient les lettres à mettre sur les touches
     * @param actionTouches le contrôleur des touches
     * @param tailleLigne nombre de touches par ligne
     */
    public Clavier(String touches, EventHandler<ActionEvent> actionTouches, int tailleLigne){
        super();
        this.clavier = new ArrayList<>();
        this.setPrefColumns(tailleLigne);
        for(int i = 0;i < touches.length();++i){
            Button btn = new Button(String.valueOf(touches.charAt(i)));
            Circle forme_btn = new Circle(30);
            btn.setShape(forme_btn);
            btn.setOnAction(actionTouches);
            this.clavier.add(btn);
        }
        this.getChildren().addAll(this.clavier);
    }

    /**
     * permet de désactiver certaines touches du clavier (et active les autres)
     * @param touchesDesactivees une chaine de caractères contenant la liste des touches désactivées
     */
    public void desactiveTouches(Set<String> touchesDesactivees){
        for (Button btn : this.clavier) {
            if (touchesDesactivees.contains(btn.getText())) {
                btn.setDisable(true);
            } else {
                btn.setDisable(false);
            }
        }
    }
}