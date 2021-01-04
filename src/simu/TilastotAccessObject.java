package simu;

import java.sql.*;

/**
 * Luokan tarkoitus on koota kaikki tietokannan kanssa tekemisissä olevat metodit.
 * @author Tuomas Rajala
 * @since 12.10.2020
 */
public class TilastotAccessObject implements ITilastotDAO{
	
	private Connection con;
	private String[] palvelupisteet = {"ilmoittautuminen", "arviointi", "laakari", "rontgen", "laboratorio", "osasto"};
	private String[] suureet = {"asiakkaatLkm", "jonoLopussa","kotiin", "palvellut", "kayttoaste", "suoritusteho", "palveluaika", 
			"oleskeluaika", "lapimenoaika", "jonoKeskim", "saaNopeus", "ilmNopeus", "arvNopeus", "laaNopeus", "rtglabNopeus"};
	
	/**
	 * Ottaa yhteyden tietokantaan.
	 * @param moottori Tarvitaan kun halutaan dataa simulaatiosta.
	 */
	public TilastotAccessObject(Moottori moottori) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch(Exception e) {
			System.out.println(e);
			System.exit(-1);
		}
		
		final String URL = "jdbc:mysql://localhost/simulaattori";
		final String USERNAME = "olso";
		final String PASSWORD = "olso";
		try {
			con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		} catch (SQLException e) {
			System.err.println(e);
		}
		
	}
	
	@Override
	public void finalize() {
		try {
			if(con != null) {
				con.close();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean createTilasto(double[][] tilastot, int i) {
		try (PreparedStatement myStatement = 
				con.prepareStatement("INSERT INTO " + palvelupisteet[i]
						+ " VALUES(id,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"))
		{
			
			for(int j = 0;j<15;j++) {
				myStatement.setDouble(j+1, tilastot[i][j]);
			}
			myStatement.executeUpdate();
			return true;
		} catch (SQLException e) {
			do {
				System.err.println("Viesti: " + e.getMessage());
				System.err.println("Virhe: " + e.getErrorCode());
				System.err.println("SQL-tila: " + e.getSQLState());
			} while(e.getNextException() != null);
		}
		
		return false;
	}
	

	@Override
	public double[] readEdellinenTilasto(int i) {
		double[] tilasto = new double[15];
		try (PreparedStatement myStatement = 
				con.prepareStatement("SELECT * FROM " + palvelupisteet[i] + " WHERE id=" + (getRowCount(palvelupisteet[i])-1)))
		{
			ResultSet rs = myStatement.executeQuery();
			while(rs.next()) {
				for(int j = 0; j<15;j++) {
					tilasto[j] = rs.getDouble(suureet[j]);
				}
			}
			return tilasto;
		} catch (SQLException e) {
			do {
				System.err.println("Viesti: " + e.getMessage());
				System.err.println("Virhe: " + e.getErrorCode());
				System.err.println("SQL-tila: " + e.getSQLState());
			} while(e.getNextException() != null);
		}
		return null;
	}

	@Override
	public double[] readTilasto(int i) {
		double[] tilasto = new double[10];
		try (PreparedStatement myStatement = 
				con.prepareStatement("SELECT * FROM " + palvelupisteet[i] + " WHERE id=" + getRowCount(palvelupisteet[i])))
		{
			ResultSet rs = myStatement.executeQuery();
			while(rs.next()) {
				for(int j = 0; j<10;j++) {
					tilasto[j] = rs.getDouble(suureet[j]);
				}
			}
			return tilasto;
		} catch (SQLException e) {
			do {
				System.err.println("Viesti: " + e.getMessage());
				System.err.println("Virhe: " + e.getErrorCode());
				System.err.println("SQL-tila: " + e.getSQLState());
			} while(e.getNextException() != null);
		}
		return null;
	}

	
	@Override
	public int getRowCount(String tauluNimi) {
		try (PreparedStatement myStatement = 
				con.prepareStatement("SELECT COUNT(id) AS rivit FROM " + tauluNimi))
		{
			ResultSet rs = myStatement.executeQuery();
			rs.next();
			int count = rs.getInt("rivit");
			rs.close();
			return count;
		} catch (SQLException e) {
			do {
				System.err.println("Viesti: " + e.getMessage());
				System.err.println("Virhe: " + e.getErrorCode());
				System.err.println("SQL-tila: " + e.getSQLState());
			} while(e.getNextException() != null);
		}
		System.out.println("Epäonnistui");
		return -1;
	}
	
	
	@Override
	public double[][] readTilastot(int i) {
		int riveja = getRowCount(palvelupisteet[i]);
		double[][] kaikkiTilastot = new double[riveja][16];
		try (Statement stmt = con.createStatement())
		{
			String query = "SELECT * FROM " + palvelupisteet[i];
			ResultSet rs = stmt.executeQuery(query);
			int rivi = 0;
			while(rs.next()) {
				
				kaikkiTilastot[rivi][0] = rs.getInt("id");
				for(int j = 1;j<=suureet.length;j++) {
					kaikkiTilastot[rivi][j] = rs.getDouble(suureet[j-1]);
				}
				rivi++;
			}
			System.out.println(kaikkiTilastot[0][1]);
			return kaikkiTilastot;
		} catch(SQLException e) {
			System.out.println(e);
		}
		
		return null;
	}
}

