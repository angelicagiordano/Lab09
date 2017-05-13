package it.polito.tdp.metrodeparis.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.javadocmd.simplelatlng.LatLng;

import it.polito.tdp.metrodeparis.model.Connessione;
import it.polito.tdp.metrodeparis.model.Fermata;


public class MetroDAO {

	public List<Fermata> getAllFermate() {

		final String sql = "SELECT id_fermata, nome, coordx, coordy FROM fermata ORDER BY nome ASC";
		List<Fermata> fermate = new ArrayList<Fermata>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Fermata f = new Fermata(rs.getInt("id_Fermata"), rs.getString("nome"), new LatLng(rs.getDouble("coordx"), rs.getDouble("coordy")));
				fermate.add(f);
			}

			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore di connessione al Database.");
		}

		return fermate;
	}
	public List<Connessione> getAllConnections() {

		final String sql = "SELECT f1.id_fermata as idF1, f2.id_fermata as idF2, f1.nome as nome1,"+
		                     " f2.nome as nome2, f1.coordx as x1, f1.coordy as y1, f2.coordx as x2, f2.coordy as y2, id_connessione, connessione.id_linea , linea.velocita "+
		                     " FROM connessione, fermata f1, fermata f2,linea "+ 
		             		" WHERE connessione.id_stazP = f1.id_fermata AND linea.id_linea=connessione.id_linea "+ 
		                     " AND connessione.id_stazA = f2.id_fermata";
				             
		
		 

		List<Connessione> connessioni = new ArrayList<Connessione>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				LatLng lat1=new LatLng(rs.getDouble("x1"), rs.getDouble("y1"));
				LatLng lat2=new LatLng(rs.getDouble("x2"), rs.getDouble("y2"));
				Fermata partenza= new Fermata(rs.getInt("idF1"), rs.getString("nome1"), lat1 ); 
				Fermata arrivo=  new Fermata(rs.getInt("idF2"), rs.getString("nome2"), lat2 ); 
				Connessione c = new Connessione(rs.getInt("id_connessione"),rs.getInt("id_linea"),partenza,arrivo, rs.getInt("velocita"));
			    connessioni.add(c);
			}
			

			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore di connessione al Database.");
		}

		return connessioni;
	}
	public List <Connessione> getSottoConnessioni(){
		final String sql ="SELECT DISTINCT id_fermata,fermata.nome as nomeF, coordX as x1,coordY as y1, c1.id_linea"+
	        " AS l1, c2.id_linea  as l2, intervallo, linea.nome as nomeL FROM fermata,connessione c1, connessione c2,linea "+
				" where c1.id_linea<>c2.id_linea AND c1.id_stazP= c2.id_stazP and c1.id_stazP=id_fermata and c2.id_linea=linea.id_linea";
	
	

		List<Connessione> connessioni = new ArrayList<Connessione>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				LatLng lat1=new LatLng(rs.getDouble("x1"), rs.getDouble("y1"));
				
				Fermata partenza= new Fermata(rs.getInt("id_fermata"), rs.getString("nomeF"), lat1 ,rs.getInt("l1")); 
				Fermata arrivo=  new Fermata(rs.getInt("id_fermata"), rs.getString("nomeF"), lat1, rs.getInt("l2") ); 
				Connessione c = new Connessione(partenza,arrivo,  rs.getInt("intervallo"));
			    connessioni.add(c);
			}
			

			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore di connessione al Database.");
		}

		return connessioni;
	
	} 
	public List<Fermata> getAllSottoFermate() {

		final String sql = "SELECT  DISTINCT id_fermata, nome, coordX, coordY ,id_linea FROM connessione, fermata  WHERE id_fermata=id_stazP";
		List<Fermata> fermate = new ArrayList<Fermata>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Fermata f = new Fermata(rs.getInt("id_Fermata"), rs.getString("nome"), new LatLng(rs.getDouble("coordx"), rs.getDouble("coordy")), rs.getInt("id_linea"));
				fermate.add(f);
			}

			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore di connessione al Database.");
		}

		return fermate;
	}
}
