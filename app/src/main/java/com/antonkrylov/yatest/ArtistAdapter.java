package com.antonkrylov.yatest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/*
    ArtistAdapter - адаптер listView, он необходим для создания и заполнения ячеек listView инфор-
    мацией об артистах. ArtistAdapter переиспользует ячейки, которые стали невидимыми, тем самым экономя
    память. ArtistAdapter запускает фоновые задачи на скачивание и кэширование маленьких изображений.
*/

public class ArtistAdapter extends ArrayAdapter<Artist> {

    private static class ViewHolder {
        TextView firstLine;
        TextView secondLine;
        TextView albumSongsTextView;
        ImageView icon;
    }

    public ArtistAdapter(Context context, ArrayList<Artist> artists) {
        super(context, 0, artists);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Artist artist = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.single_row, parent, false);

            viewHolder.firstLine = (TextView) convertView.findViewById(R.id.firstLine);
            viewHolder.secondLine = (TextView) convertView.findViewById(R.id.secondLine);
            viewHolder.albumSongsTextView = (TextView) convertView.findViewById(R.id.albumSongsTextView);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.firstLine.setText(artist.name);
        viewHolder.secondLine.setText(String.valueOf(artist.genresString));
        String albumSongQty = String.valueOf(artist.albums) + " альбом" + WordEnding.albumEnding(artist.albums)
                + ", " + String.valueOf(artist.tracks) + " пес" + WordEnding.songEnding(artist.tracks);
        viewHolder.albumSongsTextView.setText(albumSongQty);

        if (viewHolder.icon != null) {
            if (artist.coverSmallLink != null) {
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        //.showImageOnLoading(R.drawable.download)
                        .showImageForEmptyUri(R.drawable.error)
                        .showImageOnFail(R.drawable.error)
                        .resetViewBeforeLoading(true)
                        .cacheInMemory(true)
                        .cacheOnDisk(true)
                        .build();

                ImageLoader.getInstance().displayImage(String.valueOf(artist.coverSmallLink), viewHolder.icon, options);
            }
        }
        return convertView;
    }
}
