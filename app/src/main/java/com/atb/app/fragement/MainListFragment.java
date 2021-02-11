package com.atb.app.fragement;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListView;

import com.atb.app.R;
import com.atb.app.activities.MainActivity;
import com.atb.app.adapter.MainFeedAdapter;
import com.atb.app.model.NewsFeedEntity;

import java.util.ArrayList;

public class MainListFragment extends Fragment {
    View view;
    ListView list_main;
    Context context;
    ArrayList<NewsFeedEntity>newsFeedEntities = new ArrayList<>();
    MainFeedAdapter mainFeedAdapter ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list_main = (ListView)view.findViewById(R.id.list_main);

    }

    @Override
    public void onResume() {
        super.onResume();
        context =getActivity();
        getList();
    }

    void getList(){

        for(int i =0;i<10;i++){
            NewsFeedEntity newsFeedEntity = new NewsFeedEntity();
            newsFeedEntity.setId(i);
            for(int j=0;j<i;j++){
                NewsFeedEntity subentity = new NewsFeedEntity();
                subentity.setId(j);
                newsFeedEntity.getPostEntities().add(subentity);
            }
            newsFeedEntities.add(newsFeedEntity);
        }

        mainFeedAdapter = new MainFeedAdapter(context,this);
        list_main.setAdapter(mainFeedAdapter);
        mainFeedAdapter.setData(newsFeedEntities);
    }
}