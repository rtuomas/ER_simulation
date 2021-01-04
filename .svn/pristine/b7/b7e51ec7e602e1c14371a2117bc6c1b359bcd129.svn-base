package simu;

import java.util.LinkedList;

import eduni.distributions.ContinuousGenerator;
import eduni.distributions.Normal;

/**
 * Asiakas liikkuu palvelupisteeltä toiselle simulaatiossa. Palvelupisteessä vietetään tietty aika ja sinne kertyy jonoa jos asiakkaita saapuu tiheästi.
 * @author Tuomas Rajala
 * @since 12.10.2020
 */
public class Palvelupiste {

	private Moottori moottori;
	private LinkedList<Asiakas> jono = new LinkedList<Asiakas>();
	private ContinuousGenerator generator;
	private TapahtumanTyyppi skeduloitavanTapahtumanTyyppi; 
	
	private boolean varattu = false;
	private int kotiinLahteneet,saapuneetAsiakkaat,
		palvellutAsiakkaat, id, jatkaaOsastolle;
	
	private double palveluAlkaa;
	private String palvelupiste;
	private static int ID = 0;
	
	private double aktiiviaika;
	private double oleskeluaika;

	/**
	 * Konstruktorissa alustetaan kyseisen palvelupisteen tarvittavat tiedot.
	 * @param palvelupiste Palvelupisteen nimi.
	 * @param generator Kertoo millä perusteella asiakkaan palveluaika määräytyy.
	 * @param moottori Palvelupisteen luoneen luokan tuonti, jotta sen kanssa pystytään kommunikoimaan.
	 * @param tyyppi Kertoo mihin palvelupisteeseen asiakas seuraavaksi viedään (oletuksena, vaihdetaan tarvittaessa).
	 */
	public Palvelupiste(String palvelupiste, ContinuousGenerator generator, Moottori moottori, TapahtumanTyyppi tyyppi){
		this.moottori = moottori;
		this.generator = generator;
		this.skeduloitavanTapahtumanTyyppi = tyyppi;
		this.kotiinLahteneet = 0;
		this.saapuneetAsiakkaat = 0;
		this.palvellutAsiakkaat = 0;
		this.oleskeluaika = 0;
		this.id = ID++;
		this.palvelupiste = palvelupiste;
				
	}

	/**
	 * Palauttaa palvelupisteen nimen.
	 * @return palvelupisteen nimi.
	 */
	public String getPalvelupiste() {
		return palvelupiste;
	}

	/**
	 * palauttaa palvelupisteen id:n, joka on kaikille eri.
	 * @return palvelupisteen yksilöivä id.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Palauttaa palvelupisteestä kotiin lähteneiden määrän.
	 * @return kotiinlähteneiden määrä.
	 */
	public int getKotiinLahteneet() {
		return kotiinLahteneet;
	}
	
	/**
	 * Kertoo palveltujen asiakkaiden määrän.
	 * @return palauttaa palveltujen asiakkaiden määrän
	 */
	public int getPalvellutAsiakkaat() {
		return this.palvellutAsiakkaat;
	}
	
	/**
	 * Kertoo kuinka pitkän ajan palvelupiste on ollut käytössä
	 * @return aktiiviajan.
	 */
	public double getPalveluaika() {
		return this.aktiiviaika;
	}
	
	/**
	 * Kertoo kuinka kauan palvelupisteessä on vietetty yhteensä.
	 * @return palauttaa oleskeluajan.
	 */
	public double getOleskeluaika() {
		return this.oleskeluaika;
	}
	
	/**
	 * Palauttaa palvelupisteen jonon.
	 * @return palvelupisteen ajantasainen jono.
	 */
	public LinkedList<Asiakas> getJono() {
		return jono;
	}
	
	/**
	 * Lisätään asiakas palvelupisteen jonoon ja kerrotaan asiakkaalle tuloaika.
	 * @param a Kertoo mikä asiakas lisätään jonoon.
	 */
	public void lisaaJonoon(Asiakas a){   // Jonon 1. asiakas aina palvelussa
		jono.add(a);
		a.setOleskeluAlkaa(Kello.getInstance().getAika()); //tietyn asiakkaan oleskeluajan mittaukseen
		this.saapuneetAsiakkaat++;
	}

	/**
	 * Metodi poistaa jonon ensimmäisen asiakkaan (palvelussa olleen).
	 * @return palauttaa asiakkaan joka poistetaan jonosta.
	 */
	public Asiakas otaJonosta(){  // Poistetaan palvelussa ollut
		varattu = false; //Vapautetaan palvelupiste seuraavalle asiakkaalle.
		
		//Lähteekö potilas kotiin?
		//Joka viides lähtee kotiin arvioiden jälkeen. 
		//ILM, RTG, ja LAB ei lähde kukaan kotiin.
		double kotiin = Math.random()*5;
		if(kotiin<1 && id!=0 && id!=3 && id!=4) {    
			kotiinLahteneet++;
			jono.peek().setPoistumisaika(Kello.getInstance().getAika());
			jono.peek().setJatkaaTutkimuksessa(false); //Potilas lähtee kotiin
		}
		
		palvellutAsiakkaat++; //lisätään palveltujen henkilöiden määräö yhdellä.
		aktiiviaika += Kello.getInstance().getAika() - palveluAlkaa; //lasketaan aktiiviaika pp:lle
		jono.peek().setOleskeluPaattyy(Kello.getInstance().getAika()); //tietyn asiakkaan oleskeluajan mittaukseen
		oleskeluaika += jono.peek().getOleskeluPaattyy() - jono.peek().getOleskeluAlkaa();  //Lasketaan asiakkaiden kokonaisaikaa palvelupisteessä
		
		return jono.poll();
	}

