package sg.asmallmuseum.presentation.ArtView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import sg.asmallmuseum.Domain.Comment;
import sg.asmallmuseum.R;
import sg.asmallmuseum.logic.CommentManager;

public class CommentRecyclerViewAdapter extends RecyclerView.Adapter<CommentRecyclerViewAdapter.ItemViewHolder> implements View.OnClickListener {
    private CommentManager commentManager;
    private List<Comment> listData;
    private EditText editText;
    private int selectedPosition = -1;
    private FirebaseUser fUser;
    private Button button3;
    private Button button4;
    private String fEmail;
    
    public CommentRecyclerViewAdapter(){
        listData = new ArrayList<>();
    }

    public CommentRecyclerViewAdapter(){
        listData = new ArrayList<>();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);

        commentManager = new CommentManager();

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        editText = (EditText) view.findViewById(R.id.text_view3);
        button3 = (Button) view.findViewById(R.id.button3);
        button4 = (Button) view.findViewById(R.id.button4);

        button3.setVisibility(View.INVISIBLE);
        button4.setVisibility(View.INVISIBLE);

        editText.setBackground(null);
        editText.setClickable(false);
        editText.setFocusable(false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        if(fUser != null){
            fEmail = fUser.getEmail();
        }else if(fUser == null){
            fEmail = "";
        }

        if(!fEmail.equals(listData.get(position).getuEmail())){
            holder.button1.setVisibility(View.INVISIBLE);
            holder.button2.setVisibility(View.INVISIBLE);
        }else {
            if (selectedPosition == position){
                holder.button1.setVisibility(View.INVISIBLE);
                holder.button2.setVisibility(View.INVISIBLE);
                holder.button3.setVisibility(View.VISIBLE);
                holder.button4.setVisibility(View.VISIBLE);
                holder.editText.setBackgroundResource(android.R.drawable.edit_text);
                holder.editText.setFocusableInTouchMode(true);
                holder.editText.setClickable(true);
                holder.editText.setFocusable(true);
            }else{
                holder.editText.setBackground(null);
                holder.editText.setClickable(false);
                holder.editText.setFocusable(false);
                holder.button1.setVisibility(View.VISIBLE);
                holder.button2.setVisibility(View.VISIBLE);
                holder.button3.setVisibility(View.INVISIBLE);
                holder.button4.setVisibility(View.INVISIBLE);
            }

            holder.button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   /* holder.button1.setVisibility(View.INVISIBLE);
                    holder.button2.setVisibility(View.INVISIBLE);
                    holder.button3.setVisibility(View.VISIBLE);
                    holder.button4.setVisibility(View.VISIBLE);*/

                    holder.editText.setBackgroundResource(android.R.drawable.edit_text);
                    holder.editText.setFocusableInTouchMode(true);
                    holder.editText.setClickable(true);
                    holder.editText.setFocusable(true);

                    selectedPosition = position;

                    notifyDataSetChanged();

                    Toast.makeText(v.getContext(), position+"", Toast.LENGTH_SHORT).show();
                }
            });

            holder.button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String path[] = listData.get(position).getPath().split("/");
                    String category = path[1];
                    String type = path[2];
                    String ref = path[3];

                    commentManager.deleteComment(category, type, ref, listData.get(position).getCommIdx(),listData.get(position).getuEmail());

                    listData.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, listData.size());

                }
            });

            holder.button3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //edit
                    String editContent = holder.editText.getText().toString();
                    listData.get(position).setContent(editContent);
                    notifyItemChanged(position);

                    //String commIdx, String ref, String path, String uEmail, String content, String commDate
                    String path[] = listData.get(position).getPath().split("/");
                    String category = path[1];
                    String type = path[2];
                    String ref = path[3];

                    Comment comment = new Comment(path[5],ref,listData.get(position).getPath(),listData.get(0).getuEmail(),editContent,listData.get(position).getCommDate());
                    commentManager.updateComment(comment);

                    holder.button1.setVisibility(View.VISIBLE);
                    holder.button2.setVisibility(View.VISIBLE);
                    holder.button3.setVisibility(View.INVISIBLE);
                    holder.button4.setVisibility(View.INVISIBLE);

                    holder.editText.setBackground(null);
                    holder.editText.setClickable(false);
                    holder.editText.setFocusable(false);

                    selectedPosition = -1;

                    notifyDataSetChanged();

                }
            });

            holder.button4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //cancel
                    holder.button1.setVisibility(View.VISIBLE);
                    holder.button2.setVisibility(View.VISIBLE);
                    holder.button3.setVisibility(View.INVISIBLE);
                    holder.button4.setVisibility(View.INVISIBLE);

                    holder.editText.setBackground(null);
                    holder.editText.setClickable(false);
                    holder.editText.setFocusable(false);

                    selectedPosition = -1;

                    notifyDataSetChanged();
                }
            });
        }

        holder.onBind(listData.get(position));

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

    }

    @Override
    public int getItemCount() {

        return listData.size();
    }

    public void addItem(Comment data) {

        listData.add(data);
    }

    public void updateList(List<Comment> comments){
        listData.addAll(comments);
//        notifyDataSetChanged();
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView textView1;
        private TextView textView2;
        private EditText editText;
        private Button button1;
        private Button button2;
        private Button button3;
        private Button button4;

        ItemViewHolder(View itemView) {
            super(itemView);

            textView1 = itemView.findViewById(R.id.text_view1);
            textView2 = itemView.findViewById(R.id.text_view2);
            editText = itemView.findViewById(R.id.text_view3);
            button1 = itemView.findViewById(R.id.button1);
            button2 = itemView.findViewById(R.id.button2);
            button3 = itemView.findViewById(R.id.button3);
            button4 = itemView.findViewById(R.id.button4);
        }

        void onBind(Comment data) {
            textView1.setText(data.getuEmail());
            textView2.setText(data.getCommDate());
            editText.setText(data.getContent());

        }

    }

}
