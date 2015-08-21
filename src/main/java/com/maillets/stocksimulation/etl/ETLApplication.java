package com.maillets.stocksimulation.etl;

import java.io.*;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.maillets.stocksimulation.SymbolMappingLoader;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class ETLApplication {

	private static final Logger logger = LoggerFactory.getLogger(ETLApplication.class);

	@Bean
	CommandLineRunner init() {

		return arg -> {

			try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("symbol_description.data", true)))) {
				List<String> nasdaqSymbolList = SymbolMappingLoader.load("/nasdaq.data");
				List<String> nyseSymbolList = SymbolMappingLoader.load("/nyse.data");
				List<String> amexSymbolList = SymbolMappingLoader.load("/amex.data");

				nasdaqSymbolList.parallelStream().forEach(symbol -> {
					ExtractStockSummary extractor = new ExtractStockSummary(symbol);
					String description = extractor.getDescription();
					out.println(symbol + "|" + description);
				});
				nyseSymbolList.parallelStream().forEach(symbol -> {
					ExtractStockSummary extractor = new ExtractStockSummary(symbol);
					String description = extractor.getDescription();
					out.println(symbol + "|" + description);
				});
				amexSymbolList.parallelStream().forEach(symbol -> {
					ExtractStockSummary extractor = new ExtractStockSummary(symbol);
					String description = extractor.getDescription();
					out.println(symbol + "|" + description);
				});
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}

			logger.info("Init done!");
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(ETLApplication.class, args);
	}
}