	/**
	 * Aloitetaan palvelu seuraavalle asiakkaalle (jonon ensimmäiselle). Arvotaan palveluaika ja seuraava palvelupiste.
	 */
	public void aloitaPalvelu(){  //Aloitetaan uusi palvelu, asiakas on jonossa palvelun aikana
		double kello = Kello.getInstance().getAika();
		varattu = true;
		double palveluaika = generator.sample();  //palveluaika määritetään tässä
		
		palveluAlkaa = Kello.getInstance().getAika();  //Välimuuttuja aktiiviajan mittaamiseen
		
		//Arvotaan meneekö asiakas röntgeniin, labraan, molempiin vai ei kumpaankaan
		//0==molemmat, 1==rontgen, 2==labra, >2-ei kumpikaan
		int tutkimus = (int) (Math.random()*5); 
		switch(id) {
			//id == 2 == Lääkäri
			case 2:
				
				if(tutkimus==0 && !jono.peek().isRontgen() && !jono.peek().isLabra()) {
					jono.peek().setJatkaaToiseenTutkimukseen(true);
					if(moottori.getPalvelupiste(3).getJono().size() < moottori.getPalvelupiste(4).getJono().size()) {
						moottori.uusiTapahtuma(new Tapahtuma(TapahtumanTyyppi.RTG,kello+palveluaika));
						
					} else {
						moottori.uusiTapahtuma(new Tapahtuma(TapahtumanTyyppi.LAB,kello+palveluaika));
					}
				} else if(tutkimus==1 && !jono.peek().isRontgen()) {
					moottori.uusiTapahtuma(new Tapahtuma(TapahtumanTyyppi.RTG,kello+palveluaika));
				} else if(tutkimus==2 && !jono.peek().isLabra()) {
					moottori.uusiTapahtuma(new Tapahtuma(TapahtumanTyyppi.LAB,kello+palveluaika));
				} else {
					moottori.uusiTapahtuma(new Tapahtuma(skeduloitavanTapahtumanTyyppi,kello+palveluaika));
					
				}
				break;
				
			//id == 3 == Röntgen
			case 3:
				
				if(jono.peek().isJatkaaToiseenTutkimukseen() && !jono.peek().isLabra()) {
					moottori.uusiTapahtuma(new Tapahtuma(TapahtumanTyyppi.LAB2,kello+palveluaika));
				} else {
					moottori.uusiTapahtuma(new Tapahtuma(TapahtumanTyyppi.LAA2,kello+palveluaika));
				}
				jono.peek().setRontgen(true);
				break;
			
			//id == 4 == Labra
			case 4:
				
				if(jono.peek().isJatkaaToiseenTutkimukseen() && !jono.peek().isRontgen()) {
					moottori.uusiTapahtuma(new Tapahtuma(TapahtumanTyyppi.RTG2,kello+palveluaika));
				} else {
					moottori.uusiTapahtuma(new Tapahtuma(TapahtumanTyyppi.LAA3,kello+palveluaika));
				}
				jono.peek().setLabra(true);
				break;
				
			default:
				moottori.uusiTapahtuma(new Tapahtuma(skeduloitavanTapahtumanTyyppi,kello+palveluaika));
				
		}
	}

	/**
	 * Kertoo onko kyseinen palvelupiste varattu vai ei (onko asiakas palvelussa vai ei).
	 * @return true/false.
	 */
	public boolean onVarattu(){
		return varattu;
	}

	/**
	 * Onko palvleupisteessä jonoa vai ei.
	 * @return Onko palvelupisteessä jonoa vai ei?
	 */
	public boolean onJonossa(){
		return jono.size() != 0;
	}

	/**
	 * Kertoo palvelupisteeseen saapuneiden asiakkaiden määrän.
	 * @return saapuneiden asiakkaiden määrä.
	 */
	public int getAsiakkaat() {
		return saapuneetAsiakkaat;
	}

	/**
	 * Kertoo kuinka moni asiakas jatkaa osastolle.
	 * @return osastolle menneiden asiakkaiden määrä.
	 */
	public int getJatkaaOsastolle() {
		return jatkaaOsastolle;
	}

	/**
	 * Alustetaan palvelupisteiden id. Tarpeellinen, jos simuloidaan useamman kerran.
	 * @param iD palvelupisteen id josta lähdetään liikkeelle (käytännössä aina 0).
	 */
	public static void setID(int iD) {
		ID=iD;
	}
}
