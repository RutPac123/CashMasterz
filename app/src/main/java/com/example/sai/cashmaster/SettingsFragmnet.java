package com.example.sai.cashmaster;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;

import static maes.tech.intentanim.CustomIntent.customType;

public class SettingsFragmnet extends Fragment {

    private ListView listView;
    private Activity context;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       context = getActivity();
        return inflater.inflate(R.layout.fragment_settings,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = context.findViewById(R.id.listset);

        ArrayList<String> arrayList =new ArrayList<>();
        arrayList.add("How to use the app?");
        arrayList.add("Change app language");

        ArrayAdapter arrayAdapter =new ArrayAdapter(context,android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {
                     Intent intent =new Intent(context,howto.class);
                     startActivity(intent);
                    customType(context,"bottom-to-up");
                }

            }
        });
    }

}
