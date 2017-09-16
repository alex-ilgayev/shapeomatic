//package com.shapeomatic.Networking;
//
//import android.content.Context;
//import android.os.AsyncTask;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.shapeomatic.Controller.Settings;
//import com.shapeomatic.Model.User;
//import com.shapeomatic.R;
//
//import java.io.IOException;
//import java.util.List;
//
//
///**
// * Created by Alex on 10/30/2015.
// */
//public class UserUpdater {
//
//    public enum OperationKind {
//        GET_USER,
//        UPDATE_USERS,
//        SYNC_ME,
//    }
//
//    private Context _ctx;
//    private String _url;
//    private Object _obj;
//    private OperationKind _oper;
//    private AsyncTask _task;
//
//    public UserUpdater(Context ctx, Object obj, OperationKind oper) {
//        _ctx = ctx;
//        _obj = obj;
//        _oper = oper;
//    }
//
//    public void run(IResponseHandler handler) {
//        switch(_oper) {
//            case GET_USER:
//                long id = (Long)_obj;
//                _task = new HttpRestTask(_ctx, Settings.URL_USER_PATH + id, RestClient.RequestMethod.GET, null,
//                        handler, new GetUserResponseError(), 0);
//                break;
//            case UPDATE_USERS:
//                _task = new HttpRestTask(_ctx, Settings.URL_USER_PATH, RestClient.RequestMethod.GET, null,
//                        handler, new UserUpdaterResponseError(), 0);
//                break;
//            case SYNC_ME:
//                _task = new HttpRestTask(_ctx, Settings.URL_USER_PATH, RestClient.RequestMethod.POST, _obj,
//                        handler, new SyncMeResponseError(), 0);
//                break;
//        }
//        _task.execute();
//    }
//
//    private class HttpRestTask extends AsyncTask<Object, Void, String> {
//        private Context _ctx;
//        private String _url;
//        private RestClient.RequestMethod _method;
//        private Object _contentObj;
//        private IResponseHandler _callbackSuccess;
//        private IResponseError _callbackError;
//        private Exception _e;
//        private int _retry;
//
//        public HttpRestTask(Context ctx, String url, RestClient.RequestMethod method,
//                            Object contentObj, IResponseHandler callback,
//                            IResponseError error, int retry) {
//            this._ctx = ctx;
//            this._url = url;
//            this._method = method;
//            this._contentObj = contentObj;
//            this._callbackSuccess = callback;
//            this._callbackError = error;
//            this._retry = retry;
//        }
//
//        protected String doInBackground(Object... params) {
//            try {
//                String msgContent = "";
//                if(_contentObj != null)
//                    msgContent = EntityParser.objToXml(_contentObj);
//                return new RestClient(_url).execute(_method, msgContent);
//            } catch(IOException e) {
//                e.printStackTrace();
//                _e = e;
//                return null;
//            } catch(RestClient.RestResponseException e) {
//                _e = e;
//                return null;
//            }
//        }
//
//        protected void onPostExecute(String result) {
//            // special case of ssl failure.
////            if(result == null && _e != null && SSLException.class.isAssignableFrom(_e.getClass())) {
////                //Toast.makeText(_ctx, "SSL exception", Toast.LENGTH_LONG).show();
////                new PlanUpdater(_ctx).downloadPlans(Settings.urlPlan);
////                return;
////            }
//
//            if(result == null) {
//                if (_e != null) {
//                    if (IOException.class.isAssignableFrom(_e.getClass())) {
//                        _retry++;
//                        if(_retry == Settings.MAX_RETRIES) {
//                            Toast.makeText(_ctx, R.string.io_exception, Toast.LENGTH_LONG).show();
//                            _callbackError.handleError(_e);
//                        } else {
//                            new HttpRestTask(_ctx, _url, _method, _contentObj,
//                                    _callbackSuccess, _callbackError, _retry).execute();
//                        }
//                        return;
//                    }
//                    if (_e.getClass() == RestClient.RestResponseException.class) {
//                        RestClient.RestResponseException _restExc =
//                                (RestClient.RestResponseException) _e;
////                        if(_restExc.errorCode == 426) { // need to upgrade
////                            final Activity currActivity = ((TnayedApp)_ctx.getApplicationContext()).getCurrentActivity();
////                            new AlertDialog.Builder(currActivity)
////                                    .setTitle(R.string.alert_dialog_title)
////                                    .setMessage(R.string.alert_dialog_content)
////                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
////                                        public void onClick(DialogInterface dialog, int which) {
////                                            (currActivity).finish();
////                                            System.exit(0);
////                                        }
////                                    })
////                                    .setIcon(android.R.drawable.ic_dialog_alert)
////                                    .show();
////                        }
//                        //if(_updateToGui)
//                        Toast.makeText(_ctx, _ctx.getString(R.string.io_exception)
//                                + _restExc.errorCode, Toast.LENGTH_LONG).show();
//                        _callbackError.handleError(_e);
//                        return;
//                    }
//                    return;
//                }
//                else
//                    return;
//            }
//            _callbackSuccess.handleResponse(result);
//        }
//    }
//
//    public interface IResponseHandler {
//        void handleResponse(String response);
//    }
//
//    public interface IResponseError {
//        void handleError(Exception exc);
//    }
//
//    private class GetUserResponseError implements IResponseError {
//        @Override
//        public void handleError(Exception exc) {
//            Log.d(Settings.TAG, "Get User Exception: " + exc.getMessage());
//        }
//    }
//
//    private class UserUpdaterResponseError implements IResponseError {
//        @Override
//        public void handleError(Exception exc) {
//            Log.d(Settings.TAG, "User Update Exception: " + exc.getMessage());
//        }
//    }
//
//    private class SyncMeResponseError implements IResponseError {
//        @Override
//        public void handleError(Exception exc) {
//            Log.d(Settings.TAG, "Sync Me Exception: " + exc.getMessage());
//        }
//    }
//}
