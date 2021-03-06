package symlab.ust.hk.imagetagged.contentprovider;

import java.util.Arrays;
import java.util.HashSet;

import symlab.ust.hk.imagetagged.data.TapScreenDescriptor;
import symlab.ust.hk.imagetagged.data.TaskComputationalDescriptor;
import symlab.ust.hk.imagetagged.data.TaskDatabaseHelper;
import symlab.ust.hk.imagetagged.data.TaskDescriptor;
import symlab.ust.hk.imagetagged.data.TaskQoEDescriptor;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class MyTaskContentProvider extends ContentProvider {

	private TaskDatabaseHelper database;
	
	private static final String AUTHORITY = "symlab.ust.hk.imagetagged.contentprovider";
	
	//Table that collects button events
	private static final String BASE_PATH = "tasks";
	  public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
	      + "/" + BASE_PATH);
	
	
	//Table that collects screen taps
	private static final String BASE_TAPS = "taps";
	public static final Uri CONTENT_URI_TAPS = Uri.parse("content://"+ AUTHORITY
		      + "/" + BASE_TAPS); 
	
	//Table that collects QoE
	private static String BASE_QOES = "qoes";
	public static final Uri CONTENT_URI_QOES = Uri.parse("content://"+ AUTHORITY
		      + "/" + BASE_QOES);

	
	//Table that collects computatinal properties of the task
	private static String BASE_COMPUTATIONALS = "computationals";
	public static final Uri CONTENT_URI_COMPUTATIONALS = Uri.parse("content://"+ AUTHORITY
			      + "/" + BASE_COMPUTATIONALS);

	
	
	//Used by tasks
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
	      + "/tasks";
	
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
	      + "/task";
	
	private static final int TASKS = 10;
	private static final int TASK_ID = 20;
	
	//Used by taps
	public static final String CONTENT_TYPE_TAPS = ContentResolver.CURSOR_DIR_BASE_TYPE
		      + "/taps";
		
		public static final String CONTENT_ITEM_TYPE_TAPS = ContentResolver.CURSOR_ITEM_BASE_TYPE
		      + "/tap";
		
	
	private static final int TAPS = 30;
	private static final int TAP_ID = 40;
	
	//Used by qoes
	public static final String CONTENT_TYPE_QOES = ContentResolver.CURSOR_DIR_BASE_TYPE
		      + "/qoes";
		
		public static final String CONTENT_ITEM_TYPE_QOES = ContentResolver.CURSOR_ITEM_BASE_TYPE
		      + "/qoe";
		
	
	private static final int QOES = 50;
	private static final int QOE_ID = 60;
	
	//Used by computationals
		public static final String CONTENT_TYPE_COMPUTATIONAL = ContentResolver.CURSOR_DIR_BASE_TYPE
		      + "/computationals";
		
		public static final String CONTENT_ITEM_TYPE_COMPUTATIONAL = ContentResolver.CURSOR_ITEM_BASE_TYPE
		      + "/computational";
		
		private static final int COMPUTATIONALS = 70;
		private static final int TASK_COMPUTATIONAL = 80;
		
	
	
	
	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	  static {
	    sURIMatcher.addURI(AUTHORITY, BASE_PATH, TASKS);
	    sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", TASK_ID);
	    sURIMatcher.addURI(AUTHORITY, BASE_TAPS, TAPS);
	    sURIMatcher.addURI(AUTHORITY, BASE_TAPS + "/#", TAP_ID);
	    sURIMatcher.addURI(AUTHORITY, BASE_QOES, QOES);
	    sURIMatcher.addURI(AUTHORITY, BASE_QOES + "/#", QOE_ID);
	    sURIMatcher.addURI(AUTHORITY, BASE_COMPUTATIONALS, COMPUTATIONALS);
	    sURIMatcher.addURI(AUTHORITY, BASE_COMPUTATIONALS + "/#", TASK_COMPUTATIONAL);
	  }
		  
	
	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		int rowsDeleted = 0;
		long id = 0;
		switch (uriType) {
		case TASKS:
		  id = sqlDB.insert(TaskDescriptor.TABLE_TASK, null, values);
		  break;
		  
		case TAPS:
			id = sqlDB.insert(TapScreenDescriptor.TABLE_TAP, null, values);
		  break;
		  
		case QOES:
			id = sqlDB.insert(TaskQoEDescriptor.TABLE_QOE, null, values);
		  break;
		  
		case COMPUTATIONALS:
			id = sqlDB.insert(TaskComputationalDescriptor.TABLE_COMPUTATIONAL, null, values);
		  break;
		  
		default:
		  throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(BASE_PATH + "/" + id);
	}

	@Override
	public boolean onCreate() {
		database = new TaskDatabaseHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
		String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		// Using SQLiteQueryBuilder instead of query() method
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
	  // check if the caller has requested a column which does not exists
			

		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case TASKS:
			// Set the table
			checkColumns(projection);
			queryBuilder.setTables(TaskDescriptor.TABLE_TASK);
		  break;
		case TASK_ID:
		  // adding the ID to the original query
		  queryBuilder.appendWhere(TaskDescriptor.COLUMN_TASK_ID + "="
		      + uri.getLastPathSegment());
		  break;
		case TAPS:
			queryBuilder.setTables(TapScreenDescriptor.TABLE_TAP);
			break;
		
		case TAP_ID:
			  queryBuilder.appendWhere(TapScreenDescriptor.COLUMN_TAP_ID + "="
				      + uri.getLastPathSegment());
			break;
		
		case QOES:
			queryBuilder.setTables(TaskQoEDescriptor.TABLE_QOE);
			break;
		
		case QOE_ID:
			  queryBuilder.appendWhere(TaskQoEDescriptor.COLUMN_TASK_ID + "="
				      + uri.getLastPathSegment());
			break;
		
		case COMPUTATIONALS:
			queryBuilder.setTables(TaskComputationalDescriptor.TABLE_COMPUTATIONAL);
			break;
		
		case TASK_COMPUTATIONAL:
			  queryBuilder.appendWhere(TaskComputationalDescriptor.COLUMN_TASK_ID + "="
				      + uri.getLastPathSegment());
			break;
		  
		default:
		  throw new IllegalArgumentException("Unknown URI: " + uri);
		}  
		  SQLiteDatabase db = database.getWritableDatabase();
		  Cursor cursor = queryBuilder.query(db, projection, selection,
		      selectionArgs, null, null, sortOrder);
		  // make sure that potential listeners are getting notified
		  cursor.setNotificationUri(getContext().getContentResolver(), uri);
	    return cursor;

	}

	@Override
	  public int delete(Uri uri, String selection, String[] selectionArgs) {
	    int uriType = sURIMatcher.match(uri);
	    SQLiteDatabase sqlDB = database.getWritableDatabase();
	    int rowsDeleted = 0;
	    switch (uriType) {
	    case TASKS:
	      rowsDeleted = sqlDB.delete(TaskDescriptor.TABLE_TASK, selection,
	          selectionArgs);
	      break;
	    case TASK_ID:
	      String id = uri.getLastPathSegment();
	      if (TextUtils.isEmpty(selection)) {
	        rowsDeleted = sqlDB.delete(TaskDescriptor.TABLE_TASK,
	            TaskDescriptor.COLUMN_TASK_ID + "=" + id, 
	            null);
	      } else {
	        rowsDeleted = sqlDB.delete(TaskDescriptor.TABLE_TASK,
	            TaskDescriptor.COLUMN_TASK_ID + "=" + id 
	            + " and " + selection,
	            selectionArgs);
	      }
	      break;
	    default:
	      throw new IllegalArgumentException("Unknown URI: " + uri);
	    }
	    getContext().getContentResolver().notifyChange(uri, null);
	    return rowsDeleted;
	  }

	  @Override
	  public int update(Uri uri, ContentValues values, String selection,
	      String[] selectionArgs) {

	    int uriType = sURIMatcher.match(uri);
	    SQLiteDatabase sqlDB = database.getWritableDatabase();
	    int rowsUpdated = 0;
	    switch (uriType) {
	    case TASKS:
	      rowsUpdated = sqlDB.update(TaskDescriptor.TABLE_TASK, 
	          values, 
	          selection,
	          selectionArgs);
	      break;
	    case TASK_ID:
	      String id = uri.getLastPathSegment();
	      if (TextUtils.isEmpty(selection)) {
	        rowsUpdated = sqlDB.update(TaskDescriptor.TABLE_TASK, 
	            values,
	            TaskDescriptor.COLUMN_TASK_ID + "=" + id, 
	            null);
	      } else {
	        rowsUpdated = sqlDB.update(TaskDescriptor.TABLE_TASK, 
	            values,
	            TaskDescriptor.COLUMN_TASK_ID + "=" + id 
	            + " and " 
	            + selection,
	            selectionArgs);
	      }
	      break;
	     
	    case TAPS:
	    	rowsUpdated = sqlDB.update(TapScreenDescriptor.TABLE_TAP, 
	  	          values, 
	  	          selection,
	  	          selectionArgs);	    	
	    	break;
	    
	    case TAP_ID:
	    	String idtap = uri.getLastPathSegment();
		      if (TextUtils.isEmpty(selection)) {
		        rowsUpdated = sqlDB.update(TapScreenDescriptor.TABLE_TAP, 
		            values,
		            TapScreenDescriptor.COLUMN_TAP_ID + "=" + idtap, 
		            null);
		      } else {
		        rowsUpdated = sqlDB.update(TapScreenDescriptor.TABLE_TAP, 
		            values,
		            TapScreenDescriptor.COLUMN_TAP_ID + "=" + idtap 
		            + " and " 
		            + selection,
		            selectionArgs);
		      }
		      break;
	   
	    case QOES:
	    	rowsUpdated = sqlDB.update(TaskQoEDescriptor.TABLE_QOE, 
	  	          values, 
	  	          selection,
	  	          selectionArgs);	    	
	    	break;
	    
	    case QOE_ID:
	    	String idqoe = uri.getLastPathSegment();
		      if (TextUtils.isEmpty(selection)) {
		        rowsUpdated = sqlDB.update(TaskQoEDescriptor.TABLE_QOE, 
		            values,
		            TaskQoEDescriptor.COLUMN_TASK_ID + "=" + idqoe, 
		            null);
		      } else {
		        rowsUpdated = sqlDB.update(TaskQoEDescriptor.TABLE_QOE, 
		            values,
		            TaskQoEDescriptor.COLUMN_TASK_ID + "=" + idqoe 
		            + " and " 
		            + selection,
		            selectionArgs);
		      }
		      break;
	   
	    case COMPUTATIONALS:
	    	rowsUpdated = sqlDB.update(TaskComputationalDescriptor.TABLE_COMPUTATIONAL, 
	  	          values, 
	  	          selection,
	  	          selectionArgs);	    	
	    	break;
	    
	    case TASK_COMPUTATIONAL:
	    	String idcom = uri.getLastPathSegment();
		      if (TextUtils.isEmpty(selection)) {
		        rowsUpdated = sqlDB.update(TaskComputationalDescriptor.TABLE_COMPUTATIONAL, 
		            values,
		            TaskComputationalDescriptor.COLUMN_TASK_ID + "=" + idcom, 
		            null);
		      } else {
		        rowsUpdated = sqlDB.update(TaskComputationalDescriptor.TABLE_COMPUTATIONAL, 
		            values,
		            TaskComputationalDescriptor.COLUMN_TASK_ID + "=" + idcom 
		            + " and " 
		            + selection,
		            selectionArgs);
		      }
		      break;
	         
		      
	    default:
	      throw new IllegalArgumentException("Unknown URI: " + uri);
	    }
	    getContext().getContentResolver().notifyChange(uri, null);
	    return rowsUpdated;
	  }
	
	 private void checkColumns(String[] projection) {
		    String[] available = {TaskDescriptor.COLUMN_CURRENT_TASK_NAME, TaskDescriptor.COLUMN_TASK_BUTTON_ID, TaskDescriptor.COLUMN_TASK_DESCRIPTION, TaskDescriptor.COLUMN_TASK_FINISH_TIME,
		        TaskDescriptor.COLUMN_TASK_ID, TaskDescriptor.COLUMN_TASK_INITIAL_TIME};
		    if (projection != null) {
		      HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
		      HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
		      // check if all columns which are requested are available
		      if (!availableColumns.containsAll(requestedColumns)) {
		        throw new IllegalArgumentException("Unknown columns in projection");
		      }
		    }
	 }

}





