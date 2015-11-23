package fr.ac_versailles.crdp.apiscol.meta.hierarchy;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Node {

	private LinkedList<Node> children;
	private String mdid;

	public LinkedList<Node> getChildren() {
		return children;
	}

	public void setChildren(LinkedList<Node> children) {
		this.children = children;
	}

	public void addChild(Node child) {
		if (null == children)
			children = new LinkedList<Node>();
		children.add(child);
	}

	public String getMdid() {
		return mdid;
	}

	public void setMdid(String mdid) {
		this.mdid = mdid;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder().append(String.format(
				"||#%s#\r\n", mdid));
		if (null != children && children.size() > 0) {
			sb.append("\r\n[");
			Iterator<Node> it = children.iterator();
			while (it.hasNext()) {
				Node node = (Node) it.next();
				sb.append(node.toString());
			}
			sb.append("]\r\n");
		}
		return sb.toString();
	}

	public List<String> getChildrenId() {
		List<String> childrenIds = new LinkedList<String>();
		if (null != children) {
			Iterator<Node> it = children.iterator();
			while (it.hasNext()) {
				Node node = (Node) it.next();
				childrenIds.add(node.getMdid());
			}
		}
		return childrenIds;
	}

	public boolean hasChild(Node node) {
		if (children == null || children.size() == 0)
			return false;
		Iterator<Node> it = children.iterator();
		while (it.hasNext()) {
			Node nextNode = (Node) it.next();
			if (nextNode.getMdid().equals(node.getMdid())) {
				return true;
			}
		}
		return false;
	}

	public Node getChild(String mdid) {
		Iterator<Node> it = children.iterator();
		while (it.hasNext()) {
			Node nextNode = (Node) it.next();
			if (nextNode.getMdid().equals(mdid)) {
				return nextNode;
			}
		}
		return null;
	}
}