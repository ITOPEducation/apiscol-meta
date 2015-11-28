package fr.ac_versailles.crdp.apiscol.meta.hierarchy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class Node {

	private LinkedList<Node> children;
	private String mdid;
	private String nextNodeUri;

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

	public boolean isPrevious(Node previous, Node node) {
		if (children == null || children.size() == 0)
			return false;
		Iterator<Node> it = children.iterator();
		Node nextNode, previousNode = null;
		while (it.hasNext()) {
			nextNode = (Node) it.next();
			if (nextNode.getMdid().equals(node.getMdid())) {
				return previousNode != null
						&& previousNode.getMdid().equals(previous.getMdid());
			}
			previousNode = nextNode;
		}
		return false;
	}

	public boolean isNext(Node next, Node node) {
		if (children == null || children.size() == 0)
			return false;
		Iterator<Node> it = children.iterator();
		Node nextNode, previousNode = null;
		while (it.hasNext()) {
			nextNode = (Node) it.next();
			if (previousNode != null
					&& previousNode.getMdid().equals(node.getMdid())) {
				return nextNode.getMdid().equals(next.getMdid());
			}
			previousNode = nextNode;
		}
		return false;
	}

	public void registerNextNodeUri(String nextNodeUri) {
		this.nextNodeUri = nextNodeUri;
	}

	public String getNextNodeUri() {
		return nextNodeUri;
	}

	public void reorderChildren() {
		if (children == null || children.size() == 0)
			return;

		Iterator<Node> it = children.iterator();
		Node nextNode;
		ArrayList<String> nextNodeUris = new ArrayList<String>();
		while (it.hasNext()) {
			nextNode = (Node) it.next();
			if (!StringUtils.isEmpty(nextNode.getNextNodeUri())) {
				nextNodeUris.add(nextNode.getNextNodeUri());
			}
		}
		it = children.iterator();
		Node firstNode = null;
		while (it.hasNext()) {
			nextNode = (Node) it.next();
			if (!nextNodeUris.contains(nextNode.getMdid())) {
				if (firstNode == null) {
					firstNode = nextNode;
				} else {
					// impossible to determine first node : too many candidates
					return;
				}
			}

		}
		if (firstNode == null)
			return;
		children.remove(firstNode);
		children.addLast(firstNode);
		Node node = firstNode;

		while (node.getNextNodeUri() != null) {
			it = children.iterator();
			boolean found = false;
			while (it.hasNext()) {
				nextNode = (Node) it.next();
				if (nextNode.getMdid().equals(node.getNextNodeUri())) {
					node = nextNode;
					found = true;
					break;
				}
			}
			if (!found) {
				break;
			}
			children.remove(node);
			children.addLast(node);

		}

	}
}