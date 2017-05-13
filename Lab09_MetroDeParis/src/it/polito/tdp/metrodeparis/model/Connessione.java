package it.polito.tdp.metrodeparis.model;

public class Connessione {

	private int idConnection;
	private int idLinea;
	private Fermata partenza;
	private Fermata arrivo;
	private int velocita;
	private int intervallo;
	
	public Connessione(int idConnection, int idLinea, Fermata partenza, Fermata arrivo , int velocita) {
		
		this.idConnection = idConnection;
		this.idLinea = idLinea;
		this.partenza = partenza;
		this.arrivo = arrivo;
		this.velocita= velocita;
	}
    public Connessione(Fermata partenza, Fermata arrivo, int intervallo) {
		
		this.partenza = partenza;
		this.arrivo = arrivo;
		this.intervallo = intervallo;
	}

	public int getIntervallo() {
		return intervallo;
	}


	public void setIntervallo(int intervallo) {
		this.intervallo = intervallo;
	}


	


	public int getIdConnection() {
		return idConnection;
	}

	public int getVelocita() {
		return velocita;
	}

	public void setVelocita(int velocita) {
		this.velocita = velocita;
	}

	public void setIdConnection(int idConnection) {
		this.idConnection = idConnection;
	}

	public int getIdLinea() {
		return idLinea;
	}

	public void setIdLinea(int idLinea) {
		this.idLinea = idLinea;
	}

	public Fermata getPartenza() {
		return partenza;
	}

	public void setPartenza(Fermata partenza) {
		this.partenza = partenza;
	}

	public Fermata getArrivo() {
		return arrivo;
	}

	public void setArrivo(Fermata arrivo) {
		this.arrivo = arrivo;
	}
	
	
	
}
