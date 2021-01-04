package simu;



import java.text.DecimalFormat;
import java.util.ArrayList;

import controller.KontrolleriIf;
import eduni.distributions.Negexp;
import eduni.distributions.Normal;

/**
 * Toimii simulaation ytimenä. Ohjaa, suorittaa ja kertoo mitä simulaatiossa tapahtuu kunakin hetkenä.
 * Kommunikoi siinä luotavien instanssien sekä sen luoneen kontrollerin kanssa.
 * @author Tuomas Rajala 12.10.2020
 * @version 2.3
 */
public class Moottori extends Thread implements MoottoriIf{
	
	private KontrolleriIf kontrolleri;
	private ITilastotDAO tAO = new TilastotAccessObject(this);
	
	private double simulointiaika = 0;
	private long viive = 0;
	private double saa,ilm,arv,laa,rtglab;
	
	private double[] nykyinenTilasto = new double[10];
	private double[] edellinenTilasto = new double[10];
	private double[] prosenttiOsuus = new double[10];
	String[] tilastot = new String[10];
	double[][] palvelupisteenTilastot = new double[6][15];
	
	private Palvelupiste[] palvelupisteet = new Palvelupiste[6];
	private ArrayList<Asiakas> asiakkaat = new ArrayList();
	private Kello kello;
	private static DecimalFormat df = new DecimalFormat("#.##");

	private Saapumisprosessi saapumisprosessi;
	private Tapahtumalista tapahtumalista;
	
	/**
	 * Moottori luodaan kontrollerista, joka hakee ja syöttää parametrina moottorissa tarvittavat muuttujat.
	 * Luodaan palvelupisteet ja asetetaan parametreina niiden palveluajan nopeus kertoimena.
	 * Luodaan kello ja asetetaan ajaksi nolla (0).
	 * Luodaan saapumisprosessiolio, joka mahdollistaa sen että simulaatioon saapuu tietyin väliajoin uusi asiakas.
	 * @param kontrolleri Mahdollistaa kommunikoinnin sen luoneen kontrollerin kanssa.
	 * @param saa Saapumisnopeus.
	 * @param ilm Ilmoittautumisnopeus.
	 * @param arv Hoidon arvioinnin nopeus.
	 * @param laa Lääkärin nopeus.
	 * @param rtglab Röntgenin ja laboratorion nopeus.
	 */
	public Moottori(KontrolleriIf kontrolleri, double saa, double ilm, double arv, double laa, double rtglab){
		this.saa=saa;
		this.ilm=ilm;
		this.arv=arv;
		this.laa=laa;
		this.rtglab=rtglab;
		this.kontrolleri = kontrolleri;
		Palvelupiste.setID(0); // Täytyy asettaa palvelupisteet alkamaan taas nollasta, muuten ei toimi
		Asiakas.setID(1); //Asetetaan asiakasjärjestys alkamaan ykkösestä.
		
		palvelupisteet[0]=new Palvelupiste("Ilmoittautuminen", new Normal(5/ilm,2), this, TapahtumanTyyppi.ARV);	
		palvelupisteet[1]=new Palvelupiste("Hoidon arviointi", new Normal(5/arv,2), this, TapahtumanTyyppi.LAA);
		palvelupisteet[2]=new Palvelupiste("Lääkäri", new Normal(15/laa,3), this, TapahtumanTyyppi.OSASTO);
		palvelupisteet[3]=new Palvelupiste("Röntgen", new Normal(5/rtglab,3), this, TapahtumanTyyppi.LAA2);
		palvelupisteet[4]=new Palvelupiste("Laboratorio", new Normal(5/rtglab,3), this, TapahtumanTyyppi.LAA3);
		palvelupisteet[5]=new Palvelupiste("Osasto", new Normal(50,10), this, TapahtumanTyyppi.KOTI);

		kello = Kello.getInstance(); //Luodaan kello ensimmäisen kerran.
		kello.setAika(0);
		
		saapumisprosessi = new Saapumisprosessi(this, new Negexp(10/saa,131),TapahtumanTyyppi.ILM);
		tapahtumalista = new Tapahtumalista();	
		saapumisprosessi.generoiSeuraava();
	}

