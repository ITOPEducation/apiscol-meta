package fr.ac_versailles.crdp.apiscol.meta.hierarchy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.solr.common.util.Pair;

import com.sun.jersey.server.impl.cdi.SyntheticQualifier;

import fr.ac_versailles.crdp.apiscol.meta.references.RelationKinds;

public class HierarchyAnalyser {

	public enum Differencies {
		removed("removed"), added("added");
		private String value;

		private Differencies(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}

	}

	private static HashMap<String, ArrayList<Modification>> modifications;

	synchronized public static void detectChanges(Node oldTree, Node newTree) {
		// id->
		modifications = new HashMap<String, ArrayList<Modification>>();
		computeDifferencies(oldTree, newTree, modifications,
				Differencies.removed);
		computeDifferencies(newTree, oldTree, modifications, Differencies.added);
	}

	private static void computeDifferencies(Node comparaison, Node reference,
			HashMap<String, ArrayList<Modification>> modifications,
			Differencies differenceType) {
		LinkedList<Node> comparaisonChildren = comparaison.getChildren();
		if (null == comparaisonChildren) {
			return;
		}
		Iterator<Node> it = comparaisonChildren.iterator();
		while (it.hasNext()) {
			Node node = (Node) it.next();
			if (reference != null && reference.hasChild(node)) {

				computeDifferencies(node, reference.getChild(node.getMdid()),
						modifications, differenceType);
			} else {
				registerModification(modifications, comparaison.getMdid(),
						differenceType, RelationKinds.CONTIENT, node.getMdid());
				registerModification(modifications, node.getMdid(),
						differenceType, RelationKinds.FAIT_PARTIE_DE,
						comparaison.getMdid());
				computeDifferencies(node, null, modifications, differenceType);
			}

		}

	}

	private static void registerModification(
			HashMap<String, ArrayList<Modification>> modifications,
			String mdid1, Differencies differenceType, RelationKinds relation,
			String mdid2) {
		if (!modifications.containsKey(mdid1))
			modifications.put(mdid1, new ArrayList<Modification>());
		Modification modification = new Modification(differenceType,
				relation, mdid2);
		if (!modifications.get(mdid1).contains(modification))
			modifications.get(mdid1).add(modification);

	}

	public static HashMap<String, ArrayList<Modification>> getModifications() {
		return modifications;
	}
}
