package jp.dip.azurelapis.android.PicS.Datas.DatabaseUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import jp.dip.azurelapis.android.PicS.Datas.BookMark.BookMark;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by kamiyama on 14/11/02.
 * データベースとのやりとりを抽象化する
 *
 */
public class BookMarkDataBaseUtils extends SQLiteOpenHelper {

    //定数
    private static final String TABLE_NAME = "BOOK_MARK";

    //属性名
    //ID
    public static final String CLM_ID = "id";

    //URL
    public static final String CLM_URL = "url";

    //TITLE
    public static final String CLM_TITLE = "title";

    //add_data
    public static final String CLM_ADD_DATE = "add_date";

    //icon
    public static final String CLM_ICON = "icon";


    //フォーマットパターン定義
    private static final String DATE_PATTERN = "yyyy.MM.dd HH:mm:ss.SSS";

    private static final int DB_VERSION = 2;

    private Context context;

    public BookMarkDataBaseUtils(Context context){
        super(context, TABLE_NAME, null, DB_VERSION);

        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createSql = "CREATE TABLE " + TABLE_NAME +
                " ("+ CLM_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CLM_URL + " TEXT,"+
                CLM_TITLE + " TEXT,"+
                CLM_ADD_DATE + " TEXT,"+
                CLM_ICON + " BLOB"+
                ");";

        db.execSQL(createSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {

        String droptableSql = "DROP TABLE " + TABLE_NAME +";";

        db.execSQL(droptableSql);
        onCreate(db);
    }


    /**
     * データベースにブックマークを追加する
     * @param bookMark
     */
    public void insertBookMark(BookMark bookMark){

        SQLiteDatabase db = null;
        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CLM_URL, bookMark.getUrl());
        values.put(CLM_TITLE, bookMark.getTitile());

        SimpleDateFormat dateFormater = new SimpleDateFormat(DATE_PATTERN);
        values.put(CLM_ADD_DATE,  dateFormater.format(new Date()));

        Bitmap icon = bookMark.getIcon();
        byte[] bitmapdata;
        if(icon != null){

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmapdata = stream.toByteArray();
            values.put(CLM_ICON, bitmapdata);
        }


        try {
            db.insert(TABLE_NAME, null, values);

        }catch (Exception e){
            e.printStackTrace();

        }finally {
            db.close();

        }


    }

    /**
     * すべてのブックマークをロードする
     * @return
     */
    public List<BookMark> selectAllBookMark(){

        List<BookMark> ret = new ArrayList<BookMark>();

        SQLiteDatabase db = null;
        db = this.getReadableDatabase();

        try {
            Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null, null);

            while (cursor.moveToNext()) {

                int id;
                String url;
                String title;
                String addDateString;
                Date addDate;

                BookMark bookMark = new BookMark();

                id = cursor.getInt(cursor.getColumnIndex(CLM_ID));
                bookMark.setId(id);

                url = cursor.getString(cursor.getColumnIndex(CLM_URL));
                bookMark.setUrl(url);

                title = cursor.getString(cursor.getColumnIndex(CLM_TITLE));
                bookMark.setTitile(title);

                //追加日付データ
                addDateString = cursor.getString(cursor.getColumnIndex(CLM_ADD_DATE));
                SimpleDateFormat dateFormater = new SimpleDateFormat(DATE_PATTERN);
                addDate = dateFormater.parse(addDateString);
                bookMark.setAddData(addDate);


                //アイコン
                byte[] iconBytes = cursor.getBlob(cursor.getColumnIndex(CLM_ICON));
                if(iconBytes != null) {
                    Bitmap image;
                    image = BitmapFactory.decodeByteArray(iconBytes, 0, iconBytes.length);
                    bookMark.setIcon(image);
                }

                Log.d("SQL BookMark Select ", title + " " + url);

                ret.add(bookMark);

            }
        }catch (Exception e){
            e.printStackTrace();

        }finally {

            db.close();
        }

        return ret;
    }


    /**
     * DBからブックマークを削除する
     * @param id
     */
    public void deletBookMark(int id){

        SQLiteDatabase db = null;
        db = this.getWritableDatabase();

        db.delete(TABLE_NAME, CLM_ID + " = ?", new String[]{Integer.toString(id)});


    }


}
