import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
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
    private Label motCrypte;
    /**
     * la barre de progression qui indique le nombre de tentatives
     */
    private ProgressBar pg;
    /**
     * le clavier qui sera géré par une classe à implémenter
     */
    private Clavier clavier;
    /**
     * le text qui indique le niveau de difficulté
     */
    private Label leNiveau;
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
     * Booléen indiquant si une partie est en cours
     */
    private boolean partieEnCours;

    /**
     * Chaine de caractères indiquant le thème sélectionné (clair ou sombre)
     */
    private String themeCourant = "clair";
    
    /**
     * initialise les attributs (créer le modèle, charge les images, crée le chrono ...)
     */
    @Override
    public void init() {
        this.modelePendu = new MotMystere("/usr/share/dict/french", 3, 10, MotMystere.FACILE, 10);
        this.lesImages = new ArrayList<Image>();
        this.panelCentral = new BorderPane();
        this.chargerImages("./img");
        this.chrono = new Chronometre();
        this.niveaux = Arrays.asList("Facile", "Médium", "Difficile", "Expert");
        this.partieEnCours = false;
    }

    /**
     * @return le graphe de scène de la vue à partir de methodes précédantes
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
    
        // Titre
        Text titre = new Text("Jeu du Pendu");
        titre.setFont(Font.font("Arial", 40));
        titre.setStyle("-fx-font-weight: bold");
    
        // Bouton "Accueil"
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
    
        // Bouton "Paramètres"
        Image img2 = new Image("file:img/parametres.png");
        ImageView view2 = new ImageView(img2);
        view2.setFitHeight(30);
        view2.setFitWidth(30);
        this.boutonParametres = new Button();
        this.boutonParametres.setGraphic(view2);
        this.boutonParametres.setOnAction(new ControleurParametres(this));
        Tooltip tlp_parametre = new Tooltip("Appuyez pour consulter les paramètres du jeu");
        tlp_parametre.setShowDelay(Duration.seconds(0));
        this.boutonParametres.setTooltip(tlp_parametre);
    
        // Bouton "Info"
        Image img3 = new Image("file:img/info.png");
        ImageView view3 = new ImageView(img3);
        view3.setFitHeight(30);
        view3.setFitWidth(30);
        this.boutonInfo = new Button();
        this.boutonInfo.setGraphic(view3);
        Tooltip tlp_info = new Tooltip("Appuyez pour consulter les informations du jeu");
        tlp_info.setShowDelay(Duration.seconds(0));
        this.boutonInfo.setTooltip(tlp_info);
        this.boutonInfo.setOnAction(new ControleurInfos(this));
    
        // Couleur de la bannière
        bp.setBackground(new Background(new BackgroundFill(Color.LIGHTSTEELBLUE,null,null)));
        bp.setLeft(titre);
    
        // Hbox pour les trois boutons
        HBox hb = new HBox(5);
        hb.getChildren().addAll(this.boutonMaison, this.boutonParametres, this.boutonInfo);
        bp.setRight(hb);
    
        return bp;
    }
    
    /**
     * @return le panel du chronomètre
     */
    private TitledPane leChrono(){
        TitledPane chrono = new TitledPane("Chronomètre", this.chrono);
        chrono.setCollapsible(false);
        return chrono;
    }

    /**
     * @return la fenêtre de jeu avec le mot crypté, l'image, la barre
     *         de progression et le clavier
     */
    private Pane fenetreJeu() {
        BorderPane bp_jeu = new BorderPane();
    
        // Partie gauche de la fenêtre de jeu
        VBox vb_gauche = new VBox(15);
        vb_gauche.setPadding(new Insets(15));
        motCrypte = new Label(modelePendu.getMotCrypte());
        motCrypte.setFont(Font.font("Arial", 20));
        dessin = new ImageView(lesImages.get(0));
        pg = new ProgressBar();
        pg.setProgress(0);
        clavier = new Clavier("ABCDEFGHIJKLMNOPQRSTUVWXYZ-", new ControleurLettres(modelePendu, this), 9);
        vb_gauche.getChildren().addAll(motCrypte, dessin, pg, clavier);
        vb_gauche.setAlignment(Pos.BASELINE_CENTER);
    
        // Partie droite de la fenêtre de jeu
        VBox vb_droite = new VBox(15);
        vb_droite.setPadding(new Insets(15));
        leNiveau = new Label("Niveau: " + niveaux.get(modelePendu.getNiveau()));
        leNiveau.setFont(Font.font("Arial", 20));
        TitledPane tlp_chrono = leChrono();
        Button btn_nv_mot = new Button("Nouveau mot");
        btn_nv_mot.setOnAction(new ControleurLancerPartie(modelePendu, this));
        vb_droite.getChildren().addAll(leNiveau, tlp_chrono, btn_nv_mot);
    
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

        // Bouton pour lancer une partie
        this.bJouer = new Button("Lancer une partie");
        this.bJouer.setOnAction(new ControleurLancerPartie(this.modelePendu, this));
        
        ToggleGroup tgp_rdbtn = new ToggleGroup();

        RadioButton rdb_facile = new RadioButton(niveaux.get(0));
        rdb_facile.setOnAction(new ControleurNiveau(this.modelePendu));
        rdb_facile.setToggleGroup(tgp_rdbtn);
        
        RadioButton rdb_medium = new RadioButton(niveaux.get(1));
        rdb_medium.setOnAction(new ControleurNiveau(this.modelePendu));
        rdb_medium.setToggleGroup(tgp_rdbtn);
        
        RadioButton rdb_difficile = new RadioButton(niveaux.get(2));
        rdb_difficile.setOnAction(new ControleurNiveau(this.modelePendu));
        rdb_difficile.setToggleGroup(tgp_rdbtn);
        
        RadioButton rdb_expert = new RadioButton(niveaux.get(3));
        rdb_expert.setOnAction(new ControleurNiveau(this.modelePendu));
        rdb_expert.setToggleGroup(tgp_rdbtn);
        
        // Vbox pour le tiltedPane
        VBox vb_tltdPane = new VBox(10);
        vb_tltdPane.getChildren().addAll(rdb_facile, rdb_medium, rdb_difficile, rdb_expert);
        
        TitledPane tp = new TitledPane("Niveau de difficulté", vb_tltdPane);
        tp.setCollapsible(false);
    
        accueil.getChildren().addAll(bJouer, tp);
        return accueil;
    }

    /**
     * @return la page des paramètres
     */
    private Pane fenetreParamètres(){
        VBox vb_parametres = new VBox(15);
        vb_parametres.setPadding(new Insets(15));
        
        Label titre = new Label("Paramètres du jeu");
        titre.setFont(Font.font("Arial", 20));
        titre.setStyle("-fx-font-weight: bold");

        GridPane gp_parametres = new GridPane();
        gp_parametres.setPadding(new Insets(15));
        gp_parametres.setHgap(20);

        Label choix_theme = new Label("Choix du thème: ");
        choix_theme.setStyle("-fx-font-weight: bold");
        choix_theme.setFont(Font.font("Arial", 15));
        gp_parametres.add(choix_theme, 0, 0);

        ToggleGroup tgp_parametres = new ToggleGroup();

        RadioButton theme_clair = new RadioButton("Clair");
        theme_clair.setToggleGroup(tgp_parametres);
        theme_clair.setOnAction(new ControleurTheme(this, "clair"));
        gp_parametres.add(theme_clair, 1, 0);

        RadioButton theme_sombre = new RadioButton("Sombre");
        theme_sombre.setToggleGroup(tgp_parametres);
        theme_sombre.setOnAction(new ControleurTheme(this, "sombre"));
        gp_parametres.add(theme_sombre, 2, 0);

        if (this.themeCourant.equals("clair")) {
            theme_clair.setSelected(true);
        } else {
            theme_sombre.setSelected(true);
        }

        vb_parametres.getChildren().addAll(titre, gp_parametres);

        return vb_parametres;
    }

    /**
     * Permet de changer le thème du jeu (clair ou sombre)
     * @param theme Une chaîne de caractère indiquant le thème choisi (clair ou sombre)
     */
    public void setThemeJeu(String theme){
        Scene page = this.panelCentral.getScene();
        this.themeCourant = theme;
        if(theme.equals("clair")){
            page.getStylesheets().remove("themeSombre.css");
            page.getStylesheets().add("themeClair.css");
        }
        else{
            page.getStylesheets().remove("themeClair.css");
            page.getStylesheets().add("themeSombre.css");
        }
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

    /**
     * Affiche la page d'accueil
     */
    public void modeAccueil() {
        this.panelCentral.setCenter(fenetreAccueil());
        this.boutonMaison.setDisable(true);
        this.boutonParametres.setDisable(false);
    }
    
    /**
    * Affiche la page de jeu
    */
    public void modeJeu(){
        this.panelCentral.setCenter(fenetreJeu());
        this.boutonParametres.setDisable(true);
        this.boutonMaison.setDisable(false);
    }
    
    /**
    * Affiche la page des paramètres
    */
    public void modeParametres(){
        this.panelCentral.setCenter(fenetreParamètres());
        this.boutonParametres.setDisable(true);
        this.boutonMaison.setDisable(false);
    }

    /** 
     * lance une partie 
     */
    public void lancePartie(){
        modeJeu();
        this.modelePendu.setMotATrouver();
        this.chrono.resetTime();
        this.chrono.start();
        this.partieEnCours = true;
        this.majAffichage();
    }

    /**
     * raffraichit l'affichage selon les données du modèle
     */
    public void majAffichage() {
        this.motCrypte.setText(this.modelePendu.getMotCrypte());

        int tailleMot = modelePendu.getMotATrouve().length();
        int lettresTrouvees = tailleMot - modelePendu.getNbLettresRestantes();
        double progression = (double) lettresTrouvees / tailleMot;
        pg.setProgress(progression);

        int nbErreursRestantes = modelePendu.getNbErreursRestants();
        if (nbErreursRestantes >= 0 && nbErreursRestantes < lesImages.size()) {
        int indiceImage = lesImages.size() - nbErreursRestantes - 1;
        dessin.setImage(lesImages.get(indiceImage));
        }
        if (modelePendu.gagne()) {
            popUpMessageGagne();
            this.partieEnCours = false;
            this.chrono.stop();
        } else if (modelePendu.perdu()) {
            popUpMessagePerdu();
            this.partieEnCours = false;
            this.chrono.stop();
        }
    }

    /**
     * consulte le statut de la partie (en cours ou non)
     */
    public boolean partieEstEnCours(){
        return this.partieEnCours;
    }

    /**
     * accesseur du chronomètre (pour les controleur du jeu)
     * @return le chronomètre du jeu
     */
    public Chronometre getChrono(){
        return this.chrono;
    }

    /**
     * Affiche un message d'alerte indiquant qu'une partie est en cours
     * @return Le message d'alerte
     */
    public Alert popUpPartieEnCours(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"La partie est en cours!\n Etes-vous sûr de l'interrompre ?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Attention");
        alert.setHeaderText("ATTENTION !");
        return alert;
    }

     /**
     * Affiche un message expliquant les règles du jeu
     * @return Le message d'information
     */
    public Alert popUpReglesDuJeu(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION, 
        "Voici comment jouer au Pendu :\n\n" +
        "1. Le mot mystère: Un mot secret est choisi et affiché sous forme d'étoiles (*), représentant chaque lettre à deviner. \n\n" +
        "2. Les tentatives: devinez les lettres une par une en cliquant sur le clavier de la page de jeu. Chaque lettre correcte est ajoutée à la place d'une étoile dans le mot mystère.\n\n" +
        "3. Le pendu: pour chaque erreur, une partie d'une image représentant un pendu est dessinée. Le but est de deviner le mot mystère avant que le pendu ne soit complet.\n\n" +
        "4. Les erreurs: si une lettre choisie n'est pas dans le mot mystère, cela compte comme une erreur et rapproche du pendu.\n\n" +
        "5. La victoire ou la défaite: gagnez en devinant le mot mystère avant que le pendu ne soit complet et perdez si le pendu est complet avant d'avoir deviné le mot.");
        alert.setWidth(600);
        alert.setTitle("Règle du jeu");
        alert.setHeaderText("Bienvenue au jeu du pendu !");
        return alert;
    }
    
    /**
    * Affiche un message si la partie est gagnée
    * @return Le message d'information
    */
    public Alert popUpMessageGagne(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION,"VOUS ETES UN VERITABLE DICTIONNAIRE SUR PATTES !\n" + "Cliquez sur le bouton *Nouveau Mot* pour relancer une partie.");
        alert.setWidth(600);
        alert.setTitle("Partie gagnée");
        alert.setHeaderText("FELICITATIONS !");
        alert.showAndWait();
        return alert;
    }
    
     /**
    * Affiche un message si la partie est perdue
    * @return Le message d'information
    */
    public Alert popUpMessagePerdu(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION,"Le mot était: " + this.modelePendu.getMotATrouve() + " vous ferez mieux la prochaine fois :)\n" +"Cliquez sur le bouton *Nouveau Mot* pour relancer une partie.");
        alert.setWidth(600);
        alert.setTitle("Partie perdue");
        alert.setHeaderText("GAME OVER X_X");
        alert.showAndWait();
        return alert;
    }

    /**
     * créer le graphe de scène et lance le jeu
     * @param stage la fenêtre principale
     */
    @Override
    public void start(Stage stage) {
        stage.setTitle("IUTEAM'S - La plateforme de jeux de l'IUTO");
        Scene scene = this.laScene();
        scene.getStylesheets().add("themeClair.css");
        stage.setScene(scene);
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
