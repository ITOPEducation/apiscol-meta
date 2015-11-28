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
		// mdid->Modifications
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
		Node node, next = null, previous = null;
		while (it.hasNext() || next != null) {
			node = next;
			next = it.hasNext() ? it.next() : null;
			if (node == null) {
				continue;
			}

			if (reference != null && reference.hasChild(node)) {
				if (previous != null) {
					if (!reference.isPrevious(previous, node)) {
						registerModification(modifications, node.getMdid(),
								differenceType, RelationKinds.REQUIERT,
								previous.getMdid());
						registerModification(modifications, previous.getMdid(),
								differenceType, RelationKinds.EST_REQUIS_PAR,
								node.getMdid());
					}
				}
				if (next != null) {
					if (!reference.isNext(next, node)) {
						registerModification(modifications, next.getMdid(),
								differenceType, RelationKinds.REQUIERT,
								node.getMdid());
						registerModification(modifications, node.getMdid(),
								differenceType, RelationKinds.EST_REQUIS_PAR,
								next.getMdid());
					}
				}
				computeDifferencies(node, reference.getChild(node.getMdid()),
						modifications, differenceType);
			} else {
				if (previous != null) {
					registerModification(modifications, node.getMdid(),
							differenceType, RelationKinds.REQUIERT,
							previous.getMdid());
					registerModification(modifications, previous.getMdid(),
							differenceType, RelationKinds.EST_REQUIS_PAR,
							node.getMdid());
				}
				if (next != null) {
					registerModification(modifications, next.getMdid(),
							differenceType, RelationKinds.REQUIERT,
							node.getMdid());
					registerModification(modifications, node.getMdid(),
							differenceType, RelationKinds.EST_REQUIS_PAR,
							next.getMdid());
				}

				registerModification(modifications, comparaison.getMdid(),
						differenceType, RelationKinds.CONTIENT, node.getMdid());
				registerModification(modifications, node.getMdid(),
						differenceType, RelationKinds.FAIT_PARTIE_DE,
						comparaison.getMdid());
				computeDifferencies(node, null, modifications, differenceType);
			}
			previous = node;
		}

	}

	private static void registerModification(
			HashMap<String, ArrayList<Modification>> modifications,
			String mdid1, Differencies differenceType, RelationKinds relation,
			String mdid2) {
		if (!modifications.containsKey(mdid1))
			modifications.put(mdid1, new ArrayList<Modification>());
		Modification modification = new Modification(differenceType, relation,
				mdid2);
		if (!modifications.get(mdid1).contains(modification))
			modifications.get(mdid1).add(modification);

	}

	public static HashMap<String, ArrayList<Modification>> getModifications() {
		return modifications;
	}
}
