package example.com.timtro.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import example.com.timtro.activity.ChiTietOGhepActivity;
import example.com.timtro.activity.MapActivity;
import example.com.timtro.R;
import example.com.timtro.adapters.CustomListItemTimOGhep;
import example.com.timtro.models.TimNguoiOGhep;

/**
 * Created by User on 11/21/2017.
 */

public class FragmentListOGhep extends Fragment {
    private ListView listView;
    private CustomListItemTimOGhep adapter=null;
    private ArrayList<TimNguoiOGhep> list;
    private DatabaseReference databaseReference;
    private DatabaseReference mDatabase;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_oghep,container,false);
        mDatabase = FirebaseDatabase.getInstance().getReference("thongtin");
        databaseReference = mDatabase.child("nguoioghep");

        listView=(ListView) view.findViewById(R.id.lv1);
        list =new ArrayList<TimNguoiOGhep>();
        listView.setAdapter(adapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {
                    TimNguoiOGhep note = noteSnapshot.getValue(TimNguoiOGhep.class);
                    list.add(note);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        final MapActivity mainActivity= (MapActivity) getActivity();

        adapter=new CustomListItemTimOGhep(mainActivity,R.layout.custom_listview_timoghep,list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                chuyenactivity(position);
            }
        });
        return view;
    }

    private void chuyenactivity(int i){
        Intent intent = new Intent(getActivity(), ChiTietOGhepActivity.class);
        Bundle bundle=new Bundle();
        TimNguoiOGhep timNguoiOGhep = list.get(i);
        bundle.putSerializable("dulieu",timNguoiOGhep );
        intent.putExtras(bundle);
        startActivity(intent);
    }
}