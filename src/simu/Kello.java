package simu;

/**
 * Pitää yllä simulaattorin kellonaikaa, joka on sama jokaisessa luokassa johtuen singleton-tyypistä.
 * @author Tuomas Rajala 12.10.2020
 * @version 1.0
 *
 */
public class Kello {

	private double aika;
	private static Kello instanssi;
	
	/**
	 * Kun kello luodaan, asetetaan aika nollaksi (lähtötilanne).
	 */
	private Kello(){
		aika = 0;
	}
	
	/**
	 * Mahdollistaa sen että kello luodaan vain kerran.
	 * @return Palauttaa kello-olion, joka on kaikkialla sama.
	 */
	public static Kello getInstance(){
		if (instanssi == null){
			instanssi = new Kello();	
		}
		return instanssi;
	}
	
	/**
	 * Asettaa uuden kellonajan (eteenpäin).
	 * @param aika uuse kellonaika.
	 */
	public void setAika(double aika){
		this.aika = aika;
	}

	/**
	 * Palauttaa kyseisen kellon ajan.
	 * @return Kellon aika joka palautetaan.
	 */
	public double getAika(){
		return aika;
	}
}
