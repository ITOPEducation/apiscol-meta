package fr.ac_versailles.crdp.apiscol.meta.codeSnippets;

import java.util.ArrayList;
import java.util.HashMap;

public class SnippetGenerator {

	public enum SCRIPTS {
		JQUERY, APISCOL
	};

	public enum OPTIONS {
		MODE("mode"), DEVICE("device"), STYLE("style");
		private String value;

		private OPTIONS(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}
	}

	private static ArrayList<OPTIONS> tagOptions = new ArrayList<SnippetGenerator.OPTIONS>();
	{
		tagOptions.add(OPTIONS.MODE);
		tagOptions.add(OPTIONS.DEVICE);
		tagOptions.add(OPTIONS.STYLE);
	}
	private static HashMap<OPTIONS, String> optionsTokens = new HashMap<SnippetGenerator.OPTIONS, String>();
	{
		optionsTokens.put(OPTIONS.MODE, "MODE");
		optionsTokens.put(OPTIONS.DEVICE, "DEVICE");
		optionsTokens.put(OPTIONS.STYLE, "STYLE");
	}
	private static HashMap<OPTIONS, ArrayList<String>> optionsValues = new HashMap<SnippetGenerator.OPTIONS, ArrayList<String>>();
	{
		optionsValues.put(OPTIONS.MODE, new ArrayList<String>());
		optionsValues.get(OPTIONS.MODE).add("base");
		optionsValues.get(OPTIONS.MODE).add("full");
		optionsValues.put(OPTIONS.DEVICE, new ArrayList<String>());
		optionsValues.get(OPTIONS.DEVICE).add("auto");
		optionsValues.get(OPTIONS.DEVICE).add("mobile");
		optionsValues.get(OPTIONS.DEVICE).add("screen");
		optionsValues.put(OPTIONS.STYLE, new ArrayList<String>());
		optionsValues.get(OPTIONS.STYLE).add("dark-hive");
		optionsValues.get(OPTIONS.STYLE).add("black-tie");
		optionsValues.get(OPTIONS.STYLE).add("blitzer");
		optionsValues.get(OPTIONS.STYLE).add("cupertino");
		optionsValues.get(OPTIONS.STYLE).add("dot-luv");
		optionsValues.get(OPTIONS.STYLE).add("excite-bike");
		optionsValues.get(OPTIONS.STYLE).add("hot-sneaks");
		optionsValues.get(OPTIONS.STYLE).add("humanity");
		optionsValues.get(OPTIONS.STYLE).add("mint-choc");
		optionsValues.get(OPTIONS.STYLE).add("redmond");
		optionsValues.get(OPTIONS.STYLE).add("smoothness");
		optionsValues.get(OPTIONS.STYLE).add("south-street");
		optionsValues.get(OPTIONS.STYLE).add("start");
		optionsValues.get(OPTIONS.STYLE).add("swanky-purse");
		optionsValues.get(OPTIONS.STYLE).add("trontastic");
		optionsValues.get(OPTIONS.STYLE).add("ui-darkness");
		optionsValues.get(OPTIONS.STYLE).add("ui-lightness");
		optionsValues.get(OPTIONS.STYLE).add("vader");
	}

	public String getScript(SCRIPTS script, String version) {
		switch (script) {
		case JQUERY:
			return "<script src=\"http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.js\"></script>";
		case APISCOL:
			return "<script src=\"https://rawgit.com/ITOPEducation/apiscol-cdn/master/"+version+"/js/jquery.apiscol.js\"></script>";
		}
		return null;
	}

	public String getTagPattern(String metadataUri) {
		return String
				.format("<a class=\"apiscol\" data-mode=\"MODE\" data-device=\"DEVICE\" data-style=\"STYLE\" href=\"%s\"></a>",
						metadataUri);
	}

	public ArrayList<OPTIONS> getTagOptions() {
		return tagOptions;
	}

	public String getOptionToken(OPTIONS option) {
		return optionsTokens.get(option);
	}

	public ArrayList<String> getOptionsValues(OPTIONS option) {
		return optionsValues.get(option);
	}

	public String getIframe(String metadataUri) {
		return String
				.format("<iframe src=\"%s?mode=MODE&style=STYLE&device=DEVICE\"></iframe>",
						metadataUri);

	}
}
