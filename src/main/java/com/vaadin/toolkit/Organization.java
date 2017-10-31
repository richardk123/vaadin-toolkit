package com.vaadin.toolkit;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kolisek
 */
public class Organization
{
	private String name;
	private User owner = new User();
	private List<User> users = new ArrayList<>();

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

	public User addUser(User user)
	{
		users.add(user);
		return user;
	}

	public void removeUser(User user)
	{
		users.remove(user);
	}
}
