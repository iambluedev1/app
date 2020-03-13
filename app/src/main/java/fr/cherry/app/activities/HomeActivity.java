package fr.cherry.app.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;
import com.wangjie.rapidfloatingactionbutton.util.RFABShape;
import com.wangjie.rapidfloatingactionbutton.util.RFABTextUtil;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import fr.cherry.app.Cherry;
import fr.cherry.app.R;
import fr.cherry.app.models.ListModel;
import fr.cherry.app.adapters.ListModelAdapter;

public class HomeActivity extends AppCompatActivity implements RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener {

    private ListModelAdapter mAdapter;
    private RapidFloatingActionLayout rfaLayout;
    private RapidFloatingActionButton rfaButton;
    private RapidFloatingActionHelper rfabHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SwipeMenuListView listView = findViewById(R.id.listNotes);
        mAdapter = new ListModelAdapter(this);
        listView.setAdapter(mAdapter);


        SwipeMenuCreator creator = menu -> {
            SwipeMenuItem shareItem = new SwipeMenuItem(getApplicationContext());
            shareItem.setBackground(new ColorDrawable(Color.rgb(119,181, 254)));
            shareItem.setWidth(170);
            shareItem.setIcon(R.drawable.ic_share_24px);
            menu.addMenuItem(shareItem);

            SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
            deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,0x3F, 0x25)));
            deleteItem.setWidth(170);
            deleteItem.setIcon(R.drawable.ic_delete_24px);
            menu.addMenuItem(deleteItem);
        };

        listView.setMenuCreator(creator);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            ListModel m = Cherry.getInstance().getNotes().get(position);
            if(m.getType() == 0){
                Intent intent = new Intent(HomeActivity.this, EditNoteActivity.class);
                intent.putExtra("item", m.getId());
                startActivityForResult(intent, 0);
            }
        });
        listView.setOnMenuItemClickListener((position, menu, index) -> {
            switch (index) {
                case 0:
                    Intent intent = new Intent(HomeActivity.this, ShareNote.class);
                    intent.putExtra("item", position);
                    intent.putExtra("from", "main");
                    startActivity(intent);
                    break;
                case 1:
                    Cherry.getInstance().removeNote(position);

                    mAdapter.notifyDataSetChanged();
                    SwipeMenuListView listView1 = findViewById(R.id.listNotes);
                    listView1.invalidateViews();
                    listView1.refreshDrawableState();
                    break;
            }
            return false;
        });

        rfaLayout = findViewById(R.id.activity_main_rfal);
        rfaButton = findViewById(R.id.activity_main_rfab);

        RapidFloatingActionContentLabelList rfaContent = new RapidFloatingActionContentLabelList(this);
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);
        List<RFACLabelItem> items = new ArrayList<>();
        items.add(new RFACLabelItem<Integer>()
                .setLabel("créer une note")
                .setIconNormalColor(0xffd84315)
                .setIconPressedColor(0xffbf360c)
                .setWrapper(0)
        );
        items.add(new RFACLabelItem<Integer>()
                        .setLabel("créer une fiche de révision")
                        .setIconNormalColor(0xff4e342e)
                        .setIconPressedColor(0xff3e2723)
                        .setLabelColor(Color.WHITE)
                        .setLabelSizeSp(14)
                        .setLabelBackgroundDrawable(RFABShape.generateCornerShapeDrawable(0xaa000000, RFABTextUtil.dip2px(this, 4)))
                        .setWrapper(1)
        );
        rfaContent
                .setItems(items)
                .setIconShadowRadius(RFABTextUtil.dip2px(this, 5))
                .setIconShadowColor(0xff888888)
                .setIconShadowDy(RFABTextUtil.dip2px(this, 5))
        ;

        rfabHelper = new RapidFloatingActionHelper(
                this,
                rfaLayout,
                rfaButton,
                rfaContent
        ).build();

        ExtendedFloatingActionButton fab = findViewById(R.id.btnMyAccount);
        fab.setOnClickListener(view -> startActivity(new Intent(HomeActivity.this, MyAccountActivity.class)));
    }

    @Override
    public void onRFACItemLabelClick(int position, RFACLabelItem item) {
        rfabHelper.toggleContent();
        if(position == 0){
            Intent intent = new Intent(this, CreateNoteActivity.class);
            startActivityForResult(intent,0);
        }
    }

    @Override
    public void onRFACItemIconClick(int position, RFACLabelItem item) {
        rfabHelper.toggleContent();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("qsdqsdqd", "resulllt");
        mAdapter.notifyDataSetChanged();

        SwipeMenuListView listView = findViewById(R.id.listNotes);
        mAdapter = new ListModelAdapter(this);
        listView.setAdapter(mAdapter);
    }
}
