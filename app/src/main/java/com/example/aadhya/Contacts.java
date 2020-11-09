package com.example.aadhya;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class Contacts extends Fragment {
    RecyclerView rc;
    private ListView lv;
    ContactsAdapter ca;
    ArrayList<String> contactNames = new ArrayList<>();
    public static ArrayList<String> contactno = new ArrayList<>();
    ArrayList<Integer> imageid = new ArrayList<>();
    View v;
    FloatingActionButton b;
    public Drawable icon;
    String currentUserId;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    public void onDataChange() {
        currentUserId = currentUser.getUid();
        Query query = FirebaseDatabase.getInstance().getReference().child("User").child(currentUserId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Map<String, String> mp = new HashMap<>();
                    for (int i = 0; i < contactno.size(); i++)
                        mp.put(contactno.get(i), contactNames.get(i));
                    snapshot.getRef().child("Contacts").setValue(mp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_contacts, container, false);
        rc = v.findViewById(R.id.list);
        ca = new ContactsAdapter(getContext(), contactNames, contactno, imageid);
        rc.setAdapter(ca);
        rc.setLayoutManager(new LinearLayoutManager(getContext()));
        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(rc);

        currentUserId = currentUser.getUid();
        Query query = FirebaseDatabase.getInstance().getReference().child("User").child(currentUserId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Contacts").exists()) {
                    for (DataSnapshot item : snapshot.child("Contacts").getChildren()) {
                        if (!contactno.contains(item.getKey())) {
                            contactno.add(item.getKey());
                            contactNames.add((String) item.getValue());
                            imageid.add(R.drawable.user);
                            ca.notifyDataSetChanged();
                        }

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        b = v.findViewById(R.id.contactBtn);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(contactPickerIntent, 111);
            }
        });
        icon = ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.ic_baseline_delete_24);

        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 111) {
                Cursor cursor;
                try {
                    final String name, number, picture;
                    Uri uri = data.getData();
                    assert uri != null;
                    cursor = Objects.requireNonNull(getActivity()).getContentResolver().query(uri, null, null, null, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                    }
                    int phno = cursor != null ? cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER) : 0;
                    assert cursor != null;
                    int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

                    number = cursor.getString(phno);
                    name = cursor.getString(nameIndex);

                    if (contactno.indexOf(number) != -1) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AlertDialogCustom));
                        builder.setTitle("Duplicate contact");
                        builder.setMessage("Contact number already added!!");
                        builder.setNegativeButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        builder.show();
                    } else {
                        if (contactno.size() >= 5) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AlertDialogCustom));
                            builder.setTitle("Contact limit reached!");
                            builder.setMessage("Delete a few contacts to add others! Only 5 contacts can be added");
                            builder.setNegativeButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                            builder.show();
                        } else {
                            contactNames.add(name);
                            contactno.add(number);
                            onDataChange();
                            imageid.add(R.drawable.user);
                            ca.notifyDataSetChanged();
                        }

                    }
                    cursor.close();
                } catch (Exception e) {
                    StringWriter errors = new StringWriter();
                    e.printStackTrace(new PrintWriter(errors));
                    Log.i("hey", errors.toString());
                }
            }
        }
    }

    ItemTouchHelper.SimpleCallback itemTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {

        private ColorDrawable background = new ColorDrawable(Color.RED);

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
            final String tempName;
            final String tempNo;
            final int tempImgid, position;
            position = viewHolder.getAdapterPosition();
            tempImgid = imageid.get(position);
            tempName = contactNames.get(position);
            tempNo = contactno.get(position);
            contactNames.remove(viewHolder.getAdapterPosition());
            contactno.remove(viewHolder.getAdapterPosition());
            imageid.remove(viewHolder.getAdapterPosition());
            ca.notifyDataSetChanged();
            onDataChange();
            Snackbar snackbar = Snackbar.make(Objects.requireNonNull(getView()), "You just deleted a contact", BaseTransientBottomBar.LENGTH_LONG);
            snackbar.setDuration(3000);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    contactNames.add(position, tempName);
                    contactno.add(position, tempNo);
                    imageid.add(position, tempImgid);
                    ca.notifyDataSetChanged();
                    onDataChange();
                }
            });
            snackbar.show();
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            View itemView = viewHolder.itemView;
            int backgroundCornerOffset = 20; //so background is behind the rounded corners of itemView
            int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
            int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
            int iconBottom = iconTop + icon.getIntrinsicHeight();
            if (dX > 0) { // Swiping to the right
                int iconLeft = itemView.getLeft() + iconMargin;
                int iconRight = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                background.setBounds(itemView.getLeft(), itemView.getTop(),
                        itemView.getLeft() + ((int) dX) + backgroundCornerOffset, itemView.getBottom());
            } else if (dX < 0) { // Swiping to the left
                int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
                int iconRight = itemView.getRight() - iconMargin;
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                        itemView.getTop(), itemView.getRight(), itemView.getBottom());
            } else { // view is unSwiped
                icon.setBounds(0, 0, 0, 0);
                background.setBounds(0, 0, 0, 0);
            }
            background.draw(c);
            icon.draw(c);
        }

    };

}