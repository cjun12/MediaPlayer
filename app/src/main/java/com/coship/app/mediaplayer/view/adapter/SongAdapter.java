package com.coship.app.mediaplayer.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.coship.app.mediaplayer.bean.music.Song;

import java.util.List;

/**
 * Created by 980558 on 2017/4/18.
 */

public class SongAdapter extends BaseAdapter implements Filterable {
    //mTo 控件顺序
    private final static int NAME = 0;
    private final static int SINGER = 1;
    private final static int ALBUM = 2;
    private final static int FOLDER = 3;
    private final static int ALBUMPATH = 4;

    //布局无该控件
    public final static int NOCONTROL = -1;

    private Context mContext;
    private int mResource;
    private int[] mTo;
    private List<Song> mSongs;
    private LayoutInflater mInflater;


    /**
     *
     * @param context
     * @param resource
     * @param to 控件资源ID，按照Name,Singer,Album,Folder,AlbumPath输入
     * @param songs
     */
    public SongAdapter(@NonNull Context context, @LayoutRes int resource,
                       @IdRes int[] to, @NonNull List<Song> songs) {
        if(to.length!=5)
        {
            throw new IllegalArgumentException("Count of controlId should equal 5");
        }
        mContext = context;
        mResource = resource;
        mTo = to;
        mSongs = songs;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mSongs.size();
    }

    @Override
    public Object getItem(int position) {
        return mSongs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = mInflater.inflate(mResource, parent, false);
        } else {
            view = convertView;
        }
        bindView(position, view);
        return view;
    }

    private void bindView(int position, View view) {
        Song song = mSongs.get(position);
        if (song == null) {
            return;
        }
        View name = view.findViewById(mTo[NAME]);
        setViewText((TextView) name, song.getName());

        View singer = view.findViewById(mTo[SINGER]);
        setViewText((TextView) singer, song.getSinger());

        View album = view.findViewById(mTo[ALBUM]);
        setViewText((TextView) album, song.getAlbum());

        View folder = view.findViewById(mTo[FOLDER]);
        setViewText((TextView) folder, song.getFolder());

        View albumPath = view.findViewById(mTo[ALBUMPATH]);
        setViewImage((ImageView) albumPath, song.getAlbumPath());
    }

    public void setViewText(TextView v, String text) {
        v.setText(text);
    }

    public void setViewImage(ImageView v, String value) {
        try {
            v.setImageResource(Integer.parseInt(value));
        } catch (NumberFormatException nfe) {
            v.setImageURI(Uri.parse(value));
        }
    }

    @Override
    public Filter getFilter() {
        return null;
    }
}
