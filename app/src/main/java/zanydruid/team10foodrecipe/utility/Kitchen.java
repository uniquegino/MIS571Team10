package zanydruid.team10foodrecipe.utility;


import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import zanydruid.team10foodrecipe.BDconstance.DBConstant;
import zanydruid.team10foodrecipe.BDconstance.SQLCommand;
import zanydruid.team10foodrecipe.Models.Flavor;
import zanydruid.team10foodrecipe.Models.Ingredient;
import zanydruid.team10foodrecipe.Models.Nutrition;
import zanydruid.team10foodrecipe.Models.Recipe;
import zanydruid.team10foodrecipe.Models.Unit;

/**
 * Class to manipulate tables & data
 * Uses singleton pattern to create single instance
 */
public class Kitchen {
    private static Kitchen instance = null;
    private SQLiteDatabase mDatabase;
    private Context mContext;
    private List<Unit> mUnits;
//    private List<Ingredient> mIngredients;
//    private List<Nutrition> mNutritions;
//    private List<Flavor> mFlavors;
    private static final String TAG = "Kitchen";

    private Kitchen(Context context) {
        //path of database file
        mContext = context.getApplicationContext();
        String path = DBConstant.DATABASE_PATH + "/" + DBConstant.DATABASE_FILE;
        File file = new File(path);
        if (file.exists() && !file.isDirectory()) {
            mDatabase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
            // Initiate unit list
            mUnits = this.getUnitsFromDB();
            // Initiate ingredient list
            //mIngredients = this.getIngredientFromDB();
            // Initiate nutrition list
            //mNutritions = this.getNutritionsFromDB();
            // Initiate flavor list
            //mFlavors = this.getFlavorsFromDB();
        }else{
            Log.e(TAG,"path doesn't exist.");
        }

    }
    /*
     * Singleton Pattern
     * Why should we avoid multiple instances here?
     */
    public static Kitchen getInstance(Context context) {
        if (instance==null) instance = new Kitchen(context);
        return instance;
    }

    /**
     * Get List of Unit from database
     *
     * @return
     */
    private List<Unit> getUnitsFromDB(){
        Cursor cursor = this.execQuery(SQLCommand.GET_UNITS);
        CurserWrapper wrapper = new CurserWrapper(cursor);
        List<Unit> units = new ArrayList<>();
        try{
            wrapper.moveToFirst();
            while(!wrapper.isAfterLast()){
                units.add(wrapper.getUnits());
                wrapper.moveToNext();
            }
        } finally {
            wrapper.close();
        }
        return units;
    }

    /**
     * Actual method to get Units
     *
     * @return
     */
    public List<Unit> getUnits(){
        return this.mUnits;
    }

    // Get specified unit by id
    public Unit getUnitById(int id){
        for(Unit unit: mUnits){
            if(unit.getUnitId()==id){
                return unit;
            }
        }
        return null;
    }

    /**
     * Get ingredients list from database
     *
     * @return
     */
    public List<Ingredient> getIngredientsById(int id){
        String[] args = new String[1];
        args[0] = String.valueOf(id);
        Cursor cursor = this.execQuery(SQLCommand.GET_INGREDIENTS,args);
        CurserWrapper wrapper = new CurserWrapper(cursor);
        List<Ingredient> ingredients = new ArrayList<>();
        try{
            wrapper.moveToFirst();
            while(!wrapper.isAfterLast()){
                ingredients.add(wrapper.getIngredient());
                wrapper.moveToNext();
            }
        } finally {
            wrapper.close();
        }
        return ingredients;
    }

    /**
     * Actual method to get Ingredients
     *
     * @return
     */
//    public List<Ingredient> getIngredients(){
//        return this.mIngredients;
//    }

    /**
     * Get specified ingredient by id
     *
     * @param id
     * @return
     */
//    public Ingredient getIngredientById(int id){
//        for(Ingredient ingredient: mIngredients){
//            if(ingredient.getIngredientId()==id){
//                return ingredient;
//            }
//        }
//        return null;
//    }

    /**
     * Get nutrition list from database
     *
     * @return
     */
    private List<Nutrition> getNutritionsFromDB(){
        Cursor cursor = this.execQuery(SQLCommand.GET_NUTRITIONS);
        CurserWrapper wrapper = new CurserWrapper(cursor);
        List<Nutrition> nutritions = new ArrayList<>();
        try{
            wrapper.moveToFirst();
            while(!wrapper.isAfterLast()){
                nutritions.add(wrapper.getNutrition());
                wrapper.moveToNext();
            }
        } finally {
            wrapper.close();
        }
        return nutritions;
    }

