import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.ButtonBar.ButtonData ;

import java.util.List;
import java.util.Arrays;
import java.io.File;
import java.util.ArrayList;


/**
 * Vue du jeu du pendu
 */
public class Pendu extends Application {
    /**
     * modèle du jeu
     **/
    private MotMystere modelePendu;
    /**
     * Liste qui contient les images du jeu
     */
    private ArrayList<Image> lesImages;
    /**
     * Liste qui contient les noms des niveaux
     */    
    public List<String> niveaux;

    // les différents contrôles qui seront mis à jour ou consultés pour l'affichage
    /**
     * le dessin du pendu
     */
    private ImageView dessin;
    /**
     * le mot à trouver avec les lettres déjà trouvé
     */
    private Text motCrypte;
    /**
     * la barre de progression qui indique le nombre de tentatives
     */
    private ProgressBar pg;
    /**
     * le clavier qui sera géré par une classe à implémenter
     */
    // private Clavier clavier;
    /**
     * le text qui indique le niveau de difficulté
     */
    private Text leNiveau;
    /**
     * le chronomètre qui sera géré par une clasee à implémenter
     */
    private Chronometre chrono;
    /**
     * le panel Central qui pourra être modifié selon le mode (accueil ou jeu)
     */
    private BorderPane panelCentral;
    /**
     * le bouton Paramètre / Engrenage
     */
    private Button boutonParametres;
    /**
     * le bouton Accueil / Maison
     */    
    private Button boutonMaison;

    /**
     * le bouton Info
     */    
    private Button boutonInfo;
    
    /**
     * le bouton qui permet de (lancer ou relancer une partie
     */ 
    private Button bJouer;

    /**
     * initialise les attributs (créer le modèle, charge les images, crée le chrono ...)
     */
    @Override
    public void init() {
        this.modelePendu = new MotMystere("/usr/share/dict/french", 3, 10, MotMystere.FACILE, 10);
        this.lesImages = new ArrayList<Image>();
        this.panelCentral = new BorderPane();
        this.chargerImages("./img");
        // A terminer d'implementer
    }

    /**
     * @return  le graphe de scène de la vue à partir de methodes précédantes
     */
    private Scene laScene(){
        BorderPane fenetre = new BorderPane();
        fenetre.setTop(this.titre());
        fenetre.setCenter(this.panelCentral);
        return new Scene(fenetre, 800, 1000);
    }

    /**
     * @return le panel contenant le titre du jeu
     */
    private Pane titre(){
        BorderPane bp = new BorderPane();
        bp.setPadding(new Insets(30));

        Text titre = new Text("Jeu du Pendu");
        titre.setFont(Font.font("Arial", 40));
        
        Image img1 = new Image("file:img/home.png");
        ImageView view1 = new ImageView(img1);
        view1.setFitHeight(30);
        view1.setFitWidth(30);
        this.boutonMaison = new Button();
        this.boutonMaison.setGraphic(view1);
        Tooltip tlp_maison = new Tooltip("Appuyez pour revenir à l'accueil");
        tlp_maison.setShowDelay(Duration.seconds(0));
        this.boutonMaison.setTooltip(tlp_maison);
        this.boutonMaison.setOnAction(new RetourAccueil(null, this));
        
        Image img2 = new Image("file:img/parametres.png");
        ImageView view2 = new ImageView(img2);
        view2.setFitHeight(30);
        view2.setFitWidth(30);
        this.boutonParametres = new Button();
        this.boutonParametres.setGraphic(view2);
        Tooltip tlp_parametre = new Tooltip("Appuyez pour consulter les paramètres du jeu");
        tlp_parametre.setShowDelay(Duration.seconds(0));
        this.boutonParametres.setTooltip(tlp_parametre);
        
        Image img3 = new Image("file:img/info.png");
        ImageView view3 = new ImageView(img3);
        view3.setFitHeight(30);
        view3.setFitWidth(30);
        this.boutonInfo = new Button();
        this.boutonInfo.setGraphic(view3);
        Tooltip tlp_info = new Tooltip("Appuyez pour consulter les informations du jeu");
        tlp_info.setShowDelay(Duration.seconds(0));
        this.boutonInfo.setTooltip(tlp_info);
        
        bp.setBackground(new Background(new BackgroundFill(Color.LIGHTSTEELBLUE,null,null)));
        bp.setLeft(titre);

        HBox hb = new HBox(5);
        hb.getChildren().addAll(this.boutonMaison, this.boutonParametres, this.boutonInfo);
        bp.setRight(hb);

        return bp;
    }

    /**
     * @return le panel du chronomètre
     */
    private TitledPane leChrono(){
        this.chrono = new Chronometre();
        TitledPane res = new TitledPane("Chronomètre", this.chrono);
        res.setCollapsible(false);
        return res;
    }

