package com.coship.app.mediaplayer.view.activity.music;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Adapter;
import android.widget.Toast;

import com.coship.app.mediaplayer.R;
import com.coship.app.mediaplayer.bean.music.Song;
import com.coship.app.mediaplayer.presenter.music.impl.ListPresenterImpl;
import com.coship.app.mediaplayer.presenter.music.interfaces.IListPresenter;
import com.coship.app.mediaplayer.service.PlayerService;
import com.coship.app.mediaplayer.view.adapter.PagesAdapter;
import com.coship.app.mediaplayer.view.fragment.ListFragment;
import com.coship.app.mediaplayer.view.interfaces.music.IListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 980558 on 2017/4/5.
 */

public class ListActivity extends AppCompatActivity implements IListView {
    private static final String TAG = "ListActivity";
    private TabLayout tabLayout;
    private ViewPager pages;

    private IListPresenter presenter;

    private List<String> title = new ArrayList<>();
    private List<Fragment> pageList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_list_activity);
        presenter = new ListPresenterImpl(this);
        getContentResolver().registerContentObserver(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,true,);
        initTablayout();
    }

    private void initTablayout() {
        title.add("所有音乐");
        title.add("歌手");
        title.add("专辑");
        title.add("文件夹");
        title.add("搜索");

        presenter.loadAllMusic();
        presenter.loadField(ListFragment.WHOIS_SINGER);
        presenter.loadField(ListFragment.WHOIS_ALBUM);
        presenter.loadField(ListFragment.WHOIS_FOLDER);
        pageList.add(new Fragment());

        tabLayout = (TabLayout) findViewById(R.id.tab_music_list);

        PagesAdapter adapter = new PagesAdapter(getSupportFragmentManager(), pageList, title);
        pages = (ViewPager) findViewById(R.id.pages_music_list);
        pages.setAdapter(adapter);

        tabLayout.setupWithViewPager(pages);
    }

    @Override
    public void showAllSong(Adapter adapter) {

    }


    @Override
    public void showField(Map<String, Integer> fieldMap, String field) {
        if (fieldMap.isEmpty()) {
            pageList.add(new Fragment());
            return;
        }

        List<Map<String, Object>> data = new ArrayList<>();
        ListFragment listFragment = ListFragment.newInstance(R.layout.music_list_field_item,
                new String[]{"image", "field", "count"},
                new int[]{R.id.item_field_image,
                        R.id.item_field_name,
                        R.id.item_field_song_count}, field);
        for (String key : fieldMap.keySet()) {
            Map<String, Object> map = new HashMap<>();
            map.put("image", null);
            map.put("field", key);
            map.put("count", "共 " + fieldMap.get(key) + " 首");
            data.add(map);
        }
        listFragment.setListData(data);
        pageList.add(listFragment);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showPlayer(int position) {
        Intent intent = new Intent(ListActivity.this, PlayActivity.class);
        Bundle bundle = new Bundle();
        Log.i(TAG, "showPlayer: "+presenter.getList());
        bundle.putSerializable("list", (Serializable) presenter.getList());
        bundle.putInt("position", position);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public IListPresenter getPresenter() {
        return presenter;
    }

    private final class AudioObserver extends ContentObserver{
        public AudioObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            presenter.update();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
