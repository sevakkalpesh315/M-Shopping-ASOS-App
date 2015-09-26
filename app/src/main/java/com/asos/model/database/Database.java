package com.asos.model.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.asos.model.model.Product;

import java.util.ArrayList;
/**
 * @author Kalpesh Sevak
 * @version 2015.0606
 * @since 1.0
 */
public class Database extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "asos.db";
	private static final String TABLE_NAME = "products";
	private static final int DATABASE_VERSION = 1;
	private Product product = null;
	private ArrayList<Product> productList = new ArrayList<Product>();
	private SQLiteDatabase database = null;

	private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `"
			+ TABLE_NAME + "` (" + "`productID` varchar(100) DEFAULT NULL,"
			+ "`basePrice` varchar(100) DEFAULT NULL,"
			+ "`brand` varchar(100) DEFAULT NULL,"
			+ "`currentPrice` varchar(100) DEFAULT NULL,"
			+ "`hasMoreColors` varchar(100) DEFAULT NULL,"
			+ "`isInSet` varchar(100) DEFAULT NULL,"
			+ "`previousPrice` varchar(100) DEFAULT NULL,"
			+ "`productImgURL` varchar(100) DEFAULT NULL,"
			+ "`rpp` varchar(100) DEFAULT NULL,"
			+ "`title` varchar(100) DEFAULT NULL,"
			+ "`isFavourite` varchar(100) DEFAULT NULL,"
			+ "`isAddedToShoppingCart` varchar(100) DEFAULT NULL,"
			+ "`created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP);";

	public Database(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.database = this.getWritableDatabase();
	}

	public void onCreate(SQLiteDatabase db) {

		try {
			db.execSQL(CREATE_TABLE);
		} catch (SQLException msg) {
			Log.d("SQL ERROR", msg.getMessage());
		}
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}

	public int getTotalShoppingCart() {

		Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME
				+ " where isAddedToShoppingCart = 'YES'", null);

		return cursor.getCount();
	}

	public ArrayList<Product> getProducts() throws SQLException {

		if (!productList.isEmpty()) {
			productList.clear();
		}

		Cursor cur = database.query(true, TABLE_NAME, new String[] {
				"productID", "basePrice", "brand", "currentPrice",
				"hasMoreColors", "isInSet", "previousPrice", "productImgURL",
				"rpp", "title", "isFavourite", "isAddedToShoppingCart" }, null,
				null, null, null, null, null);

		while (cur.moveToNext()) {

			product = new Product();

			product.setProductID(cur.getString(cur.getColumnIndex("productID")));

			product.setBasePrice(cur.getString(cur.getColumnIndex("basePrice")));

			product.setBrand(cur.getString(cur.getColumnIndex("brand")));
			product.setCurrentPrice(cur.getString(cur
					.getColumnIndex("currentPrice")));
			product.setHasMoreColors(cur.getString(cur
					.getColumnIndex("hasMoreColors")));
			product.setIsInSet(cur.getString(cur.getColumnIndex("isInSet")));
			product.setPreviousPrice(cur.getString(cur
					.getColumnIndex("previousPrice")));

			product.setProductImgURL(cur.getString(cur
					.getColumnIndex("productImgURL")));
			product.setRpp(cur.getString(cur.getColumnIndex("rpp")));
			product.setTitle(cur.getString(cur.getColumnIndex("title")));
			product.setIsFavourite(cur.getString(cur
					.getColumnIndex("isFavourite")));
			product.setIsAddedToShoppingCart(cur.getString(cur
					.getColumnIndex("isAddedToShoppingCart")));

			productList.add(product);

		}

		return productList;
	}

	public boolean favourite(Product product) {

		boolean check = false;

		Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME
				+ " where productID = '" + product.getProductID() + "'", null);

		int count = cursor.getCount();

		Log.d("Current Fav: " + count, product.getProductID());

		if (count == 0) {
			addProduct(product);

			database.execSQL("UPDATE " + TABLE_NAME
					+ " SET isFavourite = 'YES' where productID = '"
					+ product.getProductID() + "'");

			check = true;
		} else if (count == 1) {

			Cursor cr = database.rawQuery("SELECT * FROM " + TABLE_NAME
					+ " where productID = '" + product.getProductID()
					+ "' and isFavourite = 'YES'", null);

			int c = cr.getCount();

			if (c == 0) {
				database.execSQL("UPDATE " + TABLE_NAME
						+ " SET isFavourite = 'YES' where productID = '"
						+ product.getProductID() + "'");
				check = true;
			} else {
				database.execSQL("UPDATE " + TABLE_NAME
						+ " SET isFavourite = 'NO' where productID = '"
						+ product.getProductID() + "'");
				check = false;
			}

		}
		return check;
	}

	public boolean shoppingCart(Product product) {

		boolean check = false;

		Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME
				+ " where productID = '" + product.getProductID() + "'", null);

		int count = cursor.getCount();

		Log.d("Current Fav: " + count, product.getProductID());

		if (count == 0) {
			addProduct(product);

			database.execSQL("UPDATE " + TABLE_NAME
					+ " SET isAddedToShoppingCart = 'YES' where productID = '"
					+ product.getProductID() + "'");

			check = true;
		} else if (count == 1) {

			Cursor cr = database.rawQuery("SELECT * FROM " + TABLE_NAME
					+ " where productID = '" + product.getProductID()
					+ "' and isAddedToShoppingCart = 'YES'", null);

			int c = cr.getCount();

			if (c == 0) {

				database.execSQL("UPDATE "
						+ TABLE_NAME
						+ " SET isAddedToShoppingCart = 'YES' where productID = '"
						+ product.getProductID() + "'");

				check = true;
			} else {
				database.execSQL("UPDATE "
						+ TABLE_NAME
						+ " SET isAddedToShoppingCart = 'NO' where productID = '"
						+ product.getProductID() + "'");
				check = false;
			}

		}
		return check;
	}

	private void addProduct(Product product) {

		Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME
				+ " where productID = '" + product.getProductID() + "'", null);

		int count = cursor.getCount();

		if (count == 0) {
			try {
				database.execSQL("INSERT INTO "
						+ TABLE_NAME
						+ "(productID, basePrice, brand, currentPrice, hasMoreColors,"
						+ "isInSet, previousPrice, productImgURL, rpp,"
						+ "title, isFavourite, isAddedToShoppingCart) values ('"
						+ product.getProductID() + "'," + "'"
						+ product.getBasePrice() + "'," + "'"
						+ product.getBrand() + "'," + "'"
						+ product.getCurrentPrice() + "'," + "'"
						+ product.getHasMoreColors() + "'," + "'"
						+ product.getIsInSet() + "'," + "'"
						+ product.getPreviousPrice() + "'," + "'"
						+ product.getProductImgURL() + "'," + "'"
						+ product.getRpp() + "'," + "'" + product.getTitle()
						+ "'," + "'" + product.getIsFavourite() + "'," + "'"
						+ product.getIsAddedToShoppingCart() + "')");

			} catch (SQLException ex) {
				Log.d("SQL EX", "" + ex.getMessage());
			}
		}

	}

	public void removeFromShoppingCart(Product product) {
		database.execSQL("DELETE FROM " + TABLE_NAME + " WHERE productID = '"
				+ product.getProductID() + "'");
	}

	public void resetDefaultValues() {
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(database);
	}

}