package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {

	private YelpDao dao;
	private Graph<Business, DefaultWeightedEdge> grafo;

	public Model() {
		dao = new YelpDao();
	}

	public String creaGrafo(String citta) {
		// TODO Auto-generated method stub
		dao = new YelpDao();
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		List<Business> locali = dao.getAllBusiness(citta);
		Graphs.addAllVertices(this.grafo, locali);
		for (Business b1 : locali) {
			for (Business b2 : locali) {
				if (!b1.equals(b2) && b1.getBusinessId().compareTo(b2.getBusinessId()) < 0) {
					LatLng cb1 = new LatLng(b1.getLatitude(), b1.getLongitude());
					LatLng cb2 = new LatLng(b2.getLatitude(), b2.getLongitude());
					double peso = LatLngTool.distance(cb1, cb2, LengthUnit.KILOMETER);
					Graphs.addEdge(this.grafo, b1, b2, peso);
				}
			}
		}

		return "Grafo creato con " + this.grafo.vertexSet().size() + " vertici e " + this.grafo.edgeSet().size()
				+ " archi\n";
	}

	public List<String> getCitta() {

		return dao.getCitta();
	}

	public List<Business> getLocali() {
		// TODO Auto-generated method stub
		List<Business> result = new ArrayList<>();
		for (Business b : this.grafo.vertexSet()) {
			result.add(b);
		}
		return result;
	}

	public String piuDistante(Business selezionato) {
		double max = 0;

		for (DefaultWeightedEdge e : this.grafo.edgesOf(selezionato)) {
			if (this.grafo.getEdgeWeight(e) > max) {
				max = (double) this.grafo.getEdgeWeight(e);
			}
		}
		for (DefaultWeightedEdge e : this.grafo.edgesOf(selezionato)) {
			if (this.grafo.getEdgeWeight(e) == max) {
				return "" + Graphs.getOppositeVertex(grafo, e, selezionato).getBusinessName() + grafo.getEdgeWeight(e);

			}
		}
		return null;

	}

}