	@Override
	public void setSimulointiaika(double aika) {
		simulointiaika = aika;
	}

	@Override
	public void setViive(long viive) {
		this.viive = viive;
	}
	
	@Override
	public long getViive() {
		return viive;
	}
	
	/**
	 * Moottorin sydän. Tämän periaatteen mukaan simulaatio pyörii ja siellä suoritetaan tietyt välivaiheet järjestyksessä
	 * niin kauan, kuin simulointiaika on alle  sen asetetun ajan.
	 */
	@Override
	public void run(){  // ent. aja
		while (simuloidaan()){
			viive();
			kello.setAika(nykyaika());
			suoritaBTapahtumat();
			yritaCTapahtumat();
			kontrolleri.naytaNykyinenAika(kello.getAika());
			kontrolleri.asiakkaatLkm(palvelupisteet[0].getAsiakkaat());

		}
		//tulokset();
		kokoaJaTallennaTilastot();
	}
	
	/**
	 * B-tapahtumat tarkoittavat tapahtumia, joissa siirretään asiakas palvelupisteeltä toiselle.
	 */
	private void suoritaBTapahtumat(){
		while (tapahtumalista.getSeuraavanAika() == kello.getAika()){
			suoritaTapahtuma(tapahtumalista.poista());
		}
	}
	
	/**
	 * C-tapahtumissa palvelu aloitetaan aina, jos palvelupiste on vapaa ja siellä on asiakkaita jonossa.
	 */
	private void yritaCTapahtumat(){
		for (Palvelupiste p: palvelupisteet){
			if (!p.onVarattu() && p.onJonossa()){
				p.aloitaPalvelu();
			}
		}
	}

	/**
	 * B-tapahtuma, siirretään asiakas palvelun jälkeen seuraavalle palvelupisteelle.
	 * @param t Tapahtuma, jonka ryypin mukaan suoritetaan siirto.
	 */
	private void suoritaTapahtuma(Tapahtuma t){  // Keskitetty algoritmi tapahtumien käsittelyyn ja asiakkaiden siirtymisten hallintaan

		Asiakas a = null;
		switch (t.getTyyppi()){
			
			case ILM: a = new Asiakas(); //Asiakkaan saapuminen järjestelmään.
					  asiakkaat.add(a);
					  palvelupisteet[0].lisaaJonoon(a);
					  a.lisaaHoito("Saapuminen ilmoittautumiseen.");
				      saapumisprosessi.generoiSeuraava();
				      kontrolleri.luoAsiakas(0);
				      break;
				
			case ARV: seuraavaAsiakas(a,0,1);  //Ilmoittautumisesta hoidon arviointiin.
					  break;
				
			case LAA: seuraavaAsiakas(a,1,2);  //Arvioinnista lääkäriin
					  break;  
					  
			case LAA2: seuraavaAsiakas(a,3,2);  //Röntgenistä lääkäriin
					   break;  
			
			case LAA3: seuraavaAsiakas(a,4,2);  //Labrasta lääkäriin
					   break;
					   
			case RTG2: seuraavaAsiakas(a,4,3); //Labrasta röntgeniin
					   break;  
			
			case LAB2: seuraavaAsiakas(a,3,4); //Röntgenistä labraan
					   break;  
				
			case RTG: seuraavaAsiakas(a,2,3); //Lääkäristä röntgeniin.
					  break; 
			
			case LAB: seuraavaAsiakas(a,2,4); //Lääkäristä labraan
					  break; 
				
			case OSASTO: seuraavaAsiakas(a,2,5); //Lääkäristä osastolle
				       	 break;
				    
			case KOTI: a = palvelupisteet[5].otaJonosta(); //Kotiin.
						a.setPoistumisaika(kello.getAika());
						kontrolleri.poistaAsiakas(5); //Poistetaan asiakas osastolta.
						a.raportti(); 
						break;
		}	
	}
	
