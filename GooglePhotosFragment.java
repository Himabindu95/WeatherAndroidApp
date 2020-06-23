package com.example.weatherapp.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.weatherapp.GoogleImagesRecyclerAdapter;
import com.example.weatherapp.R;

import org.json.JSONException;
import org.json.JSONObject;

public class GooglePhotosFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.googlephotos_fragment, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        final String[] images = new String[8];
        if(getArguments() != null) {

            String city = getArguments().getString("cities");
            String[] values = city.split(",");
            String q1 = values[0];
            String url = "http://hw9-vkanduku1995.us-east-2.elasticbeanstalk.com/api/googlephotos?city="+q1;

            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println("customsearch " + response.toString());
                            try {
                                String currentLocLatitude = "";
                                for(int i=0; i<8; i++) {
                                    currentLocLatitude  = response.getJSONArray("items").getJSONObject(i).getString("link");
                                    images[i] = currentLocLatitude;
                                }
                                recyclerView.setAdapter(new GoogleImagesRecyclerAdapter(getContext(), images));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();

                        }
                    });

            requestQueue.add(jsonObjectRequest);
        }
        return view;
    }
}
