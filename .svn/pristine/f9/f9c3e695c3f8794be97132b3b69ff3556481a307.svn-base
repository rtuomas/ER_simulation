package simu;

/**
 * Konsolissa simulaation tapahtumia käsittävä luokka.
 * @author Tuomas Rajala
 * @since 12.10.2020
 *
 */
public class Trace {

	public enum Level{INFO, WAR, ERR}
	
	private static Level traceLevel;
	
	/**
	 * Asettaa tapahtumien seuraamistason halutuksi.
	 * @param lvl taso.
	 */
	public static void setTraceLevel(Level lvl){
		traceLevel = lvl;
	}
	
	/**
	 * Tulostaa konsoliin tapahtumia seuraamistason mukaisesti.
	 * @param lvl seuraamistaso.
	 * @param txt konsoliin tulostettava teksti.
	 */
	public static void out(Level lvl, String txt){
		if (lvl.ordinal() >= traceLevel.ordinal()){
			System.out.println(txt);
		}
	}
}