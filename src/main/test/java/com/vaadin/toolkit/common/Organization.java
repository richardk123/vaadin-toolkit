package com.vaadin.toolkit.common;

import java.util.ArrayList;
import java.util.List;

public class Organization
{
    private String name;
    private User owner;
    private List<User> users = new ArrayList<>();

    public Organization(String name)
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

    public User getOwner()
    {
        return owner;
    }

    public void setOwner(User owner)
    {
        this.owner = owner;
    }

    public List<User> getUsers()
    {
        return users;
    }

    public void setUsers(List<User> users)
    {
        this.users = users;
    }

    public void addUser(User test)
    {
        this.users.add(test);
    }
}
