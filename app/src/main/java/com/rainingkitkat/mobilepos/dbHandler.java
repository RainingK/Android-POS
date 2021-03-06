package com.rainingkitkat.mobilepos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class dbHandler extends SQLiteOpenHelper {
    BarcodeScanner barcodeScanner = new BarcodeScanner();
    Utils utils = new Utils();

    private final static String DATABASE_NAME = "pos.db";
    private final static String TABLE_NAME = "groceryitems";
    private final static String USER_TABLE = "Users";
    private final static String COL1 = "ID";
    private final static String COL2 = "BarcodeNumber";
    private final static String COL3 = "ProductName";
    private final static String COL4 = "Price";
    private final static String COL5 = "Stock";

    public static String barcodeString = "";

    private final static String USER_COL1 = "UserID";
    private final static String USER_COL2 = "FullName";
    private final static String USER_COL3 = "Username";
    private final static String USER_COL4 = "Password";
    private final static String USER_COL5 = "Balance";

    private final static String CART_COL1 = "Product_Name";
    private final static String CART_COL2 = "Product_Price";
    private final static String CART_COL3 = "Product_Quantity";
    private final static String CART_COL4 = "Product_ID";
    private final static String CART_COL5 = "Product_username";



    public dbHandler(@Nullable Context context) {
        super(context, TABLE_NAME, null, 1);

        SQLiteDatabase db = getWritableDatabase();

        try {
            displayAllData(db);
        } catch (SQLException e){
            Log.e("dbHandler", "Database Does Not Exist");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL2 + " TEXT, " + COL3 + " TEXT, " + COL4 + " REAL, " + COL5 + " INTEGER)";

        String createUserTable = "CREATE TABLE " + USER_TABLE + " (UserID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USER_COL2 + " TEXT, " + USER_COL3 + " TEXT, " + USER_COL4 + " TEXT, " + USER_COL5  + " REAL, CONSTRAINT name_unique UNIQUE (Username))";

        String createCart = "CREATE TABLE Cart (Product_ID INTEGER PRIMARY KEY AUTOINCREMENT, Product_Name TEXT, Product_Price TEXT, Product_Quantity INTEGER, Product_username TEXT)";

        sqLiteDatabase.execSQL(createTable);
        sqLiteDatabase.execSQL(createUserTable);
        sqLiteDatabase.execSQL(createCart);


        Log.d("dbHandler", "Successfully Created Table");
        Log.d("dbHandler", "Successfully db Created UserTable");

        //Admin Account
        Log.d("dbHandler", "Before Admin Account");
        AdminSignUpAddData(sqLiteDatabase,"ME", "KAT", "pass");
        Log.d("dbHandler", "Admin Account Created");

        //Adds Data into the tables
        addData(sqLiteDatabase, "036000291452", "Lay's Tomato Ketchup Chips - 40gm", 2.25f);
        addData(sqLiteDatabase, "705632085943", "Coca-Cola Can Soft Drinks - 150ml", 2.5f);
        addData(sqLiteDatabase, "701197952225", "Arwa Water - 1.5L", 1);
        addData(sqLiteDatabase, "9501101530003", "NESCAFE GOLD Double Choc MOCHA Instant Foaming Coffee with Chocolate Mix - 23gm x 8 Sticks", 18.5f);
        addData(sqLiteDatabase, "9771234567003", "White Eggs", 18);
        addData(sqLiteDatabase, "8901072002478", "Sliced Milk Bread", 5);
        addData(sqLiteDatabase, "1234567890128", "Snickers Ice Cream Cone - 110ml ", 6);
        addData(sqLiteDatabase, "725272730706", "Al Rawabi Low Fat Vitamin D Milk - 2L", 12);
        addData(sqLiteDatabase, "012345678905", "Chicken Sandwich", 4);
        addData(sqLiteDatabase, "9788679912077", "Alokozay Black Tea Bags - 100 Bags", 11.75f);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    private void addData(SQLiteDatabase db, String barcode, String productName, float price){
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL2, barcode);
        contentValues.put(COL3, productName);
        contentValues.put(COL4, price);
        contentValues.put(COL5, 100);

        long result = db.insert(TABLE_NAME, null, contentValues);

        if(result == -1){
            Log.e("dbHandler", "Failed to insert values into database");
        } else {
            Log.i("dbHandler", "Values successfully added");
        }
    }

    public void SignUpAddData(String fullName, String username, String password){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        username = username.toLowerCase();

        contentValues.put(USER_COL2, fullName);
        contentValues.put(USER_COL3, username);
        contentValues.put(USER_COL4, password);
        contentValues.put(USER_COL5, 100);

        long result = db.insert(USER_TABLE, null, contentValues);

        if(result == -1){
            Log.e("dbHandler", "Failed to insert values into user database");
        } else {
            Log.i("dbHandler", "Values successfully added");
        }
    }

    public void AdminSignUpAddData(SQLiteDatabase db, String fullName, String username, String password){
        ContentValues contentValues = new ContentValues();

        username = username.toLowerCase();

        contentValues.put(USER_COL2, fullName);
        contentValues.put(USER_COL3, username);
        contentValues.put(USER_COL4, password);
        contentValues.put(USER_COL5, 100);

        long result = db.insert(USER_TABLE, null, contentValues);

        if(result == -1){
            Log.e("dbHandler", "Failed to insert values into user database");
        } else {
            Log.i("dbHandler", "Values successfully added");
        }
    }

    private void displayAllData(SQLiteDatabase sqLiteDatabase){
        Cursor res = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if(res.getCount() == 0){
            Log.d("dbHandler", "Database Is Empty");
        } else {
            StringBuffer buffer = new StringBuffer();

            while (res.moveToNext()){
                buffer.append("ID : " + res.getString(0) + "\n");
                buffer.append("Barcode : " + res.getString(1) + "\n");
                buffer.append("Product : " + res.getString(2) + "\n");
                buffer.append("Price : " + res.getString(3) + "\n\n");
            }

            Log.i("dbHandler", String.valueOf(buffer));
        }

        res.close();

        Cursor res1 = sqLiteDatabase.rawQuery("SELECT * FROM " + USER_TABLE, null);

        if(res1.getCount() == 0){
            Log.d("dbHandler", "Database Is Empty");
        } else {
            StringBuffer buffer = new StringBuffer();

            while (res1.moveToNext()){
                buffer.append("ID : " + res1.getString(0) + "\n");
                buffer.append("Full Name : " + res1.getString(1) + "\n");
                buffer.append("Username : " + res1.getString(2) + "\n");
                buffer.append("Password : " + res1.getString(3) + "\n");
                buffer.append("Balance : " + res1.getString(4) + "\n\n");
            }

            Log.i("dbHandler", String.valueOf(buffer));
        }

        res1.close();
    }

    //Gets The Product Name When Barcode Is Scanned
    public String getBarcode(String barcode){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String text = "";

        Cursor res = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE BarcodeNumber=" + "'" + barcode + "'", null);

        Log.d("dbHandler", "Query Complete");

        if(res.getCount() == 0){
            Log.d("dbHandler", "Not A Valid Barcode");
            res.close();
            return "Not A Valid Barcode";
        } else {
            Log.d("dbHandler", "Inside Else");

            while (res.moveToNext()){
                text = res.getString(2);
            }

            res.close();

            return text;
        }
    }

    //Gets Price From The Database
    public String getPrice(String barcode){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String text = "";

        Cursor res = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE BarcodeNumber=" + "'" + barcode + "'", null);

        Log.d("dbHandler", "Query Complete");

        if(res.getCount() == 0){
            Log.d("dbHandler", "Not A Valid Barcode");
            res.close();
            return "Not A Valid Barcode";
        } else {
            Log.d("dbHandler", "Inside Else");

            while (res.moveToNext()){
                text = res.getString(3);
            }

            res.close();

            return text;
        }
    }

    public Boolean checkUsername(String username){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        Cursor res = sqLiteDatabase.rawQuery("SELECT * FROM " + USER_TABLE + " WHERE Username =" + "'" + username + "'", null);

        if(res.getCount() == 0){
            Log.d("dbHandler", "Username Doesn't Exist");
            res.close();
            return true;
        } else {
            Log.d("dbHandler", "Username Exists");
            res.close();
            return false;
        }
    }

    public boolean verifyCredentials(String username, String password){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        Cursor res = sqLiteDatabase.rawQuery("SELECT * FROM " + USER_TABLE + " WHERE Username = '" + username + "' AND Password = '" + password + "'", null);

        if (res.getCount() == 0){
            Log.d("dbHandler", "User Doesn't Exist");
            res.close();
            return false;
        } else {
            Log.d("dbHandler", "Username Exists");
            res.close();
            return true;
        }
    }

    //Adds Product To Cart
    public void addItemToCart(String productName, String price, String username){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(CART_COL1, productName);
        contentValues.put(CART_COL2, price);
        contentValues.put(CART_COL3, 1);
        contentValues.put(CART_COL5, username);

        long result = sqLiteDatabase.insert("Cart", null, contentValues);

        if(result == -1){
            Log.e("dbHandler", "Failed to insert values into user database");
        } else {
            Log.i("dbHandler", "Values successfully added");
        }
    }

    public String getUserBalance(String username){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        String text = "";

        Cursor res = sqLiteDatabase.rawQuery("SELECT * FROM " + USER_TABLE + " WHERE Username=" + "'" + username + "'", null);

        Log.d("dbHandler", "Query Complete");

        if(res.getCount() == 0){
            Log.d("dbHandler", "Not A Valid Username");
            res.close();
            return "Not A Valid Username";
        } else {
            Log.d("dbHandler", "Inside Else");

            while (res.moveToNext()){
                text = res.getString(4);
            }

            res.close();

            return text;
        }
    }

    public List<String> getProductName(){
        List<String> productName = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        Cursor res = sqLiteDatabase.rawQuery("SELECT * FROM CART", null);

        if (res.getCount() == 0){
            Log.d("dbHandler", "Cart Table Is Empty");
        } else {
            Log.d("dbHandler", "Fount Items In Cart");
            while (res.moveToNext()){
                productName.add(res.getString(1));
            }
        }
        return productName;
    }

    public List<String> getProductPrice(){
        List<String> productName = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        Cursor res = sqLiteDatabase.rawQuery("SELECT * FROM CART", null);

        if (res.getCount() == 0){
            Log.d("dbHandler", "Cart Table Is Empty");
        } else {
            Log.d("dbHandler", "Fount Items In Cart");
            while (res.moveToNext()){
                productName.add(res.getString(2));
            }
        }
        return productName;
    }

    public List<String> getProductQuantity(){
        List<String> productName = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        Cursor res = sqLiteDatabase.rawQuery("SELECT * FROM CART", null);

        if (res.getCount() == 0){
            Log.d("dbHandler", "Cart Table Is Empty");
        } else {
            Log.d("dbHandler", "Fount Items In Cart");
            while (res.moveToNext()){
                productName.add(res.getString(3));
            }
        }
        return productName;
    }

    public void deleteItemFromCart(String username, String productName){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        sqLiteDatabase.delete("CART", "Product_username=? and Product_Name=?", new String[]{username, productName});
    }

    public void deductBalance(Double totalamount, String username){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        Cursor res = sqLiteDatabase.rawQuery("SELECT * FROM " + USER_TABLE + " WHERE Username=" + "'" + username + "'", null);
        Log.d("dbHandler", "after res");

        double amount = 0;

        if (res.getCount() == 0){
            Log.d("dbHandler", "Cart Table Is Empty");
        } else {
            Log.d("dbHandler", "Fount Items In Cart");
            while (res.moveToNext()){
                 amount = res.getDouble(4);
            }

            if(totalamount > amount){
                amount = totalamount - amount;
            }

            sqLiteDatabase.execSQL("UPDATE Users SET Balance = " + amount);
        }
    }

    /*public void getItemUsername(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        String text = "";

        Cursor res = sqLiteDatabase.rawQuery("SELECT * FROM CART WHERE Username=" + "'" + username + "'", null);

        Log.d("dbHandler", "Query Complete");

        if(res.getCount() == 0){
            Log.d("dbHandler", "Not A Valid Username");
            res.close();
            return "Not A Valid Username";
        } else {
            Log.d("dbHandler", "Inside Else");

            while (res.moveToNext()){
                text = res.getString(4);
            }

            res.close();

            return text;
        }

    }*/

    /*public String getItemID(String ProductName, String username) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        String text = "";

        Cursor res = sqLiteDatabase.rawQuery("SELECT * FROM CART " + " WHERE Product_username=" + "'" + username + "' AND Product_Name = '" + ProductName + "'", null);

        Log.d("dbHandler", "Query Complete");

        if(res.getCount() == 0){
            Log.d("dbHandler", "Not A Valid Username");
            res.close();
            return "Not A Valid Username";
        } else {
            Log.d("dbHandler", "Inside Else");

            while (res.moveToNext()){
                text = res.getString(4);
            }

            res.close();

            return text;
        }
    }*/
}
