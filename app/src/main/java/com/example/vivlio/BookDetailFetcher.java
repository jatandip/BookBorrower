package com.example.vivlio;

import com.google.gson.stream.JsonReader;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * This class fetches information from the google books api.
 * At the moment it only retrieves a book's title and author, which
 * can be accessed with {@code getTitle()} and {@code getAuthor()}.
 */
public class BookDetailFetcher {
    private URL url;
    private HttpURLConnection urlConnection;
    private String title;
    private String authors;
//    private Map<String, String> parsedData;

    /**
     * This method sends a HTTP request to the google books api,
     * then calls {@code readStream(InputStream)} to read and parse the
     * sequence of bytes.
     * @param isbn The ISBN code used as a search term
     */
    // TODO: change method returns to hashmap instead of list for better readability
    public void request(String isbn) {

        try {
            url = new URL("https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn);
            urlConnection = (HttpURLConnection) url.openConnection();

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            readStream(in);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            urlConnection.getErrorStream();
        } finally {
            urlConnection.disconnect();
        }
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return authors;
    }

    /**
     * Turns the input stream into a JSON object and reads/parses for the book's
     * title and authors. Saves them under their respective class attributes.
     * @param inStream The stream of bytes returned from the HTTP request
     * @throws IOException If an I/O error occurs in JsonReader methods
     */
    private void readStream(InputStream inStream) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(inStream, "UTF-8"));

        List<String> parsedDataArray = new ArrayList<String>();

        reader.beginObject();
        while (reader.hasNext() && parsedDataArray == null) {
            String name = reader.nextName();
            if (name.equals("items")) {
                parsedDataArray = readItemsArray(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        title = parsedDataArray.get(0);
        authors = parsedDataArray.get(1);

//        parsedData.put("title", title);
//        parsedData.put("author", authors);
    }

    /**
     * Reads items array in the JSON object
     * @param reader JsonReader object from {@code readStream(InputStream)}
     * @return Returns a list filled with the book's title and author
     * @throws IOException If an I/O error occurs in {@code JsonReader} methods
     */
    private List<String> readItemsArray(JsonReader reader) throws IOException {
        List<String> infoArray = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext() && infoArray == null) {
            reader.beginObject();
            while (reader.hasNext() && infoArray == null) {
                String name = reader.nextName();
                if (name.equals("volumeInfo")) {
                    infoArray = readVolumeInfo(reader);
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
        }
        reader.endArray();

        return infoArray;
    }

    /**
     * Reads volume info within the items array in JSON
     * @param reader JsonReader object from {@code readItemsArray(JsonReader)}
     * @return Returns a list filled with the book's title and author
     * @throws IOException
     */
    private List<String> readVolumeInfo(JsonReader reader) throws IOException {
        String title = null;
        String authors = null;
        List<String> bookInfo = new ArrayList<String>();
//        List<String> authors = new ArrayList<String>();

        reader.beginObject();
        while (reader.hasNext() && (title == null || authors == null)) {
            String name = reader.nextName();
            if (name.equals("title")) {
                title = reader.nextString();
            } else if (name.equals("authors")) {
                reader.beginArray();
//                while (reader.hasNext()) {
                while (reader.hasNext() && authors == null) {
                    authors = reader.nextString();
//                    String author = reader.nextString();
//                    authors.add(author);
                }
                reader.endArray();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        bookInfo.addAll(Arrays.asList(title, authors));

        return bookInfo;
    }

//    private String readAuthors(JsonReader reader) throws IOException {
//        reader.beginObject();
//        while (reader.hasNext()) {
//
//        }
//    }
}
