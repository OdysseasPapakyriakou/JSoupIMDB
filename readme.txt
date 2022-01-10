A simple project to scrape the IMDB top 250 page.

The data is exported to 2 .csv files using two different methods:
	1) the CSVWriter from the opencsv library
	2) the built-in OutputStreamWriter

To run the file from cdm make sure you are in the src directory and type:
	- javac -cp "C:/yourOwnDirectories/JSoupIMDB/lib/*;." JSoupScraper.java
	- java -cp "C:/yourOwnDirectories/JSoupIMDB/lib/*;." JSoupScraper