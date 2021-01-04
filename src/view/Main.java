package view;
	

import java.text.DecimalFormat;
import java.util.Arrays;

import controller.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import simu.Kello;
import simu.Palvelupiste;
import simu.Trace;
import simu.Trace.Level;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.paint.Color;


/**
 * Käyttöliittymäluokka. Rakennetaan käyttöliittymä. Annetaan käyttäjän asettamia tietoja kontrollerille, joka ohjaa
 * simulaation käynnistystä ja muita tarpeellisia asioita.
 * @author Tuomas Rajala
 * @since 12.10.2020
 */
public class Main extends Application implements GuiIf{

	private Stage primaryStage;
	
	//Kontrollerin esittely (tarvitaan käyttöliittymässä)
	private KontrolleriIf kontrolleri;

	// Käyttöliittymäkomponentit:
	private TextField aika, viive;
	//private int asiakkaatInt;
	private Text varoitus, asiakkaatLkm, kello, asiakkaatPP, jonoPP, kotiinPP, palvellutPP,
	kayttoastePP, suoritustehoPP, palveluaikaPP, oleskeluaikaPP,lapimenoaikaPP,jonopituusPP,
	kayttoasteM, suoritustehoM, palveluaikaM, oleskeluaikaM,lapimenoaikaM,jonopituusM,
	asiakkaatM, jonoM, kotiinM, palvellutM, asiakkaatTulos, jonoTulos, kotiinTulos, palvellutTulos, kayttoasteTulos, suoritustehoTulos, 
	palveluaikaTulos, oleskeluaikaTulos, lapimenoaikaTulos, jonopituusTulos;
	
	private Button kaynnistaButton, hidastaButton, nopeutaButton, haeAsiakkaatButton, haeHoitoButton, historiaButton;
	private Slider laaNopeus, saaNopeus,arvNopeus,rtglabNopeus,ilmNopeus;
	private Label laakariLabel, saapumisLabel,arvLabel,rtglabLabel,ilmLabel;
	private BorderPane pohja;
	private VBox rtglabNopeus2, laakariNopeus, saapumisNopeus, ilmoittautumisNopeus, arviointiNopeus;
	private GridPane alapalkkiGrid;
	private HBox alapalkki;
	
	private Slider[] sliderit = {saaNopeus, ilmNopeus, arvNopeus,laaNopeus, rtglabNopeus};
	private Label[] labelit = {saapumisLabel,ilmLabel,arvLabel,laakariLabel, rtglabLabel };
	private String[] tekstit = {"Saapumisen nopeus: ", "Ilmoittautumisnopeus: ","Arvioinnin nopeus: ", "Lääkärin nopeus: ", "Rtg ja Lab nopeus: "};
	private VBox[] boxit = {ilmoittautumisNopeus,arviointiNopeus,laakariNopeus, rtglabNopeus2, saapumisNopeus};
    
	ListView<String> asiakasList = new ListView<String>();
	ListView<String> hoitokertomusList = new ListView<String>();
	ObservableList<String> asiakkaat, hoitokertomus, palvelupisteet;
	ComboBox<String> comboBox;
	
	DecimalFormat formatter;
	private Visualisointi naytto;


	@Override
	public void init(){
		Trace.setTraceLevel(Level.INFO);
		kontrolleri = new Kontrolleri(this);
	}

