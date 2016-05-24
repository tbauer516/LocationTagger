package edu.uw.group2.locationtagger;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class TagList extends AppCompatActivity {

    private static final String TAG = "TAGLIST";

    private NoteListAdapter mNoteListAdapter;
    private static final String FIREBASE_URL = "https://location-tagger.firebaseio.com/notes/posts";
    private Firebase mFirebaseRef;
    private ValueEventListener mConnectedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_list);

        Firebase.setAndroidContext(this);
        mFirebaseRef = new Firebase(FIREBASE_URL);


//        Firebase ref = new Firebase("https://location-tagger.firebaseio.com/notes/posts");
//        //Firebase ref = new Firebase("https://test-2107d.firebaseio.com/");
//
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                System.out.println(dataSnapshot.getChildrenCount() + " posts");
//                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
//                    Note post = postSnapshot.getValue(Note.class);
//                    System.out.println(post.getTitle() + post.getAuthor() + post.getDescription() + post.getUid()+ post.getDateTime() + post.getLat() + post.getLng());
//                }
//
//               // System.out.println(dataSnapshot.getValue());
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//                System.out.println("The read failed: " + firebaseError.getMessage());
//            }
//        });



    }

    @Override
    protected void onStart() {
        super.onStart();

        final ListView listView = (ListView)findViewById(R.id.tagListView);

        mNoteListAdapter = new NoteListAdapter(mFirebaseRef, this, R.layout.list_view);
        listView.setAdapter(mNoteListAdapter);
        mNoteListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(mNoteListAdapter.getCount() - 1);
            }
        });

        // Finally, a little indication of connection status
        mConnectedListener = mFirebaseRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = (Boolean) dataSnapshot.getValue();
                if (connected) {
                    Toast.makeText(TagList.this, "Connected to Firebase", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TagList.this, "Disconnected from Firebase", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // No-op
            }
        });

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });

    }

    @Override
    public void onStop() {
        super.onStop();
        mFirebaseRef.getRoot().child(".info/connected").removeEventListener(mConnectedListener);
        mNoteListAdapter.cleanup();
    }


}