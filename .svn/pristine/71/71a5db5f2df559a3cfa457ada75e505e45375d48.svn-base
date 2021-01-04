package simu;

/**
 * Kello toimii tapahtumien perusteella, jotka kootaan tapahtumalistassa, joka sisältää tämän luokan instansseja.
 * @author Tuomas Rajala
 * @since 12.10.2020
 */
public class Tapahtuma implements Comparable<Tapahtuma> {
	
		
	private TapahtumanTyyppi tyyppi;
	private double aika;
	
	/**
	 * Annetaan konstruktorissa tapahtuman tyyppi sekä aika. Luodaan siis uusi tapahtuma, joka viedään tapahtumalistalle.
	 * @param tyyppi Kertoo mikä tapahtuma ajetaan
	 * @param aika Kellonaika taoahtuma-ajalle.
	 */
	public Tapahtuma(TapahtumanTyyppi tyyppi, double aika){
		this.tyyppi = tyyppi;
		this.aika = aika;
	}
	
	/**
	 * Asettaa tapahtuman tyypin eli mikä tapahtuma on kyseessä.
	 * @param tyyppi tapahtumantyyppi.
	 */
	public void setTyyppi(TapahtumanTyyppi tyyppi) {
		this.tyyppi = tyyppi;
	}
	
	/**
	 * Palauttaa tapahtumantyypin.
	 * @return tapahtumantyyppi.
	 */
	public TapahtumanTyyppi getTyyppi() {
		return tyyppi;
	}
	
	/**
	 * Asettaa tapahtumalle ajan.
	 * @param aika Kellonaika tapahtumalle.
	 */
	public void setAika(double aika) {
		this.aika = aika;
	}
	
	/**
	 * Antaa tapahtuman kellonajan.
	 * @return Kellonaika tapahtumalle.
	 */
	public double getAika() {
		return aika;
	}

	/**
	 * Tapahtumalista järjestetään tämän metodin perusteella. Tässä tapauksessa kellonajan perusteella.
	 */
	@Override
	public int compareTo(Tapahtuma arg) {
		if (this.aika < arg.aika) return -1;
		else if (this.aika > arg.aika) return 1;
		return 0;
	}
}
