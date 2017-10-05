package com.vaadin.toolkit.common;

import com.vaadin.toolkit.MyUI;

import java.util.ArrayList;
import java.util.List;

public class TestData
{
    private String name;
    private TestData test;
    private List<TestData> testList = new ArrayList<>();

    public TestData(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public TestData getTest()
    {
        return test;
    }

    public void setTest(TestData test)
    {
        this.test = test;
    }

    public List<TestData> getTestList()
    {
        return testList;
    }

    public void setTestList(List<TestData> testList)
    {
        this.testList = testList;
    }

    public void addTest(TestData test)
    {
        this.testList.add(test);
    }
}
