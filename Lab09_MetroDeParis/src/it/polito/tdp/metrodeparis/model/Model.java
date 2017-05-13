package it.polito.tdp.metrodeparis.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.AbstractBaseGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.graph.WeightedMultigraph;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.metrodeparis.dao.MetroDAO;

public class Model {

	WeightedGraph <Fermata, DefaultWeightedEdge>grafo;
	DirectedWeightedMultigraph<Fermata, DefaultWeightedEdge> grafoOrientato;
	MetroDAO mdao= new MetroDAO();
	double tempoPercorrenza=0;
	
	public List<Fermata> getAllFermate(){
		
		
		List <Fermata> fermate= new ArrayList<Fermata>(mdao.getAllFermate());
		return fermate;
		
	}
	public void creaGrafo(){
		
		grafo= new WeightedMultigraph<Fermata, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(grafo, mdao.getAllFermate());
		
		List<Connessione> connessioni= new ArrayList<Connessione>(mdao.getAllConnections());
		
		for(Connessione c: connessioni){
			
			 double d= LatLngTool.distance(c.getPartenza().getCoords(), c.getArrivo().getCoords(), LengthUnit.KILOMETER);
			double peso= (d/c.getVelocita());
			DefaultWeightedEdge e=grafo.addEdge(c.getPartenza(), c.getArrivo());
			if(e!=null){
			 grafo.setEdgeWeight(e, peso);

			}
			
			
		}
		System.out.println(grafo);
	}
	
	
	public void creaGrafoOrientato(){
		grafoOrientato= new DirectedWeightedMultigraph<Fermata, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		List<Fermata> fermate= new ArrayList<Fermata>(mdao.getAllSottoFermate());
		List<Connessione> sottoconnessioni=new ArrayList<Connessione>(mdao.getSottoConnessioni());
		List<Connessione> connessioni= new ArrayList<Connessione>(mdao.getAllConnections());
		
		Graphs.addAllVertices(grafoOrientato, fermate);
		for(Connessione c: connessioni){
			
			 double d= LatLngTool.distance(c.getPartenza().getCoords(), c.getArrivo().getCoords(), LengthUnit.KILOMETER);
			double peso= (d/c.getVelocita());
		
			c.getArrivo().setIdLinea(c.getIdLinea());
			c.getPartenza().setIdLinea(c.getIdLinea());
			DefaultWeightedEdge e=grafoOrientato.addEdge(c.getPartenza(), c.getArrivo());
			if(e!=null){
			 grafoOrientato.setEdgeWeight(e, peso);

			}}
		for(Connessione c: sottoconnessioni){
			if(!c.getArrivo().equals(c.getPartenza())){
			DefaultWeightedEdge e= grafoOrientato.addEdge(c.getPartenza(), c.getArrivo());
			grafoOrientato.setEdgeWeight(e, c.getIntervallo());
		}}
		
		
		System.out.println(grafoOrientato);
		
	}
	
	public List<Fermata> getShortestPath(Fermata f1, Fermata f2){
		DijkstraShortestPath<Fermata, DefaultWeightedEdge> path= new DijkstraShortestPath<Fermata, DefaultWeightedEdge>(grafo, f1, f2);
	
		List<DefaultWeightedEdge> archi= path.getPathEdgeList();
		List <Fermata> fermate= new ArrayList<Fermata>();
		for(DefaultWeightedEdge e: archi){
			if(!fermate.contains(grafo.getEdgeSource(e))){
			fermate.add(grafo.getEdgeSource(e));
			
			
			}
			if(!fermate.contains(grafo.getEdgeTarget(e))){
			fermate.add(grafo.getEdgeTarget(e));
			
			
			}
			tempoPercorrenza+= grafo.getEdgeWeight(e);
			tempoPercorrenza+=(30/3600);
		}
		
		return fermate;
	}
	public Double getTempoPercorrenza(){
		
		return tempoPercorrenza;
	}
	public List<Fermata> getShortestPathOrientato(Fermata f1, Fermata f2){
		List<Fermata> fermate= new ArrayList<Fermata>();
		List<DefaultWeightedEdge> archibest= new ArrayList<DefaultWeightedEdge>();
		double percorrenza=100000;
		for(Fermata f: grafoOrientato.vertexSet()){
		
			if(f1.getNome().compareTo(f.getNome())==0){
				for(Fermata fe: grafoOrientato.vertexSet()){
					if(f2.getNome().compareTo(fe.getNome())==0){
						
						DijkstraShortestPath<Fermata, DefaultWeightedEdge> path= new DijkstraShortestPath<Fermata, DefaultWeightedEdge>(grafoOrientato, f,fe);
						List<DefaultWeightedEdge> archi= new ArrayList<DefaultWeightedEdge> (path.getPathEdgeList());
						
						
						if(this.calcolaTempo(archi)<percorrenza){
						percorrenza=this.calcolaTempo(archi);
						archibest.clear();
						archibest.addAll(archi);
						
						}
						}
				}
					
				}
		
			
		}
		for(DefaultWeightedEdge e: archibest){
			if(!fermate.contains(grafoOrientato.getEdgeSource(e))){
			fermate.add(grafoOrientato.getEdgeSource(e));
			
			
			}
			if(!fermate.contains(grafoOrientato.getEdgeTarget(e))){
			fermate.add(grafoOrientato.getEdgeTarget(e));
			
			
			}
	}
		return fermate;
	}
	public double calcolaTempo(List<DefaultWeightedEdge> archi){
		
		double tempo=0;
		for(DefaultWeightedEdge e: archi){
			tempo+= grafoOrientato.getEdgeWeight(e);
		}
		return tempo;
	}
}