	/**
	 * Yhteen koottu metodi asiakkaan siirrosta seuraavalle palvelupisteelle.
	 * @param a Asiakas (null).
	 * @param mista Kertoo mistä palvelupisteeltä asiakas on lähdössä.
	 * @param mihin Kertoo mihin palvelupisteeseen asiakas on menossa.
	 */
	private void seuraavaAsiakas(Asiakas a, int mista, int mihin) {
			kontrolleri.poistaAsiakas(mista); //Poistetaan asiakas visualoinnista.
			a = palvelupisteet[mista].otaJonosta();
			a.lisaaHoito("Palvelupisteessä " + palvelupisteet[mista].getPalvelupiste());
			if(a.jatkaaTutkimuksessa()) { //Jos asiakas jatkaa simulaatiossa...
				kontrolleri.luoAsiakas(mihin); //Luodaan visualisointiin asiakas.
			   	palvelupisteet[mihin].lisaaJonoon(a);
			   	a.lisaaHoito(" ---> " + palvelupisteet[mihin].getPalvelupiste());
			} else { //Jos asiakas ei  jatka toiselle palvelupisteelle, niin se poistetaan jonosta eikä sille tehdä mitään.
				a.lisaaHoito(" ---> Kotiin");
			}
	}
	
	/**
	 * Lisää tapahtumalistalle uuden tapahtuma t:n
	 * @param t Tapahtuma, joka lisätään listaan.
	 */
	public void uusiTapahtuma(Tapahtuma t){
		tapahtumalista.lisaa(t);
	}

	/**
	 * Kertoo seuraavan tapahtumalistalla olevan tapahtuman tapahtuma-ajan.
	 * @return Palauttaa tapahtuman kellonajan.
	 */
	private double nykyaika(){
		return tapahtumalista.getSeuraavanAika();
	}
	
	/**
	 * Tarkistetaan joka kierroksen alussa, jatketaanko simulaatiota.
	 * @return Palauttaa true, jos kello on pienempi kuin asetettu simulointiaika, muuten false.
	 */
	private boolean simuloidaan(){
		Trace.out(Trace.Level.INFO, "Kello on: " + kello.getAika());
		return kello.getAika() < simulointiaika;
	}
	
