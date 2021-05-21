package com.example.project_2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_2.Models.userDB;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupsFragment extends Fragment {
    private View ContactsView;
    private RecyclerView myContactsList;
    private SearchView searchView;
    private DatabaseReference UsersRef;
    ArrayList<userDB> modelList;

    private final String accountType="Herbalist Account";

    private FirebaseAuth mAuth;
    private String currentUserID="";


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GroupsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GroupsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GroupsFragment newInstance(String param1, String param2) {
        GroupsFragment fragment = new GroupsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ContactsView = inflater.inflate(R.layout.fragment_groups, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("users");

        myContactsList = (RecyclerView) ContactsView.findViewById(R.id.contacts_list);
        myContactsList.setLayoutManager(new LinearLayoutManager(getContext()));

        searchView=ContactsView.findViewById(R.id.search_herbalist);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                UsersRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            modelList=new ArrayList<>();
                            String visit_user_id =snapshot.getRef().getKey();
                            userDB user;

                            for(DataSnapshot ds:snapshot.getChildren()){
                                user=ds.getValue(userDB.class);
                                if(user.getAccountType() !=null && user.getUsername()!=null && user.getId()!=null&&currentUserID!=null ){

                                    if(user.getAccountType().equals(accountType)&& user.getUsername().equals(query)&&!user.getId().equals(currentUserID)) {
                                        modelList.add(ds.getValue(userDB.class));
                                    }
                                }
                            }

                            herbalistAdapter myadapter=new herbalistAdapter(modelList,getContext());
                            myContactsList.setAdapter(myadapter);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                UsersRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            modelList=new ArrayList<>();
                            String visit_user_id =snapshot.getRef().getKey();
                            userDB user;

                            for(DataSnapshot ds:snapshot.getChildren()){
                                user=ds.getValue(userDB.class);
                                if(user.getAccountType() !=null && user.getUsername()!=null&& user.getId()!=null &&currentUserID!=null){


                                    if(user.getAccountType().equals(accountType)&& user.getUsername().startsWith(newText)&& !user.getId().equals(currentUserID)) {
                                        modelList.add(ds.getValue(userDB.class));
                                    }
                                }
                            }

                            herbalistAdapter myadapter=new herbalistAdapter(modelList,getContext());
                            myContactsList.setAdapter(myadapter);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
               /* FirebaseRecyclerOptions<userDB> options =
                        new FirebaseRecyclerOptions.Builder<userDB>()
                                .setQuery(UsersRef.orderByChild("username").startAt(newText), userDB.class)
                                .build();

                FirebaseRecyclerAdapter<userDB, herbalistViewHolder> adapter =new FirebaseRecyclerAdapter<userDB, herbalistViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull herbalistViewHolder holder, int position, @NonNull userDB model) {

                        String visit_user_id = getRef(position).getKey();

                        if(model.getAccountType().equals(accountType)){

                            holder.userName.setText(model.getUsername());
                            holder.userStatus.setText(model.getEmail());
                            Glide.with(GroupsFragment.this).load(model.getProfile_image()).apply(new RequestOptions().centerCrop().placeholder(R.drawable.user)).into(holder.profileImage);

                            UsersRef.child(visit_user_id).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.child("userState").hasChild("state"))
                                    {
                                        String state = snapshot.child("userState").child("state").getValue().toString();
                                        String date = snapshot.child("userState").child("date").getValue().toString();
                                        String time = snapshot.child("userState").child("time").getValue().toString();

                                        if (state.equals("online"))
                                        {
                                            holder.onlineIcon.setVisibility(View.VISIBLE);
                                        }
                                        else if (state.equals("offline"))
                                        {
                                            holder.onlineIcon.setVisibility(View.INVISIBLE);
                                        }
                                    }
                                    else
                                    {
                                        holder.onlineIcon.setVisibility(View.INVISIBLE);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });


                        }
                        else{

                        }

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                Intent profileIntent = new Intent(getContext(), ProfileActivity.class);
                                profileIntent.putExtra("visit_user_id", visit_user_id);
                                startActivity(profileIntent);
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public herbalistViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_display_layout, viewGroup, false);
                        herbalistViewHolder viewHolder = new herbalistViewHolder(view);
                        return viewHolder;
                    }
                };
                myContactsList.setAdapter(adapter);
                adapter.startListening();*/

                return false;
            }
        });


        return ContactsView;
    }

    @Override
    public void onStart() {
        super.onStart();
        //userDB userProfile=snapshot.getValue(userDB.class);
        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    modelList=new ArrayList<>();
                    userDB user;

                    for(DataSnapshot ds:snapshot.getChildren()){
                        user=ds.getValue(userDB.class);
                        if(user.getAccountType() !=null&& user.getId()!=null&&currentUserID!=null){
                            if(user.getAccountType().equals(accountType)&& !user.getId().equals(currentUserID)) {
                                modelList.add(ds.getValue(userDB.class));
                            }
                        }
                    }

                    herbalistAdapter myadapter=new herbalistAdapter(modelList,getContext());
                    myContactsList.setAdapter(myadapter);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}