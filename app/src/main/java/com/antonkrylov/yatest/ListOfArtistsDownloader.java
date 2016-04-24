package com.antonkrylov.yatest;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/*
    ListOfArtistsDownloader - класс-наследник AsyncTask, созданный для скачивания JSON объекта и его парсинга.
    При парсинге проверяется валидность данных, если какое-то поле невалидно, то в это поле пишется null.
    На выходе - ArrayList, объекты которого имеют класс Artist.
*/
class ListOfArtistsDownloader extends AsyncTask<String, Void, ArrayList<Artist>> {
    private JSONArray jsonArray = null;
    private ArrayList<Artist> artists = new ArrayList<Artist>();

    @Override
    protected ArrayList<Artist> doInBackground(String... params) {
        try {

            URL url = new URL(params[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            InputStream in = connection.getInputStream();
            connection.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            //resultString - строка = содержимое сайта
            String resultString = String.valueOf(result);

            //jsonArray - массив JSONObject'ов, каждый - описание 1 артиста
            jsonArray = new JSONArray(resultString);

            //Перебираем массив, создаем объект класса Artist, заполнеяем его
            //Проверка на КОРРЕКТНОСТЬ ДАННЫХ ПРОИСХОДИТ ЗДЕСЬ!!!
            //После этого, гарантируем, что данные корректны

            //Если нет имени, то остальное все не нужно. Если нет другого параметра, то парсим то, что есть
            for (int i=0; i<jsonArray.length(); i++) {
                try {
                    JSONObject artistJSON = jsonArray.getJSONObject(i);
                    Artist artist = new Artist();
                    artist.name = artistJSON.getString("name");//Если exception, то выполнится (1) и начнет парсить след артиста
                    //По каждому полю проверяем его наличие, тип, валидность(URL)
                    if(artistJSON.has("tracks")) {
                        if(artistJSON.get("tracks") instanceof Integer) {
                            artist.tracks = (Integer)artistJSON.get("tracks");
                        }
                    }

                    if(artistJSON.has("albums")) {
                        if(artistJSON.get("albums") instanceof Integer) {
                            artist.albums = (Integer)artistJSON.get("albums");
                        }
                    }

                    if(artistJSON.has("description")) {
                        if(artistJSON.get("description") instanceof String) {
                            String tempDesc = artistJSON.getString("description");
                            if(tempDesc != null & tempDesc.length() > 0) {
                                tempDesc = tempDesc.substring(0, 1).toUpperCase() + tempDesc.substring(1);
                            }
                            artist.description = tempDesc;
                        }
                    }

                    if(artistJSON.has("link")) {
                        if(artistJSON.get("link") instanceof String) {
                            try {
                                URL link = new URL(String.valueOf(artistJSON.get("link")));
                                artist.link = link;
                            }
                            catch (MalformedURLException e) {
                                Log.i("Bad artist link", String.valueOf(e));
                            }
                        }
                    }

                    if(artistJSON.has("genres")) {
                        try {
                            JSONArray genresJSONArray = artistJSON.getJSONArray("genres");
                            ArrayList<String> genresArrayList = new ArrayList<String>();
                            for (int k = 0; k < genresJSONArray.length(); k++) {
                                genresArrayList.add(String.valueOf(genresJSONArray.get(k)));
                            }
                            artist.genres = genresArrayList;
                            String tempGenresString = String.valueOf(genresArrayList);
                            artist.genresString = tempGenresString.substring(1, tempGenresString.length()-1);
                        } catch (Exception e) {
                            Log.i("Bad genres", String.valueOf(e));
                        }
                    }


                    if(artistJSON.has("cover")) {
                        try {
                            JSONObject coverJSONArray = (JSONObject)artistJSON.get("cover");

                            String smallCoverString = coverJSONArray.getString("small");
                            URL smallCoverURL = new URL(smallCoverString);
                            artist.coverSmallLink = smallCoverURL;

                            String bigCoverString = coverJSONArray.getString("big");
                            URL bigCoverURL = new URL(bigCoverString);
                            artist.coverBigLink = bigCoverURL;
                        }
                        catch (Exception e) {
                            Log.i("Bad covers", String.valueOf(e));
                        }
                    }
                    artists.add(artist);
                }
                catch (JSONException e) {//(1)
                    Log.i("Wrong JSON artist obj!", String.valueOf(e));
                }
            }
        }
        catch (MalformedURLException e) { //Битая ссылка
            Log.i("MalformedURLException", String.valueOf(e));
            e.printStackTrace();
        }
        catch (IOException e) { //Плохое соединение с инетом
            Log.i("MalformedURLException", String.valueOf(e));
            e.printStackTrace();
        }
        catch (JSONException e) { //Не получилось сделать JSON(array?)
            e.printStackTrace();
        }

        return artists;
    }
}