package com.finalproject.galiroth.myrecipes;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.finalproject.galiroth.myrecipes.DM.ApplicationData;
import com.finalproject.galiroth.myrecipes.DM.ImageResponse;
import com.finalproject.galiroth.myrecipes.DM.Recipe;
import com.finalproject.galiroth.myrecipes.DM.Upload;
import com.finalproject.galiroth.myrecipes.DM.User;
import com.finalproject.galiroth.myrecipes.R;
import com.finalproject.galiroth.myrecipes.Services.DownloadImageTask;
import com.finalproject.galiroth.myrecipes.Services.RecipeManager;
import com.finalproject.galiroth.myrecipes.Services.UploadService;
import com.finalproject.galiroth.myrecipes.Utils.MessageBox;

import java.io.File;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RecipeEditActivity extends AppCompatActivity {

    private Button _button, btnCbCancel;
    private EditText _etName, _etDesc, _etSteps;
    private RecipeManager _recipeMngr;
    private int MODE;//0 - add, 1 - edit
    private User _currentUser;
    private ImageView _addNewRecipeImage;
    private ImageView imgCbGallery;
    private static int RESULT_LOAD = 1;
    private View _fragmentView;
    private File _fileToLoad = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_edit);

        verifyStoragePermissions(this);

        LoadControls();
        _addNewRecipeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(intent, RESULT_LOAD);
            }
        });


        _recipeMngr = new RecipeManager(this);

        //if recipe is set -> in edit mode, else in add mode
        MODE = 0; //default: add mode
        Bundle bundle = getIntent().getExtras();
        Recipe editRecipe = null;
        if (bundle != null) {
            _currentUser = (User) bundle.get("UserObj");
            if (bundle.get("RecipeObj") != null) {
                //edit mode
                MODE = 1;
                editRecipe = (Recipe) bundle.get("RecipeObj");
            }
        }

        if (MODE == 0) {
            setTitle("Add Recipe");
            try {
                new DownloadImageTask(_addNewRecipeImage).execute("http://icons.iconseeker.com/png/fullsize/slate/food.png");
            } catch (Exception ex) {
                Log.e("Gali", ex.getMessage());
            }
        } else {
            setTitle("Edit Recipe");
            setRecipeToControls(editRecipe);
        }
    }

    private void LoadControls() {
        btnCbCancel = (Button) findViewById(R.id.btnCancel);
        btnCbCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        _button = (Button) findViewById(R.id.btnSaveRecipe);
        _button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSave_click(v);
            }
        });
        _etName = (EditText) findViewById(R.id.etRecipeName);
        _etDesc = (EditText) findViewById(R.id.etRecipeDescription);
        _etSteps = (EditText) findViewById(R.id.etRecipeSteps);
        _addNewRecipeImage = (ImageView) findViewById(R.id.addNewRecipeImage);
    }

    public void btnSave_click(View view) {

        if (MODE == 0) {
            //add recipe
            Recipe r = getRecipeFromControls();

            if (_fileToLoad != null) {
                //handle image save to Imgur site
                Upload upload = new Upload();
                upload.image = _fileToLoad;
                upload.title = r.Name;
                upload.description = r.ShortDescription;

                SaveRecipeWithImage(r, upload);

            } else {
                //add recipe without image - so set default image
                r.ImageUrl = ApplicationData.IMAGE_DEFAULT_URL;
                final MessageBox msgBx = new MessageBox(RecipeEditActivity.this);
                _recipeMngr.AddRecipe(r, new RecipeManager.RecipeCreationEvents() {
                    @Override
                    public void onSucceeded(Recipe recipe) {
                        msgBx.Show("Recipe saved", "Recipe Saved", MessageBox.MessageBoxButtons.OK, new MessageBox.MessageBoxEvents() {
                            @Override
                            public void onOKClick() {
                                goBackToMainPage();
                            }

                            @Override
                            public void onCancelClick() {

                            }
                        });
                    }

                    @Override
                    public void onFailed(Recipe recipe, String message) {
                        msgBx.Show("Recipe did not saved\n" + message, "Failed", MessageBox.MessageBoxButtons.OK);
                    }
                });
            }
        } else {
            //edit
            Bundle bundle = getIntent().getExtras();
            Recipe recipe = (Recipe) bundle.get("RecipeObj");
            Recipe newRecipe = getRecipeFromControls();
            recipe.Name = newRecipe.Name;
            recipe.ShortDescription = newRecipe.ShortDescription;
            recipe.Steps = newRecipe.Steps;
            final MessageBox msgBx = new MessageBox(RecipeEditActivity.this);
            if(_fileToLoad == null){
                //no change in image, so only save the recipe
                _recipeMngr.EditRecipe(recipe, new RecipeManager.RecipeCreationEvents() {
                    @Override
                    public void onSucceeded(Recipe recipe) {
                        msgBx.Show("Recipe saved", "Recipe Saved", MessageBox.MessageBoxButtons.OK, new MessageBox.MessageBoxEvents() {
                            @Override
                            public void onOKClick() {
                                goBackToMainPage();
                            }

                            @Override
                            public void onCancelClick() {

                            }
                        });
                    }

                    @Override
                    public void onFailed(Recipe recipe, String message) {
                        msgBx.Show("Recipe did not saved\n" + message, "Failed", MessageBox.MessageBoxButtons.OK);
                    }
                });
            } else{
                //save the image first, then save the recipe
                Upload upload = new Upload();
                upload.image = _fileToLoad;
                upload.title = recipe.Name;
                upload.description = recipe.ShortDescription;

                SaveRecipeWithImage(recipe, upload);
            }

        }

    }

    private void SaveRecipeWithImage(final Recipe recipe, Upload upload){
        //Start upload
        new UploadService(this).Execute(upload, new Callback<ImageResponse>() {
            @Override
            public void success(ImageResponse imageResponse, Response response) {
                final MessageBox msgBx = new MessageBox(RecipeEditActivity.this);
                recipe.ImageUrl = imageResponse.data.link;

                if(MODE == 0) {
                    _recipeMngr.AddRecipe(recipe, new RecipeManager.RecipeCreationEvents() {
                        @Override
                        public void onSucceeded(Recipe recipe) {
                            msgBx.Show("Recipe saved", "Recipe Saved", MessageBox.MessageBoxButtons.OK, new MessageBox.MessageBoxEvents() {
                                @Override
                                public void onOKClick() {
                                    goBackToMainPage();
                                }

                                @Override
                                public void onCancelClick() {

                                }
                            });
                        }

                        @Override
                        public void onFailed(Recipe recipe, String message) {
                            msgBx.Show("Recipe did not saved\n" + message, "Failed", MessageBox.MessageBoxButtons.OK);
                        }
                    });
                }
                else {
                    _recipeMngr.EditRecipe(recipe, new RecipeManager.RecipeCreationEvents() {
                        @Override
                        public void onSucceeded(Recipe recipe) {
                            msgBx.Show("Recipe saved", "Recipe Saved", MessageBox.MessageBoxButtons.OK, new MessageBox.MessageBoxEvents() {
                                @Override
                                public void onOKClick() {
                                    goBackToMainPage();
                                }

                                @Override
                                public void onCancelClick() {

                                }
                            });
                        }

                        @Override
                        public void onFailed(Recipe recipe, String message) {
                            msgBx.Show("Recipe did not saved\n" + message, "Failed", MessageBox.MessageBoxButtons.OK);
                        }
                    });
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private Recipe getRecipeFromControls() {
        Recipe recipe = new Recipe();
        recipe.Name = _etName.getText().toString();
        recipe.ShortDescription = _etDesc.getText().toString();
        recipe.Steps = _etSteps.getText().toString();
        recipe.CreatedUser = _currentUser.Email;
        recipe.ImageUrl = "";
        return recipe;
    }

    private void setRecipeToControls(Recipe recipe) {
        _etName.setText(recipe.Name);
        _etDesc.setText(recipe.ShortDescription);
        _etSteps.setText(recipe.Steps);
        try {
            String url = ApplicationData.IMAGE_DEFAULT_URL;
            if (!recipe.ImageUrl.isEmpty())
                url = recipe.ImageUrl;
            new DownloadImageTask(_addNewRecipeImage).execute(url);
        } catch (Exception ex) {
            Log.e("Gali", ex.getMessage());
        }
    }

    private void goBackToMainPage() {
        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.putExtra("UserObj", _currentUser);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD && resultCode == RESULT_OK && data != null) {
            Uri selectMode = data.getData();
            String[] imagesArr = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectMode, imagesArr, null, null, null);
            cursor.moveToFirst();

            int getColumnIndex = cursor.getColumnIndex(imagesArr[0]);
            String picPath = cursor.getString(getColumnIndex);
            cursor.close();

            _fileToLoad = new File(picPath);
            //boolean exists = f.exists();
            //boolean canRead = f.canRead();
            //picPath = f.getAbsolutePath();

            Bitmap bitmap = BitmapFactory.decodeFile(picPath);
            if (bitmap == null) {
                MessageBox msg = new MessageBox(this);
                msg.Show("bitmap is null", "error", MessageBox.MessageBoxButtons.OK, null);
            } else _addNewRecipeImage.setImageBitmap(bitmap);

        }
    }

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     * <p>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
