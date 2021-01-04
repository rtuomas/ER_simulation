package simu;

/**
 * Määrittää, mitä metodeja TilastotAccessObject -luokan on toteutettava.
 * @author Tuomas Rajala
 * @since 12.10.2020
 */
public interface ITilastotDAO {
	
	/**
	 * Sulkee avatun tietokannan
	 */
	public abstract void finalize();
	
	/**
	 * Lukee yhden rivin tietokannasta (viimeisen).
	 * @return Palauttaa taulukkona siinä olevat tiedot.
	 * @param i Kertoo mistä palvelupisteestä (taulukosta) on kyse.
	 */
	public double[] readTilasto(int i);
	
	/**
	 * Luo tietokantaan uuden rivin tilastoille.
	 * @param tilastot taulukkona tietokantaan vietävä data.
	 * @param i Kertoo minkä palvelupisteen datataulukkoon tieto viedään.
	 * @return Palauttaa arvon true jos tallennus onnistui, muuten false.
	 */
	boolean createTilasto(double[][] tilastot, int i);
	
	/**
	 * Kertoo kuinka monta riviä tietyssä taulukossa on tietokannassa.
	 * @param tauluNimi Kertoo mistä taulukosta on kyse.
	 * @return Palauttaa lukuarvon taulukon rivien määrästä.
	 */
	int getRowCount(String tauluNimi);
	
	/**
	 * Lukee tietokannasta tietyn taulukon toiseksi viimeisen rivin tiedot.
	 * @param i Kertoo mistä taulusta on kyse.
	 * @return Palauttaa taulukkona datan.
	 */
	double[] readEdellinenTilasto(int i);
	
	/**
	 * Lukee kaikki rivit tietystä taulukosta.
	 * @param i Kertoo mistä taulukosta on kyse.
	 * @return Palauttaa taulukkona kaikki kyseisen taulukon tiedot.
	 */
	double[][] readTilastot(int i);
}
