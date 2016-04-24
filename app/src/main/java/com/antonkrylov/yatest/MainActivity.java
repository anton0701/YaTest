package com.antonkrylov.yatest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
/*
    Автор: Крылов Антон

    MainActivity - главное Activity приложения. Здесь располагается ListView с муз. исполнителями.
    Сначала происходит инициализация listView и imageLoader'а - синглтона для скачивания изображений.
    Затем создается объект класса ListOfArtistsDownloader, который cкачивает и парсит JSON, создавая
    на выходе ArrayList объектов класса Artist. Кроме того, в ListOfArtistDownloader происходит проверка
    валидности всех URL и числовых значений.
    Затем на listView вешается кастомный адаптер класса ArtistAdapter и обработчик нажатия OnItemClickListener,
    который по нажатию на артиста делает переход на второе Activity. К этому моменту у нас есть вся информация
    об артистах, за исключением фотографий. Скачивание фотографий происходит в адаптере ArtistAdapter.

 */
public class MainActivity extends AppCompatActivity {

    ListView listView; //ListView со списком популярных исполнителей
    ArrayList<Artist> artistArrayList = null; //Arraylist артистов после парсинга JSONa
    ArtistAdapter artistAdapter = null; //Адаптер для listView

    public ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState != null) {

        } else {
            imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
            artistArrayList = new ArrayList<Artist>();
            listView = (ListView) findViewById(R.id.listView);

            try {
                ListOfArtistsDownloader downloader = new ListOfArtistsDownloader();
                artistArrayList = downloader.execute("http://download.cdn.yandex.net/mobilization-2016/artists.json").get();

                artistAdapter = new ArtistAdapter(this, artistArrayList);
                listView.setAdapter(artistAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent i = new Intent(getApplicationContext(), DetailActivity.class);
                        i.putExtra("name", artistArrayList.get(position).name);
                        i.putExtra("description", artistArrayList.get(position).description);
                        i.putExtra("link", String.valueOf(artistArrayList.get(position).link));
                        i.putExtra("coverSmallLink", String.valueOf(artistArrayList.get(position).coverSmallLink));
                        i.putExtra("coverBigLink", String.valueOf(artistArrayList.get(position).coverBigLink));
                        i.putExtra("tracks", String.valueOf(artistArrayList.get(position).tracks));
                        i.putExtra("albums", String.valueOf(artistArrayList.get(position).albums));
                        i.putExtra("genres", String.valueOf(artistArrayList.get(position).genresString));

                        startActivity(i);
                    }
                });

            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.i("InterruptedException", e.toString());
            } catch (ExecutionException e) {
                e.printStackTrace();
                Log.i("ExecutionException", e.toString());
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("AntotherException", e.toString());
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem item= menu.findItem(R.id.action_settings);
        item.setVisible(false);

        return super.onPrepareOptionsMenu(menu);
    }
}
