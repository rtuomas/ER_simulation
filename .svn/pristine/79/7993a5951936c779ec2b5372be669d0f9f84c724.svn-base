package simu;

/**
 * Kontrolleri käyttää tämän rajapinnan metodeja keskustellakseen moottorin kanssa.
 * @author Tuomas Rajala
 * @since 12.10.2020
 */
public interface MoottoriIf {
	
	/**
	 * Kertoo simulaatiolle sen suorittaman ajan.
	 * @param aika Kellonaika, jolloin simulaatio päättyy.
	 */
	public void setSimulointiaika(double aika);
	
	/**
	 * Kertoo simulaatiolle viiveen jokaisen tapahtumakokonaisuuden väliin.
	 * @param aika viiveen aika.
	 */
	public void setViive(long aika);
	
	/**
	 * Palauttaa annetun/muutetun viiveen.
	 * @return viive.
	 */
	public long getViive();
	
	/**
	 * Hakee simulaatioon saapuneiden asiakkaiden kokonaismäärän.
	 * @return lukumäärä, kuinka monta asiakasta on saapunut simulaatioon.
	 */
	public int getAsiakkaat();
	
	/**
	 * Kokoaa simulaatioon saapuneiden asiakkaiden nimet.
	 * @return Palauttaa taulukkona nimet.
	 */
	public String[] getAsiakkaidenNimet();
	
	/**
	 * Hakee ja kokoaa tietyn asiakkaan hoitokertomuksen.
	 * @param asiakasIndeksi Kertoo mistä asiakkaasta on kyse.
	 * @return Palauttaa taulukkona hoitokertomuksen.
	 */
	public String[] getAsiakkaanHoito(int asiakasIndeksi);
	
	/**
	 * Kokoaa taulukkoon palvelupisteiden nimet.
	 * @return Palauttaa taulukkona palvelupisteiden nimet.
	 */
	String[] getPalvelupisteet();
	
	/**
	 * Hakee annetun indeksin mukainen palvelupiste.
	 * @param i indeksi palvelupisteelle.
	 * @return Palauttaa Palvelupiste-luokkana kyseinen palvelupiste.
	 */
	Palvelupiste getPalvelupiste(int i);
	
	/**
	 * Hakee DataAccessObjectista tietyn tilastokokonaisuuden.
	 * @param i Kertoo mistä tilastosta on kyse.
	 * @return Palauttaa taulukkona tilaston datan.
	 */
	double[] getTilasto(int i);
	
	/**
	 * Hakee edellisen tilaston tietystä palvelupisteestä.
	 * @param i Kertoo mistä palvelupisteestä on kyse.
	 * @return Palauttaa taulukkona datan-
	 */
	double[] getEdellinenTilasto(int i);
	
	/**
	 * Laskee kuinka monta prosenttia suurempi tai pienempi on kyseisen palvelupisteen tilastot
	 * verrattuna edelliseen tilastoon.
	 * @param i Kertoo mistä palvelupisteestä on kyse.
	 * @return Palauttaa taulukkona prosenttiosuudet.
	 */
	double[] laskeProsenttiosuus(int i);
	
	/**
	 * Kokoaa ja palauttaa tietyn palvelupisteen suureet.
	 * @param i Kertoo mistä palvelupisteestä on kyse.
	 * @return Palauttaa taulukkona suureet.
	 */
	public double[] getPalvelupisteSuureet(int i);
	
	/**
	 * Hakee tietyn palvelupisteen kaikki tilastot.
	 * @param i KErtoo mistä palvelupisteestä on kyse.
	 * @return Palauttaa taulukkomuodossa kaikki tilastot.
	 */
	double[][] getKaikkiTilastot(int i);
	
	/**
	 * Kokoaa taulukkoon kaikkien palvelupisteiden sen hetkinen jono.
	 * @return Palauttaa taulukkona jonolukumäärän.
	 */
	int[] getPalvelupisteidenJono();
}
