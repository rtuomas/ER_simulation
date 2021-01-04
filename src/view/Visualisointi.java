package view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Visualisoi simulaatiossa tapahtuvat asiat.
 * @author Tuomas Rajala
 * @since 12.10.2020
 */
public class Visualisointi extends Canvas{

	private GraphicsContext asiakas, alusta;
	
	// Nämä taulukot kertovat ja määrittelevät sen missä pisteissä asiakas luodaan sekä mistä se poistetaan,
	// jotta osataan visualisoida asiakkaan siirtyminen palvelupisteeltä toiselle.
	double[] xPiste = {55,125,195,265,335,405};
	double[] yPiste = {110,110,110,110,110,110};
	double[] xPoista = {55,125,195,265,335,405};
	double[] yPoista = {110,110,110,110,110,110};
	final double[] jonoRajaArvo = {70,140,210,280,350,420};
	final double[] jonoAloitus = {55,125,195,265,335,405};
	
	double w,h; //Canvaksen leveys sekä korkeus.
	
	/**
	 * Luodaan visulisointi näyttö canvakselle käyttöliittymälle, ja asetetaan tarvittavat tiedot.
	 * @param w leveys
	 * @param h korkeus
	 */
	public Visualisointi(int w, int h) {
		super(w, h);
		alusta = this.getGraphicsContext2D();
		asiakas = this.getGraphicsContext2D();
		tyhjennaNaytto();
	}
	
	/**
	 * Aina uuden simulaation alkaessa asetetaan asiakkaiden lähtökoordinaatit alkuperäisiksi.
	 */
	public void alustaAsiakkaat() {
		for(int i = 0;i<xPiste.length;i++) {
			xPiste[i] = jonoAloitus[i];
			yPiste[i] = 110;
			xPoista[i] = jonoAloitus[i];
			yPoista[i] = 110;
		}
	}
	
	/**
	 * Luodaan canvakselle palvelupisteet sekä niiden nimet.
	 */
	public void luoPalvelupisteet() {
		alusta.setLineWidth(1);
		alusta.strokeText("ILM", 60, 30);
		alusta.strokeText("ARV", 130, 30);
		alusta.strokeText("LAA", 200, 30);
		alusta.strokeText("RTG", 270, 30);
		alusta.strokeText("LAB", 340, 30);
		alusta.strokeText("OSA", 410, 30);
		alusta.setLineWidth(3);
		alusta.strokeRect(50, 50, 50, 50);
		alusta.strokeRect(120, 50, 50, 50);
		alusta.strokeRect(190, 50, 50, 50);
		alusta.strokeRect(260, 50, 50, 50);
		alusta.strokeRect(330, 50, 50, 50);
		alusta.strokeRect(400, 50, 50, 50);
	}

	/**
	 * Ennen käynnistystä tyhjennetään canvas ja luodaan sen jälkeen palvelupisteet.
	 */
	public void tyhjennaNaytto() {
		alusta.setFill(Color.LIGHTCYAN);
		alusta.fillRect(0, 0, this.getWidth(), this.getHeight());
		luoPalvelupisteet();
	}

	/**
	 * Palvelupisteissä näkyy sen sisältämä jono ajantasaisena.
	 * Metodin avulla päivitetään sitä.
	 * @param jonomaara palvelupisteiden ajantasainen jonomäärä taulukkomuodossa.
	 */
	public void paivitaJonoMaara(int[] jonomaara) {
		int[] sijaintiX = {70,140,210,280,350,420};
		alusta.setLineWidth(1);
		for(int i = 0;i<jonomaara.length;i++) {
			alusta.setFill(Color.LIGHTCYAN);
			alusta.fillRect(sijaintiX[i], 60, 25, 25);
			alusta.strokeText(String.valueOf(jonomaara[i]), sijaintiX[i], 80);
		}
	}
	
	/**
	 * Siirtää asiakkaan palvelupisteestä toiselle visuaalisesti.
	 * @param mista Kertoo mistä palvelupisteestä asiakas poistetaan
	 * @param mihin Kertoo mihin palvelupisteeseen asiakas viedään.
	 */
	public void siirraAsiakas(int mista, int mihin) {
		luoAsiakas(mihin);
		poistaAsiakas(mista);
	}

	/**
	 * Luodaan asiakas punaisena pallona palvelupisteeseen. Jono visualisoidaan alussa luotujen taulukoiden avulla ja jonoa päivitetään tarvittaessa.
	 * @param mihin Kertoo mihin palvelupisteeseen asiakas luodaan.
	 */
	public void luoAsiakas(int mihin) {
		asiakas.setFill(Color.RED);
		asiakas.fillOval(xPiste[mihin],yPiste[mihin],10,10);
		if(yPiste[mihin]>=450) {
			xPiste[mihin] += 15;
			yPiste[mihin] = 110;
		} else {
			yPiste[mihin]+=15;
		}
		if(xPiste[mihin]==jonoRajaArvo[mihin] && yPiste[mihin]>=450) {
			xPiste[mihin] = jonoAloitus[mihin];
			yPiste[mihin] = 110;
		}
	}

	/**
	 * Poistaa asiakkaan tietystä palvelupisteestä ja päivittää jonoa samoin.
	 * @param mista Mistä palvelupisteestä asiakas poistetaan.
	 */
	public void poistaAsiakas(int mista) {
		asiakas.setFill(Color.LIGHTCYAN);
		asiakas.fillRect(xPoista[mista],yPoista[mista],10,10);
		if(yPoista[mista]>=450) {
			xPoista[mista] += 15;
			yPoista[mista] = 110;
		} else {
			yPoista[mista]+=15;
		}
		if(xPoista[mista]==jonoRajaArvo[mista] && yPoista[mista]>=450) {
			xPoista[mista] = jonoAloitus[mista];
			yPoista[mista] = 110;
		}
	}
}








