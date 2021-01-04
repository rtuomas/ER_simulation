package simu;

import java.util.PriorityQueue;

/**
 * Pitää yllä ja järjestää tapahtumat kellonajan mukaiseen järjestykseen. Simulaatio toimii tämän listan järjestyksessä.
 * @author Tuomas Rajala
 * @since 12.10.2020
 */
public class Tapahtumalista {
	private PriorityQueue<Tapahtuma> lista = new PriorityQueue<Tapahtuma>();
	
	/**
	 * Tyhjä kostruktori.
	 */
	public Tapahtumalista(){
	}
	
	/**
	 * Poistetaan tapahtumalistalta tapahtuma, joka on tapahtunut järjestyksessä.
	 * @return seuraava (kellonajan mukaan) tapahtuma ja poistetaan se.
	 */
	public Tapahtuma poista(){
		Trace.out(Trace.Level.INFO,"Poisto " + lista.peek());
		return lista.remove();
	}
	
	/**
	 * Lisätään tapahtumalistalle tapahtuma.
	 * @param t seuraava tapahtuma.
	 */
	public void lisaa(Tapahtuma t){
		lista.add(t);
	}
	
	/**
	 * Antaa listan seuraavan tapahtuman kellonajan
	 * @return seuraavan tapahtuman kellonaika.
	 */
	public double getSeuraavanAika(){
		return lista.peek().getAika();
	}
}
