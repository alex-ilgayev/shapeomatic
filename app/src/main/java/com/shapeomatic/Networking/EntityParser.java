package com.shapeomatic.Networking;


import android.util.Log;

import com.shapeomatic.Controller.Settings;
import com.shapeomatic.Model.User;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.converters.reflection.FieldDictionary;
import com.thoughtworks.xstream.converters.reflection.SortableFieldKeySorter;
import com.thoughtworks.xstream.converters.reflection.Sun14ReflectionProvider;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by Alex on 11/6/2015.
 */
public class EntityParser {

    private static String[] _userOrder = new String[] {"facebookId", "name", "score", "pic"};

    public static User xmlToUser(String in) throws XStreamException {
        try {
            XStream xstream = new XStream();
            xstream.processAnnotations(User.class);

            User ret = (User) xstream.fromXML(in);

            return ret;
        } catch(Exception e) {
            e.printStackTrace();
            Log.d(Settings.TAG, "Parsing exception.");
            //throw new XStreamParsingException();
            return null;
        }
    }

    public static String objToXml(Object obj) throws XStreamException {
        try {
            XStream xstream = new XStream();
            xstream.processAnnotations(User.class);
            xstream.processAnnotations(ArrayList.class);
            return xstream.toXML(obj);

        } catch(Exception e) {
            e.printStackTrace();
            Log.d(Settings.TAG, "Parsing exception.");
            return null;
        }
    }
}