	/**
	 * Rakennetaan käyttöliittymä pala palalta ja tuodaan se käyttäjän näkyviin.
	 */
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		// Käyttöliittymän rakentaminen
		try {
			
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			    @Override
			    public void handle(WindowEvent t) {
			        Platform.exit();
			        System.exit(0);
			    }
			});
			
			primaryStage.setTitle("Simulaattori");
			formatter = new DecimalFormat("#0.00");
			
			HBox ylapalkki = ylapalkki();
			FlowPane vasenpalkki = vasenpalkki();
			HBox alapalkki = alapalkki();
			FlowPane oikeapalkki = oikeapalkki();
			
			naytto = new Visualisointi(500,500);

			pohja = new BorderPane();
			pohja.setTop(ylapalkki);
			pohja.setBottom(alapalkki);
			pohja.setLeft(vasenpalkki);
			pohja.setRight(oikeapalkki);
			pohja.setCenter(naytto);
			pohja.setStyle("-fx-background-color: #dbd8ce;");
			
			Scene scene = new Scene(pohja);
	        primaryStage.setScene(scene);
	        primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Rakennetaan käyttöliittymän yläpalkki.
	 * @return palauttaa valmiin yläpalkin.
	 */
	private HBox ylapalkki() {
		HBox ylapalkki = new HBox();
		ylapalkki.setPadding(new Insets(10,10,10,10));
		ylapalkki.setSpacing(10);
		ylapalkki.setStyle("-fx-background-color: #dbd8ce;");
		kello = new Text("Simulaattorin kello: 0");
		
		asiakkaatLkm = new Text("Asiakkaiden lukumäärä: 0");
		varoitus = new Text();
		varoitus.setFill(Color.RED);
		ylapalkki.getChildren().addAll(varoitus, kello, asiakkaatLkm);
		ylapalkki.setAlignment(Pos.CENTER);
		return ylapalkki;
	}
	
	/**
	 * Rakennetaan käyttöliittymän vasen palkki.
	 * @return palauttaa valmiin vasemman palkin.
	 */
	private FlowPane vasenpalkki() {
		FlowPane vasenpalkki = new FlowPane();
		vasenpalkki.setPadding(new Insets(10,10,10,10));
		vasenpalkki.setVgap(10);
		vasenpalkki.setHgap(10);
		vasenpalkki.setPrefWrapLength(170);
		vasenpalkki.setStyle("-fx-background-color: #dbd8ce;");
		
		kaynnistaButton = new Button();
		kaynnistaButton.setText("Käynnistä simulointi");
		kaynnistaButton.setOnAction(e -> kontrolleri.kaynnistaSimulointi());
		kaynnistaButton.setPrefWidth(190);
		kaynnistaButton.setPrefHeight(30);
		
		hidastaButton = new Button();
		hidastaButton.setText("Hidasta");
		hidastaButton.setOnAction(e -> kontrolleri.hidasta());
		hidastaButton.setTextFill(Color.RED);
		hidastaButton.setPrefWidth(90);
		hidastaButton.setPrefHeight(30);

		nopeutaButton = new Button();
		nopeutaButton.setText("Nopeuta");
		nopeutaButton.setOnAction(e -> kontrolleri.nopeuta());
		nopeutaButton.setTextFill(Color.GREEN);
		nopeutaButton.setPrefWidth(90);
		nopeutaButton.setPrefHeight(30);
		
        HBox saato = new HBox();
        saato.setSpacing(10);
        saato.getChildren().addAll(hidastaButton,nopeutaButton);
        
        aika = new TextField("Simulointiaika");
        aika.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        aika.setPrefWidth(190);

        viive = new TextField("Viive");
        viive.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        viive.setPrefWidth(190);

        //Sliderien kuntoon laitto
        for(int i=0;i<sliderit.length;i++) {
        	sliderit[i] = new Slider();
        	sliderit[i].setMin(0.2);
        	sliderit[i].setMax(2);
        	sliderit[i].setValue(1);
        	sliderit[i].setShowTickLabels(true);
        	sliderit[i].setShowTickMarks(true);
	        labelit[i] = new Label(tekstit[i] + sliderit[i].getValue());
        }
        
        sliderit[0].valueProperty().addListener((obs, oldValue, newValue) -> {
        	labelit[0].setText(tekstit[0] + String.format("%.2f", newValue));
        });
        sliderit[0].setPrefSize(200, 30);
        
        sliderit[1].valueProperty().addListener((obs, oldValue, newValue) -> {
        	labelit[1].setText(tekstit[1] + String.format("%.2f", newValue));
        });
        sliderit[1].setPrefSize(200, 30);
        
        sliderit[2].valueProperty().addListener((obs, oldValue, newValue) -> {
        	labelit[2].setText(tekstit[2] + String.format("%.2f", newValue));
        });
        sliderit[2].setPrefSize(200, 30);
        
        sliderit[3].valueProperty().addListener((obs, oldValue, newValue) -> {
        	labelit[3].setText(tekstit[3] + String.format("%.2f", newValue));
        });
        sliderit[3].setPrefSize(200, 30);
        
        sliderit[4].valueProperty().addListener((obs, oldValue, newValue) -> {
        	labelit[4].setText(tekstit[4] + String.format("%.2f", newValue));
        });
        sliderit[4].setPrefSize(200, 30);
        
        for(int i = 0;i<sliderit.length;i++) {
        	boxit[i] = new VBox();
        	boxit[i].setSpacing(0);
        	boxit[i].getChildren().addAll(labelit[i], sliderit[i]);
        }	 
        vasenpalkki.getChildren().addAll( aika, viive, 
        		kaynnistaButton, saato, boxit[0], boxit[1], boxit[2],boxit[3],boxit[4]);
        vasenpalkki.setAlignment(Pos.CENTER);
        return vasenpalkki;
	}
	
	/**
	 * Rakennetaan käyttöliittymän alapalkki.
	 * @return palauttaa alapalkin.
	 */
	private HBox alapalkki() {
		alapalkki = new HBox();
		alapalkki.setPadding(new Insets(10,10,10,10));
		alapalkki.setSpacing(10);
		alapalkki.setStyle("-fx-background-color: #dbd8ce;");
		
		//-------------------
		alapalkkiGrid = new GridPane();
		alapalkkiGrid.setHgap(10);
		alapalkkiGrid.setVgap(5);
		alapalkkiGrid.setPadding(new Insets(0, 10, 0, 10));
		
		asiakkaatPP = new Text(); //PP=palvelupiste
		jonoPP = new Text();
		kotiinPP = new Text();
		palvellutPP = new Text();
		kayttoastePP = new Text();
		suoritustehoPP = new Text();
		palveluaikaPP = new Text();
		oleskeluaikaPP = new Text();
		lapimenoaikaPP = new Text();
		jonopituusPP = new Text();
		
		kayttoasteTulos = new Text();
		suoritustehoTulos = new Text();
		palveluaikaTulos = new Text();
		oleskeluaikaTulos = new Text();
		lapimenoaikaTulos = new Text();
		jonopituusTulos = new Text();
		asiakkaatTulos = new Text();
		jonoTulos = new Text();
		kotiinTulos = new Text();
		palvellutTulos = new Text();
		
		asiakkaatM = new Text();//M = Muutos (prosentteina)
		jonoM = new Text();
		kotiinM = new Text();
		palvellutM = new Text();
		kayttoasteM = new Text(); 
		suoritustehoM = new Text();
		palveluaikaM = new Text();
		oleskeluaikaM = new Text();
		lapimenoaikaM = new Text();
		jonopituusM = new Text();
		
		
		
		//Dropdown
		alapalkki.setVisible(false);
		
		palvelupisteet = FXCollections.observableArrayList();

		comboBox = new ComboBox<String>();

		//alapalkkiGrid.add(comboBox, 1, 1);
		alapalkkiGrid.add(asiakkaatPP, 2, 1);
		alapalkkiGrid.add(jonoPP, 2, 2);
		alapalkkiGrid.add(kotiinPP, 2, 3);
		alapalkkiGrid.add(palvellutPP, 2, 4);
		
		alapalkkiGrid.add(asiakkaatTulos, 3, 1);
		alapalkkiGrid.add(jonoTulos, 3, 2);
		alapalkkiGrid.add(kotiinTulos, 3, 3);
		alapalkkiGrid.add(palvellutTulos, 3, 4);
		
		alapalkkiGrid.add(asiakkaatM, 4, 1);
		alapalkkiGrid.add(jonoM, 4, 2);
		alapalkkiGrid.add(kotiinM, 4, 3);
		alapalkkiGrid.add(palvellutM, 4, 4);
		
		
		alapalkkiGrid.add(kayttoastePP, 5, 1);
		alapalkkiGrid.add(suoritustehoPP, 5, 2);
		alapalkkiGrid.add(palveluaikaPP, 5, 3);
		alapalkkiGrid.add(oleskeluaikaPP, 5, 4);
		alapalkkiGrid.add(lapimenoaikaPP, 5, 5);
		alapalkkiGrid.add(jonopituusPP, 5, 6);
		
		alapalkkiGrid.add(kayttoasteTulos, 6, 1);
		alapalkkiGrid.add(suoritustehoTulos, 6, 2);
		alapalkkiGrid.add(palveluaikaTulos, 6, 3);
		alapalkkiGrid.add(oleskeluaikaTulos, 6, 4);
		alapalkkiGrid.add(lapimenoaikaTulos, 6, 5);
		alapalkkiGrid.add(jonopituusTulos, 6, 6);
		
		alapalkkiGrid.add(kayttoasteM, 7, 1);
		alapalkkiGrid.add(suoritustehoM, 7, 2);
		alapalkkiGrid.add(palveluaikaM, 7, 3);
		alapalkkiGrid.add(oleskeluaikaM, 7, 4);
		alapalkkiGrid.add(lapimenoaikaM, 7, 5);
		alapalkkiGrid.add(jonopituusM, 7, 6);

		
		VBox dropDownBox = new VBox();
		dropDownBox.setSpacing(10);
		Text valitse = new Text("Valitse palvelupiste:");
		historiaButton = new Button("Historia");
		historiaButton.setOnAction(e -> naytaHistoria());
		historiaButton.setVisible(false);
		
		dropDownBox.getChildren().addAll(valitse, comboBox, historiaButton);
		
		alapalkki.getChildren().addAll(dropDownBox, alapalkkiGrid);
		return alapalkki;
	}
	
	/**
	 * Rakennetaan käyttöliittymän oikea palkki.
	 * @return Palautetaan palkki.
	 */
	private FlowPane oikeapalkki() {
		FlowPane oikeapalkki = new FlowPane();
        oikeapalkki.setPadding(new Insets(10,10,10,10));
        oikeapalkki.setVgap(10);
        oikeapalkki.setHgap(10);
        oikeapalkki.setPrefWrapLength(170);
        oikeapalkki.setStyle("-fx-background-color: #dbd8ce;");
        
        haeAsiakkaatButton = new Button();
        haeAsiakkaatButton.setText("Hae asiakkaat");
        haeAsiakkaatButton.setOnAction(e -> lisaaAsiakaatListaan());
        
        asiakasList.setPrefSize(210, 190);
        asiakkaat = FXCollections.observableArrayList();
        
        haeHoitoButton = new Button();
        haeHoitoButton.setText("Hae hoito");
        haeHoitoButton.setOnAction(e -> haeHoito());
        
		hoitokertomusList.setPrefSize(210,190);
		hoitokertomus = FXCollections.observableArrayList();
		
		
		VBox asiakasBox = new VBox();
		VBox hoitokertomusBox = new VBox();
		asiakasBox.getChildren().addAll(new Label("Valitse asiakas"), asiakasList);
		hoitokertomusBox.getChildren().addAll(new Label("Asiakkaan hoitokertomus"), hoitokertomusList);
		oikeapalkki.getChildren().addAll(haeAsiakkaatButton, asiakasBox, haeHoitoButton, hoitokertomusBox);
		return oikeapalkki;
	}	

	/**
	 * "Historia" nappia painamalla saadaan tietokannasta valitun palvelupisteen kaikki tilastot excelmäiseen taulukkoikkunaan.
	 */
	private void naytaHistoria() {
		try {
			setVaroitus("");
			Stage stage = new Stage();
			TableView<double[]> table = new TableView();
			table.getColumns().clear();
		    table.getItems().clear();
			String[] kolumnit = {"ID", "Asiakkaiden lkm", "Jono lopussa", "Kotiin", "Palvellut", 
					"Käyttöaste","Suoritusteho","Palveluaika","Oleskeluaika","Läpimenoaika","Jono keskim.",
					"saaNopeus", "ilmNopeus", "arvNopeus", "laaNopeus", "rtglabNopeus"};
			int palvelupiste = comboBox.getSelectionModel().getSelectedIndex();
			double[][] tilastot = kontrolleri.kaikkiTilastot(palvelupiste);
		    int numRows = tilastot.length ;
		    if (numRows == 0) return ;
		    int numCols = tilastot[0].length ;
		    for (int i = 0 ; i < numCols ; i++) {
		        TableColumn<double[], Number> column = new TableColumn<>(kolumnit[i]);
		        final int columnIndex = i ;
		        column.setCellValueFactory(cellData -> {
		            double[] row = cellData.getValue();
		            return new SimpleDoubleProperty(row[columnIndex]);
		        });
		        table.getColumns().add(column);
		    }
		    for (int i = 0 ; i < numRows ; i++) {

		        table.getItems().add(tilastot[i]);
		    }
			VBox box = new VBox(table);
			Scene scene = new Scene(box);
			stage.setTitle("Historia: " + kontrolleri.getPalvelupiste(palvelupiste).getPalvelupiste());
			stage.setScene(scene);
			stage.show();
		}catch (Exception e) {
			varoitus.setText("Valitse palvelupiste ensin!");
		}
		
	}
	

	/**
	 * Kun asiakas on valittu, voidaan kyseisen asiakkaan hoitokertomus saada selville nappia painamalla.
	 */
	private void haeHoito() {
		hoitokertomus.clear();
		String[] hoito;
		try {
			for(int i = 0;i<kontrolleri.getHoito().length;i++) {
				hoito = kontrolleri.getHoito();
				hoitokertomus.add(hoito[i]);
			}
			hoitokertomusList.setItems(hoitokertomus);
		} catch(Exception e) {
			setVaroitus("Valitse asiakas tai käynnistä simulaatio ensin!");
		}
	}
	
	@Override
	public void setVaroitus(String teksti) {
		varoitus.setText(teksti);
	}
	
	/**
	 * Lisätään asiakkaat järjestyksessä listalle (oikealle), jonka kautta voidaan tarkistaa hoitokertomukset.
	 */
	private void lisaaAsiakaatListaan() {
		asiakkaat.clear();
		String[] asiakkaita;
		try {
			for(int i = 0;i<kontrolleri.getAsiakkaat().length;i++) {
				asiakkaita = kontrolleri.getAsiakkaat();
				asiakkaat.add(asiakkaita[i]);
			}
			asiakasList.setItems(asiakkaat);
		}catch(Exception e) {
			setVaroitus("Käynnistä simulaation ensin!");
		}
	}
	
	@Override
	public int getAsiakasIndeksi() {
		return asiakasList.getSelectionModel().getSelectedIndex();
	}

	@Override
	public double getAika(){
		return Double.parseDouble(aika.getText());
	}

	@Override
	public long getViive(){
		return Long.parseLong(viive.getText());
	}


	@Override
	public Visualisointi getVisualisointi() {
		 return naytto;
	}
	
	@Override
	public void setasiakkaatLkm(int lkm) {
		asiakkaatLkm.setText("Asiakkaiden lukumäärä: " + lkm);
	}
	
	@Override
	public double getSaapumisNopeus() {
		return sliderit[0].getValue();
	}
	
	@Override
	public double getIlmoittautumisNopeus() {
		return sliderit[1].getValue();
	}

	@Override
	public double getArviointiNopeus() {
		return sliderit[2].getValue();
	}
	
	@Override
	public double getLaakariNopeus() {
		return sliderit[3].getValue();
	}
	
	@Override
	public double getRtgLabNopeus() {
		return sliderit[4].getValue();
	}
	
	@Override
	public boolean dropDownTrue() {
		return alapalkki.isVisible();
	}
	
	/**
	 * Taulukoidaan useat elementit, jotta ne voidaan looppina toteuttaa.
	 */
	@Override
	public void lisaaDropDown() {
		alapalkki.setVisible(true);
		comboBox.getItems().clear();
		comboBox.getSelectionModel().clearSelection();
		String[] palvelupisteita;
		palvelupisteita = kontrolleri.getPalvelupisteet();
		for(int i = 0;i<kontrolleri.getPalvelupisteet().length;i++) {
			comboBox.getItems().add(palvelupisteita[i]);
		}
		comboBox.valueProperty().addListener((obs,oldV,newV) -> {
			historiaButton.setVisible(true);
			double[] suureita = kontrolleri.getPalvelupisteetSuureet(comboBox.getSelectionModel().getSelectedIndex());
			double[] prosenttiM = kontrolleri.prosenttiOsuus(comboBox.getSelectionModel().getSelectedIndex());
			
			Text[] suureet = {asiakkaatPP, jonoPP, kotiinPP, palvellutPP, kayttoastePP, 
					suoritustehoPP, palveluaikaPP, oleskeluaikaPP, lapimenoaikaPP, jonopituusPP};
			
			String[] teksti = {"Asiakkaiden lkm palvelupisteessä: ",
					"Jonossa: ", "Kotiin lähteneet: ", "Palvellut asiakkaat: ",
					"Aktiiviaika: ", "Suoritusteho: ", "Keskimääräinen palveluaika: ",
					"Kokonaisoleskeluaika: ", "Keskimääräinen läpimenoaika: ",
					"Keskimääräinen jononpituus: "};
			
			Text[] tulokset = {asiakkaatTulos, jonoTulos, kotiinTulos, palvellutTulos, kayttoasteTulos, 
					suoritustehoTulos, palveluaikaTulos, oleskeluaikaTulos, lapimenoaikaTulos, jonopituusTulos};
			
			
			Text[] muutos = {asiakkaatM, jonoM, kotiinM, palvellutM, kayttoasteM, suoritustehoM, palveluaikaM, oleskeluaikaM, lapimenoaikaM, jonopituusM};
			
			for(int i = 0;i<teksti.length;i++) {
				suureet[i].setText(teksti[i]);
				tulokset[i].setText(String.format("%.2f", suureita[i]));
				muutos[i].setText(String.format("%.2f", prosenttiM[i]) + "%");
				if(prosenttiM[i]>0) {
					muutos[i].setFill(Color.GREEN);
				} else {
					muutos[i].setFill(Color.RED);
				}
			}
			
		});
	}
	
	@Override
	public void setSimulaattorinKello(double aika) {
		kello.setText("Simulaattorin kello: " + formatter.format(aika));
	}
	
	
	
	
	// JavaFX-sovelluksen (käyttöliittymän) käynnistäminen
	public static void main(String[] args) {
		launch(args);
	}


	
}
