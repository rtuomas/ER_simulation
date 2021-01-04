package simu;
import eduni.distributions.*;

/**
 * Luokan tarkoituksena on luoda simulaatioon uusi asiakas, joka kulkee palvelupisteeltä toiselle.
 * @author Tuomas Rajala
 * @since 12.10.2020
 */
public class Saapumisprosessi {
	private Moottori moottori;
	private ContinuousGenerator generaattori;
	private TapahtumanTyyppi tyyppi;
	
	/**
	 * Konstruktorissa annetaan tarpeelliset tiedot parametreina.
	 * @param m Moottori, jonka kanssa keskustellaan.
	 * @param g Generaattori, jonka perusteella uusia asiakas luodaan tietyn väliajan perusteella.
	 * @param tyyppi Tapahtumantyyppi kertoo mihin palvelupisteeseen asiakas luodaan (ilmoittautumiseen).
	 */
	public Saapumisprosessi(Moottori m, ContinuousGenerator g, TapahtumanTyyppi tyyppi){
		this.moottori = m;
		this.tyyppi = tyyppi;
		this.generaattori = g;
	}
	
	/**
	 * Generoi uuden asiakkaan ilmoittautumispisteeseen.
	 */
	public void generoiSeuraava(){
		Tapahtuma t = new Tapahtuma(tyyppi, Kello.getInstance().getAika()+generaattori.sample());
		moottori.uusiTapahtuma(t);
	}

}
