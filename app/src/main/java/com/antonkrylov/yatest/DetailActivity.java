package com.antonkrylov.yatest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
/*
    DetailActivity - второй Activity приложения, оно запускается после клика по артисту из listView
    на первом Activity, и содержит в себе полную информацию об артисте и его большое изображение.
    При запуске, после заполнения всех строк, синглтон imageLoader начинает в фоне скачивать (и кэшировать)
    большую фотографию артиста. После скачивания, он передает ее в объект класса ProfileImageView - кастомный
    ImageView, в котором переопределен метод setImageBitmap, так что ProfileImageView сам масштабирует
    и обрезает картинку под нужный размер.
 */
public class DetailActivity extends AppCompatActivity {

    TextView descriptionTextView;
    TextView outerLinkTextView;
    TextView albumsTracksTextView;
    TextView genresTextView;
    ProfileImageView avatarImageView;

    String coverSmallLink;
    String coverBigLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("Second Activity. On create");
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //final FrameLayout fr = (FrameLayout)findViewById(R.id.frameLayout2);

        avatarImageView = (ProfileImageView)findViewById(R.id.imageView);
        descriptionTextView = (TextView)findViewById(R.id.descriptionTextView);
        albumsTracksTextView = (TextView)findViewById(R.id.albumsTracksTextView);
        outerLinkTextView = (TextView)findViewById(R.id.outerLinkTextView);
        genresTextView = (TextView)findViewById(R.id.genresTextView);

        outerLinkTextView.setMovementMethod(LinkMovementMethod.getInstance());

        Intent i = getIntent();
        String name = i.getStringExtra("name");
        String description = i.getStringExtra("description");
        String genres = i.getStringExtra("genres");
        String linkString = i.getStringExtra("link");
        String linkHtml = "<a href='" + linkString + "'> Перейти на сайт </a>";
        coverSmallLink = i.getStringExtra("coverSmallLink");
        coverBigLink = i.getStringExtra("coverBigLink");

        getSupportActionBar().setTitle(name);

        String tracksString = i.getStringExtra("tracks");
        String albumsString = i.getStringExtra("albums");

        Integer albumsInt = Integer.parseInt(albumsString);
        Integer songsInt = Integer.parseInt(tracksString);

        if (albumsInt != null & songsInt != null) {
            String albumSongQty = albumsString + " альбом" + WordEnding.albumEnding(albumsInt)
                    + ", " + tracksString + " пес" + WordEnding.songEnding(songsInt);
            albumsTracksTextView.setText(albumSongQty);
            albumsTracksTextView.setVisibility(View.VISIBLE);
        }
        else {
            albumsTracksTextView.setVisibility(View.INVISIBLE);
        }

        if (!linkString.equals("null")) {
            outerLinkTextView.setText(Html.fromHtml(linkHtml));
            outerLinkTextView.setVisibility(View.VISIBLE);
        }
        else {
            outerLinkTextView.setVisibility(View.INVISIBLE);
        }
        if (!description.equals("null")) {
            descriptionTextView.setText(description);
            descriptionTextView.setVisibility(View.VISIBLE);
        }
        else {
            descriptionTextView.setVisibility(View.INVISIBLE);
        }
        if (!genres.equals("null")) {
            genresTextView.setText(genres);
            genresTextView.setVisibility(View.VISIBLE);
        }
        else {
            genresTextView.setVisibility(View.INVISIBLE);
        }
    }

    protected void onStart() {
        super.onStart();
        if (avatarImageView != null) {
            if (!coverBigLink.equals("null")) {

                ImageLoader imageLoader = ImageLoader.getInstance();
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .showImageOnLoading(R.drawable.download)
                        .cacheInMemory(true)
                        .cacheOnDisk(true)
                        .build();

                imageLoader.loadImage(coverBigLink, options, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                    }
                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    }
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        avatarImageView.setImageBitmap(loadedImage);
                    }
                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                    }
                });
            }
        }
    }
}
