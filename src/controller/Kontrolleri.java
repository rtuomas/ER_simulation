package controller;

import javafx.application.Platform;
import simu.Moottori;
import simu.MoottoriIf;
import simu.Palvelupiste;
import view.*;
/**
 *  Toimii viewin ja modelin välikätenä. Ohjaa ja kommunikoi käyttäjän syöttämiä tietoja ja painalluksia ohjelmistossa.
 *  Luo moottori-säikeen sen rajapinnasta sekä ottaa vastaan käyttöliittymän.
 * @author Tuomas Rajala 6.10.2020
 *
 */
public class Kontrolleri implements KontrolleriIf {

	private MoottoriIf moottori;
	private GuiIf gui;
	double[] suureita;
	
	/**
	 * Luo uuden kontrolleri-olion.
	 * @param gui gui on käyttöliittymä joka otetaan vastaan oliota luotaessa. sitä avulla kommunikoidaan käyttöliittymän kanssa.
	 */
	public Kontrolleri(GuiIf gui) {
		this.gui = gui;
	}
	
	/**
	 * Alustaa käyttöliittymän, luo moottori-olion ja syöttää sille tarvittavat parametrit, käynnistää simulaation.
	 */
	@Override
	public void kaynnistaSimulointi() {
		try {
			gui.setVaroitus(""); //Jos käynnistys onnistuu, poistetaan virheilmoitukset.
			gui.getVisualisointi().alustaAsiakkaat(); //Alustetaan asiakkaan visualisoinnin parametrit 
			
			moottori = new Moottori(this, gui.getSaapumisNopeus(),gui.getIlmoittautumisNopeus(),gui.getArviointiNopeus(),gui.getLaakariNopeus(),gui.getRtgLabNopeus()); // luodaan uusi moottorisäie jokaista simulointia varten
			//Varmistetaan että aika ja viive on oikein kirjoitettu.
			if(gui.getAika()<=0 || gui.getViive()<0) {
				gui.setVaroitus("Anna positiivinen viive/simulaatioaika");
			} else {
				moottori.setSimulointiaika(gui.getAika());
				moottori.setViive(gui.getViive());
				gui.getVisualisointi().tyhjennaNaytto();
				((Thread)moottori).start();
			}
			
			if(!gui.dropDownTrue()) {
				gui.lisaaDropDown();
			}
		}catch(Exception e){
			gui.setVaroitus("Täytä simulointiaika ja viive oikein");
		}
	}
	
	/**
	 * Siirtää asiakkaan visualisointinäytöllä palvelupisteestä toiseen.
	 * @param mista ottaa vastaa palvelupisteen mistä asiakas on lähtenyt.
	 * @param mihin kertoo mihin palvelupisteeseen asiakas on menossa seuraavaksi.
	 */
	@Override
	public void siirraAsiakas(int mista, int mihin) {
		Platform.runLater(()->gui.getVisualisointi().siirraAsiakas(mista,mihin)); 
	}
	
	/**
	 * Kysyy ja palauttaa tietyn palvelupisteen tilastoja (mm. asiakasmäärä sekä tehokkuus).
	 * @param i Tietty palvelupiste on määritelty numeroilla.
	 * @return Palauttaa taulukkona kyseisen palvelupisteen tilastot. 
	 */
	public double[] nykyinenPalvelupiste(int i) {
		return moottori.getTilasto(i);
	}
	
	/**
	 * Kysyy ja palauttaa edellisen palvelupisteen tilastoja.
	 * @param i Tietty palvelupiste on määritelty numeroilla.
	 * @return Palauttaa taulukkona kyseisen palvelupisteen tilastot. 
	 */
	public double[] edellinenPalvelupiste(int i) {
		return moottori.getEdellinenTilasto(i);
	}
	
	/**
	 * Hakee lasketut prosenttisuudet
	 * @param i palvelupisteen indeksi
	 * @return palauttaa prosenttiosuuden double-taulukossa.
	 */
	@Override
	public double[] prosenttiOsuus(int i) {
		return moottori.laskeProsenttiosuus(i);
	}
	
	/**
	 * Hakee tietyn palvelupisteen kaikki saatavilla olevat tilastot.
	 * @param i määrää mistä palvelupisteestä tilastot haetaan
	 * @return palauttaa taulukossa tilastot.
	 */
	@Override
	public double[][] kaikkiTilastot(int i){
		return moottori.getKaikkiTilastot(i);
	}
	
