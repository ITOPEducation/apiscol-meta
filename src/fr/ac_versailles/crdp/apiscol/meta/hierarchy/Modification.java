package fr.ac_versailles.crdp.apiscol.meta.hierarchy;

import fr.ac_versailles.crdp.apiscol.meta.hierarchy.HierarchyAnalyser.Differencies;
import fr.ac_versailles.crdp.apiscol.meta.references.RelationKinds;

public class Modification {
	private Differencies differency;
	private RelationKinds relation;
	private String target;

	public Modification(Differencies differency, RelationKinds relation,
			String target) {
		this.setDifferency(differency);
		this.setRelation(relation);
		this.setTarget(target);

	}

	public Differencies getDifferency() {
		return differency;
	}

	public void setDifferency(Differencies differency) {
		this.differency = differency;
	}

	public RelationKinds getRelation() {
		return relation;
	}

	public void setRelation(RelationKinds relation) {
		this.relation = relation;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof Modification) {
			Modification otherModification = (Modification) object;
			return otherModification.getRelation().toString()
					.equals(relation.toString())
					&& otherModification.getTarget().toString().equals(target)
					&& otherModification.getDifferency().toString()
							.equals(differency.toString());
		}
		return false;
	}
}