    /**
     * Actual method get Nutritions
     *
     * @return
     */
//    public List<Nutrition> getNutritions(){
//        return this.mNutritions;
//    }

    /**
     * Get specified Nutrition by id
     *
     * @param id
     * @return
     */
//    public Nutrition getNutritionById(int id){
//        for(Nutrition nutrition: mNutritions){
//            if(nutrition.getNutritionId()==id){
//                return nutrition;
//            }
//        }
//        return null;
//    }

    /**
     * Get flavor list from database
     *
     * @return
     */
    private List<Flavor> getFlavorsFromDB(){
        Cursor cursor = this.execQuery(SQLCommand.GET_FLAVORS);
        CurserWrapper wrapper = new CurserWrapper(cursor);
        List<Flavor> flavors = new ArrayList<>();
        try{
            wrapper.moveToFirst();
            while(!wrapper.isAfterLast()){
                flavors.add(wrapper.getFlavor());
            }
        } finally {
            wrapper.close();
        }
        return flavors;
    }

    /**
     * Actual method to get Flavors
     *
     * @return
     */
//    public List<Flavor> getFlavors(){
//        return this.mFlavors;
//    }

    /**
     * Get flavor by id
     *
     * @param
     * @return
     */

//    public Flavor getFlavorById(int id){
//        for(Flavor flavor: mFlavors){
//            if(flavor.getFlavorId()==id){
//                return flavor;
//            }
//        }
//        return null;
//    }

    public List<Recipe> getRecipesFromDB(){
        Cursor cursor = this.execQuery(SQLCommand.GET_RECIPES);
        CurserWrapper wrapper = new CurserWrapper(cursor);
        List<Recipe> recipes = new ArrayList<>();
        try{
            wrapper.moveToFirst();
            while(!wrapper.isAfterLast()){
                recipes.add(wrapper.getRecipe());
            }

        } finally {
            wrapper.close();
        }
        return recipes;
    }

    public Recipe getRecipeById(int id){
        String[]args = new String[1];
        args[0] = String.valueOf(id);
        Cursor cursor = this.execQuery(SQLCommand.GET_RECIPE, args);
        CurserWrapper wrapper = new CurserWrapper(cursor);
        try{
            wrapper.moveToFirst();
           if(wrapper.getCount()==0){
               return null;
           }
            return wrapper.getRecipe();
        } finally {
            wrapper.close();
        }
    }
    /**
     * Copy database file
     * From assets folder (in the project) to android folder (on device)
     */
    public static void copyDB(Context context) throws IOException,FileNotFoundException{
        String path = DBConstant.DATABASE_PATH + "/" + DBConstant.DATABASE_FILE;
        File file = new File(path);
        if (!file.exists()){
            DBOpenHelper dbhelper = new DBOpenHelper(context, path ,1);
            dbhelper.getWritableDatabase();
            InputStream is = context.getAssets().open(DBConstant.DATABASE_FILE);
            OutputStream os = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer))>0){
                os.write(buffer, 0, length);
            }
            is.close();
            os.flush();
            os.close();
        }
    }

    /**
     * execute sql without returning data, such as alter
     * @param sql
     */
    public void execSQL(String sql) throws SQLException {
        mDatabase.execSQL(sql);
    }
    /**
     * execute sql such as update/delete/insert
     * @param sql
     * @param args
     * @throws SQLException
     */
    public void execSQL(String sql, Object[] args) throws SQLException {
        mDatabase.execSQL(sql, args);
    }
    /**
     * execute sql query
     * @param sql
     * @param selectionArgs
     * @return cursor
     * @throws SQLException
     */
    public Cursor execQuery(String sql,String[] selectionArgs) throws SQLException {
        return mDatabase.rawQuery(sql, selectionArgs);
    }
    /**
     * execute query without arguments
     * @param sql
     * @return
     * @throws SQLException
     */
    public Cursor execQuery(String sql) throws SQLException {
        return this.execQuery(sql, null);
    }
    /**
     * close database
     */
    public void closeDB()
    {
        if (mDatabase !=null) mDatabase.close();
    }


    /**
     *
     * @param recipe
     * @return
     */
    public File getPhotoFile(Recipe recipe){
        File externalFileDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if(externalFileDir==null){
            return null;
        }
        return new File(externalFileDir,recipe.getPhotoFile());
    }
}
