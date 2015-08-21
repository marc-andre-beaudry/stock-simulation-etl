package com.maillets.stocksimulation.etl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExtractStockSummary {

	private static final Logger logger = LoggerFactory.getLogger(ExtractStockSummary.class);
	private static final String URL_FORMAT = "https://ca.finance.yahoo.com/q/pr?s=%s";

	private String symbol;

	public ExtractStockSummary(String symbol) {
		this.symbol = symbol;
	}

	public String getDescription() {
		String description = "";
		try {
			String url = String.format(URL_FORMAT, symbol);
			Document doc = Jsoup.connect(url).get();
			Element td = doc.select("td.yfnc_modtitlew1").first();
			Element p = td.select("p").first();
			description = p.text();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return description;
	}
}
