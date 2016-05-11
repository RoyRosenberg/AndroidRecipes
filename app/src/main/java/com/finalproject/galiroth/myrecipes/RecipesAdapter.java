package com.finalproject.galiroth.myrecipes;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.finalproject.galiroth.myrecipes.DM.ApplicationData;
import com.finalproject.galiroth.myrecipes.DM.Recipe;
import com.finalproject.galiroth.myrecipes.DM.User;
import com.finalproject.galiroth.myrecipes.R;
import com.finalproject.galiroth.myrecipes.Services.DbHelper;
import com.finalproject.galiroth.myrecipes.Services.DownloadImageTask;
import com.finalproject.galiroth.myrecipes.Services.RecipeManager;
import com.finalproject.galiroth.myrecipes.Utils.MessageBox;

import java.util.ArrayList;

/**
 * Created by Gali.Roth on 22/04/2016.
 */
public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> {

    private ArrayList<Recipe> _recipeList;
    private RecipeManager _recipesMngr;
    private User _currentUser;
    private boolean _showFavoritesButton, _showEditButton, _showDeletebutton;

    public RecipesAdapter(ArrayList<Recipe> recipeList, RecipeManager mngr, User loggedUser,
                          boolean showFavorites, boolean showEdit, boolean showDelete) {
        _recipeList = recipeList;
        _recipesMngr = mngr;
        _currentUser = loggedUser;
        _showDeletebutton = showDelete;
        _showEditButton = showEdit;
        _showFavoritesButton = showFavorites;
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
        holder.btnFavorite.setTag(current);
        try {
            String url = ApplicationData.IMAGE_DEFAULT_URL;
            if (!current.ImageUrl.isEmpty())
                url = current.ImageUrl;
            new DownloadImageTask(holder.itemImage).execute(url);
        } catch (Exception ex) {
            Log.e("Gali", ex.getMessage());
        }
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
                Recipe r = (Recipe) v.getTag();

                Intent intent = new Intent(v.getContext(), RecipeEditActivity.class);
                intent.putExtra("RecipeObj", r);
                intent.putExtra("UserObj", _currentUser);
                v.getContext().startActivity(intent);
            }
        });
        holder.btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final Recipe r =  (Recipe)v.getTag();
                MessageBox msg = new MessageBox(v.getContext());
                msg.Show("add to favorites?", "Confirm", MessageBox.MessageBoxButtons.OK, new MessageBox.MessageBoxEvents() {
                    @Override
                    public void onOKClick() {
                        //add to favorites
                        DbHelper dbHelper = new DbHelper(v.getContext());
                        dbHelper.saveRecipeToFavorites(r);
                    }

                    @Override
                    public void onCancelClick() {
                    }
                });
            }
        });

        if(!_showFavoritesButton){
            holder.btnFavorite.setVisibility(View.GONE);
        }
        if(!_showEditButton){
            holder.btnEdit.setVisibility(View.GONE);
        }
        if(!_showDeletebutton){
            holder.btnDelete.setVisibility(View.GONE);
        }
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