    /**
     * @return la fenêtre de jeu avec le mot crypté, l'image, la barre
     *         de progression et le clavier
     */
    private Pane fenetreJeu(){
        BorderPane bp_jeu = new BorderPane();

        VBox vb_gauche = new VBox(15);
        vb_gauche.setPadding(new Insets(15));

        this.motCrypte = new Text(this.modelePendu.getMotCrypte());
        this.dessin = new ImageView(this.lesImages.get(0));
        this.pg = new ProgressBar();
        this.pg.setProgress(0);
        Clavier clavier_jeu = new Clavier("ABCDEFGHIJKLMNOPQRSTUVWXYZ", new ControleurLettres(this.modelePendu,this), 8);
        vb_gauche.getChildren().addAll(this.motCrypte, this.dessin, this.pg, clavier_jeu);

        VBox vb_droite = new VBox(15);
        vb_droite.setPadding(new Insets(15));

        this.leNiveau = new Text("Niveau: " +  this.niveaux.get(this.modelePendu.getNiveau()));
        TitledPane tlp_chrono = this.leChrono();
        Button btn_nv_mot = new Button("Nouveau mot");
        btn_nv_mot.setOnAction(new ControleurLancerPartie(this.modelePendu, this));
        vb_droite.getChildren().addAll(this.leNiveau, tlp_chrono, btn_nv_mot);

        bp_jeu.setLeft(vb_gauche);
        bp_jeu.setRight(vb_droite);
        
        return bp_jeu;
    }

    /**
     * @return la fenêtre d'accueil sur laquelle on peut choisir les paramètres de jeu
     */
    private Pane fenetreAccueil(){
        VBox accueil = new VBox(15);
        accueil.setPadding(new Insets(15));

        this.bJouer = new Button("Lancer une partie");
        this.bJouer.setOnAction(new ControleurLancerPartie(this.modelePendu, this));
    
        VBox vb_tltdPane = new VBox(10);
    
        RadioButton rdb_facile = new RadioButton("Facile");
        RadioButton rdb_medium = new RadioButton("Médium");
        RadioButton rdb_difficile = new RadioButton("Difficile");
        RadioButton rdb_expert = new RadioButton("Expert");
        
        ToggleGroup tgp_rdbtn = new ToggleGroup();
        rdb_facile.setToggleGroup(tgp_rdbtn);
        rdb_medium.setToggleGroup(tgp_rdbtn);
        rdb_difficile.setToggleGroup(tgp_rdbtn);
        rdb_expert.setToggleGroup(tgp_rdbtn);
    
        vb_tltdPane.getChildren().addAll(rdb_facile, rdb_medium, rdb_difficile, rdb_expert);
        
        TitledPane tp = new TitledPane("Niveau de difficulté", vb_tltdPane);
        tp.setCollapsible(false);
    
        accueil.getChildren().addAll(bJouer, tp);
        return accueil;
    }

    /**
     * charge les images à afficher en fonction des erreurs
     * @param repertoire répertoire où se trouvent les images
     */
    private void chargerImages(String repertoire){
        for (int i=0; i<this.modelePendu.getNbErreursMax()+1; i++){
            File file = new File(repertoire+"/pendu"+i+".png");
            System.out.println(file.toURI().toString());
            this.lesImages.add(new Image(file.toURI().toString()));
        }
    }

    public void modeAccueil() {
        this.panelCentral.setCenter(fenetreAccueil());
        this.boutonMaison.setDisable(true);
        this.boutonParametres.setDisable(false);
    }
    
    public void modeJeu(){
        this.panelCentral.setCenter(fenetreJeu());
        this.boutonMaison.setDisable(false);
        this.boutonParametres.setDisable(true);
    }
    
    public void modeParametres(){
        // A implémenter
    }

    /**
     * Vérifie si une partie est en cours.
     * @return true si une partie est en cours, false sinon
     */
    public boolean partieEnCours() {
        return modelePendu.getNbEssais() > 0 && !modelePendu.gagne() && !modelePendu.perdu();
    }

    /** lance une partie */
    public void lancePartie(){
        modeJeu();
    }

    /**
     * raffraichit l'affichage selon les données du modèle
     */
    public void majAffichage() {
        // Mettre à jour le mot crypté
        this.motCrypte.setText(this.modelePendu.getMotCrypte());
    
        // Mettre à jour l'image du pendu
        int nbErreurs = this.modelePendu.getNbEssais();
        this.dessin.setImage(this.lesImages.get(nbErreurs));
    
        // Mettre à jour la barre de progression
        double progress = (double) nbErreurs / this.modelePendu.getNbErreursMax();
        this.pg.setProgress(progress);
    
        // Mettre à jour le niveau de difficulté
        this.leNiveau.setText("Niveau: " + this.niveaux.get(this.modelePendu.getNiveau()));
    }
    

    /**
     * accesseur du chronomètre (pour les controleur du jeu)
     * @return le chronomètre du jeu
     */
    public Chronometre getChrono(){
        // A implémenter
        return null; // A enlever
    }

    public Alert popUpPartieEnCours(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"La partie est en cours!\n Etes-vous sûr de l'interrompre ?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Attention");
        return alert;
    }
        
    public Alert popUpReglesDuJeu(){
        // A implementer
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        return alert;
    }
    
    public Alert popUpMessageGagne(){
        // A implementer
        Alert alert = new Alert(Alert.AlertType.INFORMATION);        
        return alert;
    }
    
    public Alert popUpMessagePerdu(){
        // A implementer    
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        return alert;
    }

    /**
     * créer le graphe de scène et lance le jeu
     * @param stage la fenêtre principale
     */
    @Override
    public void start(Stage stage) {
        stage.setTitle("IUTEAM'S - La plateforme de jeux de l'IUTO");
        stage.setScene(this.laScene());
        this.modeAccueil();
        stage.show();
    }

    /**
     * Programme principal
     * @param args inutilisé
     */
    public static void main(String[] args) {
        launch(args);
    }    
}
