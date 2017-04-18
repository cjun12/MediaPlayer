package com.coship.app.mediaplayer.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.coship.app.mediaplayer.R;
import com.coship.app.mediaplayer.bean.music.Song;
import com.coship.app.mediaplayer.presenter.music.interfaces.IListPresenter;
import com.coship.app.mediaplayer.view.activity.music.ListActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by 980558 on 2017/4/7.
 */

public class ListFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String TAG = "ListFragment";
    public final static String WHOIS_SINGER = "Singer";
    public final static String WHOIS_ALBUM = "Album";
    public final static String WHOIS_FOLDER = "Folder";
    public final static String WHOIS_ALLSONG = "AllSong";
    public final static String WHOIS_SONG = "Song";
    private int position =0;
    private String listParent = null;

    private int itemLayout;
    private SimpleAdapter songAdapter = null;
    private List<Map<String, Object>> listData = new ArrayList<>();
    private String[] from;
    private int[] to;
    private String whois;

    private ListView lv_lists;
    private View view;

    @Override
    public void setArguments(Bundle args) {
        this.itemLayout = args.getInt("itemLayout");
        this.from = args.getStringArray("from");
        this.to = args.getIntArray("to");
        this.whois = args.getString("whois");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.music_list_fragment, container, false);
        return view;
    }

    @Override
    public void onStart() {
        Log.i(TAG, "onStart: " +toString());
        super.onStart();
        initList();
    }

    private void initList() {
        lv_lists = (ListView) view.findViewById(R.id.lv_music_list);
        if(!WHOIS_SONG.equals(whois)) {
            songAdapter = new SimpleAdapter(view.getContext(), listData, itemLayout, from, to);
        }
        else{
            songAdapter = new SimpleAdapter(view.getContext(),listData,R.layout.music_list_allsong_item,
                    new String[]{"name"}, new int[]{R.id.item_song_name});
        }
        lv_lists.setAdapter(songAdapter);
        lv_lists.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "onItemClick: "+listData);
        Log.i(TAG, "onItemClick: "+whois+" "+position);
        if (whois == null)
            return;
        switch (whois) {
            case WHOIS_ALLSONG:
                ((ListActivity) getActivity()).showPlayer(position);
                break;
            case WHOIS_SONG:
                if (position == 0) {
                    showSongClassify(listParent);
                    whois = listParent;
                } else {
                    ((ListActivity) getActivity()).showPlayer(position - 1);
                }
                break;
            default:
                showFieldSong(whois, position);
                listParent = whois;
                this.position=position;
                whois = WHOIS_SONG;
        }
    }

    private void showSongClassify(String field) {
        Map<String, Integer> fieldMap = ((ListActivity) getActivity()).getPresenter().getField(field);
        listData = new ArrayList<>();
        Log.i(TAG, "showSongClassify: "+fieldMap);
        for (String key : fieldMap.keySet()) {
            Map<String, Object> map = new HashMap<>();
            map.put("image", null);
            map.put("field", key);
            map.put("count", "共 " + fieldMap.get(key) + " 首");
            listData.add(map);
        }
        songAdapter = new SimpleAdapter(view.getContext(), listData, R.layout.music_list_field_item
                , new String[]{"image", "field", "count"}
                , new int[]{R.id.item_field_image, R.id.item_field_name, R.id.item_field_song_count});
        lv_lists.setAdapter(songAdapter);
        songAdapter.notifyDataSetChanged();
    }

    private void showFieldSong(String field, int pos) {
        Map<String, Object> item = listData.get(pos);
        IListPresenter presenter = ((ListActivity) getActivity()).getPresenter();
        presenter.loadFieldSong(field, (String) item.get("field"));
        listData = new ArrayList<>();
        List<Song> songs = presenter.getList();
        item = new HashMap<>();
        item.put("name","返回上一层");
        listData.add(item);
        for (Song song : songs) {
            item = new HashMap<>();
            item.put("name", song.getName());
            listData.add(item);
        }
        songAdapter = new SimpleAdapter(view.getContext(), listData, R.layout.music_list_allsong_item
                , new String[]{"name"}, new int[]{R.id.item_song_name});
        Log.i(TAG, "showFieldSong: " + songs.toString());
        lv_lists.setAdapter(songAdapter);
        songAdapter.notifyDataSetChanged();
    }

    public static ListFragment newInstance(int itemLayout, String[] from, int[] to, String whois) {
        Bundle args = new Bundle();
        args.putInt("itemLayout", itemLayout);
        args.putStringArray("from", from);
        args.putIntArray("to", to);
        args.putString("whois", whois);
        ListFragment fragment = new ListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setListData(List<Map<String, Object>> listData) {
        this.listData.addAll(listData);
    }

    @Override
    public String toString() {
        return "ListFragment{" +
                "listParent='" + listParent + '\'' +
                ", listData=" + listData +
                ", from=" + Arrays.toString(from) +
                ", to=" + Arrays.toString(to) +
                ", whois='" + whois + '\'' +
                '}';
    }

    @Override
    public void onDestroy() {
        listData.clear();
        listData=null;
        super.onDestroy();
    }
}
