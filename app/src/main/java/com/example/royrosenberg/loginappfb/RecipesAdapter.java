package com.example.royrosenberg.loginappfb;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.royrosenberg.loginappfb.DM.Recipe;
import com.example.royrosenberg.loginappfb.DM.User;
import com.example.royrosenberg.loginappfb.Services.RecipeManager;
import com.example.royrosenberg.loginappfb.Utils.MessageBox;
import com.firebase.client.Firebase;

import java.util.ArrayList;

/**
 * Created by Roy.Rosenberg on 22/04/2016.
 */
public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> {

    private ArrayList<Recipe> _recipeList;
    private RecipeManager _recipesMngr;
    private User _currentUser;

    public RecipesAdapter(ArrayList<Recipe> recipeList, RecipeManager mngr, User loggedUser) {
        _recipeList = recipeList;
        _recipesMngr = mngr;
        _currentUser = loggedUser;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Recipe current = _recipeList.get(position);
        holder.itemTitle.setText(current.Name);
        holder.itemDetails.setText(current.ShortDescription);
        holder.recipe = current;
        holder.btnEdit.setTag(current);
        holder.btnDelete.setTag(current);
        //holder.itemImage.setImageResource(images[position]);
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                MessageBox msg = new MessageBox(v.getContext());
                msg.Show("Delete?", "Confirm", MessageBox.MessageBoxButtons.OK_CANCEL, new MessageBox.MessageBoxEvents() {
                    @Override
                    public void onOKClick() {
                        Recipe r = (Recipe) v.getTag();
                        int pos = _recipeList.indexOf(r);
                        _recipeList.remove(r);
                        _recipesMngr.DeleteRecipe(r);
                        RecipesAdapter.this.notifyItemRemoved(pos);
                    }

                    @Override
                    public void onCancelClick() {

                    }
                });
            }
        });
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Recipe r =  (Recipe)v.getTag();

                Intent intent = new Intent(v.getContext(), RecipeEditActivity.class);
                intent.putExtra("RecipeObj", r);
                intent.putExtra("UserObj", _currentUser);
                v.getContext().startActivity(intent);
                //startActivity(intent);
            }
        });
        holder.btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Recipe r =  (Recipe)v.getTag();
                MessageBox msg = new MessageBox(v.getContext());
                msg.Show("add to favorites?", "Confirm", MessageBox.MessageBoxButtons.OK);
            }
        });
    }

    @Override
    public int getItemCount() {
        return _recipeList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView itemImage;
        public TextView itemTitle;
        public TextView itemDetails;
        public Recipe recipe;
        public Button btnDelete, btnEdit, btnFavorite;

        public ViewHolder(View itemView) {
            super(itemView);
            itemImage = (ImageView) itemView.findViewById(R.id.item_image);
            itemTitle = (TextView) itemView.findViewById(R.id.item_title);
            itemDetails = (TextView) itemView.findViewById(R.id.item_details);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
            btnEdit = (Button) itemView.findViewById(R.id.btnEdit);
            btnFavorite = (Button) itemView.findViewById(R.id.btnFav);
            /*
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    Snackbar.make(v, "Click detected on " + pos, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });*/

        }
    }
}
