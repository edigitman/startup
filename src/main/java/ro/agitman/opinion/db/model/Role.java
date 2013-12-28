package ro.agitman.opinion.db.model;

import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.authz.Permission;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity
public class Role {

	@Id
	private ObjectId id;
	private String name;
	private Set<Permission> permissions = new HashSet<>();

	public Role(String name) {
		this.name = name;
	}

	public Role() {
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}
}
