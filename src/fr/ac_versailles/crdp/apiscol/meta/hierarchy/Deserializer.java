package fr.ac_versailles.crdp.apiscol.meta.hierarchy;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

public class Deserializer implements JsonDeserializer<Node> {

	@SuppressWarnings("unchecked")
	public Node deserialize(JsonElement json, Type type,
			JsonDeserializationContext context) throws JsonParseException {
		Node node = null;
		if (json == null)
			node = null;
		else {
			node = new Node();
			JsonElement idElement = json.getAsJsonObject().get("id");
			System.out.println(idElement.getAsString());
			// getAsString throws exception ?
			String id = idElement.toString();
			node.setMdid(id.replaceAll("\"", ""));
			JsonElement childrenElement = json.getAsJsonObject()
					.get("children");
			if (childrenElement != null) {
				Type listType = new TypeToken<LinkedList<Node>>() {
				}.getType();
				node.setChildren((LinkedList<Node>) context.deserialize(
						childrenElement, listType));

			}

		}
		return node;
	}
}
