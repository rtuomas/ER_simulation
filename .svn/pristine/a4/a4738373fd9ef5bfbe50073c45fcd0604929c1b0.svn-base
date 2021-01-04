package view;

/**
 * Rajapinta Mainille. Kertoo mitä metodeja sen on toteutettava ja mitä kontrolleri voi käyttää.
 * @author Tuomas Rajala
 * @since 12.10.2020
 */
public interface GuiIf {
	
	/**
	 * Hakee ja palauttaa simulaation ajan.
	 * @return Aika simulaatiossa.
	 */
	public double getAika();
	
	/**
	 * Hakee ja palauttaa annetun viiveen.
	 * @return viive simulaatiossa.
	 */
	public long getViive();
	
	/**
	 * Antaa tieton visualisointi-ikkunasta, jolla visualisoidaan simulaatiota.
	 * @return Visualisoinnin luokan.
	 */
	public Visualisointi getVisualisointi();
	
	/**
	 * Päivittää simulaatioon tulleiden asiakkaiden lukumäärän.
	 * @param lkm lukumäärä asiakkaista.
	 */
	void setasiakkaatLkm(int lkm);
	
	/**
	 * Hakee valitun asiakkaan indeksin (tableviewissä).
	 * @return Palauttaa valitun asiakkaan indeksin listalta.
	 */
	int getAsiakasIndeksi();
	
	/**
	 * Hakee lääkäripalveluun asetetun nopeuden kertoimen.
	 * @return Palauttaa kertoimen lääkärin palvelun nopeudesta.
	 */
	double getLaakariNopeus();
	
	/**
	 * Hakee simulaatioon saapuneiden asiakkaiden saapumisnopeuden (asetetaan alussa).
	 * @return Kerroin saapumisnopeudesta.
	 */
	double getSaapumisNopeus();
	
	/**
	 * Kun simulaatio on käynnistynyt, asetetaan näkyviin dropdown valikko, jonka kautta
	 * tietyn palvelupisteen tilastot saadaan esille.
	 */
	void lisaaDropDown();
	
	/**
	 * Kertoo onko dropdown valikko näkyvissä vai ei.
	 * @return true/false riippuen siitä onko dropdown näkyvissä.
	 */
	boolean dropDownTrue();
	
	/**
	 * Päivitetään ajon aikana, kertoo simulaation kellonajan.
	 * @param aika Kellonaika simulaatiossa.
	 */
	void setSimulaattorinKello(double aika);
	
	/**
	 * Hakee ja kertoo ilmoittautumispisteen nopeuden kertoimen.
	 * @return Palauttaa kertoimen ilmoittautumisnopeusta.
	 */
	double getIlmoittautumisNopeus();
	
	/**
	 * Hakee ja palauttaa röntgenin ja labran palvelupisteiden palvelunopeuden kertoimen.
	 * @return Palauttaa palvelupisteiden kertoimen.
	 */
	double getRtgLabNopeus();
	
	/**
	 * Hakee ja palauttaa arvioinnin palvelupisteen palvelun nopeuden kertoimen.
	 * @return Palauttaa kertoimen palvelunopeudesta.
	 */
	public double getArviointiNopeus();
	
	/**
	 * Asettaa varoitustekstin, jos yritetään painaa jotain nappia jota ei voida
	 * painaa ennen tiettyjä rutiineja.
	 * @param teksti varoitusteksti.
	 */
	void setVaroitus(String teksti);
}