	/**
	 * Hidastaa simulaation kulkua (suurentaa viivettä).
	 */
	@Override
	public void hidasta() { // hidastetaan moottorisäiettä
		try {
			moottori.setViive((long)(moottori.getViive()*1.10));
		}catch(Exception e) {
			gui.setVaroitus("Käynnistä simulaatio ensin!");
		}
	}
	
	/**
	 * Nopeuttaa simulaation kulkua (alentaa viivettä).
	 */
	@Override
	public void nopeuta() { // nopeutetaan moottorisäiettä
		try {
			moottori.setViive((long)(moottori.getViive()*0.9));
		}catch(Exception e) {
			gui.setVaroitus("Käynnistä simulaatio ensin!");
		}	
	}
	
	/**
	 * Hakee palvelupisteen indeksin perusteella.
	 * @param i indeksi, kertoo mikä palvelupiste haetaan.
	 * @return palauttaa haetun palvelupisteen.
	 */
	@Override
	public Palvelupiste getPalvelupiste(int i) {
		return moottori.getPalvelupiste(i);
	}
	
	/**
	 * Hakee palvelupisteiden nimet moottorista.
	 * @return palvelupisteiden nimet taulukossa.
	 */
	@Override
	public String[] getPalvelupisteet() {
		return moottori.getPalvelupisteet();
	}
	
	/**
	 * Hakee tietyn palvelupisteen tilastoja, joihin lukeutuu mm. asiakkaiden määrä.
	 * @param i indeksi kertoo määrää mistä palvelupisteestä tieto haetaan.
	 * @return palauttaa taulukossa suureita.
	 */
	@Override
	public double[] getPalvelupisteetSuureet(int i) {
		return moottori.getPalvelupisteSuureet(i);
	}
	
	/**
	 * Hakee moottorista kaikkien asiakkaiden nimet.
	 * @return asiakkaiden nimet taulukossa.
	 */
	@Override
	public String[] getAsiakkaat() {
		return moottori.getAsiakkaidenNimet();
	}
	
	/**
	 * Hakee tietyn asiakkaan hoitokertomuksen.
	 * @return hoitokertomus taulukossa.
	 */
	@Override
	public String[] getHoito() {
		return moottori.getAsiakkaanHoito(gui.getAsiakasIndeksi());
	}
	
	/**
	 * Luo asiakkaan tiettyyn palvelupisteeseen visualisoinnissa.
	 * @param mihin Mihin palvelupisteeseen asiakas luodaan.
	 */
	@Override
	public void luoAsiakas(int mihin) {
		Platform.runLater(()->gui.getVisualisointi().luoAsiakas(mihin)); 
	}
	
	/**
	 * Poistaa visualisoinnissa asiakkaan tietystä palvelupisteestä.
	 * @param mista Mistä palvelupisteestä asiakas poistetaan.
	 */
	@Override
	public void poistaAsiakas(int mista) {
		Platform.runLater(()->gui.getVisualisointi().poistaAsiakas(mista)); 
	}

	/**
	 * Päivittää simulaatioon tulleiden asiakkaiden kokonaismäärän.
	 * Päivittää jonomäärän visualisointiin.
	 * @param asiakkaat asiakkaiden määrä.
	 */
	@Override
	public void asiakkaatLkm(int asiakkaat) {
		Platform.runLater(()->this.gui.setasiakkaatLkm(asiakkaat));
		Platform.runLater(()->gui.getVisualisointi().
				paivitaJonoMaara(moottori.getPalvelupisteidenJono()));
	}
	
	/**
	 * Päivittää visualisointiin palvelupisteiden jonomäärä.
	 */
	@Override
	public void asiakkaatLkmNaytto() {
		Platform.runLater(()->gui.getVisualisointi().
				paivitaJonoMaara(moottori.getPalvelupisteidenJono()));
	}
	
	/**
	 * Päivittää kellonajan simulaation ollessa käynnissä
	 * @param aika kellonaika simulaatiossa.
	 */
	@Override
	public void naytaNykyinenAika(double aika) {
		Platform.runLater(()->gui.setSimulaattorinKello(aika));
	}

}
