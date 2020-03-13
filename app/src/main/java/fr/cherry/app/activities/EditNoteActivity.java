package fr.cherry.app.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import com.chinalwb.are.AREditText;
import com.chinalwb.are.styles.toolbar.ARE_ToolbarDefault;
import com.chinalwb.are.styles.toolbar.IARE_Toolbar;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_AlignmentCenter;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_AlignmentLeft;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_AlignmentRight;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_At;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Bold;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Hr;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Image;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Italic;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Link;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_ListBullet;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_ListNumber;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Quote;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Strikethrough;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Subscript;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Superscript;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Underline;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Video;
import com.chinalwb.are.styles.toolitems.IARE_ToolItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import fr.cherry.app.Cherry;
import fr.cherry.app.R;
import fr.cherry.app.models.ListModel;

public class EditNoteActivity extends AppCompatActivity {

    private IARE_Toolbar mToolbar;
    private AREditText mEditText;
    private boolean scrollerAtEnd;
    private ListModel listModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        listModel = Cherry.getInstance().getNotes().stream().filter(u -> u.getId() == intent.getIntExtra("item", 0)).findFirst().get();
        Log.d("qsd0", intent.getIntExtra("item", 0)+"");
        initToolbar();
    }

    private void initToolbar() {
        mToolbar = this.findViewById(R.id.areToolbar);
        IARE_ToolItem bold = new ARE_ToolItem_Bold();
        IARE_ToolItem italic = new ARE_ToolItem_Italic();
        IARE_ToolItem underline = new ARE_ToolItem_Underline();
        IARE_ToolItem strikethrough = new ARE_ToolItem_Strikethrough();
        IARE_ToolItem quote = new ARE_ToolItem_Quote();
        IARE_ToolItem listNumber = new ARE_ToolItem_ListNumber();
        IARE_ToolItem listBullet = new ARE_ToolItem_ListBullet();
        IARE_ToolItem hr = new ARE_ToolItem_Hr();
        IARE_ToolItem link = new ARE_ToolItem_Link();
        IARE_ToolItem subscript = new ARE_ToolItem_Subscript();
        IARE_ToolItem superscript = new ARE_ToolItem_Superscript();
        IARE_ToolItem left = new ARE_ToolItem_AlignmentLeft();
        IARE_ToolItem center = new ARE_ToolItem_AlignmentCenter();
        IARE_ToolItem right = new ARE_ToolItem_AlignmentRight();
        IARE_ToolItem image = new ARE_ToolItem_Image();
        IARE_ToolItem video = new ARE_ToolItem_Video();
        IARE_ToolItem at = new ARE_ToolItem_At();
        mToolbar.addToolbarItem(bold);
        mToolbar.addToolbarItem(italic);
        mToolbar.addToolbarItem(underline);
        mToolbar.addToolbarItem(strikethrough);
        mToolbar.addToolbarItem(quote);
        mToolbar.addToolbarItem(listNumber);
        mToolbar.addToolbarItem(listBullet);
        mToolbar.addToolbarItem(hr);
        mToolbar.addToolbarItem(link);
        mToolbar.addToolbarItem(subscript);
        mToolbar.addToolbarItem(superscript);
        mToolbar.addToolbarItem(left);
        mToolbar.addToolbarItem(center);
        mToolbar.addToolbarItem(right);
        mToolbar.addToolbarItem(image);
        mToolbar.addToolbarItem(video);
        mToolbar.addToolbarItem(at);

        mEditText = this.findViewById(R.id.arEditText);
        mEditText.setToolbar(mToolbar);

        setHtml();

        initToolbarArrow();
    }

    private void setHtml() {
        mEditText.fromHtml(listModel.getText());
    }

    private void initToolbarArrow() {
        final ImageView imageView = this.findViewById(R.id.arrow);
        if (this.mToolbar instanceof ARE_ToolbarDefault) {
            ((ARE_ToolbarDefault) mToolbar).getViewTreeObserver().addOnScrollChangedListener(() -> {
                int scrollX = ((ARE_ToolbarDefault) mToolbar).getScrollX();
                int scrollWidth = ((ARE_ToolbarDefault) mToolbar).getWidth();
                int fullWidth = ((ARE_ToolbarDefault) mToolbar).getChildAt(0).getWidth();

                if (scrollX + scrollWidth < fullWidth) {
                    imageView.setImageResource(R.drawable.ic_arrow_forward_24px);
                    scrollerAtEnd = false;
                } else {
                    imageView.setImageResource(R.drawable.ic_arrow_back_24px);
                    scrollerAtEnd = true;
                }
            });
        }

        imageView.setOnClickListener(view -> {
            if (scrollerAtEnd) {
                ((ARE_ToolbarDefault) mToolbar).smoothScrollBy(-Integer.MAX_VALUE, 0);
                scrollerAtEnd = false;
            } else {
                int hsWidth = ((ARE_ToolbarDefault) mToolbar).getChildAt(0).getWidth();
                ((ARE_ToolbarDefault) mToolbar).smoothScrollBy(hsWidth, 0);
                scrollerAtEnd = true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();
        if (menuId == R.id.action_save) {
            String html = this.mEditText.getHtml();
            listModel.setText(html);
            return true;
        }else if(menuId == R.id.action_edit_title){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Editer le titre");

            final EditText input = new EditText(this);

            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setText(listModel.getTitle());
            builder.setView(input);

            builder.setPositiveButton("Sauvegarder", (dialog, which) -> listModel.setTitle(input.getText().toString()));
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            builder.show();
        }else if(menuId == R.id.action_share){
            Intent intent = new Intent(EditNoteActivity.this, ShareNote.class);
            intent.putExtra("from", "edit_note");
            intent.putExtra("id", listModel.getId());
            intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mToolbar.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        String html = this.mEditText.getHtml();
        listModel.setText(html);

        Intent intent=new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}
