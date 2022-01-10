import com.opencsv.CSVWriter;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class JSoupScraper
{
    public static boolean isGoodResponse(Connection.Response response) throws IOException
    {
        String contentType = response.contentType();
        assert contentType != null;
        boolean isHTML = contentType.contains("html") || contentType.contains("xml");

        System.out.println(response.statusCode() + " : " + response.url() + " Content type: " + contentType);

        return (response.statusCode() >= 200 && response.statusCode() < 300) && isHTML;
    }

    public static Document simpleGet(String url)
    {
        Connection.Response response;
        try {
            InputStream inStream = new URL(url).openStream();
            response = Jsoup.connect(url).followRedirects(false).timeout(10000).execute();
            if (isGoodResponse(response)) {
                return Jsoup.parse(inStream, "UTF-8", url);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String[][] getData(Document dom)
    {
        String[][] data = new String[250][3];

        Elements body = dom.select("tbody.lister-list");
        // System.out.println(body.select("tr").size());

        int r = 0;
        for (Element e : body.select("tr")){
            String title = e.select("td.posterColumn img").attr("alt").replace(",", ";");
            String year = e.select("td.titleColumn span.secondaryInfo").text().replaceAll("[^\\d]", "");
            String rating = e.select("td.ratingColumn.imdbRating").text().trim();

            data[r][0] = title;
            data[r][1] = year;
            data[r][2] = rating;
            r ++;
        }
        return data;
    }

    public static void writeOpenCSV(String[][] data)
    {
        File file = new File("C:\\Users\\odyse\\Desktop\\Java\\JSoupIMDB\\outputData\\imdbOpenCSV.csv");

        try {
            // FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter(file);

            // CSVWriter object with FileWriter object as parameter
            CSVWriter writer = new CSVWriter(outputfile);

            String[] columnNames = {"Title", "Year", "Rating"};
            writer.writeNext(columnNames);
            for (String[] arr : data) {
                writer.writeNext(arr);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void writeCSV(String[][] data)
    {
        File file = new File("C:\\Users\\odyse\\Desktop\\Java\\JSoupIMDB\\outputData\\imdOutputStreamCSV.csv");
        try {

            FileOutputStream fw = new FileOutputStream(file);
            OutputStreamWriter out = new OutputStreamWriter(fw, StandardCharsets.UTF_8);
            String columnNames = "Title,Year,Rating";
            out.append(columnNames);
            out.append("\n");

            for (String[] r : data){
                int c = 0;
                for (String word : r){
                    out.append(word);
                    if (c < 2) {
                        out.append(",");
                        c++;
                    } else {
                        out.append("\n");
                    }
                }
            }

            out.flush();
            out.close();
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args)
    {
        System.setProperty("file.encoding","UTF-8");

        Document dom = simpleGet("https://www.imdb.com/chart/top");
        assert dom != null;
        String[][] data = getData(dom);
        System.out.println(Arrays.deepToString(data));
        writeOpenCSV(data);
        writeCSV(data);
    }

}
