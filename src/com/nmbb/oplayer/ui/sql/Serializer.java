//package com.nmbb.oplayer.ui.sql;
//
///**
// * Created with IntelliJ IDEA.
// * User: Maxim
// * Date: 3/5/13
// * Time: 8:26 PM
// * To change this template use File | Settings | File Templates.
// */
//import com.nmbb.oplayer.ui.products.Video;
//
//import java.io.*;
//
//public class Serializer {
//    public static byte[] serialize(Object video) throws IOException {
////        ByteArrayOutputStream baos = new ByteArrayOutputStream();
////        ObjectOutputStream oos = new ObjectOutputStream(baos);
////        oos.writeObject(video);
////        oos.flush();
////        return baos.toByteArray();
//
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        ObjectOutput out = null;
//        byte[] yourBytes;
//        out = new ObjectOutputStream(bos);
//        out.writeObject(video);
//        yourBytes = bos.toByteArray();
//
//        out.close();
//        bos.close();
//
//        return yourBytes;
//    }
//
//    public static Video deSerialize(byte[] bytes) throws IOException, ClassNotFoundException {
//        ByteArrayInputStream b = new ByteArrayInputStream(bytes);
//        ObjectInputStream o = new ObjectInputStream(b);
//        return (Video)o.readObject();
//    }
//}
