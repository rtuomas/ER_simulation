package simu;

import java.util.ArrayList;

/**
 * Luokka esittää simulaattorissa kulkevaa palvelupisteissä käyvää asiakasta.
 * @author Tuomas Rajala 12.10.2020
 * @version 1.0
 *
 */
public class Asiakas {
	private double saapumisaika, poistumisaika, oleskeluAlkaa, oleskeluPaattyy;
	private int id;
	private static int i = 1;
	private static long sum = 0;
	private boolean jatkaaTutkimuksessa, rontgen, labra,
		jatkaaToiseenTutkimukseen;
	private ArrayList<String> hoitokertomus;
	private String nimi;
	
	/**
	 * Konstruktori alustaa uuden asiakkaan.
	 */
	public Asiakas(){
	    id = i++;
	    this.nimi = "Asiakas " + id;
	    this.jatkaaTutkimuksessa = true;
	    this.jatkaaToiseenTutkimukseen = false;
		saapumisaika = Kello.getInstance().getAika();
		Trace.out(Trace.Level.INFO, "Uusi asiakas:" + id + ":"+saapumisaika);
		this.hoitokertomus = new ArrayList();
	}
	
	/**
	 * Pitää yllä asiakkaan hoitokertomusta aina kun siirryttäessä palvelupisteestä toiselle.
	 * @param hoito kertoo kyseisen hoidon tarkoituksen joka lisätään hoitokertomukseen.
	 */
	public void lisaaHoito(String hoito) {
		this.hoitokertomus.add(hoito);
	}
	
	/**
	 * Palauttaa tietyn asiakkaan hoitokertomuksen kokonaisuudessaan.
	 * @return Asiakkaan hoitokertomus
	 */
	public ArrayList<String> getHoitokertomus(){
		return this.hoitokertomus;
	}
	
	/**
	 * Kertoo onko kyseinen asiakas käynyt röntgentutkimuksessa.
	 * @return boolean arvo false/true riippuen siitä onko asiakas käynyt röntgenissä.
	 */
	public boolean isRontgen() {
		return rontgen;
	}
	
	/**
	 * Asettaa boolean arvon rontgenille, jos asiakas on käynyt röntgenissä.
	 * @param rontgen boolean arvo (käytännössä aina true, koska asetetaan vain jos asiakas on käynyt röntgenissä).
	 */
	public void setRontgen(boolean rontgen) {
		this.rontgen = rontgen;
	}
	
	/**
	 * Kertoo onko asiakas käynyt laboratoriossa.
	 * @return boolean arvo true/false riippuen siitä onko asiakas käynyt labrassa. (oletuksena false).
	 */
	public boolean isLabra() {
		return labra;
	}
	
	/**
	 * Asettaa boolean arvon labralle, jos asiakas on käynyt labrassa.
	 * @param labra Käytännössä aina true, koska asetetaan vain jos asiakas on käynyt labrassa. (oletuksena false).
	 */
	public void setLabra(boolean labra) {
		this.labra = labra;
	}
	
	/**
	 * Kertoo jatkaatko asiakas palvelupisteestä toiseen.
	 * @return boolean arvo true/false riippuen siitä jatkaako asiakas palvelupisteestä toiselle.
	 */
	public boolean jatkaaTutkimuksessa() {
		return jatkaaTutkimuksessa;
	}
	
	/**
	 * Määritellään jatkaako asiakas seuraavaan palvelupisteeseen vai ei.
	 * @param tutkimuksessa Käytännössä aina false, koska asetetaan vain jos asiakas lähtee kotiin.
	 */
	public void setJatkaaTutkimuksessa(boolean tutkimuksessa) {
		this.jatkaaTutkimuksessa = tutkimuksessa;
	}

	/**
	 * Antaa kellonajan millon asiakas on poistunut simulaatiosta, eli lähtenyt kotiin.
	 * @return Kellonaika poistumisajalle.
	 */
	public double getPoistumisaika() {
		return poistumisaika;
	}