	/**
	 * Kertoo ja pysäyttää threadin asetetun viiveen ajaksi.
	 */
	private void viive() {
		System.out.println ("Viive " + viive);
		try {
			sleep(viive);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Palvelupiste getPalvelupiste(int i) {
		return palvelupisteet[i];
	}
	
	@Override
	public String[] getPalvelupisteet() {
		String[] pp = new String[palvelupisteet.length];
		for(int i = 0;i<pp.length;i++) {
			pp[i] = palvelupisteet[i].getPalvelupiste();
		}
		return pp;
	}
	
	@Override
	public int[] getPalvelupisteidenJono() {
		int[] jonot = new int[palvelupisteet.length];
		for(int i = 0;i<jonot.length;i++) {
			jonot[i] = palvelupisteet[i].getJono().size();
		}
		return jonot;
	}
	
	/**
	 * Hakee tietyn palvelupisteen nimen.
	 * @param i Kertoo mistä palvelupisteestä on kyse.
	 * @return Palauttaa tietyn palvelupisteen nimen.
	 */
	public String getPalvelupisteNimi(int i) {
		return palvelupisteet[i].getPalvelupiste();
	}
	
	@Override
	public int getAsiakkaat() {
		return asiakkaat.size();
	}
	
	@Override
	public String[] getAsiakkaidenNimet() {
		String[] nimet = new String[asiakkaat.size()];
		for(int i = 0;i<asiakkaat.size();i++) {
			nimet[i] = asiakkaat.get(i).getNimi();
		}
		
		return nimet;
				
	}
	
	@Override
	public String[] getAsiakkaanHoito(int i) {
		String[] hoito = new String[asiakkaat.get(i).getHoitokertomus().size()];
		for(int j = 0;j<hoito.length;j++) {
			hoito[j] = asiakkaat.get(i).getHoitokertomus().get(j);
		}
		return hoito;		
	}
	
	/**
	 * Tilastointien hakuun, kokoamiseen, laskemiseen ja tietokantaan syöttämiseen tarkoitettu metodi.
	 * Haetaan yksitellen jokaisen palvelupisteen sinne kerätyt tilastot, lisätään ne taulukkoon ja lähetetään tiedot
	 * DataAccessObjectille, joka tallentaa ne tietokantaan.
	 * Jokaiselle palvelupisteelle on oma taulukkonsa.
	 */
	private void kokoaJaTallennaTilastot() {
		tilastot = new String[10];
		
		//Monet lukuarvot ensin kerrotaan sadalla, jonka jälkeen jaetaan sadalla.
		//Tämä teknisistä syistä, kun halusin pyöristää lukuarvot kahteen desimaaliin.
		for(int i = 0 ;i<6;i++) {
			palvelupisteenTilastot[i][0] = palvelupisteet[i].getAsiakkaat(); //asiakkaat lkm
			palvelupisteenTilastot[i][1] = palvelupisteet[i].getJono().size(); //jonon pituus simulaation päätyttyä
			palvelupisteenTilastot[i][2] = palvelupisteet[i].getKotiinLahteneet(); //Kotiin lähteneet
			palvelupisteenTilastot[i][3] = palvelupisteet[i].getPalvellutAsiakkaat(); //Palvellut asiakkaat
			palvelupisteenTilastot[i][4] = Math.round(palvelupisteet[i].getPalveluaika() / kello.getAika() * 100.0*100.0)/100.0; //Käyttöaste (%)
			palvelupisteenTilastot[i][5] = Math.round(palvelupisteet[i].getPalvellutAsiakkaat()/kello.getAika()*100.0)/100.0; //Suoritusteho
			palvelupisteenTilastot[i][6] = Math.round(palvelupisteet[i].getPalveluaika()/palvelupisteet[i].getPalvellutAsiakkaat()*100.0)/100.0; //Keskimääräinen palveluaika		
			palvelupisteenTilastot[i][7] = Math.round(palvelupisteet[i].getOleskeluaika()*100.0)/100.0; //Kokonaisoleskeluaika		
			palvelupisteenTilastot[i][8] = Math.round(palvelupisteet[i].getOleskeluaika()/palvelupisteet[i].getPalvellutAsiakkaat()*100.0)/100.0; //Keskimääräinen läpimenoaika		
			palvelupisteenTilastot[i][9] = Math.round(palvelupisteet[i].getOleskeluaika()/kello.getAika()*100.0)/100.0; //keskimääräinen jononpituus
			palvelupisteenTilastot[i][10] = Math.round(this.saa*100.0)/100.0; //Saapumisnopeus
			palvelupisteenTilastot[i][11] = Math.round(this.ilm*100.0)/100.0; //Ilmoittautumisnopeus
			palvelupisteenTilastot[i][12] = Math.round(this.arv*100.0)/100.0; //Arvioinnin nopeus
			palvelupisteenTilastot[i][13] = Math.round(this.laa*100.0)/100.0; //Lääkärin nopeus
			palvelupisteenTilastot[i][14] = Math.round(this.rtglab*100.0)/100.0; //Röntgenin ja labran nopeus
			System.out.println("Tilastot tallennettu? " + tAO.createTilasto(palvelupisteenTilastot, i));
		}
	}
	
	@Override
	public double[] getTilasto(int i) {
		return tAO.readTilasto(i);
	}
	
	@Override
	public double[] getEdellinenTilasto(int i) {
		return tAO.readEdellinenTilasto(i);
	}
	
	
	@Override
	public double[] laskeProsenttiosuus(int i) {
		nykyinenTilasto = tAO.readTilasto(i);
		edellinenTilasto = tAO.readEdellinenTilasto(i);
		for(int j = 0;j<nykyinenTilasto.length;j++) {
			prosenttiOsuus[j] = (nykyinenTilasto[j]-edellinenTilasto[j])/edellinenTilasto[j]*100.0;
		}
		return prosenttiOsuus;
	}

	@Override
	public double[] getPalvelupisteSuureet(int i) {
		double[] suureet = new double[10];
		for(int j = 0;j<10;j++) {
			suureet[j] = palvelupisteenTilastot[i][j];
		}
		return suureet;
	}
	
	@Override
	public double[][] getKaikkiTilastot(int i) {
		return tAO.readTilastot(i);
	}
	
	
}