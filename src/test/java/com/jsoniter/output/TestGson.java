package com.jsoniter.output;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jsoniter.extra.GsonCompatibilityMode;
import com.jsoniter.spi.JsoniterSpi;
import junit.framework.TestCase;

public class TestGson extends TestCase {

    private GsonCompatibilityMode gsonCompatibilityMode;

    public void setUp() {
        gsonCompatibilityMode = new GsonCompatibilityMode.Builder().build();
        JsoniterSpi.registerExtension(gsonCompatibilityMode);
    }

    public void tearDown() {
        JsoniterSpi.deregisterExtension(gsonCompatibilityMode);
    }

    public static class TestObject1 {
        @SerializedName("field-1")
        public String field1;
    }

    public void test_SerializedName_on_field() {
        Gson gson = new GsonBuilder().create();
        TestObject1 obj = new TestObject1();
        obj.field1 = "hello";
        String output = gson.toJson(obj);
        assertEquals("{\"field-1\":\"hello\"}", output);
        output = JsonStream.serialize(obj);
        assertEquals("{\"field-1\":\"hello\"}", output);
    }

    public static class TestObject2 {
        @Expose(serialize = false)
        public String field1;
    }

    public void test_Expose() {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        TestObject2 obj = new TestObject2();
        obj.field1 = "hello";
        String output = gson.toJson(obj);
        assertEquals("{}", output);

        JsoniterSpi.deregisterExtension(gsonCompatibilityMode);
        gsonCompatibilityMode = new GsonCompatibilityMode.Builder()
                .excludeFieldsWithoutExposeAnnotation().build();
        JsoniterSpi.registerExtension(gsonCompatibilityMode);
        output = JsonStream.serialize(obj);
        assertEquals("{}", output);
    }

    public static class TestObject3 {
        public String getField1() {
            return "hello";
        }
    }

    public void test_getter_should_be_ignored() {
        Gson gson = new GsonBuilder().create();
        TestObject3 obj = new TestObject3();
        String output = gson.toJson(obj);
        assertEquals("{}", output);
        output = JsonStream.serialize(obj);
        assertEquals("{}", output);
    }

    public static class TestObject4 {
        public String field1;
    }

    public void test_excludeFieldsWithoutExposeAnnotation() {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        TestObject4 obj = new TestObject4();
        obj.field1 = "hello";
        String output = gson.toJson(obj);
        assertEquals("{}", output);

        JsoniterSpi.deregisterExtension(gsonCompatibilityMode);
        gsonCompatibilityMode = new GsonCompatibilityMode.Builder()
                .excludeFieldsWithoutExposeAnnotation().build();
        JsoniterSpi.registerExtension(gsonCompatibilityMode);
        output = JsonStream.serialize(obj);
        assertEquals("{}", output);
    }
}
