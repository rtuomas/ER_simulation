package controller;

import simu.Palvelupiste;
/**
 * Tämä rajapinta ja sen metodit tarjotaan käyttöliitymälle, ja mitä Kontrolleri -luokan on toteutettava.
 * @author Tuomas Rajala
 * @since 12.10.2020
 */
public interface KontrolleriIf {
	
	/**
	 * Käynnistää simulaation
	 */
	public void kaynnistaSimulointi();
	
	/**
	 * Nopeuttaa simulaation kulkua (alentaa viivettä).
	 */
	public void nopeuta();
	
	/**
	 * Hidastaa simulaation kulkua (suurentaa viivettä).
	 */
	public void hidasta();
	
	/**
	 * Hakee moottorista kaikkien asiakkaiden nimet.
	 * @return asiakkaiden nimet taulukossa.
	 */
	public String[] getAsiakkaat();
	
	/**
	 * Hakee tietyn asiakkaan hoitokertomuksen.
	 * @return hoitokertomus taulukossa.
	 */
	String[] getHoito();
	
	/**
	 * Hakee palvelupisteiden nimet moottorista.
	 * @return palvelupisteiden nimet taulukossa.
	 */
	String[] getPalvelupisteet();
	
	/**
	 * Hakee palvelupisteen indeksin perusteella.
	 * @param i indeksi, kertoo mikä palvelupiste haetaan.
	 * @return palauttaa haetun palvelupisteen.
	 */
	Palvelupiste getPalvelupiste(int i);
	
	/**
	 * Hakee lasketut prosenttisuudet
	 * @param i palvelupisteen indeksi
	 * @return palauttaa prosenttiosuuden double-taulukossa.
	 */
	double[] prosenttiOsuus(int i);
	
	/**
	 * Hakee tietyn palvelupisteen tilastoja, joihin lukeutuu mm. asiakkaiden määrä.
	 * @param i indeksi kertoo määrää mistä palvelupisteestä tieto haetaan.
	 * @return palauttaa taulukossa suureita.
	 */
	double[] getPalvelupisteetSuureet(int i);
	
	/**
	 * Hakee tietyn palvelupisteen kaikki saatavilla olevat tilastot.
	 * @param i määrää mistä palvelupisteestä tilastot haetaan
	 * @return palauttaa taulukossa tilastot.
	 */
	double[][] kaikkiTilastot(int i);
	
	/**
	 * Päivittää simulaatioon tulleiden asiakkaiden kokonaismäärän.
	 * Päivittää jonomäärän visualisointiin.
	 * @param asiakkaat asiakkaiden määrä.
	 */
	public void asiakkaatLkm(int asiakkaat);
	
	/**
	 * Päivittää kellonajan simulaation ollessa käynnissä
	 * @param aika kellonaika simulaatiossa.
	 */
	void naytaNykyinenAika(double aika);
	
	/**
	 * Siirtää asiakkaan visualisoinnissa palvelupisteeltä toiselle.
	 * @param mista Mistä palvelupisteestä asiakas poistetaan.
	 * @param mihin Mihin palvelupisteeseen asiakas luodaan.
	 */
	void siirraAsiakas(int mista, int mihin);
	
	/**
	 * Luo asiakkaan tiettyyn palvelupisteeseen visualisoinnissa.
	 * @param mihin Mihin palvelupisteeseen asiakas luodaan.
	 */
	public void luoAsiakas(int mihin);
	
	/**
	 * Päivittää visualisointiin palvelupisteiden jonomäärä.
	 */
	void asiakkaatLkmNaytto();
	
	/**
	 * Poistaa visualisoinnissa asiakkaan tietystä palvelupisteestä.
	 * @param mista Mistä palvelupisteestä asiakas poistetaan.
	 */
	void poistaAsiakas(int mista);
}