	/**
	 * Asetetaan asiakkaan poistumisaika simulaatiosta.
	 * @param poistumisaika Kellonaika poistumiselle.
	 */
	public void setPoistumisaika(double poistumisaika) {
		this.poistumisaika = poistumisaika;
	}

	/**
	 * Kertoo asiakkaan saapumisajan simulaatioon (ilmoittautumispisteelle).
	 * @return Kellonaika saapumiselle.
	 */
	public double getSaapumisaika() {
		return saapumisaika;
	}

	/**
	 * Asettaa saapumisajan asiakkaalle.
	 * @param saapumisaika Kellonaika saapumiselle.
	 */
	public void setSaapumisaika(double saapumisaika) {
		this.saapumisaika = saapumisaika;
	}
	
	/**
	 * Antaa asiakkaan raportin kun hän poistuu simulaatiosta.
	 */
	public void raportti(){
		Trace.out(Trace.Level.INFO, "Asiakas "+id+ " saapui:" +saapumisaika);
		Trace.out(Trace.Level.INFO,"Asiakas "+id+ " poistui:" +poistumisaika);
		Trace.out(Trace.Level.INFO,"Asiakas "+id+ " viipyi:" +(poistumisaika-saapumisaika));
		sum += (poistumisaika-saapumisaika);
		double keskiarvo = sum/id;
		System.out.println("Asiakkaiden l�pimenoaikojen keskiarvo "+ keskiarvo);
	}

	/**
	 * Palauttaa palvelupisteeseen tultaessa kyseisen kellonajan.
	 * @return Kellonaika, jolloin asiakas saapuu palvelupisteeseen.
	 */
	public double getOleskeluAlkaa() {
		return oleskeluAlkaa;
	}

	/**
	 * Asettaa kellonajan, jolloin asiakas saapuu palvelupisteen jonoon.
	 * @param oleskeluAlkaa Kellonaika
	 */
	public void setOleskeluAlkaa(double oleskeluAlkaa) {
		this.oleskeluAlkaa = oleskeluAlkaa;
	}

	/**
	 * Palauttaa kellonajan, jolloin palvelupisteesä oloaika on päättynyt (asiakas palveltu).
	 * @return Kellonaika oleskeluajan päätyttyä.
	 */
	public double getOleskeluPaattyy() {
		return oleskeluPaattyy;
	}

	/**
	 * Asettaa ajan jolloin oleskelupäättyy (asiakas on palveltu).
	 * @param oleskeluPaattyy Kellonaika.
	 */
	public void setOleskeluPaattyy(double oleskeluPaattyy) {
		this.oleskeluPaattyy = oleskeluPaattyy;
	}

	/**
	 * Kertoo jatkaako asiakas röntgenistä labraan tai labrasta röntgeniin.
	 * @return true/false riippuen siitä mitä lääkärissä on määrätty.
	 */
	public boolean isJatkaaToiseenTutkimukseen() {
		return jatkaaToiseenTutkimukseen;
	}

	/**
	 * Lääkäri määrää arvon jatkaako asiakas molempiin tutkimuksiin8röntgen ja labra) vai ei.
	 * @param jatkaaToiseenTutkimukseen Arvo jatkaako toiseenkin tutkimukseen.
	 */
	public void setJatkaaToiseenTutkimukseen(boolean jatkaaToiseenTutkimukseen) {
		this.jatkaaToiseenTutkimukseen = jatkaaToiseenTutkimukseen;
	}

	/**
	 * Palauttaa kontruktorissa määritetty asiakkaan nimi.
	 * @return Asiakkaan nimi.
	 */
	public String getNimi() {
		return nimi;
	}

	/**
	 * Asettaa staattisen muuttujan i arvon j:ksi, kun simuloidaan toisen kerran ja halutaan palauttaa arvo alkuperäiseksi.
	 * @param j Käytännössä arvo 1.
	 */
	public static void setID(int j) {
		i=j;
	}
}
