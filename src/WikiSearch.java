import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class WikiSearch {
    public static StringBuilder getInfo(String request) throws IOException {
        String encodedRequest = URLEncoder.encode(request, "UTF-8");
        request = "https://ru.wikipedia.org/w/api.php?action=query&list=search&utf8=&format=json&srsearch=" + "\"" + encodedRequest + "\""; //запрос к api
        URL url = new URL(request);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //открываем соединение
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            response.append(line);
            System.out.println();
        }
        reader.close();
        connection.disconnect();

        if((response.indexOf("\"totalhits\":0") != -1) || responseCode != 200){
            return new StringBuilder();
        }
        else{
            return response;
        }

    }
    public static List<String> parsing(StringBuilder response){
        List<String> searchResults = new ArrayList<>();  //обрабатываем полученные данные и добавляем в searchResults
        String[] parts = response.toString().split("\"title\":\"");
        for (int i = 1; i < parts.length; i++) {
            String title = parts[i].substring(0, parts[i].indexOf("\""));
            String urls = "https://ru.wikipedia.org/wiki/" + title.replace(" ", "_");
            searchResults.add(urls);
        }
        return searchResults;
    }

    public static void printSearchResults(List<String> searchResults) {
        for (int i = 0; i < searchResults.size(); i++) { //выводим названия статей
            String title = searchResults.get(i).substring(searchResults.get(i).lastIndexOf("/") + 1).replace("_", " ");
            System.out.println((i + 1) + ". " + title);
        }
    }

    public static void openURL(String url){
        try {
            Desktop.getDesktop().browse(new URL(url).toURI()); //открываем url в браузере
        } catch (Exception e) {
            System.err.println("Не удалось открыть URL в браузере: " + e.getMessage());
        }

    }
}
